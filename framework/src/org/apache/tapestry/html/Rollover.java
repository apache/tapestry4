//  Copyright 2004 The Apache Software Foundation
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

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IScript;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.components.LinkEventType;
import org.apache.tapestry.engine.IScriptSource;

/**
 *  Combines a link component (such as {@link org.apache.tapestry.link.DirectLink}) 
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

public abstract class Rollover extends AbstractComponent
{
    private IScript _parsedScript;

    /**
     *  Converts an {@link IAsset} binding into a usable URL.  Returns null
     *  if the binding does not exist or the binding's value is null.
     *
     **/

    protected String getAssetURL(IAsset asset, IRequestCycle cycle)
    {
        if (asset == null)
            return null;

        return asset.buildURL(cycle);
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        // No body, so we skip it all if not rewinding (assumes no side effects on
        // accessors).

        if (cycle.isRewinding())
            return;

        String imageURL = null;
        String focusURL = null;
        String blurURL = null;
        boolean dynamic = false;
        String imageName = null;

        Body body = Body.get(cycle);
        if (body == null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("Rollover.must-be-contained-by-body"),
                this,
                null,
                null);

        ILinkComponent serviceLink =
            (ILinkComponent) cycle.getAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME);

        if (serviceLink == null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("Rollover.must-be-contained-by-link"),
                this,
                null,
                null);

        boolean linkDisabled = serviceLink.isDisabled();

        if (linkDisabled)
        {
            imageURL = getAssetURL(getDisabled(), cycle);

            if (imageURL == null)
                imageURL = getAssetURL(getImage(), cycle);
        }
        else
        {
            imageURL = getAssetURL(getImage(), cycle);
            focusURL = getAssetURL(getFocus(), cycle);
            blurURL = getAssetURL(getBlur(), cycle);

            dynamic = (focusURL != null) || (blurURL != null);
        }

        if (imageURL == null)
            throw Tapestry.createRequiredParameterException(this, "image");

        writer.beginEmpty("img");

        writer.attribute("src", imageURL);

        writer.attribute("border", 0);

        if (dynamic)
        {
            if (focusURL == null)
                focusURL = imageURL;

            if (blurURL == null)
                blurURL = imageURL;

            imageName = writeScript(cycle, body, serviceLink, focusURL, blurURL);

            writer.attribute("name", imageName);
        }

        renderInformalParameters(writer, cycle);

        writer.closeTag();

    }

    private IScript getParsedScript()
    {
        if (_parsedScript == null)
        {
            IEngine engine = getPage().getEngine();
            IScriptSource source = engine.getScriptSource();

            IResourceLocation scriptLocation =
                getSpecification().getSpecificationLocation().getRelativeLocation(
                    "Rollover.script");

            _parsedScript = source.getScript(scriptLocation);
        }

        return _parsedScript;
    }

    private String writeScript(
        IRequestCycle cycle,
        Body body,
        ILinkComponent link,
        String focusURL,
        String blurURL)
    {
        String imageName = body.getUniqueString(getId());
        String focusImageURL = body.getPreloadedImageReference(focusURL);
        String blurImageURL = body.getPreloadedImageReference(blurURL);

        Map symbols = new HashMap();

        symbols.put("imageName", imageName);
        symbols.put("focusImageURL", focusImageURL);
        symbols.put("blurImageURL", blurImageURL);

        getParsedScript().execute(cycle, body, symbols);

        // Add attributes to the link to control mouse over/out.
        // Because the script is written before the <body> tag,
        // there won't be any timing issues (such as cause
        // bug #113893).

        link.addEventHandler(LinkEventType.MOUSE_OVER, (String) symbols.get("onMouseOverName"));
        link.addEventHandler(LinkEventType.MOUSE_OUT, (String) symbols.get("onMouseOutName"));

        return imageName;
    }

    public abstract IAsset getBlur();

    public abstract IAsset getDisabled();

    public abstract IAsset getFocus();

    public abstract IAsset getImage();
}