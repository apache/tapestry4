package net.sf.tapestry.util;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;

import net.sf.tapestry.IPropertySource;

/**
 *  Implementation of {@link net.sf.tapestry.IPropertySource}
 *  that returns values defined as Servlet initialization parameters
 *  (defined as <code>&lt;init-param&gt;</code> in the
 *  <code>web.xml</code> deployment descriptor.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *
 **/ 

public class ServletPropertySource implements IPropertySource
{
    private ServletConfig _config;
    
    public ServletPropertySource(ServletConfig config)
    {
        _config = config;
    }   
    
    /**
     *  Invokes {@link ServletConfig#getInitParameter(java.lang.String)}.
     * 
     **/
    
    public String getPropertyValue(String propertyName)
    {
        return _config.getInitParameter(propertyName);
    }

}
