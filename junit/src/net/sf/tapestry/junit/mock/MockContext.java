package net.sf.tapestry.junit.mock;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
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

public class MockContext extends AttributeHolder
    implements ServletContext
{

    public ServletContext getContext(String arg0)
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

    public String getMimeType(String arg0)
    {
        return null;
    }

    public Set getResourcePaths(String arg0)
    {
        return null;
    }

    public URL getResource(String arg0) throws MalformedURLException
    {
        return null;
    }

    public InputStream getResourceAsStream(String arg0)
    {
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String arg0)
    {
        return null;
    }

    public RequestDispatcher getNamedDispatcher(String arg0)
    {
        return null;
    }

    public Servlet getServlet(String arg0) throws ServletException
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

    public void log(String arg0)
    {
    }

    public void log(Exception arg0, String arg1)
    {
    }

    public void log(String arg0, Throwable arg1)
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

    public String getInitParameter(String arg0)
    {
        return null;
    }

    public Enumeration getInitParameterNames()
    {
        return null;
    }

    public String getServletContextName()
    {
        return null;
    }


    public MockSession createSession()
    {
        String id = Long.toHexString(System.currentTimeMillis());
        
        return new MockSession(this, id);
    }
}
