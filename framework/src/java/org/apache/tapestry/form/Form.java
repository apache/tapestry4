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

package org.apache.tapestry.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.RenderRewoundException;
import org.apache.tapestry.StaleLinkException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.engine.ActionServiceParameter;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.IdAllocator;
import org.apache.tapestry.util.StringSplitter;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.web.WebResponse;

/**
 * Component which contains form element components. Forms use the action or direct services to
 * handle the form submission. A Form will wrap other components and static HTML, including form
 * components such as {@link TextArea},{@link TextField},{@link Checkbox}, etc. [ <a
 * href="../../../../../ComponentReference/Form.html">Component Reference </a>]
 * <p>
 * When a form is submitted, it continues through the rewind cycle until <em>after</em> all of its
 * wrapped elements have renderred. As the form component render (in the rewind cycle), they will be
 * updating properties of the containing page and notifying thier listeners. Again: each form
 * component is responsible not only for rendering HTML (to present the form), but for handling it's
 * share of the form submission.
 * <p>
 * Only after all that is done will the Form notify its listener.
 * <p>
 * Starting in release 1.0.2, a Form can use either the direct service or the action service. The
 * default is the direct service, even though in earlier releases, only the action service was
 * available.
 * 
 * @author Howard Lewis Ship, David Solis
 */

public abstract class Form extends AbstractComponent implements IForm, IDirect
{
    private static class HiddenValue
    {
        String _name;

        String _value;

        String _id;

        private HiddenValue(String name, String value)
        {
            this(name, null, value);
        }

        private HiddenValue(String name, String id, String value)
        {
            _name = name;
            _id = id;
            _value = value;
        }
    }

    private boolean _rewinding;

    private String _name;

    /**
     * Used when rewinding the form to figure to match allocated ids (allocated during the rewind)
     * against expected ids (allocated in the previous request cycle, when the form was rendered).
     * 
     * @since 3.0
     */

    private int _allocatedIdIndex;

    /**
     * The list of allocated ids for form elements within this form. This list is constructed when a
     * form renders, and is validated against when the form is rewound.
     * 
     * @since 3.0
     */

    private List _allocatedIds = new ArrayList();

    /**
     * {@link Map}, keyed on {@link FormEventType}. Values are either a String (the name of a
     * single event), or a {@link List}of Strings.
     * 
     * @since 1.0.2
     */

    private Map _events;

    private static final int EVENT_MAP_SIZE = 3;

    private IdAllocator _elementIdAllocator = new IdAllocator();

    private String _encodingType;

    private List _hiddenValues;

    /**
     * Reserved id that stores the list of allocated ids and the (optional) list of reserved ids.
     * 
     * @since 3.1
     */

    public static final String FORM_IDS = "formids";

    /**
     * @since 3.1
     */
    private Set _standardReservedIds = new HashSet();

    {
        _standardReservedIds.addAll(Arrays.asList(ServiceConstants.RESERVED_IDS));
        _standardReservedIds.add(FORM_IDS);
    }

    /**
     * Returns the currently active {@link IForm}, or null if no form is active. This is a
     * convienience method, the result will be null, or an instance of {@link IForm}, but not
     * necessarily a <code>Form</code>.
     */

    public static IForm get(IRequestCycle cycle)
    {
        return (IForm) cycle.getAttribute(ATTRIBUTE_NAME);
    }

    /**
     * Indicates to any wrapped form components that they should respond to the form submission.
     * 
     * @throws ApplicationRuntimeException
     *             if not rendering.
     */

    public boolean isRewinding()
    {
        if (!isRendering())
            throw Tapestry.createRenderOnlyPropertyException(this, "rewinding");

        return _rewinding;
    }

    /**
     * Injected.
     * 
     * @since 3.1
     */

    public abstract IEngineService getDirectService();

    /**
     * Injected.
     * 
     * @since 3.1
     */

    public abstract IEngineService getActionService();

    /**
     * Returns true if this Form is configured to use the direct service.
     * <p>
     * This is derived from the direct parameter, and defaults to true if not bound.
     * 
     * @since 1.0.2
     */

    public abstract boolean isDirect();

    /**
     * Returns true if the stateful parameter is bound to a true value. If stateful is not bound,
     * also returns the default, true.
     * 
     * @since 1.0.1
     */

    public boolean getRequiresSession()
    {
        return isStateful();
    }

    /**
     * Constructs a unique identifier (within the Form). The identifier consists of the component's
     * id, with an index number added to ensure uniqueness.
     * <p>
     * Simply invokes
     * {@link #getElementId(org.apache.tapestry.form.IFormComponent, java.lang.String)}with the
     * component's id.
     * 
     * @since 1.0.2
     */

