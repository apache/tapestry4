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

package org.apache.tapestry.resource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.apache.commons.hivemind.Resource;
import org.apache.commons.hivemind.util.AbstractResource;
import org.apache.commons.hivemind.util.LocalizedResource;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.LocalizedContextResourceFinder;

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

public class ContextResourceLocation extends AbstractResource
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

    public Resource getLocalization(Locale locale)
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

    protected Resource buildNewResourceLocation(String path)
    {
        return new ContextResourceLocation(_context, path);
    }

}
