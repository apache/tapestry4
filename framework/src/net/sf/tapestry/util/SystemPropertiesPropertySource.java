package net.sf.tapestry.util;

import net.sf.tapestry.IPropertySource;

/**
 *  Obtain properties from JVM system properties.
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.3
 *
 **/

public class SystemPropertiesPropertySource implements IPropertySource
{
    private static IPropertySource _shared;
    
    public static synchronized IPropertySource getInstance()
    {
        if (_shared == null)
            _shared = new SystemPropertiesPropertySource();
            
        return _shared; 
    }

    /**
     *  Delegates to {@link System#getProperty(java.lang.String)}.
     * 
     **/
    
    public String getPropertyValue(String propertyName)
    {
        return System.getProperty(propertyName);
    }

}
