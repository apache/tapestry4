package net.sf.tapestry.junit.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.sf.tapestry.ApplicationRuntimeException;

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

        return (String[]) keys.toArray(array);
    }

    public void setAttribute(String name, Object value)
    {
        _attributes.put(name, value);
    }

    public void removeAttribute(String name)
    {
        _attributes.remove(name);
    }

    /**
     *  Serializes and then deserializes the {@link Map}
     *  containing all attributes.
     * 
     **/

    public void simulateFailover()
    {
        byte[] serialized = serializeAttributes();
        
        _attributes = null;
        
        Map restoredAttributes = deserializeAttributes(serialized);
        
        _attributes = restoredAttributes;
    }

    private byte[] serializeAttributes()
    {
        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(_attributes);

            oos.close();

            return bos.toByteArray();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException("Unable to serialize attributes.", ex);
        }
    }

    private Map deserializeAttributes(byte[] serialized)
    {
        try
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
            ObjectInputStream ois = new ObjectInputStream(bis);

            Map result = (Map) ois.readObject();

            return result;
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException("Unable to deserialize attributes.", ex);
        }
    }
}
