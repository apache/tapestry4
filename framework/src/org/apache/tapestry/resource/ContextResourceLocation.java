//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.resource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.LocalizedContextResourceFinder;
import org.apache.tapestry.util.LocalizedResource;

/**
 *  Implementation of {@link org.apache.tapestry.IResourceLocation}
 *  for resources found within the web application context.
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class ContextResourceLocation extends AbstractResourceLocation
{
    private static final Log LOG = LogFactory.getLog(ContextResourceLocation.class);

    private ServletContext _context;

    public ContextResourceLocation(ServletContext context, String path)
    {
        this(context, path, null);
    }

    public ContextResourceLocation(ServletContext context, String path, Locale locale)
    {
        super(path, locale);

        _context = context;
    }

    /**
     *  Locates the resource using {@link LocalizedContextResourceFinder}
     *  and {@link ServletContext#getResource(java.lang.String)}.
     * 
     **/

    public IResourceLocation getLocalization(Locale locale)
    {
        LocalizedContextResourceFinder finder = new LocalizedContextResourceFinder(_context);

        String path = getPath();
        LocalizedResource localizedResource = finder.resolve(path, locale);

        if (localizedResource == null)
            return null;

        String localizedPath = localizedResource.getResourcePath();
        Locale pathLocale = localizedResource.getResourceLocale();

        if (localizedPath == null)
            return null;

        if (path.equals(localizedPath))
            return this;

        return new ContextResourceLocation(_context, localizedPath, pathLocale);
    }

    public URL getResourceURL()
    {
        try
        {
            return _context.getResource(getPath());
        }
        catch (MalformedURLException ex)
        {
            LOG.warn(
                Tapestry.format(
                    "ContextResourceLocation.unable-to-reference-context-path",
                    getPath()),
                ex);

            return null;
        }
    }

    public String toString()
    {
        return "context:" + getPath();
    }

    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder(3265, 143);

        builder.append(getPath());

        return builder.toHashCode();
    }

    protected IResourceLocation buildNewResourceLocation(String path)
    {
        return new ContextResourceLocation(_context, path);
    }

}
