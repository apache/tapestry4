package com.primix.tapestry.components.html.valid;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.components.html.*;
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
 *  Base class for several classes that validate their input from the user.
 *
 * <p>All subclasses share the following features:
 *	<ul>
 *	<li>They perform a validation when the containing form is submitted
 *  <li>If there's a validation error, they notify their 
 *      {@link IValidationDelegate delegate}
 *  <li>The exact value entered by the user is supplied as the value when the
 *  page is rendered (to allow the user to correct the error)
 *  <li>Because the component is stateful (the value, and the error flag) it should not
 *  be wrapped by a {@link Foreach} component
 *  <li>They are implemented using a {@link TextField} with the id <code>captive</code>
 *  (this is an implementation detail, likely to change)
 *  <li>A body is not allowed
 *  </ul>
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public abstract class AbstractValidatingTextField
extends AbstractComponent
implements ILifecycle, IValidatingTextField
{
	private TextField captive;
	private IValidationDelegate delegate;
	private IBinding delegateBinding;

	private String text;
	private boolean error;

	private IBinding displayNameBinding;
	private String displayNameValue;

	/**
	*  Contains strings used to generate default error messages.
	*
	*/

	private ResourceBundle strings;

	private IBinding requiredBinding;
	private boolean staticRequired;
	private boolean requiredValue;

	/**
	*  Return's the captive {@link TextField}'s name property; this faciliates
	*  client-side scripting involving the component.
	*
	*/

	public String getName()
	{
		return captive.getName();
	}

	public IBinding getRequiredBinding()
	{
		return requiredBinding;
	}

	public void setRequiredBinding(IBinding value)
	{
		requiredBinding = value;

		staticRequired = value.isStatic();
		if (staticRequired)
			requiredValue = value.getBoolean();
	}

	/**
	*  Indicates if the field is required.  Returns false if the required
	*  parameter is unbound.
	*
	*/

	public boolean isRequired()
	{
		if (staticRequired)
			return requiredValue;

		if (requiredBinding == null)
			return false;

		return requiredBinding.getBoolean();
	}

	/**
	*  Gets and formats a localized string from the <code>com.worldlink.presentation.component.ValidationStrings</code>
	*  property bundle.
	*
	*/

	protected String getString(String key, Object[] arguments)
	{
		String string;

		if (strings == null)
			strings = ResourceBundle.getBundle(
				"com.primix.tapestry.components.html.valid.ValidationStrings",
				page.getLocale());

		try
		{
			string = strings.getString(key);
		}
		catch (MissingResourceException e)
		{
			throw new ApplicationRuntimeException(
				"No validation string for key: " + key + ".", e);
		};


		return MessageFormat.format(string, arguments);
	}

	protected String getString(String key, Object argument)
	{
		return getString(key, new Object[]
		{ argument 
		});
	}

	protected String getString(String key, Object arg1, Object arg2)
	{
		return getString(key, new Object[]
		{ arg1, arg2 
		});
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

	public IBinding getDelegateBinding()
	{
		return delegateBinding;
	}

	public void setDelegateBinding(IBinding value)
	{
		delegateBinding = value;
	}

	/**
	*  Returns the component's delegate, or throws 
	*  {@link NullValueForBindingException}.
	*
	*/

	public IValidationDelegate getDelegate()
	{
		if (delegate == null)
		{
			delegate = (IValidationDelegate)delegateBinding.getValue();

			if (delegate == null)
				throw new NullValueForBindingException(delegateBinding);
		}

		return delegate;
	}

	/**
	*  Returns the String representation of the value.  Invokes
	*  {@link #read()} to get that value.
	*
	*/

	public String getText()
	{
		if (text == null)
			text = read();

		return text;
	}


	/**
	*  Invoked by {@link #getText()} to read the underlying data value
	*  (in a way specific to the subclass implementation) and convert it to
	*  a String.
	*
	*/

	protected abstract String read();

	/**
	*  Invoked by the {@link TextField}, we start by storing the
	*  (trimmed) value, and clear the error flag.  We then
	*  invoke {@link #update(String)} to validate the value and, if acceptible,
	*  update through the binding.
	*
	*/

	public void setText(String value)
	{
		error = false;

		// Record the text value supplied, after trimming it.  Note that if
		// there's an error (and the same page is rendered as a response)
		// then the invalid value will be sent back to the user.

		if (value == null)
			text = "";
		else
			text = value.trim();

		update(text);
	}

	/**
	*  Invoked from {@link #setText(String)} to validate that the new value
	*  (submitted in the form by the user) conforms to the rules for
	*  this component.  If not, it should invoke
	* {@link #notifyDelegate(ValidationConstraint, String)}.
	*
	*  <p>If the value is acceptible, then the component should update
	*  through its parameter (this is very component specific).
	*/

	protected abstract void update(String value);

	/**
	*  Invoked (from {@link #update(String)}, usually} to indicate an error
	*  converting from the submitted text value to the appropriate
	*  data value.
	*
	*  <p>Sets the error flag to true, then invokes
	*  {@link IValidationDelegate#invalidField(IValidatingTextField, ValidationConstraint, String)}
	*  to tell the listener that there was an error.
	*
	*/

	protected void notifyDelegate(ValidationConstraint constraint, 
		String defaultErrorMessage)
	{
		error = true;

		getDelegate().invalidField(this, constraint, defaultErrorMessage);
	}

	/**
	*  Returns true if there was a validation error.
	*
	*/

	public boolean getError()
	{
		return error;
	}

	/**
	*  Allows the error state to be set externally.  This may occur
	*  if two fields are validated against each other (for example,
	*  two {@link DateField}s that specify a range).
	*
	*/

	public void setError(boolean value)
	{
		error = value;
	}

	/**
	*  Clear the error, text and delegate properties at the end of request cycle.
	*
	*/

	public void reset()
	{
		error = false;
		text = null;
		delegate = null;
	}

	/**
	*  Forces the component to re-read through its text binding.  Normally
	*  this only occurs once per request cycle.  This clears the text property,
	*  but not the error property.
	*
	*/

	public void refresh()
	{
		text = null;
	}

	/**
	*  Delegates most renderring to the captive {@link TextField} component.
	*  If there's an error, then wrapped elements are also rendered (after
	*  the captive TextField).
	*
	*  <p>If there's an error, but no wrapped elements, then a default
	*  indicator <code>&lt;font color="red"&gt;>**&lt;/font&gt;</code>
	*  is written.
	*
	*/

	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		boolean rendering;
		IValidationDelegate delegate = null;
		String displayName = null;

		if (captive == null)
			captive = (TextField)getComponent("captive");

		rendering = !cycle.isRewinding();

		if (rendering && error)
		{
			delegate = getDelegate();
			displayName = getDisplayName();

			delegate.writeErrorPrefix(this, writer, cycle);
		}

		captive.render(writer, cycle);

		if (rendering && error)
			delegate.writeErrorSuffix(this, writer, cycle);

		// If rendering and there's either an error in the field,
		// or the field is required but the value is currently null,
		// then we may have identified the default field (which will
		// automatically receive focus).

		if (rendering && (error | (isRequired() && isNull(getText()))))
			addSelect(cycle);

		// That's OK, but an ideal situation would know about non-validating
		// text fields, and also be able to put the cursor in the
		// first field, period (even if there are no required or error fields).
		// Still, this pretty much rocks!
	}

	private boolean isNull(String value)
	{
		return (value == null ||
			value.trim().length() == 0);
	}

	private static final String SELECTED_ATTRIBUTE_NAME = 
		"com.primix.tapestry.component.validating.SelectedFieldSet";

	/**
	*  Creates JavaScript to set the cursor on the first required or error
	*  field encountered while rendering.  This only works if the text field
	*  is wrapped by a {@link Body} component (which is almost always true).
	*
	*/

	private void addSelect(IRequestCycle cycle)
	{
		Body body;
		Form form;
		String formName;
		String textFieldName;
		String fullName;

		// If some other field has taken the honors, then let it.

		if (cycle.getAttribute(SELECTED_ATTRIBUTE_NAME) != null)
			return;

		body = Body.get(cycle);

		// If not wrapped by a Body, then do nothing.

		if (body == null)
			return;

		form = Form.get(cycle);

		formName = form.getName();
		textFieldName = captive.getName();

		fullName = "document." + formName + "." + textFieldName;

		body.addOtherInitialization(fullName + ".focus();");
		body.addOtherInitialization(fullName + ".select();");

		// Put a marker in, indicating that the selected field is known.

		cycle.setAttribute(SELECTED_ATTRIBUTE_NAME, Boolean.TRUE);
	}
}

