//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.form;

import java.awt.Point;

import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;

/**
 *  Used to create an image button inside a {@link Form}.  Although it
 *  is occasionally useful to know the {@link Point} on the image that was clicked
 *  (i.e., use the image as a kind of image map, which was the original intent
 *  of the HTML element), it is more commonly used to provide a graphic
 *  image for the user to click, rather than the rather plain &lt;input type=submit&gt;.
 *
 * <table border=1>
 * <tr> 
 *    <td>Parameter</td>
 *    <td>Type</td>
 *	  <td>Direction</td>
 *    <td>Required</td> 
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 *  <tr>
 *    <td>image</td>
 *    <td>{@link IAsset}</td>
 *    <td>in</td>
 *   	<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The image to show.</td>
 *	</tr>
 *
 * <tr>
 *		<td>name</td>
 *		<td>{@link String}</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>The name to use for the form element.  Under Netscape Navigator 4, this
 * name becomes the tooltip.  The name may be modified (by adding a number to the end)
 * to ensure that it is unique within the form. </td> </tr>
 *
 *  <tr>
 *	  <td>disabled</id>
 *	  <td>boolean</td>
 *    <td>in</td>
 *    <td>no</td>
 *	  <td>&nbsp;</td>
 *    <td>If set to true, the button will be disabled (will not respond to
 *  the mouse).  If an alternate image is defined, it will be displayed (typically
 *  a greyed-out version of the normal image). </td> </tr>
 *
 *  <tr>
 * 	  <td>disabledImage</td>
 *	  <td>{@link IAsset}</td>
 *	  <td>in</td>
 *	  <td>no</td>
 *		<td>&nbsp;</td>
 *	  <td>An alternate image to display if the component is disabled.  If the
 *  component is disabled and this parameter is not specified,
 *  the normal image is used. </td> </tr>
 *
 * <tr>
 *		<td>point</td>
 *		<td>java.awt.Point</td>
 *		<td>out</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>The point at which the image was clicked; used for rare
 * components that actually need to know (typically, using the image button
 * list a simple image map).</td> </tr>
 *
 *  <tr>
 *      <td>selected</td>
 *      <td>java.lang.Object</td>
 *      <td>out</td>
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
 *      <td>in</td>
 *      <td>no</td>
 *      <td>&nbsp;</td>
 *      <td>Tag used with the selected parameter to indicate which image button
 *  on a form was clicked.
 *
 *  <p>This parameter is required if the selected paremeter is used.</td>
 *  </tr>
 *
 *  <tr>
 * 		<td>listener</td>
 * 		<td>{@link IActionListener}</td>
 * 		<td>in</td>
 * 		<td>no</td>
 * 		<td>&nbsp;</td>
 * 		<td>If specified, the listener is notified.  This notification occurs
 *  as the component is rewinded, i.e., prior to the {@link IForm form}'s listener.
 *  In addition, the selected property (if bound) will be updated <em>before</em>
 *  the listener is notified.
 * 		</td>
 *   </tr>
 * 
 * </table>
 *
 * <p>Informal parameters are allowed.  A body is not allowed.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class ImageSubmit extends AbstractFormComponent
{
    private IAsset image;
    private IAsset disabledImage;
    private Object tag;
    private String name;
    private IActionListener listener;
    private boolean disabled;

    private IBinding pointBinding;
    private IBinding selectedBinding;

    public void setPointBinding(IBinding value)
    {
        pointBinding = value;
    }

    public IBinding getPointBinding()
    {
        return pointBinding;
    }

    public void setSelectedBinding(IBinding value)
    {
        selectedBinding = value;
    }

    public IBinding getSelectedBinding()
    {
        return selectedBinding;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        IForm form = getForm(cycle);

        boolean rewinding = form.isRewinding();

        String finalName = 
        	(name == null) ? form.getElementId(this) : form.getElementId(name);
        
        if (rewinding)
        {
            // If disabled, do nothing.

            if (disabled)
                return;

            RequestContext context = cycle.getRequestContext();

            // Image clicks get submitted as two request parameters: 
            // foo.x and foo.y

            String parameterName = name + ".x";

            String value = context.getParameter(parameterName);

            if (value == null)
                return;

            // The point parameter is not really used, unless the
            // ImageButton is used for its original purpose (as a kind
            // of image map).  In modern usage, we only care about
            // whether the user clicked on the image (and thus submitted
            // the form), not where in the image the user actually clicked.

            if (pointBinding != null)
            {
                int x = Integer.parseInt(value);

                parameterName = name + ".y";
                value = context.getParameter(parameterName);

                int y = Integer.parseInt(value);

                pointBinding.setObject(new Point(x, y));
            }

            // Notify the application, by setting the select parameter
            // to the tag parameter.

            if (selectedBinding != null)
                selectedBinding.setObject(tag);

            if (listener != null)
                listener.actionTriggered(this, cycle);

            return;
        }

        // Not rewinding, do the real render

        IAsset finalImage = (disabled && disabledImage != null) ? disabledImage : image;

        String imageURL = image.buildURL(cycle);

        writer.beginEmpty("input");
        writer.attribute("type", "image");
        writer.attribute("name", name);

        if (disabled)
            writer.attribute("disabled");

        // NN4 places a border unless you tell it otherwise.
        // IE ignores the border attribute and never shows a border.

        writer.attribute("border", 0);

        writer.attribute("src", imageURL);

        generateAttributes(writer, cycle);

        writer.closeTag();
    }

    public boolean getDisabled()
    {
        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    public IAsset getDisabledImage()
    {
        return disabledImage;
    }

    public void setDisabledImage(IAsset disabledImage)
    {
        this.disabledImage = disabledImage;
    }

    public IAsset getImage()
    {
        return image;
    }

    public void setImage(IAsset image)
    {
        this.image = image;
    }

    public IActionListener getListener()
    {
        return listener;
    }

    public void setListener(IActionListener listener)
    {
        this.listener = listener;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Object getTag()
    {
        return tag;
    }

    public void setTag(Object tag)
    {
        this.tag = tag;
    }

}