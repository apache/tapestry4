/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.html;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IScript;
import org.apache.tapestry.RequestCycleException;
import org.apache.tapestry.RequiredParameterException;
import org.apache.tapestry.ScriptException;
import org.apache.tapestry.ScriptSession;
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

    protected String getAssetURL(IAsset asset, IRequestCycle cycle) throws RequestCycleException
    {
        if (asset == null)
            return null;

        return asset.buildURL(cycle);
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
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
            throw new RequestCycleException(
                Tapestry.getString("Rollover.must-be-contained-by-body"),
                this);

        ILinkComponent serviceLink =
            (ILinkComponent) cycle.getAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME);

        if (serviceLink == null)
            throw new RequestCycleException(
                Tapestry.getString("Rollover.must-be-contained-by-link"),
                this);

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

            IResourceLocation scriptLocation =
                getSpecification().getSpecificationLocation().getRelativeLocation(
                    "Rollover.script");

            _parsedScript = source.getScript(scriptLocation);
        }

        return _parsedScript;
    }

    private String writeScript(Body body, ILinkComponent link, String focusURL, String blurURL)
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

        link.addEventHandler(LinkEventType.MOUSE_OVER, (String) symbols.get("onMouseOverName"));
        link.addEventHandler(LinkEventType.MOUSE_OUT, (String) symbols.get("onMouseOutName"));

        String imageName = (String) symbols.get("imageName");

        // Return the value that must be assigned to the 'name' attribute
        // of the <img> tag.

        return imageName;

    }

    public abstract IAsset getBlur();

    public abstract IAsset getDisabled();

    public abstract IAsset getFocus();

    public abstract IAsset getImage();
}