// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.html;

import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.components.LinkEventType;

/**
 * Combines a link component (such as
 * {@link org.apache.tapestry.link.DirectLink}) with an &lt;img&gt; and
 * JavaScript code to create a rollover effect that works with both Netscape
 * Navigator and Internet Explorer. [ <a
 * href="../../../../../ComponentReference/Rollover.html">Component Reference
 * </a>]
 * 
 * @author Howard Lewis Ship
 */

public abstract class Rollover extends AbstractComponent
{

    /**
     * Converts an {@link IAsset}binding into a usable URL. Returns null if the
     * binding does not exist or the binding's value is null.
     */

    protected String getAssetURL(IAsset asset)
    {
        if (asset == null) return null;

        return asset.buildURL();
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        // No body, so we skip it all if not rewinding (assumes no side effects
        // on
        // accessors).

        if (cycle.isRewinding()) return;

        String imageURL = null;
        String mouseOverURL = null;
        String mouseOutURL = null;
        boolean dynamic = false;
        String imageId = null;

        PageRenderSupport pageRenderSupport = TapestryUtils
                .getPageRenderSupport(cycle, this);

        ILinkComponent serviceLink = (ILinkComponent) cycle
                .getAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME);

        if (serviceLink == null)
            throw new ApplicationRuntimeException(Tapestry
                    .getMessage("Rollover.must-be-contained-by-link"), this,
                    null, null);

        boolean linkDisabled = serviceLink.isDisabled();

        if (linkDisabled)
        {
            imageURL = getAssetURL(getDisabled());

            if (imageURL == null) imageURL = getAssetURL(getImage());
        }
        else
        {
            imageURL = getAssetURL(getImage());
            mouseOverURL = getAssetURL(getMouseOver());
            mouseOutURL = getAssetURL(getMouseOut());

            dynamic = (mouseOverURL != null) || (mouseOutURL != null);
        }

        if (imageURL == null)
            throw Tapestry.createRequiredParameterException(this, "image");

        writer.beginEmpty("img");

        writer.attribute("src", imageURL);

        if (dynamic)
        {
            if (mouseOverURL == null) mouseOverURL = imageURL;

            if (mouseOutURL == null) mouseOutURL = imageURL;

            imageId = writeScript(cycle, pageRenderSupport, serviceLink,
                    mouseOverURL, mouseOutURL);

            writer.attribute("id", imageId);
        }

        renderInformalParameters(writer, cycle);

        writer.closeTag();

    }

    // Injected

    public abstract IScript getScript();

    private String writeScript(IRequestCycle cycle,
            PageRenderSupport pageRenderSupport, ILinkComponent link,
            String mouseOverImageURL, String mouseOutImageURL)
    {
        String imageId = pageRenderSupport.getUniqueString(getId());
        String preloadedMouseOverImageURL = pageRenderSupport
                .getPreloadedImageReference(mouseOverImageURL);
        String preloadedMouseOutImageURL = pageRenderSupport
                .getPreloadedImageReference(mouseOutImageURL);

        Map symbols = new HashMap();

        symbols.put("imageId", imageId);
        symbols.put("mouseOverImageURL", preloadedMouseOverImageURL);
        symbols.put("mouseOutImageURL", preloadedMouseOutImageURL);

        getScript().execute(cycle, pageRenderSupport, symbols);

        // Add attributes to the link to control mouse over/out.
        // Because the script is written before the <body> tag,
        // there won't be any timing issues (such as cause
        // bug #113893).

        link.addEventHandler(LinkEventType.MOUSE_OVER, (String) symbols
                .get("onMouseOverName"));
        link.addEventHandler(LinkEventType.MOUSE_OUT, (String) symbols
                .get("onMouseOutName"));

        return imageId;
    }

    public abstract IAsset getMouseOut();

    public abstract IAsset getDisabled();

    public abstract IAsset getMouseOver();

    public abstract IAsset getImage();
}
