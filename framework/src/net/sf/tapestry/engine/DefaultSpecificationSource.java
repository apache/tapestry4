//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.engine;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRenderDescription;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.ISpecificationSource;
import net.sf.tapestry.ResourceUnavailableException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.parse.SpecificationParser;
import net.sf.tapestry.spec.ApplicationSpecification;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.util.xml.DocumentParseException;

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
    private static final Category CAT = Category.getInstance(DefaultSpecificationSource.class);

    private IResourceResolver _resolver;
    private ApplicationSpecification _specification;

    private SpecificationParser _parser;

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

    public DefaultSpecificationSource(IResourceResolver resolver, ApplicationSpecification specification)
    {
        _resolver = resolver;
        _specification = specification;
    }

    /**
     *  Clears the specification cache.  This is used during debugging.
     *
     **/

    public void reset()
    {
        _componentCache.clear();
        _pageCache.clear();
    }

    /**
     *  Gets a specification.  The type is either a component specification
     *  path, or an alias to a component (registerred in the application
     *  specification).  The former always starts with a slash, the latter
     *  never does.
     *
     *  <p>If an alias (i.e, starts with a slash), then the value is passed through
     *  {@link ApplicationSpecification#getComponentAlias(String)} to
     *  get a resource on the classpath that is parsed.
     *
     *  @deprecated To be removed in 2.3
     **/

    public ComponentSpecification getSpecification(String type) throws ResourceUnavailableException
    {
        return getComponentSpecification(type);
    }

    protected ComponentSpecification parseSpecification(String resourcePath, boolean asPage)
        throws ResourceUnavailableException
    {
        ComponentSpecification result = null;
        URL URL;
        InputStream inputStream;

        if (CAT.isDebugEnabled())
            CAT.debug("Parsing component specification " + resourcePath);

        URL = _resolver.getResource(resourcePath);

        if (URL == null)
        {
            throw new ResourceUnavailableException(
                Tapestry.getString("DefaultSpecificationSource.unable-to-locate-specification", resourcePath));
        }

        try
        {
            inputStream = URL.openStream();
        }
        catch (IOException ex)
        {
            throw new ResourceUnavailableException(
                Tapestry.getString("DefaultSpecificationSource.unable-to-open-specification", resourcePath),
                ex);
        }

        if (_parser == null)
            _parser = new SpecificationParser();

        try
        {
            if (asPage)
                result = _parser.parsePageSpecification(inputStream, resourcePath);
            else
                result = _parser.parseComponentSpecification(inputStream, resourcePath);
        }
        catch (DocumentParseException ex)
        {
            throw new ResourceUnavailableException(
                Tapestry.getString("DefaultSpecificationSource.unable-to-parse-specification", resourcePath),
                ex);
        }

        result.setSpecificationResourcePath(resourcePath);

        return result;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("DefaultSpecificationSource@");
        buffer.append(Integer.toHexString(hashCode()));

        buffer.append('[');

        if (_componentCache != null)
        {
            synchronized (_componentCache)
            {
                buffer.append(_componentCache.keySet());
            }
        }

        buffer.append(']');

        return buffer.toString();
    }

    /** @since 1.0.6 **/

    public synchronized void renderDescription(IMarkupWriter writer)
    {

        writer.print("DefaultSpecificationSource[");

        writeCacheDescription(writer, "page", _pageCache);
        ;
        writer.beginEmpty("br");
        writer.println();

        writeCacheDescription(writer, "component", _componentCache);
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
            String key = (String) i.next();

            if (first)
            {
                writer.begin("ul");
                first = false;
            }

            writer.begin("li");
            writer.print(key);
            writer.end();
        }

        if (!first)
            writer.end(); // <ul>
    }

    public synchronized ComponentSpecification getComponentSpecification(String type)
        throws ResourceUnavailableException
    {
        String resourceName;

        ComponentSpecification result = (ComponentSpecification) _componentCache.get(type);

        if (result == null)
        {
            if (type.startsWith("/"))
                resourceName = type;
            else
            {
                resourceName = _specification.getComponentSpecificationPath(type);

                if (resourceName == null)
                    throw new ResourceUnavailableException(
                        Tapestry.getString("DefaultSpecificationSource.no-match-for-alias", type));
            }

            result = parseSpecification(resourceName, false);

            _componentCache.put(type, result);
            if (resourceName != type)
                _componentCache.put(resourceName, result);
        }

        return result;
    }

    public synchronized ComponentSpecification getPageSpecification(String resourcePath)
        throws ResourceUnavailableException
    {
        ComponentSpecification result = (ComponentSpecification)_pageCache.get(resourcePath);
        
        if (result == null)
        {
            result = parseSpecification(resourcePath, true);
            
            _pageCache.put(resourcePath, result);
        }
        
        return result;
    }

}