package com.primix.tapestry.components.html.valid;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.components.html.form.*;
import java.util.*;
import java.text.*;

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

/**
 * 
 *  A {@link Form} component that can be used to
 *  create a text field that validates that the user
 *  has entered a value (if required) and that the
 *  value has a known minimum length.
 *
 *  <p>When the form is submitted, the result is filtered:  If it fails
 *  validation, then no update (through the text binding) is performed.  Instead,
 *  an error flag is set.  Also, the invalid text is kept so that it can
 *  be the default value for the form element when the page is rendered.
 *
 *  <p>Doesn't work inside a {@link Foreach} ... it maintains a little bit
 *  of state (invalid text, error flag) 
 *  that will get confused if the component is re-used on the page.
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
 *		<td>The text inside the text field.  This value will only be
 *  read once per request cycle (don't use a ValidatingTextField inside
 *  a {@link Foreach}).
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
 *  <tr>
 *      <td>delegate</td>
 *      <td>{@link IValidationDelegate}</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>Object used to assist in error reporting.</td>
 *  </tr>
 *
 *  <tr>
 * 		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>Applied to the underlying {@link TextField}.</td>
 *	</tr>
 *
 *  <tr>
 * 		<td>hidden</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>Applied to the underlying {@link TextField}.</td>
 *	</tr>
 *
 *	<tr>
 *		<td>displayWidth</td>
 *		<td>integer</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>Applied to the underlying {@link TextField}.</tr>
 *
 *	<tr>
 *		<td>maximumLength</td>
 *		<td>integer</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>Applied to the underlying {@link TextField}.</td> </tr>
 *
 *	</table>
 *
 *  <p>May not have informal parameters or a body.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class ValidatingTextField
extends AbstractValidatingTextField
implements ILifecycle
{
    private IBinding textBinding;
    private IBinding minimumLengthBinding;

    public IBinding getTextBinding()
    {
        return textBinding;
    }

    public void setTextBinding(IBinding value)
    {
        textBinding = value;
    }

    public IBinding getMinimumLengthBinding()
    {
        return minimumLengthBinding;
    }

    public void setMinimumLengthBinding(IBinding value)
    {
        minimumLengthBinding = value;
    }

    /**
     *  Reads the current value of the text parameter.
     *
     */

    protected String read()
    {
        return textBinding.getString();
    }



    protected void update(String value)
    {
        int minimumLength = 0;
        int length;
        String errorMessage;

        length = value.length();

        if (length == 0)
        {
            if (isRequired())
            {
                errorMessage =
                    getString("field-is-required", getDisplayName());

                notifyDelegate(ValidationConstraint.REQUIRED,
                     errorMessage);

                return;
            }

            textBinding.setObject(value);
            return;
        }


        // Non-zero length, but is there a minimum?

        if (minimumLengthBinding != null)
        {
            minimumLength = minimumLengthBinding.getInt();

            if (length < minimumLength)
            {
                errorMessage =
                    getString("field-too-short", new Integer(minimumLength), 
                    getDisplayName());

                notifyDelegate(ValidationConstraint.MINIMUM_WIDTH,
                     errorMessage);

                return;
            }
        }

        // Valid!  Update through our binding.

        textBinding.setObject(value);

    }

}