    public String getElementId(IFormComponent component)
    {
        return getElementId(component, component.getId());
    }

    /**
     * Constructs a unique identifier from the base id. If possible, the id is used as-is.
     * Otherwise, a unique identifier is appended to the id.
     * <p>
     * This method is provided simply so that some components ({@link ImageSubmit}) have more
     * specific control over their names.
     * 
     * @since 1.0.3
     */

    public String getElementId(IFormComponent component, String baseId)
    {
        String result = _elementIdAllocator.allocateId(baseId);

        if (_rewinding)
        {
            if (_allocatedIdIndex >= _allocatedIds.size())
            {
                throw new StaleLinkException(FormMessages.formTooManyIds(
                        this,
                        _allocatedIds.size(),
                        component), this);
            }

            String expected = (String) _allocatedIds.get(_allocatedIdIndex);

            if (!result.equals(expected))
                throw new StaleLinkException(FormMessages.formIdMismatch(
                        this,
                        _allocatedIdIndex,
                        expected,
                        result,
                        component), this);
        }
        else
        {
            _allocatedIds.add(result);
        }

        _allocatedIdIndex++;

        component.setName(result);

        return result;
    }

    /**
     * Returns the name generated for the form. This is used to faciliate components that write
     * JavaScript and need to access the form or its contents.
     * <p>
     * This value is generated when the form renders, and is not cleared. If the Form is inside a
     * {@link org.apache.tapestry.components.Foreach}, this will be the most recently generated
     * name for the Form.
     * <p>
     * This property is exposed so that sophisticated applications can write JavaScript handlers for
     * the form and components within the form.
     * 
     * @see AbstractFormComponent#getName()
     */

    public String getName()
    {
        return _name;
    }

    /** @since 3.0 * */

    protected void prepareForRender(IRequestCycle cycle)
    {
        super.prepareForRender(cycle);

        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new ApplicationRuntimeException(FormMessages.formsMayNotNest(), this, null, null);

        cycle.setAttribute(ATTRIBUTE_NAME, this);
    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        _allocatedIdIndex = 0;
        _allocatedIds.clear();

        _events = null;

        _elementIdAllocator.clear();

        if (_hiddenValues != null)
            _hiddenValues.clear();

        cycle.removeAttribute(ATTRIBUTE_NAME);

        _encodingType = null;

        IValidationDelegate delegate = getDelegate();

        if (delegate != null)
            delegate.setFormComponent(null);

        super.cleanupAfterRender(cycle);
    }

    protected void writeAttributes(IMarkupWriter writer, ILink link)
    {
        writer.begin(getTag());
        writer.attribute("method", getMethod());
        writer.attribute("name", _name);
        writer.attribute("action", link.getURL(null, false));

        if (_encodingType != null)
            writer.attribute("enctype", _encodingType);
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String actionId = cycle.getNextActionId();

        boolean renderForm = !cycle.isRewinding();
        boolean rewound = cycle.isRewound(this);

        _rewinding = rewound;

        _allocatedIdIndex = 0;

        if (rewound)
            reconstructAllocatedIds(cycle);

        // When rendering, use a nested writer so that an embedded Upload
        // component can force the encoding type.

        IMarkupWriter nested = writer.getNestedWriter();

        // Note: not safe to invoke getNamespace() in Portlet world
        // except during a RenderRequest.

        if (renderForm)
            _name = getDisplayName() + actionId + getResponse().getNamespace();

        renderBody(nested, cycle);

        if (renderForm)
        {

            ILink link = getLink(cycle, actionId);

            writeAttributes(writer, link);

            renderInformalParameters(writer, cycle);
            writer.println();

            // Write the hidden's, or at least, reserve the query parameters
            // required by the Gesture.

            String extraIds = writeLinkParameters(writer, link, !renderForm);

            // What's this for? It's part of checking for stale links.
            // We record the list of allocated ids.
            // On rewind, we check that the stored list against which
            // ids were allocated. If the persistent state of the page or
            // application changed between render (previous request cycle)
            // and rewind (current request cycle), then the list
            // of ids will change as well.

            writeHiddenField(writer, FORM_IDS, buildAllocatedIdList());

            if (HiveMind.isNonBlank(extraIds))
                writeHiddenField(writer, FORM_IDS, extraIds);

            writeHiddenValues(writer);

            nested.close();

            writer.end(getTag());

            // Write out event handlers collected during the rendering.

            emitEventHandlers(writer, cycle);
        }

        if (rewound)
        {
            int expected = _allocatedIds.size();

            // The other case, _allocatedIdIndex > expected, is
            // checked for inside getElementId(). Remember that
            // _allocatedIdIndex is incremented after allocating.

            if (_allocatedIdIndex < expected)
            {
                String nextExpectedId = (String) _allocatedIds.get(_allocatedIdIndex);

                throw new StaleLinkException(FormMessages.formTooFewIds(this, expected
                        - _allocatedIdIndex, nextExpectedId), this);
            }

            IActionListener listener = getListener();

            if (listener != null)
                listener.actionTriggered(this, cycle);

            // Abort the rewind render.

            throw new RenderRewoundException(this);
        }
    }

