package net.sf.tapestry.junit.mock;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 *  
 *  Base class for holders of named attributes such as
 *  {@link javax.servlet.http.HttpSession}, 
 *  {@link javax.servlet.http.HttpServletRequest}
 *  and {@link javax.servlet.ServletContext}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class AttributeHolder
{
    private Map _attributes = new HashMap();
    
    public Object getAttribute(String name)
    {
        return _attributes.get(name);
    }


    public Enumeration getAttributeNames()
    {
        return getEnumeration(_attributes);
    }

    protected Enumeration getEnumeration(Map map)
    {
        return Collections.enumeration(map.keySet());
    }

    public String[] getAttributeNamesArray()
    {
        Set keys = _attributes.keySet();
        int count = keys.size();
        
        String[] array = new String[count];
        
        return (String[])keys.toArray(array);
    }

    public void setAttribute(String name, Object value)
    {
        _attributes.put(name, value);
    }


    public void removeAttribute(String name)
    {
        _attributes.remove(name);
    }

}
