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
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(System.getProperty("user.dir"));
        buffer.append("/context");       
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

    public RequestDispatcher getRequestDispatcher(String path)
    {
        return null;
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

}
