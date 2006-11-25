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
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.engine.ILink;

/**
 * Default implementation of {@link org.apache.tapestry.link.ILinkRenderer},
 * which does nothing special. Can be used as a base class to provide additional
 * handling.
 * 
 * @author Howard Lewis Ship, David Solis
 * @since 3.0
 */

public class DefaultLinkRenderer implements ILinkRenderer
{

    /**
     * A shared instance used as a default for any link that doesn't explicitly
     * override.
     */

    public static final ILinkRenderer SHARED_INSTANCE = new DefaultLinkRenderer();

    public void renderLink(IMarkupWriter writer, IRequestCycle cycle,
            ILinkComponent linkComponent)
    {
        IMarkupWriter wrappedWriter = null;

        if (cycle.getAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME) != null)
            throw new ApplicationRuntimeException(LinkMessages.noNesting(),
                    linkComponent, null, null);

        cycle.setAttribute(Tapestry.LINK_COMPONENT_ATTRIBUTE_NAME,
                linkComponent);
        
        boolean hasBody = getHasBody();

        boolean disabled = linkComponent.isDisabled() || cycle.isRewinding();

        if (!disabled)
        {
            if (hasBody)
                writer.begin(getElement());
            else 
                writer.beginEmpty(getElement());
            
            linkComponent.renderAdditionalAttributes(writer, cycle);
            
            writer.attribute(getUrlAttribute(), constructURL(linkComponent,
                    cycle));
            
            String target = linkComponent.getTarget();
            
            if (HiveMind.isNonBlank(target))
                writer.attribute(getTargetAttribute(), target);
            
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
            
            // linkComponent.renderAdditionalAttributes(writer, cycle);
            
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
     */

    protected void beforeBodyRender(IMarkupWriter writer, IRequestCycle cycle,
            ILinkComponent link)
    {
    }

    /**
     * Invoked after the body of the link is rendered, but before
     * {@link ILinkComponent#renderAdditionalAttributes(IMarkupWriter, IRequestCycle)}is
     * invoked (but only if the link is not disabled).
     * <p>
     * This implementation does nothing.
     */

    protected void afterBodyRender(IMarkupWriter writer, IRequestCycle cycle,
            ILinkComponent link)
    {
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
