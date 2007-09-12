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

package org.apache.tapestry.link;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.tapestry.*;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.util.ScriptUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link org.apache.tapestry.link.ILinkRenderer},
 * which does nothing special. Can be used as a base class to provide additional
 * handling.
 * 
 * @since 3.0
 */

public class DefaultLinkRenderer implements ILinkRenderer
{

    /**
     * A shared instance used as a default for any link that doesn't explicitly
     * override.
     */

    public static final ILinkRenderer SHARED_INSTANCE = new DefaultLinkRenderer();

    public void renderLink(IMarkupWriter writer, IRequestCycle cycle, ILinkComponent linkComponent)
    {
        IMarkupWriter wrappedWriter = null;
        
        if (cycle.getAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME) != null)
            throw new ApplicationRuntimeException(LinkMessages.noNesting(), linkComponent, null, null);
        
        cycle.setAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME, linkComponent);
        
        boolean hasBody = getHasBody();

        boolean disabled = linkComponent.isDisabled() || cycle.isRewinding();

        if (!disabled)
        {
            if (hasBody)
                writer.begin(getElement());
            else 
                writer.beginEmpty(getElement());
            
            linkComponent.renderAdditionalAttributes(writer, cycle);
            
            writer.attribute(getUrlAttribute(), constructURL(linkComponent, cycle));
            
            String target = linkComponent.getTarget();
            
            if (HiveMind.isNonBlank(target))
                writer.attribute(getTargetAttribute(), target);
            
            if (DirectLink.class.isInstance(linkComponent)) {
                DirectLink direct = (DirectLink)linkComponent;
                
                renderAsyncParams(writer, cycle, direct);
            }
            
            beforeBodyRender(writer, cycle, linkComponent);
            
            // Allow the wrapped components a chance to render.
            // Along the way, they may interact with this component
            // and cause the name variable to get set.
            
            wrappedWriter = writer.getNestedWriter();
        } else 
            wrappedWriter = writer;
        
        if (hasBody) 
            linkComponent.renderBody(wrappedWriter, cycle);
        
        if (!disabled) {
            
            afterBodyRender(writer, cycle, linkComponent);
                        
            if (hasBody) {
                wrappedWriter.close();
                
                // Close the <element> tag
                
                writer.end();
            } else 
                writer.closeTag();
        }
        
        cycle.removeAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME);
    }

    /**
     * Converts the EngineServiceLink into a URI or URL. This implementation
     * gets the scheme and anchor from the component (both of which may be
     * null), and invokes
     * {@link ILink#getURL(String, String, int, String, boolean)}.
     */

    protected String constructURL(ILinkComponent component, IRequestCycle cycle)
    {
        ILink link = component.getLink(cycle);
        
        String scheme = component.getScheme();
        Integer port = component.getPort();
        int portI = (port == null) ? 0 : port.intValue();
        String anchor = component.getAnchor();
        
        return link.getURL(scheme, null, portI, anchor, true);
    }

    /**
     * Invoked after the href attribute has been written but before the body of
     * the link is rendered (but only if the link is not disabled).
     * <p>
     * This implementation does nothing.
     * </p>
     *
     * @param writer
     *          Markup writer.
     * @param cycle
     *          Current request cycle.
     * @param link
     *          The link component being rendered.
     */

    protected void beforeBodyRender(IMarkupWriter writer, IRequestCycle cycle, ILinkComponent link)
    {
    }

    /**
     * Invoked after the body of the link is rendered, but before
     * {@link ILinkComponent#renderAdditionalAttributes(IMarkupWriter, IRequestCycle)}is
     * invoked (but only if the link is not disabled).
     * 
     * <p>
     * This implementation does nothing.
     * </p>
     * @param writer
     *          Markup writer.
     * @param cycle
     *          Current request cycle.
     * @param link
     *          The link component being rendered.
     */

    protected void afterBodyRender(IMarkupWriter writer, IRequestCycle cycle, ILinkComponent link)
    {
    }

    /**
     * For {@link DirectLink} components only, manages writing out event handlers for link
     * if any of the dynamic (async/json/etc) parameters are set on the component.
     * 
     * <p>
     *  Will try to write the logic into the <code>onClick</code> attribute of the link 
     *  if not bound, otherwise it will render it using the {@link DirectLink#getScript()} script.
     * </p>
     * 
     * @param writer
     *          The writer to render attributes into.
     * @param cycle
     *          The current request cycle.
     * @param link
     *          The component link being rendered for.
     */
    protected void renderAsyncParams(IMarkupWriter writer, IRequestCycle cycle, DirectLink link)
    {
        List comps = link.getUpdateComponents();
        
        if (!link.isAsync() && !link.isJson() 
                && (comps == null
                || comps.size() <= 0))
            return;
        
        if (!link.isParameterBound("onclick") && !link.isParameterBound("onClick")) {
            writer.attribute("onclick", 
                    "return tapestry.linkOnClick(this.href,'" + link.getClientId() + "', " + link.isJson() + ")");
            return;
        }
        
        PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, link);
        
        if (prs == null)
            return;
        
        Map parms = new HashMap();
        
        parms.put("component", link);
        parms.put("json", Boolean.valueOf(link.isJson()));
        parms.put("key", ScriptUtils.functionHash("onclick" + link.hashCode()));
        
        // execute script template
        
        link.getScript().execute(link, cycle, prs, parms);
    }
    
    /** @since 3.0 * */

    protected String getElement()
    {
        return "a";
    }

    protected String getUrlAttribute()
    {
        return "href";
    }

    protected String getTargetAttribute()
    {
        return "target";
    }

    protected boolean getHasBody()
    {
        return true;
    }
}
