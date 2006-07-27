// Copyright 2005 The Apache Software Foundation
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.hivemind.Location;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.NestedMarkupWriter;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.StaleLinkException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.util.IdAllocator;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * Encapsulates most of the behavior of a Form component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class FormSupportImpl implements FormSupport
{
    /**
     * Name of query parameter storing the ids alloocated while rendering the form, as a comma
     * seperated list. This information is used when the form is submitted, to ensure that the
     * rewind allocates the exact same sequence of ids.
     */

    public static final String FORM_IDS = "formids";

    /**
     * Names of additional ids that were pre-reserved, as a comma-sepereated list. These are names
     * beyond that standard set. Certain engine services include extra parameter values that must be
     * accounted for, and page properties may be encoded as additional query parameters.
     */

    public static final String RESERVED_FORM_IDS = "reservedids";

    /**
     * Indicates why the form was submitted: whether for normal ("submit"), refresh, or because the
     * form was canceled.
     */

    public static final String SUBMIT_MODE = "submitmode";
    
    /**
     * Attribute set to true when a field has been focused; used to prevent conflicting JavaScript
     * for field focusing from being emitted.
     */

    public static final String FIELD_FOCUS_ATTRIBUTE = "org.apache.tapestry.field-focused";
    
    private static final Set _standardReservedIds;

    static
    {
        Set set = new HashSet();

        set.addAll(Arrays.asList(ServiceConstants.RESERVED_IDS));
        set.add(FORM_IDS);
        set.add(RESERVED_FORM_IDS);
        set.add(SUBMIT_MODE);
        set.add(FormConstants.SUBMIT_NAME_PARAMETER);

        _standardReservedIds = Collections.unmodifiableSet(set);
    }

    private static final Set _submitModes;

    static
    {
        Set set = new HashSet();
        set.add(FormConstants.SUBMIT_CANCEL);
        set.add(FormConstants.SUBMIT_NORMAL);
        set.add(FormConstants.SUBMIT_REFRESH);

        _submitModes = Collections.unmodifiableSet(set);
    }

    /**
     * Used when rewinding the form to figure to match allocated ids (allocated during the rewind)
     * against expected ids (allocated in the previous request cycle, when the form was rendered).
     */

    private int _allocatedIdIndex;

    /**
     * The list of allocated ids for form elements within this form. This list is constructed when a
     * form renders, and is validated against when the form is rewound.
     */

    private final List _allocatedIds = new ArrayList();

    private final IRequestCycle _cycle;

    private final IdAllocator _elementIdAllocator = new IdAllocator();

    private String _encodingType;

    private final List _deferredRunnables = new ArrayList();

    /**
     * Map keyed on extended component id, value is the pre-rendered markup for that component.
     */

    private final Map _prerenderMap = new HashMap();

    /**
     * {@link Map}, keyed on {@link FormEventType}. Values are either a String (the function name
     * of a single event handler), or a List of Strings (a sequence of event handler function
     * names).
     */

    private Map _events;

    private final IForm _form;

    private final List _hiddenValues = new ArrayList();

    private final boolean _rewinding;

    private final IMarkupWriter _writer;

    private final IValidationDelegate _delegate;

    private final PageRenderSupport _pageRenderSupport;

    private final JSONObject _profile;
    
    private boolean _fieldUpdating;
    
    public FormSupportImpl(IMarkupWriter writer, IRequestCycle cycle, IForm form)
    {
        Defense.notNull(writer, "writer");
        Defense.notNull(cycle, "cycle");
        Defense.notNull(form, "form");

        _writer = writer;
        _cycle = cycle;
        _form = form;
        _delegate = form.getDelegate();
        
        _rewinding = cycle.isRewound(form);
        _allocatedIdIndex = 0;
        
        _pageRenderSupport = TapestryUtils.getOptionalPageRenderSupport(cycle);
        _profile = new JSONObject();
    }

    /**
     * Alternate constructor used for testing only.
     * 
     * @param cycle
     */
    FormSupportImpl(IRequestCycle cycle)
    {
        _cycle = cycle;
        _form = null;
        _rewinding = false;
        _writer = null;
        _delegate = null;
        _pageRenderSupport = null;
        _profile = null;
    }

    /**
     * {@inheritDoc}
     */
    public IForm getForm()
    {
        return _form;
    }
    
    /**
     * Adds an event handler for the form, of the given type.
     */

    public void addEventHandler(FormEventType type, String functionName)
    {
        if (_events == null)
            _events = new HashMap();

        List functionList = (List) _events.get(type);

        // The value can either be a String, or a List of String. Since
        // it is rare for there to be more than one event handling function,
        // we start with just a String.

        if (functionList == null)
        {
            functionList = new ArrayList();

            _events.put(type, functionList);
        }

        functionList.add(functionName);
    }

    /**
     * Adds hidden fields for parameters provided by the {@link ILink}. These parameters define the
     * information needed to dispatch the request, plus state information. The names of these
     * parameters must be reserved so that conflicts don't occur that could disrupt the request
     * processing. For example, if the id 'page' is not reserved, then a conflict could occur with a
     * component whose id is 'page'. A certain number of ids are always reserved, and we find any
     * additional ids beyond that set.
     */

    private void addHiddenFieldsForLinkParameters(ILink link)
    {
        String[] names = link.getParameterNames();
        int count = Tapestry.size(names);

        StringBuffer extraIds = new StringBuffer();
        String sep = "";
        boolean hasExtra = false;

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
                hasExtra = true;
            }
            
            addHiddenFieldsForLinkParameter(link, name);
        }
        
        if (hasExtra)
            addHiddenValue(RESERVED_FORM_IDS, extraIds.toString());
    }
    
    public void addHiddenValue(String name, String value)
    {
        _hiddenValues.add(new HiddenFieldData(name, value));
    }

    public void addHiddenValue(String name, String id, String value)
    {
        _hiddenValues.add(new HiddenFieldData(name, id, value));
    }

    /**
     * Converts the allocateIds property into a string, a comma-separated list of ids. This is
     * included as a hidden field in the form and is used to identify discrepencies when the form is
     * submitted.
     */

    private String buildAllocatedIdList()
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

    private void emitEventHandlers(String formId)
    {
        if (_events == null || _events.isEmpty())
            return;

        StringBuffer buffer = new StringBuffer();

        Iterator i = _events.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();
            FormEventType type = (FormEventType) entry.getKey();
            Object value = entry.getValue();

            buffer.append("Tapestry.");
            buffer.append(type.getAddHandlerFunctionName());
            buffer.append("('");
            buffer.append(formId);
            buffer.append("', function (event)\n{");

            List l = (List) value;
            int count = l.size();

            for (int j = 0; j < count; j++)
            {
                String functionName = (String) l.get(j);

                if (j > 0)
                {
                    buffer.append(";");
                }

                buffer.append("\n  ");
                buffer.append(functionName);

                // It's supposed to be function names, but some of Paul's validation code
                // adds inline code to be executed instead.

                if (!functionName.endsWith(")"))
                {
                    buffer.append("()");
                }
            }

            buffer.append(";\n});\n");
        }

        // TODO: If PRS is null ...

        _pageRenderSupport.addInitializationScript(buffer.toString());
    }

    /**
     * Constructs a unique identifier (within the Form). The identifier consists of the component's
     * id, with an index number added to ensure uniqueness.
     * <p>
     * Simply invokes
     * {@link #getElementId(org.apache.tapestry.form.IFormComponent, java.lang.String)}with the
     * component's id.
     */

    public String getElementId(IFormComponent component)
    {
        return getElementId(component, component.getId());
    }

    /**
     * Constructs a unique identifier (within the Form). The identifier consists of the component's
     * id, with an index number added to ensure uniqueness.
     * <p>
     * Simply invokes
     * {@link #getElementId(org.apache.tapestry.form.IFormComponent, java.lang.String)}with the
     * component's id.
     */

    public String getElementId(IFormComponent component, String baseId)
    {
        // $ is not a valid character in an XML/XHTML id, so convert it to an underscore.
        
        String filteredId = TapestryUtils.convertTapestryIdToNMToken(baseId);

        String result = _elementIdAllocator.allocateId(filteredId);

        if (_rewinding)
        {
            if (_allocatedIdIndex >= _allocatedIds.size())
            {
                throw new StaleLinkException(FormMessages.formTooManyIds(_form, _allocatedIds
                        .size(), component), component);
            }

            String expected = (String) _allocatedIds.get(_allocatedIdIndex);
            
            if (!result.equals(expected))
                throw new StaleLinkException(FormMessages.formIdMismatch(
                        _form,
                        _allocatedIdIndex,
                        expected,
                        result,
                        component), component);
        }
        else
        {
            _allocatedIds.add(result);
        }

        _allocatedIdIndex++;

        component.setName(result);

        return result;
    }

    public boolean isRewinding()
    {
        return _rewinding;
    }

    private void preallocateReservedIds()
    {
        for (int i = 0; i < ServiceConstants.RESERVED_IDS.length; i++)
            _elementIdAllocator.allocateId(ServiceConstants.RESERVED_IDS[i]);
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

    private void reinitializeIdAllocatorForRewind()
    {
        String allocatedFormIds = _cycle.getParameter(FORM_IDS);

        String[] ids = TapestryUtils.split(allocatedFormIds);

        for (int i = 0; i < ids.length; i++)
            _allocatedIds.add(ids[i]);

        // Now, reconstruct the the initial state of the
        // id allocator.

        preallocateReservedIds();

        String extraReservedIds = _cycle.getParameter(RESERVED_FORM_IDS);

        ids = TapestryUtils.split(extraReservedIds);

        for (int i = 0; i < ids.length; i++)
            _elementIdAllocator.allocateId(ids[i]);
    }
    
    public void render(String method, IRender informalParametersRenderer, ILink link, 
            String scheme, Integer port)
    {
        String formId = _form.getName();

        emitEventManagerInitialization(formId);

        // Convert the link's query parameters into a series of
        // hidden field values (that will be rendered later).

        addHiddenFieldsForLinkParameters(link);

        // Create a hidden field to store the submission mode, in case
        // client-side JavaScript forces an update.

        addHiddenValue(SUBMIT_MODE, null);

        // And another for the name of the component that
        // triggered the submit.

        addHiddenValue(FormConstants.SUBMIT_NAME_PARAMETER, null);

        IMarkupWriter nested = _writer.getNestedWriter();

        _form.renderBody(nested, _cycle);

        runDeferredRunnables();
        
        int portI = (port == null) ? 0 : port.intValue();
        writeTag(_writer, method, link.getURL(scheme, null, portI, null, false));
        
        // For XHTML compatibility
        _writer.attribute("id", formId);

        if (_encodingType != null)
            _writer.attribute("enctype", _encodingType);

        // Write out event handlers collected during the rendering.

        emitEventHandlers(formId);

        informalParametersRenderer.render(_writer, _cycle);

        // Finish the <form> tag

        _writer.println();

        writeHiddenFields();

        // Close the nested writer, inserting its contents.
        
        nested.close();

        // Close the <form> tag.

        _writer.end();

        String fieldId = _delegate.getFocusField();
        
        if (_pageRenderSupport == null)
            return;
        
        // If the form doesn't support focus, or the focus has already been set by a different form,
        // then do nothing.
        
        if (fieldId != null && _form.getFocus() 
                && _cycle.getAttribute(FIELD_FOCUS_ATTRIBUTE) != null) {
            
            _pageRenderSupport.addInitializationScript(_form, "tapestry.form.focusField('" + fieldId + "');");
            _cycle.setAttribute(FIELD_FOCUS_ATTRIBUTE, Boolean.TRUE);
        }
        
        // register the validation profile with client side form manager
        
        if (_form.isClientValidationEnabled()) {
            _pageRenderSupport.addInitializationScript(_form, "tapestry.form.clearProfiles('"
                    + formId + "'); tapestry.form.registerProfile('" + formId + "'," 
                    + _profile.toString() + ");");
        }
    }

    /**
     * Pre-renders the form, setting up some client-side form support. Returns the name of the
     * client-side form event manager variable.
     */
    protected void emitEventManagerInitialization(String formId)
    {
        if (_pageRenderSupport == null)
            return;
        
        _pageRenderSupport.addInitializationScript(_form, "dojo.require(\"tapestry.form\");"
                + "tapestry.form.registerForm('" + formId + "');");
    }
    
    public String rewind()
    {
        _form.getDelegate().clear();

        String mode = _cycle.getParameter(SUBMIT_MODE);
        
        // On a cancel, don't bother rendering the body or anything else at all.

        if (FormConstants.SUBMIT_CANCEL.equals(mode))
            return mode;

        reinitializeIdAllocatorForRewind();

        _form.renderBody(_writer, _cycle);
        
        // New, handles cases where an eventlistener
        // causes a form submission.
        BrowserEvent event = new BrowserEvent(_cycle);
        _form.getEventInvoker().invokeFormListeners(this, _cycle, event);
        
        int expected = _allocatedIds.size();
        
        // The other case, _allocatedIdIndex > expected, is
        // checked for inside getElementId(). Remember that
        // _allocatedIdIndex is incremented after allocating.
        
        if (_allocatedIdIndex < expected)
        {
            String nextExpectedId = (String) _allocatedIds.get(_allocatedIdIndex);
            
            throw new StaleLinkException(FormMessages.formTooFewIds(_form, expected
                    - _allocatedIdIndex, nextExpectedId), _form);
        }

        
        runDeferredRunnables();

        if (_submitModes.contains(mode))
            return mode;

        // Either something wacky on the client side, or a client without
        // javascript enabled.

        return FormConstants.SUBMIT_NORMAL;

    }

    private void runDeferredRunnables()
    {
        Iterator i = _deferredRunnables.iterator();
        while (i.hasNext())
        {
            Runnable r = (Runnable) i.next();

            r.run();
        }
    }

    public void setEncodingType(String encodingType)
    {

        if (_encodingType != null && !_encodingType.equals(encodingType))
            throw new ApplicationRuntimeException(FormMessages.encodingTypeContention(
                    _form,
                    _encodingType,
                    encodingType), _form, null, null);

        _encodingType = encodingType;
    }

    /**
     * Overwridden by {@link org.apache.tapestry.wml.GoFormSupportImpl} (WML).
     */
    protected void writeHiddenField(IMarkupWriter writer, String name, String id, String value)
    {
        writer.beginEmpty("input");
        writer.attribute("type", "hidden");
        writer.attribute("name", name);
        
        if (HiveMind.isNonBlank(id))
            writer.attribute("id", id);
        
        writer.attribute("value", value == null ? "" : value);
        writer.println();
    }

    /**
     * Writes out all hidden values previously added by
     * {@link #addHiddenValue(String, String, String)}. Writes a &lt;div&gt; tag around
     * {@link #writeHiddenFieldList()}. Overriden by
     * {@link org.apache.tapestry.wml.GoFormSupportImpl}.
     */
    
    protected void writeHiddenFields()
    {
        IMarkupWriter writer = getHiddenFieldWriter();
        
        writer.begin("div");
        writer.attribute("style", "display:none;");
        writer.attribute("id", _form.getName() + "hidden");
        
        writeHiddenFieldList(writer);
        
        writer.end();
    }
    
    /**
     * Writes out all hidden values previously added by
     * {@link #addHiddenValue(String, String, String)}, plus the allocated id list.
     */
    
    protected void writeHiddenFieldList(IMarkupWriter writer)
    {
        writeHiddenField(writer, FORM_IDS, null, buildAllocatedIdList());
        
        Iterator i = _hiddenValues.iterator();
        while (i.hasNext())
        {
            HiddenFieldData data = (HiddenFieldData) i.next();
            
            writeHiddenField(writer, data.getName(), data.getId(), data.getValue());
        }
    }
    
    /**
     * Determines if a hidden field change has occurred, which would require
     * that we write hidden form fields using the {@link ResponseBuilder} 
     * writer.
     * 
     * @return The default {@link IMarkupWriter} if not doing a managed ajax/json
     *          response, else whatever is returned from {@link ResponseBuilder}.
     */
    protected IMarkupWriter getHiddenFieldWriter()
    {
        if (!_fieldUpdating || !_cycle.getResponseBuilder().isDynamic()) {
            return _writer;
        }
        
        return _cycle.getResponseBuilder().getWriter(_form.getName() + "hidden", 
                ResponseBuilder.ELEMENT_TYPE);
    }
    
    private void addHiddenFieldsForLinkParameter(ILink link, String parameterName)
    {
        String[] values = link.getParameterValues(parameterName);

        // In some cases, there are no values, but a space is "reserved" for the provided name.

        if (values == null)
            return;

        for (int i = 0; i < values.length; i++)
        {
            addHiddenValue(parameterName, values[i]);
        }
    }

    protected void writeTag(IMarkupWriter writer, String method, String url)
    {
        writer.begin("form");
        writer.attribute("method", method);
        writer.attribute("action", url);
    }

    public void prerenderField(IMarkupWriter writer, IComponent field, Location location)
    {
        Defense.notNull(writer, "writer");
        Defense.notNull(field, "field");

        String key = field.getExtendedId();

        if (_prerenderMap.containsKey(key))
            throw new ApplicationRuntimeException(FormMessages.fieldAlreadyPrerendered(field),
                    field, location, null);
        
        NestedMarkupWriter nested = writer.getNestedWriter();
        
        TapestryUtils.storePrerender(_cycle, field);
        
        field.render(nested, _cycle);
        
        TapestryUtils.removePrerender(_cycle);
        
        _prerenderMap.put(key, nested.getBuffer());
    }
    
    public boolean wasPrerendered(IMarkupWriter writer, IComponent field)
    {
        String key = field.getExtendedId();

        // During a rewind, if the form is pre-rendered, the buffer will be null,
        // so do the check based on the key, not a non-null value.

        if (!_prerenderMap.containsKey(key))
            return false;

        String buffer = (String) _prerenderMap.get(key);

        writer.printRaw(buffer);

        _prerenderMap.remove(key);

        return true;
    }
    
    public void addDeferredRunnable(Runnable runnable)
    {
        Defense.notNull(runnable, "runnable");

        _deferredRunnables.add(runnable);
    }

    public void registerForFocus(IFormComponent field, int priority)
    {
        _delegate.registerForFocus(field, priority);
    }

    /** 
     * {@inheritDoc}
     */
    public JSONObject getProfile()
    {
        return _profile;
    }

    /** 
     * {@inheritDoc}
     */
    public boolean isFormFieldUpdating()
    {
        return _fieldUpdating;
    }
    
    /** 
     * {@inheritDoc}
     */
    public void setFormFieldUpdating(boolean value)
    {
        _fieldUpdating = value;
    }
}
