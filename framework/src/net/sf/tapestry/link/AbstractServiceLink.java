/*
 * Tapestry Web Application Framework
 * Copyright (c) 2002 by Howard Lewis Ship 
 *
 * mailto:hship@users.sf.net
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 **/
package net.sf.tapestry.link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.components.IServiceLink;
import net.sf.tapestry.components.ServiceLinkEventType;
import net.sf.tapestry.html.Body;

/**
 *  Base class for
 *  implementations of {@link IServiceLink}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public abstract class AbstractServiceLink extends AbstractComponent implements IServiceLink
{
    private boolean disabled;

    protected Body body;

    protected static final int MAP_SIZE = 3;

    protected Map eventHandlers;

    public boolean isDisabled()
    {

        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    /**
     *  Adds an event handler (typically, from a wrapped component such
     *  as a {@link net.sf.tapestry.html.Rollover}).
     *
     **/

    public void addEventHandler(ServiceLinkEventType eventType, String functionName)
    {
        Object currentValue;

        if (eventHandlers == null)
            eventHandlers = new HashMap(MAP_SIZE);

        currentValue = eventHandlers.get(eventType);

        // The first value is added as a String

        if (currentValue == null)
        {
            eventHandlers.put(eventType, functionName);
            return;
        }

        // When adding the second value, convert to a List

        if (currentValue instanceof String)
        {
            List list = new ArrayList();
            list.add(currentValue);
            list.add(functionName);

            eventHandlers.put(eventType, list);
            return;
        }

        // For the third and up, add the new function to the List

        List list = (List) currentValue;
        list.add(functionName);
    }

    /**
     *  Renders the link.  This is somewhat complicated, because a
     *  nested {@link IMarkupWriter response writer} is used
     *  to render the contents of the link, before the link
     *  itself actually renders.
     *
     *  <p>This is to support components such as {@link Rollover}, which
     *  must specify some attributes of the service link 
     *  as they render in order to
     *  create some client-side JavaScript that works.  Thus the
     *  service link renders its wrapped components into
     *  a temporary buffer, then renders its own HTML.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        IMarkupWriter wrappedWriter;

        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new RequestCycleException(Tapestry.getString("AbstractServiceLink.no-nesting"), this);

        body = Body.get(cycle);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        if (!disabled)
        {
            writer.begin("a");
            writer.attribute("href", getURL(cycle));

            // Allow the wrapped components a chance to render.
            // Along the way, they may interact with this component
            // and cause the name variable to get set.

            wrappedWriter = writer.getNestedWriter();
        }
        else
            wrappedWriter = writer;

        renderWrapped(wrappedWriter, cycle);

        if (!disabled)
        {
            // Write any attributes specified by wrapped components.

            writeEventHandlers(writer);

            // Generate additional attributes from informal parameters.

            generateAttributes(writer, cycle);

            // Dump in HTML provided by wrapped components

            wrappedWriter.close();

            // Close the <a> tag

            writer.end();
        }

        cycle.removeAttribute(ATTRIBUTE_NAME);
    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        eventHandlers = null;
        body = null;

        super.cleanupAfterRender(cycle);
    }

    protected void writeEventHandlers(IMarkupWriter writer) throws RequestCycleException
    {
        String name = null;

        if (eventHandlers == null)
            return;

        Iterator i = eventHandlers.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();
            ServiceLinkEventType type = (ServiceLinkEventType) entry.getKey();

            name = writeEventHandler(writer, name, type.getAttributeName(), entry.getValue());
        }

    }

    protected String writeEventHandler(
        IMarkupWriter writer,
        String name,
        String attributeName,
        Object value)
        throws RequestCycleException
    {
        String wrapperFunctionName;

        if (value instanceof String)
        {
            wrapperFunctionName = (String) value;
        }
        else
        {
            if (body == null)
                throw new RequestCycleException(
                    Tapestry.getString("AbstractServiceLink.events-need-body"),
                    this,
                    null);

            if (name == null)
                name = "Link" + body.getUniqueId();

            wrapperFunctionName = attributeName + "_" + name;

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

            body.addOtherScript(buffer.toString());
        }

        writer.attribute(attributeName, "javascript:" + wrapperFunctionName + "();");

        return name;

    }

    /**
     * 
     *  Implemented by subclasses to provide the URL for the HTML HREF attribute.
     * 
     *  @since 2.0.2
     * 
     **/

    protected abstract String getURL(IRequestCycle cycle) throws RequestCycleException;
}