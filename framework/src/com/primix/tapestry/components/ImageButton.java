package com.primix.tapestry.components;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import java.awt.Point;

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
 *  Used to create an image button inside a {@link Form}.
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
 *    <td>image</td>
 *    <td>{@link IAsset}</td>
 *    <td>R</td>
 *   	<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The image to show.</td>
 *	</tr>
 *
 *
 * <tr>
 *		<td>listener</td>
 *		<td>{@link IActionListener}</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>A listener that can respond when this component is used to submit
 * the form.</td>
 * </tr>
 *
 * <tr>
 *		<td>point</td>
 *		<td>java.awt.Point</td>
 *		<td>W</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>The point at which the image was clicked; used for rare
 * components that actually need to know (typically, using the image button
 * list a simple image map).</td> </tr>
 *
 * </table>
 *
 * <p>Informal parameters are allowed.  A body is not allowed.
 *
 *
 * <P>TBD:
 * <ul>
 * <li>disabled image and enabled property
 * <li>rollover support
 * </ul>
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class ImageButton extends AbstractFormComponent
{
	private IBinding imageBinding;
	private IBinding pointBinding;
	
	private static final String[] reservedNames = 
		{ "type", "name", "border", "src" };

	public ImageButton(IPage page, IComponent container, 
		String id, ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}
	
	public void setImageBinding(IBinding value)
	{
		imageBinding = value;
	}
	
	public IBinding getImageBinding()
	{
		return imageBinding;
	}
	
	public void setPointBinding(IBinding value)
	{
		pointBinding = value;
	}
	
	public IBinding getPointBinding()
	{
		return pointBinding;
	}

	public void render(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		Form form;
		boolean rewinding;
		String actionId;
		String name;
		String value;
		int x;
		int y;
		RequestContext context;
		IActionListener listener;
		IAsset image;
		String imageURL;
		
		form = getForm(cycle);
		
		rewinding = form.isRewinding();
		
		actionId = cycle.getNextActionId();
		
		if (rewinding)
		{
			context = cycle.getRequestContext();
			
			// Image clicks get submitted as two request parameters: 
			// foo.x and foo.y
			
			name = actionId + ".x";
				
			value = context.getParameter(name);
			
			if (name == null)
				return;
			
			if (pointBinding != null)
			{
				x = Integer.parseInt(value);
			
				name = actionId + ".y";
				value = context.getParameter(name);
				
				y = Integer.parseInt(value);
				
				pointBinding.setValue(new Point(x, y));
			}
			
			listener = getListener(cycle);
			
			if (listener != null)
				listener.actionTriggered(this, cycle);
			
			return;	
		}
		
		// Not rewinding, do the real render
		
		writer.beginOrphan("input");
		writer.attribute("type", "image");
		writer.attribute("name", actionId);
		
		// Netscape places a border unless you tell it otherwise.
		// IE ignores the border attribute and never shows a border.
		
		writer.attribute("border", 0);
		
		image = (IAsset)imageBinding.getValue();
		imageURL = image.buildURL(cycle);
		
		writer.attribute("src", imageURL);

		generateAttributes(cycle, writer, reservedNames);
		
		writer.closeTag();
	}	
}

