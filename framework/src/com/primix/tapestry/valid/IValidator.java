/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */
 
package com.primix.tapestry.valid;

/**
 *  An object that works with an {@link IField} to format output
 *  (convert object values to strings values) and to process input
 *  (convert strings to object values and validate them).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 */
public interface IValidator
{
	/**
	 *  All validators must implement a required property.  If true,
	 *  the client must supply a non-null value.
	 *  
	 **/
	
	public boolean isRequired();
	
	/**
	 *  Invoked during renderring to convert an object value (which may be null)
	 *  to a String.  It is acceptible to return null.  The string will be the
	 *  VALUE attribute of the HTML text field.
	 * 
	 **/
	
	public String toString(IField field, Object value);
	
	
	/**
	 *  Converts input, submitted by the client, into an object value.
	 *  May return null if the input is null (and the required flag is false).
	 * 
	 *  <p>The input string will already have been trimmed.  It may be null.
	 * 
	 *  @throws ValidatorException if the string cannot be converted into
	 *  an object, or the object is
	 *  not valid (due to other constraints).
	 **/
	
	public Object toObject(IField field, String input)
	throws ValidatorException;
	
}

