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

import java.util.HashMap;
import java.util.Map;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IScript;
import net.sf.tapestry.IScriptSource;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;
import net.sf.tapestry.ResourceUnavailableException;
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.components.IServiceLink;
import net.sf.tapestry.components.ServiceLinkEventType;

/**
 *  Combines a link component (such as {@link net.sf.tapestry.link.Direct}) 
 *  with an &lt;img&gt; and JavaScript code
 *  to create a rollover effect that works with both Netscape Navigator and 
 *  Internet Explorer.
 *
 *  A <code>Rollover</code> must be contained within a {@link Body} and within
 *  an {@link IServiceLink}.  The JavaScript handlers for dealing with mouse
 *  movement are applied to the &lt;a&gt; created by the {@link net.sf.tapestry.components.IServiceLink}.  For compatibility
 *  with Netscape 4, the handlers are attached using HTML attributes (it would cleaner
 *  and easier to do so using the DOM).
 *
 * <table border=1>
 * <tr> 
 *    <td>Parameter</td>
 *    <td>Type</td>
 *	  <td>Direction </td>
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
 *		<td>The (initial) image to show.</td>
 *	</tr>
 *
 * <tr>
 *		<td>focus</td>
 *		<td>{@link IAsset}</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>If provided (and the component is not disabled), then a JavaScript <code>onMouseOver</code> event handler
 * is added to change the image.  An <code>onMouseOut</code> event handler is also
 * provided, to change the button to either it's base image, or its blur image. </td> </tr>
 *
 * <tr>
 *		<td>blur</td>
 *		<td>{@link IAsset}</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>If provided (and the component is not disabled), then a JavaScript <code>onMouseOut</code> event handler
 *  is added to change the image when the mouse is moved off of the image. </td> </tr>
 *
 * <tr>
 *
 * <tr>
 *		<td>disabled</td>
 *		<td>{@link IAsset}</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>Image used when the <code>Rollover</code> is disabled.  A <code>Rollover</code>
 * is disabled when the {@link IServiceLink} containing it is disabled.  If no disabled
 * asset is specified, then image asset is used instead.  
 * A disabled <code>Rollover</code> does not respond to the mouse.
    </td> </tr>
 *
 * <tr>
 *
 * </table>
 *
 *  <p>Informal parameters are allowed and are applied to the &lt;img&gt; tag.  A body
 *  is not allowed.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Rollover extends AbstractComponent
{
    private IScript parsedScript;

    private IAsset image;
    private IAsset blur;
    private IAsset focus;
    private IAsset disabled;

    /**
     *  Converts an {@link IAsset} binding into a usable URL.  Returns null
     *  if the binding does not exist or the binding's value is null.
     *
     **/

    protected String getAssetURL(IAsset asset, IRequestCycle cycle)
        throws RequestCycleException
    {

        if (asset == null)
            return null;

        return asset.buildURL(cycle);
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        String imageURL = null;
        String focusURL = null;
        String blurURL = null;
        String uniqueId = null;
        String onMouseOverName = null;
        boolean dynamic = false;
        String onMouseOutName = null;
        String focusImageName = null;
        String blurImageName = null;
        String imageName = null;
        String script;

        Body body = Body.get(cycle);
        if (body == null)
            throw new RequestCycleException(
                Tapestry.getString("Rollover.must-be-contained-by-body"),
                this);

        IServiceLink serviceLink =
            (IServiceLink) cycle.getAttribute(IServiceLink.ATTRIBUTE_NAME);
        if (serviceLink == null)
            throw new RequestCycleException(
                Tapestry.getString("Rollover.must-be-contained-by-link"),
                this);

        // No body, so we skip it all if not rewinding (assumes no side effects on
        // accessors).

        if (cycle.isRewinding())
            return;

        boolean linkDisabled = serviceLink.isDisabled();

        if (linkDisabled)
        {
            imageURL = getAssetURL(disabled, cycle);

            if (imageURL == null)
                imageURL = getAssetURL(image, cycle);
        }
        else
        {
            imageURL = getAssetURL(image, cycle);
            focusURL = getAssetURL(focus, cycle);
            blurURL = getAssetURL(blur, cycle);

            dynamic = (focusURL != null) || (blurURL != null);
        }

        if (imageURL == null)
            throw new RequiredParameterException(this, "image", null);

        writer.beginEmpty("img");

        writer.attribute("src", imageURL);

        writer.attribute("border", 0);

        if (dynamic)
        {
            if (focusURL == null)
                focusURL = imageURL;

            if (blurURL == null)
                blurURL = imageURL;

            try
            {
                imageName = writeScript(body, serviceLink, focusURL, blurURL);
            }
            catch (ResourceUnavailableException ex)
            {
                throw new RequestCycleException(this, ex);
            }
            catch (ScriptException ex)
            {
                throw new RequestCycleException(this, ex);
            }

            writer.attribute("name", imageName);
        }

        generateAttributes(writer, cycle);

        writer.closeTag();

    }

    private IScript getParsedScript() throws ResourceUnavailableException
    {
        if (parsedScript == null)
        {
            IEngine engine = getPage().getEngine();
            IScriptSource source = engine.getScriptSource();

            parsedScript = source.getScript("/net/sf/tapestry/html/Rollover.script");
        }

        return parsedScript;
    }

    private String writeScript(
        Body body,
        IServiceLink link,
        String focusURL,
        String blurURL)
        throws ResourceUnavailableException, ScriptException
    {
        String uniqueId = body.getUniqueId();
        String focusImageURL = body.getPreloadedImageReference(focusURL);
        String blurImageURL = body.getPreloadedImageReference(blurURL);

        Map symbols = new HashMap();

        symbols.put("uniqueId", uniqueId);
        symbols.put("focusImageURL", focusImageURL);
        symbols.put("blurImageURL", blurImageURL);

        ScriptSession session = getParsedScript().execute(symbols);

        body.process(session);

        // Add attributes to the link to control mouse over/out.
        // Because the script is written before the <body> tag,
        // there won't be any timing issues (such as cause
        // bug #113893).

        link.addEventHandler(
            ServiceLinkEventType.MOUSE_OVER,
            (String) symbols.get("onMouseOverName"));
        link.addEventHandler(
            ServiceLinkEventType.MOUSE_OUT,
            (String) symbols.get("onMouseOutName"));

        String imageName = (String) symbols.get("imageName");

        // Return the value that must be assigned to the 'name' attribute
        // of the <img> tag.

        return imageName;

    }

    public IAsset getBlur()
    {
        return blur;
    }

    public void setBlur(IAsset blur)
    {
        this.blur = blur;
    }

    public IAsset getDisabled()
    {
        return disabled;
    }

    public void setDisabled(IAsset disabled)
    {
        this.disabled = disabled;
    }

    public IAsset getFocus()
    {
        return focus;
    }

    public void setFocus(IAsset focus)
    {
        this.focus = focus;
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