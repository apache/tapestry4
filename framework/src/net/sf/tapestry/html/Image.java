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

package net.sf.tapestry.html;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.BindingException;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;

/**
 *  Used to insert an image.  To create a rollover image, use the
 *  {@link Rollover} class, which integrates a link with the image assets
 *  used with the button.
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
 *
 * <tr>
 *		<td>border</td>
 *		<td>int</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>0</td>
 *		<td>Corresponds to the HTML <code>border</code> attribute.</td>
 * </tr>
 *
 * </table>
 *
 * <p>Informal parameters are allowed.  A body is not allowed.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Image extends AbstractComponent
{
	private IAsset image;
	private int border;

    /**
     *  Renders the &lt;img&gt; element.
     *
     * <table border=1>
     * <tr> <th>attribute</th>  <th>value</th> </tr>
     * <tr> <td>src</td> <td>from <code>image</code> property</td> </tr>
     * <tr> <td>border</td> <td>from <code>border</td> property, or 0 if not
     *		specified </td> </tr>
     * </table>
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        String imageURL = null;
        int border = 0;
        IAsset imageAsset;

        // Doesn't contain a body so no need to do anything on rewind (assumes no
        // sideffects to accessor methods via bindings).

        if (cycle.isRewinding())
            return;

        imageURL = image.buildURL(cycle);

        writer.beginEmpty("img");

        writer.attribute("src", imageURL);

        writer.attribute("border", border);

        generateAttributes(writer, cycle);

        writer.closeTag();

    }

    public int getBorder()
    {
        return border;
    }

    public void setBorder(int border)
    {
        this.border = border;
    }

    public IAsset getImage()
    {
        return image;
    }

    public void setImage(IAsset image)
    {
        this.image = image;
    }

}