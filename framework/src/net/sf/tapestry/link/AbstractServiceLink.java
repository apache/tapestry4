/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
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
     *  <p>This is to support components such as {@link net.sf.tapestry.html.Rollover}, which
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

        renderBody(wrappedWriter, cycle);

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