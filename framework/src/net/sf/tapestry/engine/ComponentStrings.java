package net.sf.tapestry.engine;

import java.util.Properties;

import net.sf.tapestry.IComponentStrings;

/**
 *  Implementation of {@link IComponentStrings}.  This is basically
 *  a wrapper around an instance of {@link Properties}.  This ensures
 *  that the properties are, in fact, read-only (which ensures that
 *  they don't have to be synchronized).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public class ComponentStrings implements IComponentStrings
{
	private Properties properties;
	
	public ComponentStrings(Properties properties)
	{
	    this.properties = properties;
	}
	
    public String getString(String key, String defaultValue)
		{
		    return  properties.getProperty(key, defaultValue);
    }

    public String getString(String key)
    {
		String result = properties.getProperty(key);
		
		if (result == null)
			result = "[" + key.toUpperCase() + "]";
			
		return result;
    }

}
