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
 *  Implements a component that manages an HTML &lt;input type=text&gt; or
 *  &lt;input type=password&gt; form element.
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
 *   	<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>The text inside the text field.  The binding is only updated
 *			when the the component is not disabled.
 *
 *			<p>Corresponds to the <code>value</code> HTML attribute.</td>
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
 *			<p>Corresponds to the <code>size</code> HTML attribute.</td> </tr>
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
 *			<p>Corresponds to the <code>maxlength</code> HTML attribute.</td> </tr>
 *
 *	</table>
 *
 * <p>Informal parameters are allowed, but the component may not contain a body.
 *
 *
 *  @author Howard Ship
 *  @version $Id$
 */


package com.primix.tapestry.form;

import com.primix.tapestry.*;

public class TextField extends AbstractFormComponent
{
	private IBinding textBinding;

	private IBinding displayWidthBinding;

	private boolean staticDisplayWidth;
	private int displayWidthValue;

	private IBinding maximumLengthBinding;

	private boolean staticMaximumLength;
	private int maximumLengthValue;

	private IBinding hiddenBinding;

	private boolean staticHidden;
	private boolean hiddenValue;

	private IBinding disabledBinding;

	private String name;

	private static final String[] reservedNames =
    	{ "type", "size", "maxlength", "value" };


	public String getName()
	{
		return name;
	}

	public IBinding getDisabledBinding()
	{
		return disabledBinding;
	}

	public IBinding getDisplayWidthBinding()
	{
		return displayWidthBinding;
	}

	public IBinding getHiddenBinding()
	{
		return hiddenBinding;
	}

	public IBinding getTextBinding()
	{
		return textBinding;
	}

	/**
	*  Renders the form element, or responds when the form containing the element
	*  is submitted (by checking {@link Form#isRewinding()}.
	*
	**/

	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		boolean rewinding;
		IActionListener listener;
		String value;
		boolean disabled = false;
		int displayWidth;
		int maximumLength;
		boolean hidden = false;
		Form form;

		form = getForm(cycle);

		// It isn't enough to know whether the cycle in general is rewinding, need to know
		// specifically if the form which contains this component is rewinding.

		rewinding = form.isRewinding();

		// Used whether rewinding or not.

		name = form.getNextElementId("TextField");

		if (disabledBinding != null)
			disabled = disabledBinding.getBoolean();	

		if (textBinding == null)
			throw new RequiredParameterException(this, "text", null);		

		if (rewinding)
		{
			if (!disabled)
			{
				value = cycle.getRequestContext().getParameter(name);

				textBinding.setString(value);
			}

			return;
		}

		if (staticHidden)
			hidden = hiddenValue;
		else if (hiddenBinding != null)
			hidden = hiddenBinding.getBoolean();


		writer.beginEmpty("input");

		writer.attribute("type", hidden ? "password" : "text");

		if (disabled)
			writer.attribute("disabled");

		writer.attribute("name", name);

		if (displayWidthBinding != null)
		{
			if (staticDisplayWidth)
				displayWidth = displayWidthValue;
			else
				displayWidth = displayWidthBinding.getInt();

			writer.attribute("size", displayWidth);
		}

		if (maximumLengthBinding != null)
		{
			if (staticMaximumLength)
				maximumLength = maximumLengthValue;
			else
				maximumLength = maximumLengthBinding.getInt();

			writer.attribute("maxlength", maximumLength);
		}

		value = textBinding.getString();
		if (value != null)
			writer.attribute("value", value);

		generateAttributes(cycle, writer, reservedNames);

		writer.closeTag();
	}

	public void setDisabledBinding(IBinding value)
	{
		disabledBinding = value;
	}

	public void setDisplayWidthBinding(IBinding value)
	{
		displayWidthBinding = value;

		staticDisplayWidth = value.isStatic();

		if (staticDisplayWidth)
			displayWidthValue = value.getInt();
	}

	public void setHiddenBinding(IBinding value)
	{
		hiddenBinding = value;

		staticHidden = value.isStatic();

		if (staticHidden)
			hiddenValue = value.getBoolean();
	}

	public void setMaximumLengthBinding(IBinding value)
	{
		maximumLengthBinding = value;

		staticMaximumLength = value.isStatic();

		if (staticMaximumLength)
			maximumLengthValue = value.getInt();
	}

	public IBinding getMaximumLengthBinding()
	{
		return maximumLengthBinding;
	}
	
	public void setTextBinding(IBinding value)
	{
		textBinding = value;
	}
}

