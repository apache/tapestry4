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

package org.apache.tapestry.junit.mock;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    private static final Map _suffixToContentType = new HashMap();

    static {
        _suffixToContentType.put("html", "text/html");
        _suffixToContentType.put("gif", "image/gif");
        _suffixToContentType.put("png", "image/png");
    }

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
        int lastx = path.lastIndexOf('.');
        String suffix = path.substring(lastx + 1);

        return(String) _suffixToContentType.get(suffix);
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
