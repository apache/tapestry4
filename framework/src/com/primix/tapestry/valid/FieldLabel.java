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

package com.primix.tapestry.valid;

import com.primix.tapestry.*;

/**
 *  Used to label an {@link IValidatingTextField}.  Because such fields
 *  know their displayName (user-presentable name), there's no reason
 *  to hard code the label in a page's HTML template (this also helps
 *  with localization).

 *  <p>A FieldLabel
 *  may also have a {@link IValidationDelegate delegate} that
 *  modifies the formatting of the label to match the state of the
 *  field (i.e., if the field is required or in error).
 *
 *
 * <table border=1>
 * <tr> 
 *    <td>Parameter</td>
 *    <td>Type</td>
 *	  <td>Read / Write </td>
 *    <td>Required</td> 
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 * <tr>
 *  <td>field</td>
 *  <td>{@link IValidatingTextField}</td>
 *  <td>R</td>
 *  <td>yes</td>
 *  <td>&nbsp;</td>
 *  <td>The field to be labeled.</td>
 * </tr>
 *
 * <tr>
 *   <td>delegate</td>
 *   <td>{@link IValidationDelegate}</td>
 *   <td>R</td>
 *   <td>no</td>
 *   <td>&nbsp;</td>
 *   <td>An optional delegate that may provide additional formatting
 *  for the label.</td>
 *  </tr>
 *
 *  </table>
 *
 * <p>Informal parameters are not allowed.  A body is not allowed.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class FieldLabel
extends AbstractComponent
{
	private IBinding fieldBinding;
	private IBinding delegateBinding;

	public void setFieldBinding(IBinding value)
	{
		fieldBinding = value;
	}

	public IBinding getFieldBinding()
	{
		return fieldBinding;
	}

	public void setDelegateBinding(IBinding value)
	{
		delegateBinding = value;
	}

	public IBinding getDelegateBinding()
	{
		return delegateBinding;
	}

	/**
	*  Gets the {@link IValidatingTextField} 
	*  and optional {@link IValidationDelegate delegate},
	*  then renders the label obtained from the field.
	*
	*/

	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		IValidatingTextField field;
		IValidationDelegate delegate = null;

		if (cycle.isRewinding())
			return;

		try
		{
			field = (IValidatingTextField)fieldBinding.getObject("field",
				IValidatingTextField.class);

			if (field == null)
				throw new RequiredParameterException(this, "field", fieldBinding);

			if (delegateBinding != null)
				delegate = (IValidationDelegate)delegateBinding.getObject("delegate",
					IValidationDelegate.class);

			if (delegate != null)
				delegate.writeLabelPrefix(field, writer, cycle);

			writer.print(field.getDisplayName());

			if (delegate != null)
				delegate.writeLabelSuffix(field, writer, cycle);
		}
		catch (BindingException ex)
		{
			throw new RequestCycleException(this, ex);
		}
	}
}
