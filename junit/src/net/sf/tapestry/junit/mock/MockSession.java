package net.sf.tapestry.junit.mock;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 *  Mock implementation of {@link javax.servlet.http.HttpSession}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class MockSession extends AttributeHolder implements HttpSession
{
    private MockContext _context;
    private String _id;
    
    public MockSession(MockContext context, String id)
    {
        _context = context;
        _id = id;
    }
    
    public long getCreationTime()
    {
        return 0;
    }

    public String getId()
    {
        return _id;
    }

    public long getLastAccessedTime()
    {
        return 0;
    }

    public ServletContext getServletContext()
    {
        return _context;
    }

    public void setMaxInactiveInterval(int arg0)
    {
    }

    public int getMaxInactiveInterval()
    {
        return 0;
    }

    public HttpSessionContext getSessionContext()
    {
        return null;
    }

    public Object getValue(String name)
    {
        return getAttribute(name);
    }

    public String[] getValueNames()
    {
        return getAttributeNamesArray();
    }

    public void putValue(String name, Object value)
    {
        setAttribute(name, value);
    }

    public void removeValue(String name)
    {
        removeAttribute(name);
    }

    public void invalidate()
    {
    }

    public boolean isNew()
    {
        return false;
    }

}
