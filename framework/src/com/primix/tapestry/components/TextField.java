package com.primix.tapestry.components;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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
 * <p><b>This will be merged with {@link Text} shortly.</b>
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
 *		<td>post</td>
 *		<td>The text inside the text field.  The binding is only updated
 *			when the the TextField component is not disabled.
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
 *		<td>maximumWidth</td>
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
 *  <tr>
 *    <td>listener</td>
 *    <td>{@link IActionListener}</td>
 * 	  <td>R</td>
 * 	  <td>no</td>
 *	  <td>&nbsp;</td>
 *	  <td>The listener is notified after the text binding has been updated.  The
 *		  listener is notified <em>even if the TextField is disabled</em>.</td>
 *	</tr>
 *
 *	</table>
 *
 * <p>Informal parameters are allowed, but the component may not contain a body.
 *
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class TextField extends AbstractFormComponent
{
	private IBinding textBinding;

	private IBinding displayWidthBinding;

	private boolean staticDisplayWidth;
	private int displayWidthValue;

	private IBinding maximumWidthBinding;

	private boolean staticMaximumWidth;
	private int maximumWidthValue;

	private IBinding hiddenBinding;

	private boolean staticHidden;
	private boolean hiddenValue;

	private IBinding disabledBinding;

	private static final String[] reservedNames =
    	{ "type", "size", "maxlength", "value" };

	public TextField(IPage page, IComponent container, String id,
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
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
	*  <table border=1>
	*  <tr>  <th>attribute</th>  <th>value</th> </tr>
	*  <tr>  <td>type</td> <td>text or hidden, as per <code>hidden</code> property</td> </tr>
	*  <tr>  <td>name</td>  <td>from {@link IRequestCycle#getNextActionId()}</td> </tr>
	*  <tr>  <td>disabled</td>  <td>ommited, unless the <code>disabled</code> property is
	* 	true.  </td> </tr>
	*  <tr> <td>size</td> <td>from <code>displayWidth</code> property</td> </tr>
	*  <tr> <td>maxlength</td> <td>from <code>maximumWidth</code> property</td> </tr>
	*  <tr>  <td>value</td> <td>from <code>text</code> property</td> </tr>
	*  </table>
	**/

	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		boolean rewinding;
		String name;
		IActionListener listener;
		String value;
		boolean disabled = false;
		int displayWidth;
		int maximumWidth;
		boolean hidden = false;
		Form form;

		form = getForm(cycle);

		// It isn't enough to know whether the cycle in general is rewinding, need to know
		// specifically if the form which contains this component is rewinding.

		rewinding = form.isRewinding();

		// Used whether rewinding or not.

		name = cycle.getNextActionId();

		if (disabledBinding != null)
			disabled = disabledBinding.getBoolean();	

		if (textBinding == null)
			throw new RequiredParameterException(this, "text", cycle);		

		if (rewinding)
		{
			if (!disabled)
			{
				value = cycle.getRequestContext().getParameter(name);

				textBinding.setString(value);
			}

			listener = getListener(cycle);

			if (listener != null)
				listener.actionTriggered(this, cycle);	
		}
		else
		{
			if (staticHidden)
				hidden = hiddenValue;
			else if (hiddenBinding != null)
				hidden = hiddenBinding.getBoolean();


			writer.beginOrphan("input");

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

			if (maximumWidthBinding != null)
			{
				if (staticMaximumWidth)
					maximumWidth = maximumWidthValue;
				else
					maximumWidth = maximumWidthBinding.getInt();

				writer.attribute("maxlength", maximumWidth);
			}

			value = textBinding.getString();
			if (value != null)
				writer.attribute("value", value);

			generateAttributes(cycle, writer, reservedNames);

			writer.closeTag();
		}
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

	public void setMaximumWidthBinding(IBinding value)
	{
		maximumWidthBinding = value;

		staticMaximumWidth = value.isStatic();

		if (staticMaximumWidth)
			maximumWidthValue = value.getInt();
	}

	public void setTextBinding(IBinding value)
	{
		textBinding = value;
	}
}

