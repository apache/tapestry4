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
 *  Implements a component that manages an HTML &lt;textarea&gt; form element.
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
 *		<td>The text inside the textarea.  The parameter is only updated
 *			when the the Text component is not disabled.</td>
 *	</tr>
 *
 *  <tr>
 * 		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>Controls whether the textarea is active or not.  If disabled, then
 *			any value that comes up when the form is submitted is ignored.
 *			
 *			<p>Corresponds to the <code>disabled</code> HTML attribute.</td>
 *	</tr>
 *
 *	<tr>
 *		<td>columns</td>
 *		<td>integer</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>The width of the textarea, in characters.  If zero, or unspecified
 *		    the value is left to the client browser to determine.
 *
 *			<p>Corresponds to the <code>cols</code> HTML attribute.</td> </tr>
 *
 *	<tr>
 *		<td>rows</td>
 *		<td>integer</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>The number of rows in the textarea. If
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
 *	  <td>The listener is notified after the text binding has been updated.</td>
 *	</tr>
 *
 * <tr>
 *	</table>
 *
 * <p>Informal parameters are allowed.  The component may not contain a body.
 
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Text extends AbstractFormComponent
{
	private IBinding textBinding;
	private IBinding rowsBinding;
	private IBinding columnsBinding;
	private IBinding disabledBinding;

	private static final String[] reservedNames =
    { "name", "cols"};

	public Text(IPage page, IComponent container, String id,
		ComponentSpecification specification)
	{ 
		super(page, container, id, specification);
	}

	public IBinding getColumnsBinding()
	{
		return columnsBinding;
	}

	public IBinding getDisabledBinding()
	{
		return disabledBinding;
	}

	public IBinding getRowsBinding()
	{
		return rowsBinding;
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
	*  <tr>  <td>name</td>  <td>from {@link IRequestCycle#getNextActionId()}</td> </tr>
	*  <tr>  <td>disabled</td>  <td>ommited, unless the <code>disabled</code> property is
	* 	true.  </td> </tr>
	*  <tr> <td>rows</td> <td>from <code>rows</code> property</td> </tr>
	*  <tr> <td>cols</td> <td>from <code>columns</code> property</td> </tr>
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
		Form form;
		boolean compressed;

		form = getForm(cycle);

		if (textBinding == null)
			throw new RequiredParameterException(this, "text", cycle);		

		// It isn't enough to know whether the cycle in general is rewinding, need to know
		// specifically if the form which contains this component is rewinding.

		rewinding = form.isRewinding();

		// Used whether rewinding or not.

		name = form.getNextElementId("Text");

		if (disabledBinding != null)
			disabled = disabledBinding.getBoolean();

		if (rewinding)
		{
			if (!disabled)
			{
				value = cycle.getRequestContext().getParameter(name);

				textBinding.setString(value);
				
				// Now, notify the listener.
				
				listener = getListener(cycle);

				if (listener != null)
					listener.actionTriggered(this, cycle);	
			}

		}
		else
		{
			compressed = writer.compress(true);

			writer.begin("textarea");

			writer.attribute("name", name);

			if (disabled)
				writer.attribute("disabled");

			if (rowsBinding != null)
				writer.attribute("rows", rowsBinding.getInt());

			if (columnsBinding != null)
				writer.attribute("cols", columnsBinding.getInt());

			generateAttributes(cycle, writer, reservedNames);

			value = textBinding.getString();
			if (value != null)
				writer.print(value);

			writer.end();

			writer.setCompressed(compressed);
		}
	}

	public void setColumnsBinding(IBinding value)
	{
		columnsBinding = value;
	}

	public void setDisabledBinding(IBinding value)
	{
		disabledBinding = value;
	}

	public void setRowsBinding(IBinding value)
	{
		rowsBinding = value;
	}

	public void setTextBinding(IBinding value)
	{
		textBinding = value;
	}
}

