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
package net.sf.tapestry;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.tapestry.parse.ComponentTemplate;
import net.sf.tapestry.parse.TemplateToken;
import net.sf.tapestry.parse.TokenType;
import net.sf.tapestry.spec.ComponentSpecification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base implementation for most components that use an HTML template.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public class BaseComponent extends AbstractComponent
{
    private static final Log LOG = LogFactory.getLog(BaseComponent.class);

    private int _outerCount = 0;

    private static final int OUTER_INIT_SIZE = 5;

    private IRender[] _outer;

    /**
     *  A class used with invisible localizations.  Constructed
     *  from {@link TokenType#LOCALIZATION} {@link TemplateToken}s.
     * 
     *  @since 2.0.4
     * 
     **/

    private class LocalizedStringRender implements IRender
    {
        private String _key;
        private Map _attributes;
        private boolean _raw;

        private LocalizedStringRender(String key, boolean raw, Map attributes)
        {
            _key = key;
            _raw = raw;
            _attributes = attributes;
        }

        public void render(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
        {
            if (cycle.isRewinding())
                return;

            if (_attributes != null)
            {
                writer.begin("span");

                Iterator i = _attributes.entrySet().iterator();

                while (i.hasNext())
                {
                    Map.Entry entry = (Map.Entry) i.next();
                    String attributeName = (String) entry.getKey();
                    String attributeValue = (String) entry.getValue();

                    writer.attribute(attributeName, attributeValue);
                }
            }

            String value = getString(_key);

            if (_raw)
                writer.printRaw(value);
            else
                writer.print(value);

            if (_attributes != null)
                writer.end();
        }

        public String toString()
        {
            StringBuffer buffer = new StringBuffer("LocalizedStringRender@");
            buffer.append(Integer.toHexString(hashCode()));
            buffer.append('[');
            buffer.append(_key);

            if (_attributes != null)
            {
                buffer.append(' ');
                buffer.append(_attributes);
            }

            buffer.append(']');

            return buffer.toString();
        }
    }

    /**
     *  Adds an element as an outer element for the receiver.  Outer
     *  elements are elements that should be directly rendered by the
     *  receiver's <code>render()</code> method.  That is, they are
     *  top-level elements on the HTML template.
     *
     * 
     **/

    private void addOuter(IRender element)
    {
        if (_outer == null)
        {
            _outer = new IRender[OUTER_INIT_SIZE];
            _outer[0] = element;

            _outerCount = 1;
            return;
        }

        // No more room?  Make the array bigger.

        if (_outerCount == _outer.length)
        {
            IRender[] newOuter;

            newOuter = new IRender[_outer.length * 2];

            System.arraycopy(_outer, 0, newOuter, 0, _outerCount);

            _outer = newOuter;
        }

        _outer[_outerCount++] = element;
    }

    /**
     *
     *  Reads the receiver's template and figures out which elements wrap which
     *  other elements.
     *
     *  <P>This is coded as a single, big, ugly method for efficiency.
     * 
     **/

    private void readTemplate(IRequestCycle cycle, IPageLoader loader) throws PageLoaderException
    {
        Set seenIds = new HashSet();
        IPageSource pageSource = loader.getEngine().getPageSource();

        if (LOG.isDebugEnabled())
            LOG.debug(this +" reading template");

        ITemplateSource source = loader.getTemplateSource();
        ComponentTemplate componentTemplate = source.getTemplate(cycle, this);

        int count = componentTemplate.getTokenCount();

        // The stack can never be as large as the number of tokens, so this is safe.

        IComponent[] componentStack = new IComponent[count];
        IComponent activeComponent = null;
        int stackx = 0;

        for (int i = 0; i < count; i++)
        {
            TemplateToken token = componentTemplate.getToken(i);
            TokenType type = token.getType();

            if (type == TokenType.TEXT)
            {
                addText(activeComponent, token);

                continue;
            }

            if (type == TokenType.OPEN)
            {
                IComponent component = addStartComponent(activeComponent, token, pageSource, seenIds);

                componentStack[stackx++] = activeComponent;

                activeComponent = component;

                continue;
            }

            if (type == TokenType.CLOSE)
            {
                try
                {
                    activeComponent = componentStack[--stackx];
                }
                catch (IndexOutOfBoundsException ex)
                {
                    // This is now almost impossible to reach, because the
                    // TemplateParser does a great job of checking for most of these cases.

                    throw new PageLoaderException(Tapestry.getString("BaseComponent.unbalanced-close-tags"), this);
                }

                continue;
            }

            if (type == TokenType.LOCALIZATION)
            {
                addStringLocalization(activeComponent, token);

                continue;
            }
        }

        // This is also pretty much unreachable, and the message is kind of out
        // of date, too.

        if (stackx != 0)
            throw new PageLoaderException(Tapestry.getString("BaseComponent.unbalance-open-tags"), this);

        checkAllComponentsReferenced(seenIds);

        if (LOG.isDebugEnabled())
            LOG.debug(this +" finished reading template");
    }

    /** @since 2.1 **/

    private void addStringLocalization(IComponent activeComponent, TemplateToken token)
    {
        IRender renderer = new LocalizedStringRender(token.getId(), token.isRaw(), token.getAttributes());

        if (activeComponent == null)
            addOuter(renderer);
        else
            activeComponent.addBody(renderer);
    }

    /** @since 2.1 **/

    private IComponent addStartComponent(
        IComponent activeComponent,
        TemplateToken token,
        IPageSource pageSource,
        Set seenIds)
        throws PageLoaderException, BodylessComponentException
    {
        String id = token.getId();
        IComponent component = null;

        try
        {
            component = getComponent(id);

        }
        catch (NoSuchComponentException ex)
        {
            throw new PageLoaderException(
                Tapestry.getString("BaseComponent.undefined-embedded-component", getExtendedId(), id),
                this,
                ex);
        }

        // Make sure the template contains each component only once.

        if (seenIds.contains(id))
            throw new PageLoaderException(
                Tapestry.getString("BaseComponent.multiple-component-references", getExtendedId(), id),
                this);

        seenIds.add(id);

        if (activeComponent == null)
            addOuter(component);
        else
        {
            // If you use a <jwc> tag in the template, you can get here.
            // If you use a normal tag and a jwcid attribute, the
            // body is automatically editted out.

            if (!activeComponent.getSpecification().getAllowBody())
                throw new BodylessComponentException(activeComponent);

            activeComponent.addBody(component);
        }

        addStaticBindings(component, token.getAttributes(), pageSource);

        return component;
    }

    /** @since 2.1 **/

    private void addText(IComponent activeComponent, TemplateToken token) throws BodylessComponentException
    {
        // Get a render for the token.  This allows the token and the render
        // to be shared across sessions.

        IRender element = token.getRender();

        if (activeComponent == null)
            addOuter(element);
        else
        {

            // The new template parser edits text out automatically;
            // this code probably can't be reached.

            if (!activeComponent.getSpecification().getAllowBody())
                throw new BodylessComponentException(activeComponent);

            activeComponent.addBody(element);
        }
    }

    /**
     *  Adds static bindings for any attrributes specified in the HTML
     *  template, skipping any that are reserved (explicitly, or
     *  because they match a formal parameter name).
     *
     **/

    private void addStaticBindings(IComponent component, Map attributes, IPageSource pageSource)
    {
        if (attributes == null || attributes.isEmpty())
            return;

        ComponentSpecification spec = component.getSpecification();

        boolean rejectInformal = !spec.getAllowInformalParameters();

        Iterator i = attributes.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();

            String name = (String) e.getKey();

            // If matches a formal parameter name, allow it to be set
            // unless there's already a binding.

            boolean isFormal = (spec.getParameter(name) != null);

            if (isFormal)
            {
                if (component.getBinding(name) != null)
                    continue;
            }
            else
            {
                // Skip informal parameters if the component doesn't allow them.

                if (rejectInformal)
                    continue;

                // If the name is reserved (matches a formal parameter
                // or reserved name, caselessly), then skip it.

                if (spec.isReservedParameterName(name))
                    continue;
            }

            String value = (String) e.getValue();

            IBinding binding = pageSource.getStaticBinding(value);

            component.setBinding(name, binding);
        }
    }

    private void checkAllComponentsReferenced(Set seenIds) throws PageLoaderException
    {
        // First, contruct a modifiable copy of the ids of all expected components
        // (that is, components declared in the specification).

        Map components = getComponents();

        Set ids = components.keySet();

        // If the seen ids ... ids referenced in the template, matches
        // all the ids in the specification then we're fine.

        if (seenIds.containsAll(ids))
            return;

        // Create a modifiable copy.  Remove the ids that are referenced in
        // the template.  The remainder are worthy of note.

        ids = new HashSet(ids);
        ids.removeAll(seenIds);

        int count = ids.size();

        String key =
            (count == 1) ? "BaseComponent.missing-component-spec-single" : "BaseComponent.missing-component-spec-multi";

        StringBuffer buffer = new StringBuffer(Tapestry.getString(key, getExtendedId()));

        Iterator i = ids.iterator();
        int j = 1;

        while (i.hasNext())
        {
            if (j == 1)
                buffer.append(' ');
            else
                if (j == count)
                {
                    buffer.append(' ');
                    buffer.append(Tapestry.getString("BaseComponent.and"));
                    buffer.append(' ');
                }
                else
                    buffer.append(", ");

            buffer.append(i.next());

            j++;
        }

        buffer.append('.');

        LOG.error(buffer.toString());
    }

    /**
     *   Renders the top level components contained by the receiver.
     *
     *   @since 2.0.3
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Begin render " + getExtendedId());

        for (int i = 0; i < _outerCount; i++)
            _outer[i].render(writer, cycle);

        if (LOG.isDebugEnabled())
            LOG.debug("End render " + getExtendedId());
    }

    /**
     *  Loads the template for the component, and invokes
     *  {@link #finishLoad()}.  Subclasses must invoke this method first,
     *  before adding any additional behavior, though its usually
     *  simpler to override {@link #finishLoad()} instead.
     *
     **/

    public void finishLoad(IRequestCycle cycle, IPageLoader loader, ComponentSpecification specification)
        throws PageLoaderException
    {
        readTemplate(cycle, loader);

        finishLoad();
    }
}