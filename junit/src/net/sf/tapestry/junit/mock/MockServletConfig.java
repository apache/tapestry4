package net.sf.tapestry.junit.mock;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 *  An implementation of {@link javax.servlet.ServletConfig} used
 *  for Mock testing. 
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class MockServletConfig implements ServletConfig, IInitParameterHolder
{
    private String _name;
    private ServletContext _context;
    private Map _initParameters = new HashMap();

    public MockServletConfig(String name, ServletContext context)
    {
        _name = name;
        _context = context;
    }

    public String getInitParameter(String name)
    {
        return (String) _initParameters.get(name);
    }

    public Enumeration getInitParameterNames()
    {
        return Collections.enumeration(_initParameters.keySet());
    }

    public ServletContext getServletContext()
    {
        return _context;
    }

    public String getServletName()
    {
        return _name;
    }

    public void setInitParameter(String name, String value)
    {
        _initParameters.put(name, value);
    }

}
