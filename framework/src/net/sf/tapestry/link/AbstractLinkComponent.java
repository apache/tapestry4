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
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.components.ILinkComponent;
import net.sf.tapestry.components.LinkEventType;
import net.sf.tapestry.engine.EngineServiceLink;
import net.sf.tapestry.engine.ILink;
import net.sf.tapestry.html.Body;

/**
 *  Base class for
 *  implementations of {@link ILinkComponent}.  Includes a disabled attribute
 *  (that should be bound to a disabled parameter), 
 *  an anchor attribute, and a
 *  renderer attribute (that should be bound to a renderer parameter).  A default,
 *  shared instance of {@link net.sf.tapestry.link.DefaultLinkRenderer} is
 *  used when no specific renderer is provided.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public abstract class AbstractLinkComponent extends AbstractComponent implements ILinkComponent
{
    private boolean _disabled;
    private ILinkRenderer _renderer = DefaultLinkRenderer.SHARED_INSTANCE;
    private String _anchor;

    private Map _eventHandlers;

    public boolean isDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    /**
     *  Adds an event handler (typically, from a wrapped component such
     *  as a {@link net.sf.tapestry.html.Rollover}).
     *
     **/

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
     *  Renders the link by delegating to an instance
     *  of {@link ILinkRenderer}.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        _renderer.renderLink(writer, cycle, this);
    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        _eventHandlers = null;

        super.cleanupAfterRender(cycle);
    }

    protected void writeEventHandlers(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        String name = null;

        if (_eventHandlers == null)
            return;

        Body body = Body.get(cycle);

        if (body == null)
            throw new RequestCycleException(
                Tapestry.getString("AbstractLinkComponent.events-need-body"),
                this);

        Iterator i = _eventHandlers.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();
            LinkEventType type = (LinkEventType) entry.getKey();

            name = writeEventHandler(writer, body, name, type.getAttributeName(), entry.getValue());
        }

    }

    protected String writeEventHandler(
        IMarkupWriter writer,
        Body body,
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

    /** @since 2.4 **/

    public ILinkRenderer getRenderer()
    {
        return _renderer;
    }

    /** @since 2.4 **/

    public void setRenderer(ILinkRenderer renderer)
    {
        _renderer = renderer;
    }

    public void renderAdditionalAttributes(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        writeEventHandlers(writer, cycle);

        // Generate additional attributes from informal parameters.

        generateAttributes(writer, cycle);
    }

    /**
     *  Utility method for subclasses; Gets the named service from the engine
     *  and invokes {@link net.sf.tapestry.IEngineService#buildGesture(IRequestCycle, IComponent, Object[])}
     *  on it.
     * 
     *  @since 2.4
     * 
     **/

    protected ILink getLink(
        IRequestCycle cycle,
        String serviceName,
        Object[] serviceParameters)
    {
        IEngineService service = cycle.getEngine().getService(serviceName);

        return service.getLink(cycle, this, serviceParameters);
    }
    
    public String getAnchor()
    {
        return _anchor;
    }

    public void setAnchor(String anchor)
    {
        _anchor = anchor;
    }

}