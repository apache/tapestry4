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

import com.primix.tapestry.*;
import java.util.List;
import java.util.Map;

/**
 *  Interface used to communicate errors from an {@link IField} 
 *  component to some application-specific code. In addition,
 *  controls how fields that are in error are presented (they can be
 *  marked in various ways by the delegate; the default implementation
 *  adds two red asterisks to the right of the field).
 *
 *  <p>The interface is designed so that a single instance can be shared
 *  with many instances of {@link IField}.
 *
 *  <p>Starting with release 1.0.8, this interface was extensively revised
 *  (in a non-backwards compatible way) to move the tracking of errors and
 *  invalid values (during a request cycle) to the delegate.  It has evolved from
 *  a largely stateless conduit for error messages into a very stateful tracker
 *  of field state.
 *  
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public interface IValidationDelegate
{
	/**
	 *  Invoked before other methods to configure the delegate for the given field.
	 * 
	 *  @since 1.0.8
	 *
	 **/
	
	public void setField(IField field);

	/**
	 *  Returns true if the current field is in error (that is, had bad input
	 *  submitted by the end user).
	 * 
	 *  @since 1.0.8
	 * 
	 **/
	
	public boolean isInError();
	
	/**
	 *  Returns the invalid string submitted by the client.
	 * 
	 * @since 1.0.8
	 * 
	 **/

	public String getInvalidInput();


	/**
	 *  Returns a {@link List} of {@link IFieldTracking}, in default order
	 *  (the order in which fields are renderred). A caller should
	 *  not change the values (the List is immutable).  
	 *  May return null if no fields are in error.
	 * 
	 *  @since 1.0.8
	 **/
	
	public List getFieldTracking();

	/**
	 *  Resets any tracking information for the current field.  This will
	 *  clear the field's inError flag, and set its error message and invalid input value
	 *  to null.
	 * 
	 *  @since 1.0.8
	 * 
	 **/
	
	public void reset();

	/**
	 *  The error notification method, invoked during the rewind phase
	 *  (that is, while HTTP parameters are being extracted from the request
	 *  and assigned to various object properties).  
	 *
	 *  <p>Typically, the delegate stores this information directly
	 *  into a {@link IValidationFieldTracking}.
	 *  Finicky delegates may, instead, use the constraint and
	 *  displayName (from the field) to form their own error message.
	 *
	 */

	public void record(ValidatorException ex);

	/**
	 *  Invoked before the field is rendered.  If the field is in error,
	 *  the delegate may decorate the field in some way (to highlight its
	 *  error state).
	 *
	 */

	public void writePrefix(
		IResponseWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException;

	/**
	 *  Invoked just before the &lt;input type=text&gt; element is closed.
	 *  The delete can write additional attributes.  This is often used
	 *  to set the CSS class of the field so that it can be displayed 
	 *  differently, if in error (or required).
	 *
	 *  @since 1.0.5
	 */

	public void writeAttributes(
		IResponseWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException;

	/**
	 *  Invoked after the field is rendered, so that the
	 *  delegate may decorate the field (if it is in error).
	 *
	 */

	public void writeSuffix(
		IResponseWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException;

	/**
	 *  Invoked by a {@link FieldLabel} just before writing the name
	 *  of the field.
	 *
	 */

	public void writeLabelPrefix(
		IField field,
		IResponseWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException;

	/**
	 *  Invoked by a {@link FieldLabel} just after writing the name
	 *  of the field.
	 *
	 */

	public void writeLabelSuffix(
		IField field,
		IResponseWriter writer,
		IRequestCycle cycle)
		throws RequestCycleException;
		
	/**
	 *   Returns true if any field has errors.
	 * 
	 **/
	
	public boolean getHasErrors();
}