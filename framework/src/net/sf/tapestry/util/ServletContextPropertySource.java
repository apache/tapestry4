package net.sf.tapestry.util;

/**
 *  Implementation of {@link net.sf.tapestry.IPropertySource}
 *  that returns values defined as ServletContext initialization parameters
 *  (defined as <code>&lt;init-param&gt;</code> in the
 *  <code>web.xml</code> deployment descriptor.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *
 **/ 

import javax.servlet.ServletContext;

import net.sf.tapestry.IPropertySource;

public class ServletContextPropertySource implements IPropertySource
{
    private ServletContext _context;

    public ServletContextPropertySource(ServletContext context)
    {
        _context = context;
    }

    /**
     *  Invokes {@link ServletContext#getInitParameter(java.lang.String)}.
     * 
     **/
    
    public String getPropertyValue(String propertyName)
    {
        return _context.getInitParameter(propertyName);
    }

}
