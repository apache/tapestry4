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
 *  Defines the interface for an object that tracks validation errors for
 *  fields.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 */

public interface IFieldTracking
{
	/**
	 * Returns the field component.
	 * 
	 **/
	
	public IField getField();
	
	/**
	 *  Returns the localized error message recorded for the field.
	 * 
	 **/
	
	public String getErrorMessage();
	
	public void setErrorMessage(String value);
	

	/**
	 *  Returns the invalid input recorded for the field.  This is stored
	 *  so that, on a subsequent render, the smae invalid input can be presented
	 *  to the client to be corrected.
	 * 
	 **/
	
	public String getInvalidInput();
	
	public void setInvalidInput(String value);
	
	/**
	 *  Returns the name of the field, that is, the name assigned by the form
	 *  (this will differ from the field's id when any kind of looping operation
	 *  is in effect).
	 * 
	 **/
	
	public String getFieldName();
	
	/**
	 *  Returns the validation constraint that was violated by the input.  This
	 *  may be null if the constraint isn't known.
	 * 
	 **/
	
	public ValidationConstraint getConstraint();

	public void setConstraint(ValidationConstraint value);
}

