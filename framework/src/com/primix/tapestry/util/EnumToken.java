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

package com.primix.tapestry.util;

import java.lang.reflect.*;
import java.io.*;

/**
 * Used to represent an {@link Enum}.
 *
 * @author Howard Ship
 * @version $Id$
 */

class EnumToken
{
	private String className;
	private String enumerationId;
    
    private int hashCode = 0;
    
	EnumToken(Enum enum)
	{
		this.className = enum.getClass().getName();
		this.enumerationId = enum.getEnumerationId();
	}
    
	EnumToken(String className, String enumerationId)
	{
		this.className = className;
		this.enumerationId = enumerationId;
	}
    
	public boolean equals(Object object)
	{
		if (object == null)
			return false;

		if (this == object)
			return true;

		if (!(object instanceof EnumToken))
        	return false;

		EnumToken other = (EnumToken)object;

		return className.equals(other.className) &&
			   enumerationId.equals(other.enumerationId);

	}
    
	/**
	*  Computes a hash code based on the hash code's of the className and
	*  serializationId properties.  The value is cached (very useful in
    *  pre-JDK 1.3 JVMs, since they did <em>not</em> cache the hash code
    *  of Strings (!!!).
	*
	*/

	public int hashCode()
	{
    	if (hashCode == 0)
        	hashCode = className.hashCode() ^ enumerationId.hashCode();
        
        return hashCode;    
	}
    
	public String toString()
	{
		StringBuffer buffer;

		buffer = new StringBuffer("EnumToken[");

		buffer.append(className);
		buffer.append(' ');
		buffer.append(enumerationId);
		buffer.append(']');

		return buffer.toString();
	}
}

