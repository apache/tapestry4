/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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