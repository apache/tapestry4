/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.hivemind.util.ClasspathResource;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.parse.SpecificationParser;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;
import org.apache.tapestry.spec.LibrarySpecification;
import org.apache.tapestry.util.IRenderDescription;
import org.apache.tapestry.util.pool.Pool;
import org.apache.tapestry.util.xml.DocumentParseException;

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
     *  @since 3.0
     * 
     **/

    private static final String PARSER_POOL_KEY = "org.apache.tapestry.SpecificationParser";

    private ClassResolver _resolver;
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
     *  Reference to the shared {@link org.apache.tapestry.util.pool.Pool}.
     * 
     *  @see IEngine#getPool()
     * 
     *  @since 3.0
     * 
     **/

    private Pool _pool;

    public DefaultSpecificationSource(
        ClassResolver resolver,
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

    public synchronized void reset()
    {
        _componentCache.clear();
        _pageCache.clear();
        _libraryCache.clear();
        _namespaceCache.clear();

        _applicationNamespace = null;
        _frameworkNamespace = null;
    }

    protected IComponentSpecification parseSpecification(
        Resource resourceLocation,
        boolean asPage)
    {
        IComponentSpecification result = null;

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
                Tapestry.format(
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

    protected ILibrarySpecification parseLibrarySpecification(Resource resourceLocation)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Parsing library specification " + resourceLocation);

        try
        {
            return getParser().parseLibrarySpecification(resourceLocation);
        }
        catch (DocumentParseException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "DefaultSpecificationSource.unable-to-parse-specification",
                    resourceLocation),
                ex);
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

    public synchronized IComponentSpecification getComponentSpecification(Resource resourceLocation)
    {
        IComponentSpecification result =
            (IComponentSpecification) _componentCache.get(resourceLocation);

        if (result == null)
        {
            result = parseSpecification(resourceLocation, false);

            _componentCache.put(resourceLocation, result);
        }

        return result;
    }

    public synchronized IComponentSpecification getPageSpecification(Resource resourceLocation)
    {
        IComponentSpecification result = (IComponentSpecification) _pageCache.get(resourceLocation);

        if (result == null)
        {
            result = parseSpecification(resourceLocation, true);

            _pageCache.put(resourceLocation, result);
        }

        return result;
    }

    public synchronized ILibrarySpecification getLibrarySpecification(Resource resourceLocation)
    {
        ILibrarySpecification result = (LibrarySpecification) _libraryCache.get(resourceLocation);

        if (result == null)
        {
            result = parseLibrarySpecification(resourceLocation);
            _libraryCache.put(resourceLocation, result);
        }

        return result;
    }

    /** @since 2.2 **/

    protected SpecificationParser getParser()
    {
        SpecificationParser result = (SpecificationParser) _pool.retrieve(PARSER_POOL_KEY);

        if (result == null)
            result = new SpecificationParser(_resolver);

        return result;
    }

    /** @since 3.0 **/

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
            Resource frameworkLocation =
                new ClasspathResource(_resolver, "/org/apache/tapestry/Framework.library");

            ILibrarySpecification ls = getLibrarySpecification(frameworkLocation);

            _frameworkNamespace = new Namespace(INamespace.FRAMEWORK_NAMESPACE, null, ls, this);
        }

        return _frameworkNamespace;
    }

}