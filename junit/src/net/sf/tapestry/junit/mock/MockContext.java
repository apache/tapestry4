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
package net.sf.tapestry.junit.mock;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 *  Mock implementation of {@link javax.servlet.ServletContext}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class MockContext extends AttributeHolder implements ServletContext, IInitParameterHolder
{
    private MockSession _session;

    /**
     *  Directory, relative to the current directory (i.e., System property user.dir)
     *  that is the context root.
     * 
     **/

    private String _rootDirectory = "context";
    private String _servletContextName = "test";
    private Map _initParameters = new HashMap();

    public ServletContext getContext(String name)
    {
        return null;
    }

    public int getMajorVersion()
    {
        return 2;
    }

    public int getMinorVersion()
    {
        return 1;
    }

    public String getMimeType(String path)
    {
        return null;
    }

    public Set getResourcePaths(String arg0)
    {
        return null;
    }

    public URL getResource(String path) throws MalformedURLException
    {
        if (path == null || !path.startsWith("/"))
            throw new MalformedURLException("Not a valid context path.");

        StringBuffer buffer = new StringBuffer();

        buffer.append(System.getProperty("user.dir"));
        buffer.append("/");
        buffer.append(_rootDirectory);

        // Path has a leading slash

        buffer.append(path);

        File file = new File(buffer.toString());

        if (file.exists())
            return file.toURL();

        return null;
    }

    public InputStream getResourceAsStream(String path)
    {
        try
        {
            URL url = getResource(path);

            if (url == null)
                return null;

            return url.openStream();
        }
        catch (MalformedURLException ex)
        {
            return null;
        }
        catch (IOException ex)
        {
            return null;
        }
    }

    /**
     *  Gets a dispatcher for the given path.  Path should be a relative path (relative
     *  to the context).  A special case:  "NULL" returns null (i.e., when a 
     *  dispatcher can't be found).
     * 
     **/

    public RequestDispatcher getRequestDispatcher(String path)
    {
        if (path.endsWith("/NULL"))
            return null;

        StringBuffer buffer = new StringBuffer(_rootDirectory);
        buffer.append(path);

        // Simulate the handling of directories by serving the index.html
        // in the directory.

        if (path.endsWith("/"))
            buffer.append("index.html");

        return new MockRequestDispatcher(buffer.toString());
    }

    public RequestDispatcher getNamedDispatcher(String name)
    {
        return null;
    }

    public Servlet getServlet(String name) throws ServletException
    {
        return null;
    }

    public Enumeration getServlets()
    {
        return null;
    }

    public Enumeration getServletNames()
    {
        return null;
    }

    public void log(String message)
    {
        log(message, null);
    }

    public void log(Exception exception, String message)
    {
        log(message, exception);
    }

    public void log(String message, Throwable exception)
    {
    }

    public String getRealPath(String arg0)
    {
        return null;
    }

    public String getServerInfo()
    {
        return "Tapestry Mock Objects";
    }

    public String getInitParameter(String name)
    {
        return (String) _initParameters.get(name);
    }

    public Enumeration getInitParameterNames()
    {
        return Collections.enumeration(_initParameters.keySet());
    }

    public void setInitParameter(String name, String value)
    {
        _initParameters.put(name, value);
    }

    public String getServletContextName()
    {
        return _servletContextName;
    }

    public MockSession createSession()
    {
        if (_session == null)
        {
            String id = Long.toHexString(System.currentTimeMillis());

            _session = new MockSession(this, id);
        }

        return _session;
    }

    public MockSession getSession()
    {
        return _session;
    }

    public void setServletContextName(String servletContextName)
    {
        _servletContextName = servletContextName;
    }

    public String getRootDirectory()
    {
        return _rootDirectory;
    }

    public void setRootDirectory(String rootDirectory)
    {
        _rootDirectory = rootDirectory;
    }

}
