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

package com.primix.tapestry.form;

import com.primix.tapestry.*;

// Appease Javadoc
import com.primix.tapestry.components.*;
import com.primix.tapestry.html.*;

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
 *   	<td>yes</td>
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


public class TextField extends AbstractTextField
{
	private IBinding textBinding;

	public IBinding getTextBinding()
	{
		return textBinding;
	}
	
	public void setTextBinding(IBinding value)
	{
		textBinding = value;
	}
	
	public String readValue()
	{
		return textBinding.getString();
	}
	
	public void updateValue(String value)
	{
		textBinding.setString(value);
	}
}