    /**
     * Adds an additional event handler.
     * 
     * @since 1.0.2
     */

    public void addEventHandler(FormEventType type, String functionName)
    {
        if (_events == null)
            _events = new HashMap(EVENT_MAP_SIZE);

        Object value = _events.get(type);

        // The value can either be a String, or a List of String. Since
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

        // The third and subsequent function just
        // adds to the List.

        List list = (List) value;
        list.add(functionName);
    }

    protected void emitEventHandlers(IMarkupWriter writer, IRequestCycle cycle)
    {

        if (_events == null || _events.isEmpty())
            return;

        PageRenderSupport pageRenderSupport = TapestryUtils.getPageRenderSupport(cycle, this);

        StringBuffer buffer = new StringBuffer();

        Iterator i = _events.entrySet().iterator();
        while (i.hasNext())
        {

            Map.Entry entry = (Map.Entry) i.next();
            FormEventType type = (FormEventType) entry.getKey();
            Object value = entry.getValue();

            buffer.append("document.");
            buffer.append(_name);
            buffer.append(".");
            buffer.append(type.getPropertyName());
            buffer.append(" = ");

            // The typical case; one event one event handler. Easy enough.

            if (value instanceof String)
            {
                buffer.append(value.toString());
                buffer.append(";");
            }
            else
            {
                // Build a composite function in-place

                buffer.append("function ()\n{\n");

                boolean combineWithAnd = type.getCombineUsingAnd();

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

                buffer.append(";\n}");
            }

            buffer.append("\n\n");
        }

        pageRenderSupport.addInitializationScript(buffer.toString());
    }

    /**
     * Simply invokes {@link #render(IMarkupWriter, IRequestCycle)}.
     * 
     * @since 1.0.2
     */

    public void rewind(IMarkupWriter writer, IRequestCycle cycle)
    {
        render(writer, cycle);
    }

    /**
     * Method invoked by the direct service.
     * 
     * @since 1.0.2
     */

    public void trigger(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();

        cycle.rewindForm(this, (String) parameters[0]);
    }

    /**
     * Builds the EngineServiceLink for the form, using either the direct or action service.
     * 
     * @since 1.0.3
     */

    private ILink getLink(IRequestCycle cycle, String actionId)
    {
        if (isDirect())
        {
            Object parameter = new DirectServiceParameter(this, new Object[]
            { actionId });
            return getDirectService().getLink(cycle, parameter);
        }

        // I'd love to pull out support for the action service entirely!

        Object parameter = new ActionServiceParameter(this, actionId);

        return getActionService().getLink(cycle, parameter);
    }

    /**
     * Writes parameters provided by the {@link ILink}. These parameters define the information
     * needed to dispatch the request, plus state information. The names of these parameters must be
     * reserved so that conflicts don't occur that could disrupt the request processing. For
     * example, if the id 'page' is not reserved, then a conflict could occur with a component whose
     * id is 'page'. A certain number of ids are always reserved, and we find any additional ids
     * beyond that set.
     * 
     * @return a list of additional reserved ids (not contained within
     *         {@link ServiceConstants#RESERVED_IDS}.
     */

    private String writeLinkParameters(IMarkupWriter writer, ILink link, boolean reserveOnly)
    {
        String[] names = link.getParameterNames();
        int count = Tapestry.size(names);

        StringBuffer extraIds = new StringBuffer();
        String sep = "";

        // All the reserved ids, which are essential for
        // dispatching the request, are automatically reserved.
        // Thus, if you have a component with an id of 'service', its element id
        // will likely be 'service$0'.

        preallocateReservedIds();

        for (int i = 0; i < count; i++)
        {
            String name = names[i];

            // Reserve the name.

            if (!_standardReservedIds.contains(name))
            {
                _elementIdAllocator.allocateId(name);

                extraIds.append(sep);
                extraIds.append(name);

                sep = ",";
            }

            if (!reserveOnly)
                writeHiddenFieldsForParameter(writer, link, name);
        }

        return extraIds.toString();
    }

