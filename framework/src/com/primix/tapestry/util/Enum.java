package com.primix.foundation;

import java.io.*;
import java.util.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 * Defines properties of enumerated types.  Enumerated types
 * are special classes that take the place of C enums.  The class
 * typically defines a number of public static constants for the
 * elements of the type, and makes its constructors private.
 *
 * <p>The end result is that you can use simple equality checking
 * (the == operator) for the type.  Since it is still a first-class
 * object, it may also be extended with operations.
 *
 * <p>The problem is serialization.  If you serialize such an object and
 * then deserialize it, you get a different object.  Forunately, as of
 * JDK 1.2, we can override this and use <code>readResolve()</code>
 *
 * <p>When an Enum is constructed, it is recorded into an identity table
 * using a unique enumerationId (the enumerationId must be unique for all instances
 * of the same class).  When the Enum is de-serialized, it uses the enumerationId
 * to locate the existing singleton instance.
 *
 * <p>It would be nice if this class was {@link Externalizable}, not {@link Serializable},
 * but that requires public no-arguments constructors, which would spoil things.
 *
 * @author Howard Ship
 * @version $Id$
 */
 
public class Enum implements Serializable
{
    private String enumerationId;

	/**
	 *  Used to resolve tokens back to <code>Enum</code> instances during deserialization.
	 *  The key is a {@link EnumToken} that identifies the class of the the Enum and
     *  its serialization id.
	 *
	 */

	private static Map identity = new HashMap(23);

	/**
	*  Returns a String that identifies the object.  The combination of class name and
	*  enumerationId should be unique.  The serializationId should be a fixed, 
	*  constant value set in the constructor.
	*/

	public String getEnumerationId()
	{
    	return enumerationId;
	}

	/**
     *  Registers the new Enum.  The serializationId must be non-null and unique
     *  among all instances of Enum for the same class.
     *
     */
     
	protected Enum(String enumerationId)
	{
    	if (enumerationId == null)
        	throw new RuntimeException("Must provide non-null enumerationId.");
        
        this.enumerationId = enumerationId;
        
        register(this);
	}
            
	/**
     *  Returns the class name (with the package stripped off), and the serializationId,
     *  seperated by a period.
     *  For example, "Suit.CLUBS" for org.example.Suit with a serializationId of "CLUBS".
     *
     */
     
    public String toString()
    {
    	StringBuffer buffer;
        String name;
        int dot;
        
        name = getClass().getName();
        
        dot = name.lastIndexOf('.');
 		if (dot > 0)
          name = name.substring(dot + 1);
                
 		buffer = new StringBuffer();
        buffer.append(name);
        buffer.append('[');
        buffer.append(enumerationId);
        buffer.append(']');
        
        return buffer.toString();
    }
        
	/**
	*  Registers the singleton instance for later re-use during deserialization.
	*
	*/

	private static synchronized void register(Enum enum)
	{
    	EnumToken key;
        
        key = new EnumToken(enum);
        
        if (identity.containsKey(key))
        	throw new RuntimeException(
            "com.primix.foundation.Enum.register(): " +
            key + " is already registered.  " +
            "The enumerationId property must be unique within the class.");
            
		identity.put(new EnumToken(enum), enum);
	}

    /**
     *  Invoked from subclass' <code>readResolve()</code> method.
     *
     *  <p>This is a bit of serialization black magic ... the method
     *  <code>readResolve()</code> MUST BE implemented by the actual class, it can't
     *  be inherited.  Subclasses should simply invoke this method:
	 *
	 *  <p><pre>
	 *  private Object readResolve()
	 *  {
	 *  	return getSingleton();
	 *  }
	 *  </pre>
	 *
	 * <p>Since
	 *  <code>readResolve()</code> returns a <code>java.lang.Object</code>
	 *  there's no reason to cast the singleton to <code>Enum</code>.
     *
     */
    
	protected Object getSingleton()
	{
    	Object result;
        EnumToken key;
        
    	key = new EnumToken(this);
        
        // Get the previously registered version.
        
        synchronized(identity)     
        {
        	result = identity.get(key);
        }
        
        if (result == null)
        	throw new RuntimeException(
            key + " does not map to a known instance in this JVM.  " +
            "This instance of " + getClass().getName() + " was serialized in a JVM that defined " +
            "a different set of values.");
            
        return result;
	}


}
