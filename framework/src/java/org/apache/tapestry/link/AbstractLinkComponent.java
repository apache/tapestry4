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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.components.LinkEventType;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 * Base class for implementations of {@link ILinkComponent}. Includes a disabled attribute (that
 * should be bound to a disabled parameter), an anchor attribute, and a renderer attribute (that
 * should be bound to a renderer parameter). A default, shared instance of
 * {@link org.apache.tapestry.link.DefaultLinkRenderer} is used when no specific renderer is
 * provided.
 * 
 * @author Howard Lewis Ship
 */

public abstract class AbstractLinkComponent extends AbstractComponent implements ILinkComponent
{
    private Map _eventHandlers;

    public abstract boolean isDisabled();

    /**
     * Adds an event handler (typically, from a wrapped component such as a
     * {@link org.apache.tapestry.html.Rollover}).
     */

    public void addEventHandler(LinkEventType eventType, String functionName)
    {
        Object currentValue;

        if (_eventHandlers == null)
            _eventHandlers = new HashMap();

        currentValue = _eventHandlers.get(eventType);

        // The first value is added as a String

        if (currentValue == null)
        {
            _eventHandlers.put(eventType, functionName);
            return;
        }

        // When adding the second value, convert to a List

        if (currentValue instanceof String)
        {
            List list = new ArrayList();
            list.add(currentValue);
            list.add(functionName);

            _eventHandlers.put(eventType, list);
            return;
        }

        // For the third and up, add the new function to the List

        List list = (List) currentValue;
        list.add(functionName);
    }

    /**
     * Renders the link by delegating to an instance of {@link ILinkRenderer}.
     */

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        getRenderer().renderLink(writer, cycle, this);
    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        _eventHandlers = null;

        super.cleanupAfterRender(cycle);
    }

    protected void writeEventHandlers(IMarkupWriter writer, IRequestCycle cycle)
    {
        String name = null;

        if (_eventHandlers == null)
            return;

        PageRenderSupport pageRenderSupport = TapestryUtils.getPageRenderSupport(cycle, this);

        Iterator i = _eventHandlers.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();
            LinkEventType type = (LinkEventType) entry.getKey();

            name = writeEventHandler(
                    writer,
                    pageRenderSupport,
                    name,
                    type.getAttributeName(),
                    entry.getValue());
        }

    }

    protected String writeEventHandler(IMarkupWriter writer, PageRenderSupport pageRenderSupport,
            String name, String attributeName, Object value)
    {
        String wrapperFunctionName;

        if (value instanceof String)
        {
            wrapperFunctionName = (String) value;
        }
        else
        {
            String finalName = name == null ? pageRenderSupport.getUniqueString("Link") : name;

            wrapperFunctionName = attributeName + "_" + finalName;

            StringBuffer buffer = new StringBuffer();

            buffer.append("function ");
            buffer.append(wrapperFunctionName);
            buffer.append(" ()\n{\n");

            Iterator i = ((List) value).iterator();
            while (i.hasNext())
            {
                String functionName = (String) i.next();
                buffer.append("  ");
                buffer.append(functionName);
                buffer.append("();\n");
            }

            buffer.append("}\n\n");

            pageRenderSupport.addBodyScript(buffer.toString());
        }

        writer.attribute(attributeName, "javascript:" + wrapperFunctionName + "();");

        return name;
    }

    /** @since 3.0 * */

    public abstract ILinkRenderer getRenderer();

    public abstract void setRenderer(ILinkRenderer renderer);

    public void renderAdditionalAttributes(IMarkupWriter writer, IRequestCycle cycle)
    {
        writeEventHandlers(writer, cycle);

        // Generate additional attributes from informal parameters.

        renderInformalParameters(writer, cycle);
    }

    /**
     * Utility method for subclasses; Gets the named service from the engine and invokes
     * {@link IEngineService#getLink(org.apache.tapestry.IComponent, Object[])}on
     * it.
     * 
     * @since 3.0
     * @deprecated To be removed in 4.1; links may now have the necessary engine service injected.
     */

    protected ILink getLink(IRequestCycle cycle, String serviceName, Object parameter)
    {
        IEngineService service = cycle.getEngine().getService(serviceName);

        return service.getLink(false, parameter);
    }

    public abstract String getAnchor();

    public ILink getLink(IRequestCycle cycle)
    {
        return null;
    }

    /**
     * Sets the renderer parameter property to its default value
     * {@link DefaultLinkRenderer#SHARED_INSTANCE}.
     * 
     * @since 3.0
     */
    protected void finishLoad()
    {
        setRenderer(DefaultLinkRenderer.SHARED_INSTANCE);
    }

}