    private void preallocateReservedIds()
    {
        for (int i = 0; i < ServiceConstants.RESERVED_IDS.length; i++)
            _elementIdAllocator.allocateId(ServiceConstants.RESERVED_IDS[i]);
    }

    /**
     * @since 3.0
     */

    protected void writeHiddenField(IMarkupWriter writer, String name, String value)
    {
        writeHiddenField(writer, name, null, value);
    }

    protected void writeHiddenField(IMarkupWriter writer, String name, String id, String value)
    {
        writer.beginEmpty("input");
        writer.attribute("type", "hidden");
        writer.attribute("name", name);

        if (HiveMind.isNonBlank(id))
            writer.attribute("id", id);

        writer.attribute("value", value);
        writer.println();
    }

    /**
     * @since 2.2
     */

    private void writeHiddenFieldsForParameter(IMarkupWriter writer, ILink link,
            String parameterName)
    {
        String[] values = link.getParameterValues(parameterName);

        // In some cases, there are no values, but a space is "reserved" for the provided name.

        if (values == null)
            return;

        for (int i = 0; i < values.length; i++)
        {
            writeHiddenField(writer, parameterName, values[i]);
        }
    }

    /**
     * Converts the allocateIds property into a string, a comma-separated list of ids. This is
     * included as a hidden field in the form and is used to identify discrepencies when the form is
     * submitted.
     * 
     * @since 3.0
     */

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
     * Invoked when rewinding a form to re-initialize the _allocatedIds and _elementIdAllocator.
     * Converts a string passed as a parameter (and containing a comma separated list of ids) back
     * into the allocateIds property. In addition, return the state of the ID allocater back to
     * where it was at the start of the render.
     * 
     * @see #buildAllocatedIdList()
     * @since 3.0
     */

    protected void reconstructAllocatedIds(IRequestCycle cycle)
    {
        String[] values = cycle.getParameters(FORM_IDS);

        StringSplitter splitter = new StringSplitter(',');

        String renderList = values[0];
        if (HiveMind.isNonBlank(renderList))
        {

            String[] ids = splitter.splitToArray(values[0]);

            for (int i = 0; i < ids.length; i++)
                _allocatedIds.add(ids[i]);
        }

        // Now, reconstruct the the initial state of the
        // id allocator.

        preallocateReservedIds();

        if (values.length > 1)
        {
            String extraReservedIds = values[1];
            String[] ids = splitter.splitToArray(extraReservedIds);

            for (int i = 0; i < ids.length; i++)
                _elementIdAllocator.allocateId(ids[i]);
        }
    }

    public abstract WebResponse getResponse();

    public abstract IValidationDelegate getDelegate();

    public abstract void setDelegate(IValidationDelegate delegate);

    public abstract IActionListener getListener();

    public abstract String getMethod();

    public abstract boolean isStateful();

    public void setEncodingType(String encodingType)
    {
        if (_encodingType != null && !_encodingType.equals(encodingType))
            throw new ApplicationRuntimeException(FormMessages.encodingTypeContention(
                    this,
                    _encodingType,
                    encodingType), this, null, null);

        _encodingType = encodingType;
    }

    /**
     * Returns the tag of the form. The WML equivalent, {@link org.apache.tapestry.wml.Go},
     * overrides this.
     * 
     * @since 3.0
     */

    protected String getTag()
    {
        return "form";
    }

    /**
     * Returns the name of the element. The WML equivalent, {@link org.apache.tapestry.wml.Go},
     * overrides this.
     * 
     * @since 3.0
     */

    protected String getDisplayName()
    {
        return "Form";
    }

    /** @since 3.0 */

    public void addHiddenValue(String name, String value)
    {
        if (_hiddenValues == null)
            _hiddenValues = new ArrayList();

        _hiddenValues.add(new HiddenValue(name, value));
    }

    /** @since 3.0 */

    public void addHiddenValue(String name, String id, String value)
    {
        if (_hiddenValues == null)
            _hiddenValues = new ArrayList();

        _hiddenValues.add(new HiddenValue(name, id, value));
    }

    /**
     * Writes hidden values accumulated during the render (by components invoking
     * {@link #addHiddenValue(String, String)}.
     * 
     * @since 3.0
     */

    protected void writeHiddenValues(IMarkupWriter writer)
    {
        int count = Tapestry.size(_hiddenValues);

        for (int i = 0; i < count; i++)
        {
            HiddenValue hv = (HiddenValue) _hiddenValues.get(i);

            writeHiddenField(writer, hv._name, hv._id, hv._value);
        }
    }
}