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
package net.sf.tapestry.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IDirect;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RenderOnlyPropertyException;
import net.sf.tapestry.RenderRewoundException;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.StaleLinkException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.engine.ILink;
import net.sf.tapestry.html.Body;
import net.sf.tapestry.util.IdAllocator;
import net.sf.tapestry.util.StringSplitter;
import net.sf.tapestry.valid.IValidationDelegate;

/**
 *  Component which contains form element components.  Forms use the
 *  action or direct services to handle the form submission.  A Form will wrap
 *  other components and static HTML, including
 *  form components such as {@link TextArea}, {@link TextField}, {@link Checkbox}, etc.
 * 
 *  [<a href="../../../../../ComponentReference/Form.html">Component Reference</a>]
 * 
 *  <p>When a form is submitted, it continues through the rewind cycle until
 *  <em>after</em> all of its wrapped elements have renderred.  As the form
 *  component render (in the rewind cycle), they will be updating
 *  properties of the containing page and notifying thier listeners.  Again:
 *  each form component is responsible not only for rendering HTML (to present the
 *  form), but for handling it's share of the form submission.
 *
 *  <p>Only after all that is done will the Form notify its listener.
 *
 *  <p>Starting in release 1.0.2, a Form can use either the direct service or
 *  the action service.  The default is the direct service, even though
 *  in earlier releases, only the action service was available.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public abstract class Form extends AbstractComponent implements IForm, IDirect
{
    private boolean _rewinding;
    private boolean _rendering;
    private String _name;

    /**
     *  Used when rewinding the form to figure to match allocated ids (allocated during
     *  the rewind) against expected ids (allocated in the previous request cycle, when
     *  the form was rendered).
     * 
     *  @since 2.4
     * 
     **/

    private int _allocatedIdIndex;

    /**
     *  The list of allocated ids for form elements within this form.  This list
     *  is constructed when a form renders, and is validated against when the
     *  form is rewound.
     * 
     *  @since 2.4
     * 
     **/

    private List _allocatedIds = new ArrayList();

    /**
     *  {@link Map}, keyed on {@link FormEventType}.  Values are either a String (the name
     *  of a single event), or a {@link List} of Strings.
     *
     *  @since 1.0.2
     **/

    private Map _events;

    private static final int EVENT_MAP_SIZE = 3;

    private IdAllocator _elementIdAllocator = new IdAllocator();

    /**
     *  Returns the currently active {@link IForm}, or null if no form is
     *  active.  This is a convienience method, the result will be
     *  null, or an instance of {@link IForm}, but not necessarily a
     *  <code>Form</code>.
     *
     **/

    public static IForm get(IRequestCycle cycle)
    {
        return (IForm) cycle.getAttribute(ATTRIBUTE_NAME);
    }

    /**
     *  Indicates to any wrapped form components that they should respond to the form
     *  submission.
     *
     *  @throws RenderOnlyPropertyException if not rendering.
     **/

    public boolean isRewinding()
    {
        if (!_rendering)
            throw new RenderOnlyPropertyException(this, "rewinding");

        return _rewinding;
    }

    /**
     *  Returns true if this Form is configured to use the direct
     *  service.
     *
     *  <p>This is derived from the direct parameter, and defaults
     *  to true if not bound.
     *
     *  @since 1.0.2
     **/

    public abstract boolean isDirect();

    /**
     *  Returns true if the stateful parameter is bound to
     *  a true value.  If stateful is not bound, also returns
     *  the default, true.
     *
     *  @since 1.0.1
     **/

    public boolean getRequiresSession()
    {
		return isStateful();
    }

    /**
     *  Constructs a unique identifier (within the Form).  The identifier
     *  consists of the component's id, with an index number added to
     *  ensure uniqueness.
     *
     *  <p>Simply invokes {@link #getElementId(String)} with the component's id.
     *
     *
     *  @since 1.0.2
     **/

    public String getElementId(IComponent component) throws RequestCycleException
    {
        return getElementId(component, component.getId());
    }

    /**
     *  Constructs a unique identifier from the base id.  If possible, the
     *  id is used as-is.  Otherwise, a unique identifier is appended
     *  to the id.
     *
     *  <p>This method is provided simply so that some components
     * ({@link ImageSubmit}) have more specific control over
     *  their names.
     *
     *  @since 1.0.3
     *
     **/

    public String getElementId(IComponent component, String baseId) throws RequestCycleException
    {
        String result = _elementIdAllocator.allocateId(baseId);

        if (_rewinding)
        {
            if (_allocatedIdIndex >= _allocatedIds.size())
            {
                throw new StaleLinkException(
                    Tapestry.getString(
                        "Form.too-many-ids",
                        getExtendedId(),
                        Integer.toString(_allocatedIds.size()),
                        component.getExtendedId()),
                    this);
            }

            String expected = (String) _allocatedIds.get(_allocatedIdIndex);

            if (!result.equals(expected))
                throw new StaleLinkException(
                    Tapestry.getString(
                        "Form.id-mismatch",
                        new Object[] {
                            getExtendedId(),
                            Integer.toString(_allocatedIdIndex + 1),
                            expected,
                            result,
                            component.getExtendedId()}),
                    this);
        }
        else
        {
            _allocatedIds.add(result);
        }

        _allocatedIdIndex++;

        return result;
    }

    /**
     *  Returns the name generated for the form.  This is used to faciliate
     *  components that write JavaScript and need to access the form or
     *  its contents.
     *
     *  <p>This value is generated when the form renders, and is not cleared.
     *  If the Form is inside a {@link net.sf.tapestry.components.Foreach}, 
     *  this will be the most recently
     *  generated name for the Form.
     *
     *  <p>This property is exposed so that sophisticated applications can write
     *  JavaScript handlers for the form and components within the form.
     *
     *  @see AbstractFormComponent#getName()
     *
     **/

    public String getName()
    {
        return _name;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new RequestCycleException(Tapestry.getString("Form.forms-may-not-nest"), this);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        String actionId = cycle.getNextActionId();
        _name = "Form" + actionId;

        boolean renderForm = !cycle.isRewinding();
        boolean rewound = cycle.isRewound(this);

        _rewinding = rewound;

        ILink link = getLink(cycle, actionId);

        if (renderForm)
        {
        	String method = getMethod();
        	
            writer.begin("form");
            writer.attribute("method", (method == null) ? "post" : method);
            writer.attribute("name", _name);
            writer.attribute("action", link.getURL(null, false));

            generateAttributes(writer, cycle);
            writer.println();
        }

        // Write the hidden's, or at least, reserve the query parameters
        // required by the Gesture.

        writeLinkParameters(writer, link, !renderForm);

        _allocatedIdIndex = 0;

        _rendering = true;

        if (rewound)
        {
            String storedIdList = cycle.getRequestContext().getParameter(_name);

            reconstructAllocatedIds(storedIdList);
        }

        renderBody(writer, cycle);

        if (renderForm)
        {
            // What's this for?  It's part of checking for stale links.  
            // We record the list of allocated ids.
            // On rewind, we check that the stored list against which
            // ids were allocated.  If the persistent state of the page or
            // application changed between render (previous request cycle)
            // and rewind (current request cycle), then the list
            // of ids will change as well.

            writer.beginEmpty("input");
            writer.attribute("type", "hidden");
            writer.attribute("name", _name);
            writer.attribute("value", buildAllocatedIdList());
            writer.println();

            writer.end("form");

            // Write out event handlers collected during the rendering.

            emitEventHandlers(writer, cycle);
        }

        if (rewound)
        {
            int expected = _allocatedIds.size();

            // The other case, _allocatedIdIndex > expected, is
            // checked for inside getElementId().  Remember that
            // _allocatedIdIndex is incremented after allocating.

            if (_allocatedIdIndex < expected)
            {
                String nextExpectedId = (String) _allocatedIds.get(_allocatedIdIndex);

                throw new StaleLinkException(
                    Tapestry.getString(
                        "Form.too-few-ids",
                        getExtendedId(),
                        Integer.toString(expected - _allocatedIdIndex),
                        nextExpectedId),
                    this);
            }

			IActionListener listener = getListener();

            if (listener != null)
                listener.actionTriggered(this, cycle);

            // Abort the rewind render.

            throw new RenderRewoundException(this);
        }

        cycle.removeAttribute(ATTRIBUTE_NAME);
    }

    /**
     *  Adds an additional event handler.
     *
     *  @since 1.0.2
     * 
     **/

    public void addEventHandler(FormEventType type, String functionName)
    {
        if (_events == null)
            _events = new HashMap(EVENT_MAP_SIZE);

        Object value = _events.get(type);

        // The value can either be a String, or a List of String.  Since
        // it is rare for there to be more than one event handling function,
        // we start with just a String.

        if (value == null)
        {
            _events.put(type, functionName);
            return;
        }

        // The second function added converts it to a List.

        if (value instanceof String)
        {
            List list = new ArrayList();
            list.add(value);
            list.add(functionName);

            _events.put(type, list);
            return;
        }

        // The third and subsequent function justs
        // adds to the List.

        List list = (List) value;
        list.add(functionName);
    }

    private void emitEventHandlers(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        StringBuffer buffer = null;

        if (_events == null || _events.isEmpty())
            return;

        Body body = Body.get(cycle);

        if (body == null)
            throw new RequestCycleException(
                Tapestry.getString("Form.needs-body-for-event-handlers"),
                this);

        Iterator i = _events.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();
            FormEventType type = (FormEventType) entry.getKey();
            Object value = entry.getValue();

            String formPath = "document." + _name;
            String propertyName = type.getPropertyName();
            String finalFunctionName;

            boolean combineWithAnd = type.getCombineUsingAnd();

            // The typical case; one event one event handler.  Easy enough.

            if (value instanceof String)
            {
                finalFunctionName = (String) value;
            }
            else
            {

                String compositeName = propertyName + "_" + _name;

                if (buffer == null)
                    buffer = new StringBuffer(200);

                buffer.append("\nfunction ");
                buffer.append(compositeName);
                buffer.append("()\n{");

                List l = (List) value;
                int count = l.size();

                for (int j = 0; j < count; j++)
                {
                    String functionName = (String) l.get(j);

                    if (j > 0)
                    {

                        if (combineWithAnd)
                            buffer.append(" &&");
                        else
                            buffer.append(";");
                    }

                    buffer.append("\n  ");

                    if (combineWithAnd)
                    {
                        if (j == 0)
                            buffer.append("return ");
                        else
                            buffer.append("  ");
                    }

                    buffer.append(functionName);
                    buffer.append("()");
                }

                buffer.append(";\n}\n\n");

                finalFunctionName = compositeName;
            }

            body.addOtherInitialization(
                formPath + "." + propertyName + " = " + finalFunctionName + ";");

        }

        if (buffer != null)
            body.addOtherScript(buffer.toString());

    }

    /**
     *  Simply invokes {@link #render(IMarkupWriter, IRequestCycle)}.
     *
     *  @since 1.0.2
     * 
     **/

    public void rewind(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        render(writer, cycle);
    }

    /**
     *  Method invoked by the direct service.
     *
     *  @since 1.0.2
     *
     **/

    public void trigger(IRequestCycle cycle) throws RequestCycleException
    {
        Object[] parameters = cycle.getServiceParameters();

        cycle.rewindForm(this, (String) parameters[0]);
    }

    /**
     *  Builds the EngineServiceLink for the form, using either the direct or
     *  action service. 
     *
     *  @since 1.0.3
     *
     **/

    private ILink getLink(IRequestCycle cycle, String actionId)
    {
        String serviceName = null;

        if (isDirect())
            serviceName = Tapestry.DIRECT_SERVICE;
        else
            serviceName = Tapestry.ACTION_SERVICE;

        IEngine engine = cycle.getEngine();
        IEngineService service = engine.getService(serviceName);

        // A single service parameter is used to store the actionId.

        return service.getLink(cycle, this, new String[] { actionId });
    }

    private void writeLinkParameters(IMarkupWriter writer, ILink link, boolean reserveOnly)
    {
        String[] names = link.getParameterNames();
        int count = Tapestry.size(names);

        for (int i = 0; i < count; i++)
        {

            String name = names[i];

            // Reserve the name.

            _elementIdAllocator.allocateId(name);

            if (!reserveOnly)
                writeHiddenFieldsForParameter(writer, link, name);
        }
    }

    /**
     *  @since 2.2
     * 
     **/

    private void writeHiddenFieldsForParameter(
        IMarkupWriter writer,
        ILink link,
        String parameterName)
    {
        String[] values = link.getParameterValues(parameterName);

        for (int i = 0; i < values.length; i++)
        {
            writer.beginEmpty("input");
            writer.attribute("type", "hidden");
            writer.attribute("name", parameterName);
            writer.attribute("value", values[i]);
            writer.println();
        }
    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        _rendering = false;

        _allocatedIdIndex = 0;
        _allocatedIds.clear();

        _events = null;

        _elementIdAllocator.clear();

        super.cleanupAfterRender(cycle);
    }



    /**
     *  Converts the allocateIds property into a string, a comma-separated list of ids.
     *  This is included as a hidden field in the form and is used to identify
     *  discrepencies when the form is submitted.
     * 
     *  @since 2.4
     * 
     **/

    protected String buildAllocatedIdList()
    {
        StringBuffer buffer = new StringBuffer();
        int count = _allocatedIds.size();

        for (int i = 0; i < count; i++)
        {
            if (i > 0)
                buffer.append(',');

            buffer.append(_allocatedIds.get(i));
        }

        return buffer.toString();
    }

    /**
     *  Converts a string passed as a parameter (and containing a comma
     *  separated list of ids) back into the allocateIds property.
     * 
     *  @see #buildAllocatedIdList()
     * 
     *  @since 2.4
     * 
     **/

    protected void reconstructAllocatedIds(String storedIdList)
    {
        if (Tapestry.isNull(storedIdList))
            return;

        StringSplitter splitter = new StringSplitter(',');

        String[] ids = splitter.splitToArray(storedIdList);

        for (int i = 0; i < ids.length; i++)
            _allocatedIds.add(ids[i]);
    }

    public abstract IValidationDelegate getDelegate();

    public abstract void setDelegate(IValidationDelegate delegate);

    public abstract void setDirect(boolean direct);
    
    public abstract IActionListener getListener();

    public abstract String getMethod();

    /**
     *  Invoked when not rendering, so it uses the stateful binding.
     *  If not bound, returns true.
     * 
     **/

    public boolean isStateful()
    {
    	IBinding statefulBinding = getStatefulBinding();
    	
        if (statefulBinding == null)
            return true;

        return statefulBinding.getBoolean();
    }

    public abstract IBinding getStatefulBinding();
    
    protected void finishLoad()
    {
        setDirect(true);
    }

}