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
 *  Used to create an image button inside a {@link Form}.  Although it
 *  is occasionally useful to know the {@link Point} on the image that was clicked
 *  (i.e., use the image as a kind of image map, which was the original intent
 *  of the HTML element), it is more commonly used to provide a graphic
 *  image for the user to click, rather than the rather plain &lt;input type=submit&gt;.
 *
 * <p>Typically, the listener for this component will set a flag which
 * will be used when the containing form's listener is notified.  To use
 * a simple button with a text label, use the {@link Submit} component.
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
 *  <tr>
 *	  <td>disabled</id>
 *	  <td>boolean</td>
 *    <td>R</td>
 *    <td>no</td>
 *	  <td>&nbsp;</td>
 *    <td>If set to true, the button will be disabled (will not respond to
 *  the mouse).  If an alternate image is defined, it will be displayed (typically
 *  a greyed-out version of the normal image). </td> </tr>
 *
 *  <tr>
 * 	  <td>disabledImage</td>
 *	  <td>{@link IAsset}</td>
 *	  <td>R</td>
 *	  <td>no</td>
 *		<td>&nbsp;</td>
 *	  <td>An alternate image to display if the component is disabled.</td> </tr>
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
 *  @author Howard Ship
 *  @version $Id$
 */


public class ImageButton extends AbstractFormComponent
{
	private IBinding imageBinding;
	private IBinding pointBinding;
	private IBinding disabledBinding;
	private IBinding disabledImageBinding;
	
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

	public IBinding getDisabledBinding()
	{
		return disabledBinding;
	}
	
	public void setDisabledBinding(IBinding value)
	{
		disabledBinding = value;
	}
	
	public IBinding getDisabledImageBinding()
	{
		return disabledImageBinding;
	}

	public void setDisabledImageBinding(IBinding value)
	{
		disabledImageBinding = value;
	}
	
	public void render(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		Form form;
		boolean rewinding;
		String name;
		String parameterName;
		String value;
		int x;
		int y;
		RequestContext context;
		IActionListener listener;
		IAsset image = null;
		String imageURL;
		boolean disabled = false;
		
		form = getForm(cycle);
		
		rewinding = form.isRewinding();
		
		name = "ImageButton" + cycle.getNextActionId();
		
		if (disabledBinding != null)
			disabled = disabledBinding.getBoolean();
		
		if (rewinding)
		{
			// If disabled, do nothing.
			
			if (disabled)
				return;
				
			context = cycle.getRequestContext();
			
			// Image clicks get submitted as two request parameters: 
			// foo.x and foo.y
			
			parameterName = name + ".x";
				
			value = context.getParameter(parameterName);
			
			if (name == null)
				return;
			
			if (pointBinding != null)
			{
				x = Integer.parseInt(value);
			
				parameterName = name + ".y";
				value = context.getParameter(parameterName);
				
				y = Integer.parseInt(value);
				
				pointBinding.setValue(new Point(x, y));
			}
			
			// That was a lot of work to set the point binding.
			// In reality, what usually happens is that the ImageButton
			// exists so that its listener may be invoked.
			
			listener = getListener(cycle);
			
			if (listener != null)
				listener.actionTriggered(this, cycle);
			
			return;	
		}
		
		// Not rewinding, do the real render
		
		if (disabled)
			image = (IAsset)disabledImageBinding.getValue();
			
		if (image == null)
			image = (IAsset)imageBinding.getValue();				

		imageURL = image.buildURL(cycle);

		writer.beginOrphan("input");
		writer.attribute("type", "image");
		writer.attribute("name", name);
		
		if (disabled)
			writer.attribute("disabled");
			
		// Netscape places a border unless you tell it otherwise.
		// IE ignores the border attribute and never shows a border.
		
		writer.attribute("border", 0);

		writer.attribute("src", imageURL);

		generateAttributes(cycle, writer, reservedNames);
		
		writer.closeTag();
	}	
}

