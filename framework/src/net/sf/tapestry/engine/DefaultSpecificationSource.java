/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.engine;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.IRenderDescription;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.ISpecificationSource;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.parse.SpecificationParser;
import net.sf.tapestry.resource.ClasspathResourceLocation;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.IApplicationSpecification;
import net.sf.tapestry.spec.ILibrarySpecification;
import net.sf.tapestry.spec.LibrarySpecification;
import net.sf.tapestry.util.StringSplitter;
import net.sf.tapestry.util.pool.Pool;
import net.sf.tapestry.util.xml.DocumentParseException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Default implementation of {@link ISpecificationSource} that
 *  expects to use the normal class loader to locate component
 *  specifications from within the classpath.
 *
 * <p>Caches specifications in memory forever, or until {@link #reset()} is invoked.
 *
 * <p>An instance of this class acts like a singleton and is shared by multiple sessions,
 * so it must be threadsafe.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public class DefaultSpecificationSource implements ISpecificationSource, IRenderDescription
{
    private static final Log LOG = LogFactory.getLog(DefaultSpecificationSource.class);

    /**
     *  Key used to get and store {@link SpecificationParser} instances
     *  from the Pool.
     * 
     *  @since 2.4
     * 
     **/

    private static final String PARSER_POOL_KEY = "net.sf.tapestry.SpecificationParser";

    private IResourceResolver _resolver;
    private IApplicationSpecification _specification;

    private INamespace _applicationNamespace;
    private INamespace _frameworkNamespace;

    /**
     *  Contains previously parsed component specifications.
     *
     **/

    private Map _componentCache = new HashMap();

    /**
     *  Contains previously parsed page specifications.
     * 
     *  @since 2.2
     * 
     **/

    private Map _pageCache = new HashMap();

    /**
     *  Contains previously parsed library specifications, keyed
     *  on specification resource path.
     * 
     *  @since 2.2
     * 
     **/

    private Map _libraryCache = new HashMap();

    /**
     *  Contains {@link INamespace} instances, keyed on id (which will
     *  be null for the application specification).
     * 
     **/

    private Map _namespaceCache = new HashMap();

    /**
     *  Reference to the shared {@link net.sf.tapestry.util.pool.Pool}.
     * 
     *  @see IEngine#getPool()
     * 
     *  @since 2.4
     * 
     **/

    private Pool _pool;

    public DefaultSpecificationSource(
        IResourceResolver resolver,
        IApplicationSpecification specification,
        Pool pool)
    {
        _resolver = resolver;
        _specification = specification;
        _pool = pool;
    }

    /**
     *  Clears the specification cache.  This is used during debugging.
     *
     **/

    public void reset()
    {
        _componentCache.clear();
        _pageCache.clear();
        _libraryCache.clear();
        _namespaceCache.clear();

        _applicationNamespace = null;
        _frameworkNamespace = null;
    }

    protected ComponentSpecification parseSpecification(
        IResourceLocation resourceLocation,
        boolean asPage)
    {
        ComponentSpecification result = null;

        if (LOG.isDebugEnabled())
            LOG.debug("Parsing component specification " + resourceLocation);

        SpecificationParser parser = getParser();

        try
        {
            if (asPage)
                result = parser.parsePageSpecification(resourceLocation);
            else
                result = parser.parseComponentSpecification(resourceLocation);
        }
        catch (DocumentParseException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "DefaultSpecificationSource.unable-to-parse-specification",
                    resourceLocation),
                ex);
        }
        finally
        {
            discardParser(parser);
        }

        return result;
    }

    protected ILibrarySpecification parseLibrarySpecification(IResourceLocation resourceLocation)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Parsing library specification " + resourceLocation);

        try
        {
            return getParser().parseLibrarySpecification(resourceLocation, _resolver);
        }
        catch (DocumentParseException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "DefaultSpecificationSource.unable-to-parse-specification",
                    resourceLocation),
                ex);
        }

    }

    /** @since 2.2 **/

    private InputStream openSpecification(IResourceLocation resourceLocation)
    {
        URL URL = resourceLocation.getResourceURL();

        if (URL == null)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "DefaultSpecificationSource.unable-to-locate-specification",
                    resourceLocation));
        }

        try
        {
            return URL.openStream();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "DefaultSpecificationSource.unable-to-open-specification",
                    resourceLocation),
                ex);
        }
    }

    /** @since 2.2 **/

    private void close(InputStream stream)
    {
        try
        {
            if (stream != null)
                stream.close();
        }
        catch (IOException ex)
        {
            // Ignore it.
        }
    }

    public synchronized String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("applicationNamespace", _applicationNamespace);
        builder.append("frameworkNamespace", _frameworkNamespace);
        builder.append("specification", _specification);

        return builder.toString();
    }

    /** @since 1.0.6 **/

    public synchronized void renderDescription(IMarkupWriter writer)
    {
        writer.print("DefaultSpecificationSource[");

        writeCacheDescription(writer, "page", _pageCache);
        writer.beginEmpty("br");
        writer.println();

        writeCacheDescription(writer, "component", _componentCache);
        writer.print("]");
        writer.println();
    }

    private void writeCacheDescription(IMarkupWriter writer, String name, Map cache)
    {
        Set keySet = cache.keySet();

        writer.print(Tapestry.size(keySet));
        writer.print(" cached ");
        writer.print(name);
        writer.print(" specifications:");

        boolean first = true;

        Iterator i = keySet.iterator();
        while (i.hasNext())
        {
            // The keys are now IResourceLocation instances

            Object key = i.next();

            if (first)
            {
                writer.begin("ul");
                first = false;
            }

            writer.begin("li");
            writer.print(key.toString());
            writer.end();
        }

        if (!first)
            writer.end(); // <ul>
    }

    /**
     *  Gets a component specification.
     * 
     *  @param resourcePath the complete resource path to the specification.
     *  @throws ApplicationRuntimeException if the specification cannot be obtained.
     * 
     **/

    public synchronized ComponentSpecification getComponentSpecification(IResourceLocation resourceLocation)
    {
        ComponentSpecification result =
            (ComponentSpecification) _componentCache.get(resourceLocation);

        if (result == null)
        {
            result = parseSpecification(resourceLocation, false);

            _componentCache.put(resourceLocation, result);
        }

        return result;
    }

    public synchronized ComponentSpecification getPageSpecification(IResourceLocation resourceLocation)
    {
        ComponentSpecification result = (ComponentSpecification) _pageCache.get(resourceLocation);

        if (result == null)
        {
            result = parseSpecification(resourceLocation, true);

            _pageCache.put(resourceLocation, result);
        }

        return result;
    }

    public synchronized ILibrarySpecification getLibrarySpecification(IResourceLocation resourceLocation)
    {
        ILibrarySpecification result = (LibrarySpecification) _libraryCache.get(resourceLocation);

        if (result == null)
        {
            result = parseLibrarySpecification(resourceLocation);
            _libraryCache.put(resourceLocation, result);
        }

        return result;
    }

    public synchronized INamespace getNamespace(String id)
    {
        INamespace result = (INamespace) _namespaceCache.get(id);

        if (result == null)
        {
            result = findNamespace(id);

            _namespaceCache.put(id, result);
        }

        return result;
    }

    /** @since 2.2 **/

    protected SpecificationParser getParser()
    {
        SpecificationParser result = (SpecificationParser) _pool.retrieve(PARSER_POOL_KEY);

        if (result == null)
            result = new SpecificationParser();

        return result;
    }

    /** @since 2.4 **/

    protected void discardParser(SpecificationParser parser)
    {
        _pool.store(PARSER_POOL_KEY, parser);
    }

    public synchronized INamespace getApplicationNamespace()
    {
        if (_applicationNamespace == null)
            _applicationNamespace = new Namespace(null, null, _specification, this);

        return _applicationNamespace;
    }

    public synchronized INamespace getFrameworkNamespace()
    {
        if (_frameworkNamespace == null)
        {
            IResourceLocation frameworkLocation =
                new ClasspathResourceLocation(_resolver, "/net/sf/tapestry/Framework.library");

            ILibrarySpecification ls = getLibrarySpecification(frameworkLocation);

            _frameworkNamespace = new Namespace(INamespace.FRAMEWORK_NAMESPACE, null, ls, this);
        }

        return _frameworkNamespace;
    }

    /** 
     * 
     *  Finds or creates the namespace.
     * 
     *  @param id the id, or id path, of the namespace.
     *  @return the namespace,
     *  @throws ApplicationRuntimeException if the namespace does not exist
     *  @since 2.2 
     * 
     **/

    private INamespace findNamespace(String id)
    {
        StringSplitter splitter = new StringSplitter('.');

        String idPath[] = splitter.splitToArray(id);

        INamespace n = getApplicationNamespace();

        for (int i = 0; i < idPath.length; i++)
        {
            n = n.getChildNamespace(idPath[i]);
        }

        return n;
    }
}