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
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.components.IServiceLink;
import net.sf.tapestry.components.ServiceLinkEventType;

/**
 *  Combines a link component (such as {@link net.sf.tapestry.link.DirectLink}) 
 *  with an &lt;img&gt; and JavaScript code
 *  to create a rollover effect that works with both Netscape Navigator and 
 *  Internet Explorer.
 *
 *  [<a href="../../../../../ComponentReference/Rollover.html">Component Reference</a>]
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Rollover extends AbstractComponent
{
    private IScript _parsedScript;

    private IAsset _image;
    private IAsset _blur;
    private IAsset _focus;
    private IAsset _disabled;

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
            imageURL = getAssetURL(_disabled, cycle);

            if (imageURL == null)
                imageURL = getAssetURL(_image, cycle);
        }
        else
        {
            imageURL = getAssetURL(_image, cycle);
            focusURL = getAssetURL(_focus, cycle);
            blurURL = getAssetURL(_blur, cycle);

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
            catch (ScriptException ex)
            {
                throw new RequestCycleException(this, ex);
            }

            writer.attribute("name", imageName);
        }

        generateAttributes(writer, cycle);

        writer.closeTag();

    }

    private IScript getParsedScript()
    {
        if (_parsedScript == null)
        {
            IEngine engine = getPage().getEngine();
            IScriptSource source = engine.getScriptSource();

            _parsedScript = source.getScript("/net/sf/tapestry/html/Rollover.script");
        }

        return _parsedScript;
    }

    private String writeScript(
        Body body,
        IServiceLink link,
        String focusURL,
        String blurURL)
        throws ScriptException
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
        return _blur;
    }

    public void setBlur(IAsset blur)
    {
        _blur = blur;
    }

    public IAsset getDisabled()
    {
        return _disabled;
    }

    public void setDisabled(IAsset disabled)
    {
        _disabled = disabled;
    }

    public IAsset getFocus()
    {
        return _focus;
    }

    public void setFocus(IAsset focus)
    {
        _focus = focus;
    }

    public IAsset getImage()
    {
        return _image;
    }

    public void setImage(IAsset image)
    {
        _image = image;
    }

}