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

import com.primix.tapestry.IRender;
import com.primix.tapestry.components.Block;
import com.primix.tapestry.form.IFormComponent;

/**
 *  Defines the interface for an object that tracks validation errors.  This 
 *  interface is now poorly named, in that it tracks errors that may <em>not</em>
 *  be associated with a specific field.
 * 
 *  <p>The initial release (1.0.8) stored an error <em>message</em>.  Starting in
 *  release 1.0.9, this was changed to an error <em>renderrer</em>.  This increases
 *  the complexity slightly, but allows for much, much greater flexibility in how
 *  errors are ultimately presented to the user.  For example, you could devote part
 *  of a template to a {@link Block} that contained a detail error message and links
 *  to other parts of the application (for example, perhaps a pop-up help message).
 * 
 *  <p>However, in most cases, the renderrer will simply be a wrapper 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 */

public interface IFieldTracking
{
	/**
	 *  Returns the field component.  This may return null if the error
	 *  is not associated with any particular field.
	 * 
	 **/
	
	public IFormComponent getFormComponent();
	
	/**
	 *  Returns an object that will render the error message.
	 * 
	 *  @since 1.0.9
	 * 
	 **/
	
	public IRender getRenderer();
	
	/**
	 *  Sets the error renderrer, the object that will render the error message.
	 *  Typically, this is just a {@link RenderString}, but it could be a component
	 *  or virtually anything.
	 * 
	 *  @since 1.0.9
	 * 
	 **/
	
	public void setRenderer(IRender value);
	

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
	 *  (this will differ from the component's id when any kind of looping operation
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

