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

package org.apache.tapestry.util;

import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;

/**
 *  Default implementation of {@link org.apache.tapestry.IResourceResolver} based
 *  around {@link Thread#getContextClassLoader()} (which is set by the
 *  servlet container).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 * 
 **/

public class DefaultResourceResolver implements IResourceResolver
{
    private static final Log LOG = LogFactory.getLog(DefaultResourceResolver.class);

    private ClassLoader _loader;

    /**
     *  Constructs a new instance using
     *  {@link Thread#getContextClassLoader()}.
     * 
     **/
    
    public DefaultResourceResolver()
    {
        this(Thread.currentThread().getContextClassLoader());
    }

    public DefaultResourceResolver(ClassLoader loader)
    {
        _loader = loader;
    }

    public URL getResource(String name)
    {
        boolean debug = LOG.isDebugEnabled();

        if (debug)
            LOG.debug("getResource(" + name + ")");

        String stripped = removeLeadingSlash(name);

        URL result = _loader.getResource(stripped);

        if (debug)
        {
            if (result == null)
                LOG.debug("Not found.");
            else
                LOG.debug("Found as " + result);
        }

        return result;
    }

    private String removeLeadingSlash(String name)
    {
        if (name.startsWith("/"))
            return name.substring(1);

        return name;
    }

    /**
     *  Invokes {@link Class#forName(java.lang.String, boolean, java.lang.ClassLoader)}.
     *  
     *  @param name the complete class name to locate and load
     *  @return The loaded class
     *  @throws ApplicationRuntimeException if loading the class throws an exception
     *  (typically  {@link ClassNotFoundException} or a security exception)
     * 
     **/
    
    public Class findClass(String name)
    {
        try
        {
            return Class.forName(name, true, _loader);
        }
        catch (Throwable t)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("ResourceResolver.unable-to-load-class", name, _loader, t.getMessage()),
                t);
        }
    }

    /**
     * 
     *  OGNL Support for dynamic class loading.  Simply invokes {@link #findClass(String)}.
     * 
     **/
    
    public Class classForName(String name, Map map) throws ClassNotFoundException
    {
        return findClass(name);
    }

    /** @since 2.4 **/
    
    public ClassLoader getClassLoader()
    {
        return _loader;
    }

}
