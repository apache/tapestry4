/*
 * Tapestry Web Application Framework
 * Copyright (c) 2002 by Howard Lewis Ship
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

package net.sf.tapestry.contrib.valid;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.form.*;

import com.primix.tapestry.valid.IValidator;
import com.primix.tapestry.valid.StringValidator;
import com.primix.tapestry.valid.ValidField;

import java.util.*;
import java.text.*;

/**
 *
 *  Backwards compatible version of the 1.0.7 ValidatingTextField component.
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
 *  <tr>
 *    <td>text</td>
 *    <td>java.lang.String</td>
 *    <td>R / W</td>
 *   	<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The text inside the text field.
 *
 *  <p>When the form is submitted, the binding is only updated if the value
 *  is valid.</td>
 *	</tr>
 *
 *  <tr>
 *      <td>minimumLength</td>
 *      <td>int</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>0</td>
 *      <td>The minimum length (number of characters read) for the field.  The
 *  value provided in the request is trimmed of leading and trailing whitespace.
 *
 *  <p>If a field is not required and no value is given, then minimumLength is ignored.
 *  Minimum length only applies if <em>some</em> non-null value is given.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>required</td>
 *      <td>boolean</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>false</td>
 *      <td>If true, then a non-null value must be provided.  A value consisting
 *  only of whitespace is considered null. </td>
 *  </tr>
 *
 *  <tr>
 *      <td>displayName</td>
 *      <td>String</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>A textual name for the field that is used when formulating error messages.
 *      </td>
 *  </tr>
 *
 *	</table>
 *
 *  <p>May not have a body.  May have informal parameters.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.8
 *  @see ValidField
 */

public class ValidatingTextField extends ValidField
{
	private IBinding minimumLengthBinding;
	private IBinding requiredBinding;

	/** Returns the valueBinding. **/
	
	public IBinding getTextBinding()
	{
		return getValueBinding();
	}
	
	/** Updates valueBinding. **/
	
	public void setTextBinding(IBinding value)
	{
		setValueBinding(value);
	}

	public IBinding getMinimumLengthBinding()
	{
		return minimumLengthBinding;
	}

	public void setMinimumLengthBinding(IBinding value)
	{
		minimumLengthBinding = value;
	}


	public IBinding getRequiredBinding()
	{
		return requiredBinding;
	}

	public void setRequiredBinding(IBinding requiredBinding)
	{
		this.requiredBinding = requiredBinding;
	}

	/*
	 * Overrides {@link ValidField#getValidator()} to construct
	 * a validator on the fly.
	 * 
	 */
	 
	public IValidator getValidator()
	{
		StringValidator validator = new StringValidator();
		
		if (requiredBinding != null)
		{
			boolean required = requiredBinding.getBoolean();
			
			validator.setRequired(required);
		}
		
		if (minimumLengthBinding != null)
		{
			int minimumLength = minimumLengthBinding.getInt();
			
			validator.setMinimumLength(minimumLength);
		}
		
		return validator;
	}
}