package net.sf.tapestry.junit.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *  Mock implementation of {@link javax.servlet.http.HttpServletRequest}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class MockRequest 
extends AttributeHolder 
implements HttpServletRequest
{
    /**
     *  Map of String[].
     * 
     **/
    
    private Map _parameters = new HashMap();
    
    private String _method = "GET";
    
    private String _contextPath = "/context";
    
    private MockContext _servletContext;
    private MockSession _session;
    
    public String getAuthType()
    {
        return null;
    }

    public Cookie[] getCookies()
    {
        return null;
    }

    public long getDateHeader(String arg0)
    {
        return 0;
    }

    public String getHeader(String arg0)
    {
        return null;
    }

    public Enumeration getHeaders(String arg0)
    {
        return null;
    }

    public Enumeration getHeaderNames()
    {
        return null;
    }

    public int getIntHeader(String arg0)
    {
        return 0;
    }

    public String getMethod()
    {
        return _method;
    }

    public String getPathInfo()
    {
        return null;
    }

    public String getPathTranslated()
    {
        return null;
    }

    public String getContextPath()
    {
        return _contextPath;
    }

    public String getQueryString()
    {
        return null;
    }

    public String getRemoteUser()
    {
        return null;
    }

    public boolean isUserInRole(String arg0)
    {
        return false;
    }

    public Principal getUserPrincipal()
    {
        return null;
    }

    public String getRequestedSessionId()
    {
        return null;
    }

    public String getRequestURI()
    {
        return null;
    }

    public StringBuffer getRequestURL()
    {
        return null;
    }

    public String getServletPath()
    {
        return null;
    }

    public HttpSession getSession(boolean arg0)
    {
        if (_session == null)
            _session = _servletContext.createSession();
            
            return _session;
    }

    public HttpSession getSession()
    {
        return _session;
    }

    public boolean isRequestedSessionIdValid()
    {
        return false;
    }

    public boolean isRequestedSessionIdFromCookie()
    {
        return false;
    }

    public boolean isRequestedSessionIdFromURL()
    {
        return false;
    }

    public boolean isRequestedSessionIdFromUrl()
    {
        return false;
    }


    public String getCharacterEncoding()
    {
        return null;
    }

    public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException
    {
    }

    public int getContentLength()
    {
        return 0;
    }

    public String getContentType()
    {
        return null;
    }

    public ServletInputStream getInputStream() throws IOException
    {
        return null;
    }

    public String getParameter(String name)
    {
        String[] values = getParameterValues(name);
        
        if (values == null || values.length == 0)
            return null;
            
            return values[0];
    }

    public Enumeration getParameterNames()
    {
        return Collections.enumeration(_parameters.keySet());
    }

    public String[] getParameterValues(String name)
    {
        return (String[])_parameters.get(name);
    }

    /** 
     *  Not part of 2.1 API, not used by Tapestry.
     * 
     **/
    
    public Map getParameterMap()
    {
        return null;
    }

    public String getProtocol()
    {
        return null;
    }

    public String getScheme()
    {
        return null;
    }

    public String getServerName()
    {
        return null;
    }

    public int getServerPort()
    {
        return 0;
    }

    public BufferedReader getReader() throws IOException
    {
        return null;
    }

    public String getRemoteAddr()
    {
        return null;
    }

    public String getRemoteHost()
    {
        return null;
    }

    public Locale getLocale()
    {
        return null;
    }

    public Enumeration getLocales()
    {
        return null;
    }

    public boolean isSecure()
    {
        return false;
    }

    public RequestDispatcher getRequestDispatcher(String arg0)
    {
        return null;
    }

    public String getRealPath(String arg0)
    {
        return null;
    }

    public void setContextPath(String contextPath)
    {
        _contextPath = contextPath;
    }

    public void setMethod(String method)
    {
        _method = method;
    }

    public void setParameter(String name, String[] values)
    {
        _parameters.put(name, values);
    }
    
    public void setParameter(String name, String value)
    {
        setParameter(name, new String[] { value });
    }

}
