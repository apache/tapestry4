//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.contrib.valid;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.StringValidator;
import org.apache.tapestry.valid.ValidField;

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
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *  @see ValidField
 * 
 **/

public abstract class ValidatingTextField extends ValidField
{
	private IBinding minimumLengthBinding;
	private IBinding requiredBinding;
	private IBinding valueBinding;

	/* (non-Javadoc)
	 * @see org.apache.tapestry.valid.ValidField#getValue()
	 */
	public Object getValue()
	{
		if (getTextBinding() != null)
		{
			return getTextBinding().getObject();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.tapestry.valid.ValidField#setValue(java.lang.Object)
	 */
	public void setValue(Object value)
	{
		if(getTextBinding() != null) {
			getTextBinding().setObject(value);
		}			
		// otherwise do nothing, we have nowhere to bind the value to
	}

	public IBinding getValueBinding()
	{
		return valueBinding;
	}

	public void setValueBinding(IBinding binding)
	{
		valueBinding = binding;
	}

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

	/**
	 * Overrides {@link ValidField#getValidator()} to construct
	 * a validator on the fly.
	 * 
	 **/
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