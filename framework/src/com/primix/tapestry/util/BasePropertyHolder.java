package com.primix.foundation;

import java.util.*;

/**
 *  Base class implementation for the {@link IPropertyHolder} interface.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class BasePropertyHolder
implements IPropertyHolder
{
	private static final int MAP_SIZE = 7;
	private Map properties;
	
	public String getProperty(String name)
	{
		if (properties == null)
			return null;
		
		return (String)properties.get(name);
	}
	
	public void setProperty(String name, String value)
	{
		if (value == null)
		{
			removeProperty(name);
			return;
		}	
		
		if (properties == null)
			properties = new HashMap(MAP_SIZE);
		
		properties.put(name, value);
	}
	
	public void removeProperty(String name)
	{
		if (properties == null)
			return;
			
		properties.remove(name);
	}
	
	public Collection getPropertyNames()
	{
		if (properties == null)
			return Collections.EMPTY_LIST;
			
		// Slightly dangerous, should wraps this as an unmodifiable, perhaps?
		
		return properties.keySet();
	}

}