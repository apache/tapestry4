package com.primix.foundation;

import java.util.*;

/**
 *  An interface that defines an object that can store named propertys.  The names
 *  and the properties are Strings.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public interface IPropertyHolder
{
	/**
	 *  Returns a Collection of Strings, the names of all
	 *  properties held by the receiver.  May return an empty collection.
	 *
	 */
	 
	public Collection getPropertyNames();
	
	/**
	 *  Sets a named property.  The new value replaces the existing value, if any.
	 *  Setting a property to null is the same as removing the property.
	 *
	 */
	 
	public void setProperty(String name, String value);
	
	/**
	 *  Removes the named property, if present.
	 *
	 */
	 
	public void removeProperty(String name);
	
	/**
	 *  Retrieves the named property, or null if the property is not defined.
	 *
	 */
	 
	public String getProperty(String name);
}