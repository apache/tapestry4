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
import com.primix.tapestry.components.*;
import com.primix.tapestry.event.PageDetachListener;
import com.primix.tapestry.event.PageEvent;
import com.primix.tapestry.form.*;
import com.primix.tapestry.form.IFormComponent;
import com.primix.tapestry.html.Body;
import java.util.*;
import java.text.*;

/**
 *
 *  A {@link Form} component that creates a text field that
 *  allows for validation of user input and conversion between string and object
 *  values.  A ValidatingTextField uses an {@link IValidationDelegate} to 
 *  track errors and an {@link IValidator} to convert between strings and objects
 *  (as well as perform validations).  The validation delegate is shared by all validating
 *  text fields in a form, the validator may be shared my multiple elements as desired.
 * 
 * <p>This class was heavily redesigned in release 1.0.8. 
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
 *    <td>java.lang.Object</td>
 *    <td>R / W</td>
 *   	<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The value to be displayed (on render), and updated (when the form is
 *  submitted, if the submitted value is valid).  The {@link ITranslator} converts between
 *  object values and Strings.
 *  </td>
 *	</tr>
 *
 *	<tr>
 *		<td>hidden</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>If true, then the text field is written as a
 *			&lt;input type=password&gt; form element.  </td>  </tr>
 *
 *  <tr>
 * 		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>Controls whether the text field is active or not.  If disabled, then
 *			any value that comes up when the form is submitted is ignored.
 *			
 *			<p>Corresponds to the <code>disabled</code> HTML attribute.</td>
 *	</tr>
 *
 *	<tr>
 *		<td>displayWidth</td>
 *		<td>integer</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>Controls the display width of the text control in the client browser.  If
 *			unspecified or zero, then the width is left to the client browser to
 *			determine.
 *
 *		<p>Corresponds to the <code>size</code> HTML attribute.</td> </tr>
 *
 *	<tr>
 *		<td>maximumLength</td>
 *		<td>integer</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>Controls the maximum characters that the text control will accept.  If
 *			unspecified or zero, then the value is left to the client browser to
 *			determine.
 *
 *		<p>Corresponds to the <code>maxlength</code> HTML attribute.</td> </tr>
 *
 *  <tr>
 *      <td>displayName</td>
 *      <td>String</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>A textual name for the field that is used when formulating error messages.
 * 	Also used by the {@link FieldLabel} component to properly label the field.
 *      </td>
 *  </tr>

 *
 *  <tr>
 *      <td>validator</td>
 *      <td>{@link IValidator}</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>Object used to convert object values to Strings (for renderring)
 *  and to validate and convert Strings into object values (when the form
 *  is submitted).</td>
 *  </tr>
 *
 *	</table>
 *
 *  <p>May not have a body.  May have informal parameters,
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class ValidField extends AbstractTextField
	implements IField, IFormComponent, PageDetachListener
{
	private IBinding valueBinding;

	private IBinding displayNameBinding;
	private String displayNameValue;
	
	private IBinding validatorBinding;
	private IValidator validator;
	

	/**
	 *  Registers this component as a {@link PageDetachListener}.
	 *
	 */

	protected void finishLoad()
	{
		page.addPageDetachListener(this);
	}


	public IBinding getValueBinding()
	{
		return valueBinding;
	}
	
	public void setValueBinding(IBinding value)
	{
		valueBinding = value;
	}

	public IBinding getDisplayNameBinding()
	{
		return displayNameBinding;
	}

	public void setDisplayNameBinding(IBinding value)
	{
		displayNameBinding = value;

		if (value.isStatic())
			displayNameValue = value.getString();
	}

	public String getDisplayName()
	{
		// Return the static value, if known.

		if (displayNameValue != null)
			return displayNameValue;

		// Otherwise, a dynamic value (how strange).

		return displayNameBinding.getString();
	}


	/**
	 *  Return the component's {@link IValidator}, or
	 *  throws {@link NullValueForBindingException}.  The validator
	 *  is only resolved <em>once</em> per request cycle.
	 * 
	 **/

	public IValidator getValidator()
	{
		if (validator == null)
		{
			validator = (IValidator)validatorBinding.getObject(
			"validator",
			IValidator.class);
			
			if (validator == null)
				throw new NullValueForBindingException(validatorBinding);
		}
		
		return validator;
	}


	/**
	 *  Clear the validator property at the end of request cycle.
	 *
	 *  @since 1.0.5
	 */

	public void pageDetached(PageEvent event)
	{
		validator = null;
	}

	/**
	 *
	 * Renders the component, which involves the {@link IValidationDelegate delegate}.
	 *
	 * <p>During a render, the <em>first</em> field rendered that is either
	 * in error, or required but null gets special treatment.  JavaScript is added
	 * to select that field (such that the cursor jumps right to the field when the
	 * page loads).
	 *
	 */

	public void render(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		boolean rendering;
		IValidationDelegate delegate = getForm().getDelegate();
		IValidator translator = getValidator();
		
		String displayName = null;

		rendering = !cycle.isRewinding();

		delegate.setField(this);
		
		if (rendering)
			delegate.writePrefix(writer, cycle);

		super.render(writer, cycle);

		if (rendering)
			delegate.writeSuffix(writer, cycle);

		// If rendering and there's either an error in the field,
		// or the field is required but the value is currently null,
		// then we may have identified the default field (which will
		// automatically receive focus).

		if (rendering && delegate.isInError())
			addSelect(cycle);

		// That's OK, but an ideal situation would know about non-validating
		// text fields, and also be able to put the cursor in the
		// first field, period (even if there are no required or error fields).
		// Still, this pretty much rocks!

	}

	/**
	 *  Invokes {@link IValidationDelegate#writeAttributes(IResponseWriter,IRequestCycle)}.
	 *
	 */

	protected void beforeCloseTag(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		getForm().getDelegate().writeAttributes(writer, cycle);
	}

	private static final String SELECTED_ATTRIBUTE_NAME =
		"com.primix.tapestry.component.html.valid.SelectedFieldSet";

	/**
	 *  Creates JavaScript to set the cursor on the first required or error
	 *  field encountered while rendering.  This only works if the text field
	 *  is wrapped by a {@link Body} component (which is almost always true).
	 *
	 */

	private void addSelect(IRequestCycle cycle)
	{
		// If some other field has taken the honors, then let it.

		if (cycle.getAttribute(SELECTED_ATTRIBUTE_NAME) != null)
			return;

		Body body = Body.get(cycle);

		// If not wrapped by a Body, then do nothing.

		if (body == null)
			return;

		IForm form = Form.get(cycle);

		String formName = form.getName();
		String textFieldName = getName();

		String fullName = "document." + formName + "." + textFieldName;

		body.addOtherInitialization(fullName + ".focus();");
		body.addOtherInitialization(fullName + ".select();");

		// Put a marker in, indicating that the selected field is known.

		cycle.setAttribute(SELECTED_ATTRIBUTE_NAME, Boolean.TRUE);
	}


	protected String readValue()
	throws RequestCycleException
	{
		IValidationDelegate delegate = getForm().getDelegate();
		
		if (delegate.isInError())
			return delegate.getInvalidInput();
			
		Object value = valueBinding.getObject();
		IValidator validator = getValidator();
		
		String result = validator.toString(this, value);
		
		if (Tapestry.isNull(result) && validator.isRequired())
			addSelect(getPage().getRequestCycle());
			
		return result;
	}
	
	protected void updateValue(String value)
	throws RequestCycleException
	{
		IValidator validator = getValidator();
		Object objectValue = null;
		IValidationDelegate delegate = getForm().getDelegate();
		
		try
		{
			objectValue = validator.toObject(this, value);
		}
		catch (ValidatorException ex)
		{
			delegate.record(ex);
			return;
		}

		valueBinding.setObject(objectValue);
		delegate.reset();
	}

	public Class getValueType()
	{
		return valueBinding.getType();
	}

	public IBinding getValidatorBinding()
	{
		return validatorBinding;
	}

	public void setValidatorBinding(IBinding value)
	{
		validatorBinding = value;
	}

}