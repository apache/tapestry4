package com.primix.tapestry.components;

import com.primix.tapestry.*;
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
 * <p>It is likely that this component's functionality will be rolled into
 * the {@link Submit} component at some point in the near future.
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
 *		<td>point</td>
 *		<td>java.awt.Point</td>
 *		<td>W</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>The point at which the image was clicked; used for rare
 * components that actually need to know (typically, using the image button
 * list a simple image map).</td> </tr>
 *
 *  <tr>
 *      <td>selected</td>
 *      <td>java.lang.Object</td>
 *      <td>W</td>
 *      <td>no</td>
 *      <td>&nbsp;</td>
 *      <td>This parameter is bound to a property that is
 *    updated when the image button is clicked by the user (submitting
 *  the form).  The property
 *  is updated to match the tag parameter.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>tag</td>
 *      <td>java.lang.Object</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>&nbsp;</td>
 *      <td>Tag used with the selected parameter to indicate which image button
 *  on a form was clicked.
 *
 *  <p>This parameter is required if the selected paremeter is used.</td>
 *  </tr>
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
    private IBinding selectedBinding;
    private IBinding tagBinding;
    private Object staticTagValue;
    private String name;

    public String getName()
    {
        return name;
    }
	
	private static final String[] reservedNames = 
		{ "type", "name", "border", "src" };

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
	
	public void setSelectedBinding(IBinding value)
	{
	    selectedBinding = value;
	}

	public IBinding getSelectedBinding()
	{
	    return selectedBinding;
	}

	public void setTagBinding(IBinding value)
	{
	    tagBinding = value;

	    if (value.isStatic())
	        staticTagValue = value.getValue();
	}

	public IBinding getTagBinding()
	{
	    return tagBinding;
	}

	public void render(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		Form form;
		boolean rewinding;
		String parameterName;
		String value;
		int x;
		int y;
		RequestContext context;
		IAsset image = null;
		String imageURL;
		boolean disabled = false;
		Object tagValue = staticTagValue;

		form = getForm(cycle);
		
		rewinding = form.isRewinding();
		
		name = form.getNextElementId("ImageButton");
		
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
			
			if (value == null)
				return;
			
            // The point parameter is not really used, unless the
            // ImageButton is used for its original purpose (as a kind
            // of image map).  In modern usage, we only care about
            // whether the user clicked on the image (and thus submitted
            // the form), not where in the image the user actually clicked.

			if (pointBinding != null)
			{
				x = Integer.parseInt(value);
			
				parameterName = name + ".y";
				value = context.getParameter(parameterName);
				
				y = Integer.parseInt(value);
				
				pointBinding.setValue(new Point(x, y));
			}

            // Notify the application, bug setting the select parameter
            // to the tag parameter.

            if (selectedBinding == null)
                return;

            if (tagBinding == null)
                throw new RequestCycleException(
                    "The tag parameter is required if the selected parameter is bound.",
                    this, cycle);


			 // OK, now to notify the application code (via the parameters)
			 // that *this* ImageButton was selected.  We do this by applying
			 // a tag (presumably, specific to the ImageButton in question)
			 // to the selected binding.  When the containing Form's listener
             // is invoked, it can determine which (if any) ImageButton
             // (or Submit) was clicked.
	
	
			 if (tagValue == null)
			     tagValue = tagBinding.getValue();
	
             if (tagValue == null)
                throw new RequiredParameterException(this, "tag", tagBinding, cycle);

			 selectedBinding.setValue(tagValue);			
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

