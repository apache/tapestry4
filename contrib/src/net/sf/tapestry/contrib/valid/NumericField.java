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

import com.primix.tapestry.util.*;
import com.primix.tapestry.valid.IValidator;
import com.primix.tapestry.valid.NumberValidator;
import com.primix.tapestry.valid.ValidField;

import java.util.*;
import java.text.*;
import java.math.*;

/**
 *
 * Backwards compatible version of the 1.0.7 NumericField component.
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
 *    <td>value</td>
 *    <td>{@link Number}</td>
 *    <td>R / W</td>
 *   	<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The value to be updated.
 *
 *  <p>When the form is submitted, this parameter is only updated if the value
 *  is valid.
 *
 *  <p>When rendering, a null value will render as the empty string.  A value
 *  of zero will render normally.
 *
 *  <p>When the form is submitted, the {@link IBinding#getType() type} of the binding
 *  is used to determine what kind of object to convert the string to.
 *
 * </td>
 *	</tr>
 *
 *  <tr>
 *      <td>minimum</td>
 *      <td>{@link Number}</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>&nbsp;</td>
 *      <td>The minimum value accepted for the field.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>maximum</td>
 *      <td>{@link Number}</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>&nbsp;</td>
 *      <td>The maximum value accepted for the field.</td>
 * </tr>
 *
 *  <tr>
 *      <td>required</td>
 *      <td>boolean</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>false</td>
 *      <td>If true, then a non-null value must be provided. If the field is not
 * required, and a null (all whitespace) value is supplied in the field, then the
 * value parameter is <em>not</em> updated.</td>
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
 *  <tr>
 *      <td>delegate</td>
 *      <td>{@link IValidationDelegate}</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>Object used to assist in error reporting.</td>
 *  </tr>
 *
 *	</table>
 *
 *  <p>May not contain a body.  May have informal parameters.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.8
 *  @see ValidField
 *
 */

public class NumericField extends ValidField
{
	private IBinding minimumBinding;
	private IBinding maximumBinding;
	private IBinding requiredBinding;

	public IBinding getMinimumBinding()
	{
		return minimumBinding;
	}

	public void setMinimumBinding(IBinding value)
	{
		minimumBinding = value;
	}

	public IBinding getMaximumBinding()
	{
		return maximumBinding;
	}

	public void setMaximumBinding(IBinding value)
	{
		maximumBinding = value;
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
	 */
	 
	public IValidator getValidator()
	{
		NumberValidator validator = new NumberValidator();
		
		if (minimumBinding != null)
		{
			Number minimum = (Number)minimumBinding.getObject("minimum", Number.class);
			validator.setMinimum(minimum);
		}
		
		if (maximumBinding != null)
		{
			Number maximum = (Number)maximumBinding.getObject("maximum", Number.class);
			validator.setMaximum(maximum);
		}
		
		if (requiredBinding != null)
		{
			boolean required = requiredBinding.getBoolean();
			validator.setRequired(required);
		}
		
		return validator;
	}

}