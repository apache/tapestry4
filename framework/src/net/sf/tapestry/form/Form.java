//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.BindingException;
import net.sf.tapestry.Gesture;
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
import net.sf.tapestry.RequiredParameterException;
import net.sf.tapestry.StaleLinkException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.html.Body;
import net.sf.tapestry.valid.IValidationDelegate;

/**
 *  Component which contains form element components.  Forms use the
 *  action service to handle the form submission.  A Form will wrap
 *  other components and static HTML, including
 *  form components such as {@link Text}, {@link TextField}, {@link Checkbox}, etc.
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
 * <table border=1>
 * <tr> 
 *    <td>Parameter</td>
 *    <td>Type</td>
 *	  <td>Direction</td>
 *    <td>Required</td> 
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 *  <tr>
 *    <td>method</td>
 *    <td>java.lang.String</td>
 *    <td>in</td>
 *   	<td>no</td>
 *		<td>post</td>
 *		<td>The value to use for the method attribute of the &lt;form&gt; tag.</td>
 *	</tr>
 *
 *
 *  <tr>
 *    <td>listener</td>
 *    <td>{@link IActionListener}</td>
 * 	  <td>in</td>
 * 	  <td>yes</td>
 *	  <td>&nbsp;</td>
 *	  <td>The listener, informed <em>after</em> the wrapped components of the form
 *	      have had a chance to absorb the request.</td>
 *	</tr>
 *
 * <tr>
 *	<td>stateful</td>
 *  <td>boolean</td>
 *	<td>in</td>
 *	<td>no</td>
 *	<td>true</td>
 *	<td>If true (the default), then the component requires an active (i.e., non-new)
 *  {@link javax.servlet.http.HttpSession} when triggered.  Failing that, it throws a {@link StaleLinkException}.
 *  If false, then no check is necessary.  Generally, forms are stateful, but it is possible
 *  to put a stateless form onto the Home page of an application.</td>
 * </tr>
 *
 * <tr>
 *		<td>direct</td>
 *		<td>boolean</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>true</td>
 *		<td>If true (the default), then the direct service is used for the form.  This
 *  decreases the amount of work required to process the form submission, and is acceptible
 *  for most forms, even those that contain 
 *  {@link net.sf.tapestry.components.Foreach}es (but not those that are
 *  inside a {@link net.sf.tapestry.components.Foreach}.
 *
 *  <p>An abbreviated form of the rewind cycle takes place, that only references the form
 *  and the components it wraps.
 *  </td>
 * </tr>
 *
 *  <tr>
 *      <td>delegate</td>
 *      <td>{@link IValidationDelegate}</td>
 *      <td>in</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>Object used to assist in error tracking and reporting.  A single
 *  instance is shared by all {@link net.sf.tapestry.valid.ValidField} and
 *  {@link net.sf.tapestry.valid.FieldLabel} in a single form.</td>
 *  </tr>
 *
 *	</table>
 *
 * <p>Informal parameters are allowed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class Form extends AbstractComponent implements IForm, IDirect, PageDetachListener
{
    private String _method;
    private IActionListener _listener;
    private boolean _rewinding;
    private boolean _rendering;
    private String _name;
    private boolean _statefulMode = true;
    private boolean _direct = true;
    private IValidationDelegate _delegate;

    // Need the stateful binding, since isStateful() can be invoked
    // when not rendering.

    private IBinding _statefulBinding;

    /**
     *  Number of element ids allocated.
     *
     *  @since 1.0.2
     *
     **/

    private int _elementCount;

    /**
     *  {@link Map}, keyed on {@link FormEventType}.  Values are either a String (the name
     *  of a single event), or a {@link List} of Strings.
     *
     *  @since 1.0.2
     **/

    private Map _events;

    private static final int EVENT_MAP_SIZE = 3;

    /**
     * A Map, keyed on component id, used to allocate new component ids.
     *
     * @since 1.0.2
     *
     **/

    private Map _allocatorMap;

    /**
     *  Class used to allocate ids (used as form element names).
     *
     **/

    private static class IdAllocator
    {
        String baseId;
        int index;

        IdAllocator(String baseId)
        {
            this.baseId = baseId;
        }

        public String nextId()
        {
            return baseId + index++;
        }
    }

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

    public boolean isDirect()
    {
        return _direct;
    }

    /**
     *  Returns true if the stateful parameter is bound to
     *  a true value.  If stateful is not bound, also returns
     *  the default, true.
     *
     *  @since 1.0.1
     **/

    public boolean getRequiresSession()
    {
        // Can't rely on stateful property, since that is only valid
        // during render ... so we go direct to the binding.

        if (_statefulBinding == null)
            return true;

        return _statefulBinding.getBoolean();
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

    public String getElementId(IComponent component)
    {
        return getElementId(component.getId());
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

    public String getElementId(String baseId)
    {
        if (_allocatorMap == null)
            _allocatorMap = new HashMap();

        String result = null;

        IdAllocator allocator = (IdAllocator) _allocatorMap.get(baseId);

        if (allocator == null)
        {
            result = baseId;
            allocator = new IdAllocator(baseId);
        }
        else
            result = allocator.nextId();

        // Record the allocated id.  This protects against degenerate
        // names by the developer, such as 'foo' (in a Foreach) and
        // 'foo0' elsewhere.

        _allocatorMap.put(result, allocator);

        _elementCount++;

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

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        if (cycle.getAttribute(ATTRIBUTE_NAME) != null)
            throw new RequestCycleException(Tapestry.getString("Form.forms-may-not-nest"), this);

        cycle.setAttribute(ATTRIBUTE_NAME, this);

        String actionId = cycle.getNextActionId();
        _name = "Form" + actionId;

        boolean renderForm = !cycle.isRewinding();
        boolean rewound = cycle.isRewound(this);

        _rewinding = rewound;

        Gesture g = getGesture(cycle, actionId);

        if (renderForm)
        {
            writer.begin("form");
            writer.attribute("method", (_method == null) ? "post" : _method);
            writer.attribute("name", _name);
            writer.attribute("action", g.getBareURL());

            generateAttributes(writer, cycle);
            writer.println();
        }

        // Write the hidden's, or at least, reserve the query parameters
        // required by the Gesture.

        writeGestureParameters(writer, g, !renderForm);

        _elementCount = 0;

        _rendering = true;
        renderWrapped(writer, cycle);

        if (renderForm)
        {
            // What's this for?  It's part of checking for stale links.  
            // We record how many elements we allocated ids for.
            // On rewind, we check that the same number of elements
            // ids were allocated.  If the persistent state of the page or
            // application changed between render (previous request cycle)
            // and rewind (current request cycle), then
            // this count might change.
            //
            // In some cases, state can change without changing this
            // number -- hopefully, such changes are benign since we
            // don't have a way to detect them.

            writer.beginEmpty("input");
            writer.attribute("type", "hidden");
            writer.attribute("name", _name);
            writer.attribute("value", _elementCount);
            writer.println();

            writer.end("form");

            // Write out event handlers collected during the rendering.

            emitEventHandlers(writer, cycle);
        }

        if (rewound)
        {
            String actual = cycle.getRequestContext().getParameter(_name);

            if (actual == null || Integer.parseInt(actual) != _elementCount)
                throw new StaleLinkException(Tapestry.getString("Form.bad-element-count", getExtendedId()), getPage());

            _listener.actionTriggered(this, cycle);

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

    private void emitEventHandlers(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        StringBuffer buffer = null;

        if (_events == null || _events.isEmpty())
            return;

        Body body = Body.get(cycle);

        if (body == null)
            throw new RequestCycleException(Tapestry.getString("Form.needs-body-for-event-handlers"), this);

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

                buffer.append("function ");
                buffer.append(compositeName);
                buffer.append("()\n{\n");

                List l = (List) value;
                int count = l.size();
                for (int j = 0; j < count; j++)
                {
                    String functionName = (String) l.get(j);

                    buffer.append("  ");

                    if (j > 0 && combineWithAnd)
                        buffer.append("&& ");

                    buffer.append(functionName);
                    buffer.append("()");

                    // If combining normally, or on the very last
                    // name, add a semicolon to end the statement.

                    if (j + 1 == count || !combineWithAnd)
                        buffer.append(';');

                    buffer.append('\n');
                }

                buffer.append("}\n\n");

                finalFunctionName = compositeName;
            }

            body.addOtherInitialization(formPath + "." + propertyName + " = " + finalFunctionName + ";");

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

        cycle.rewindForm(this, (String)parameters[0]);
    }

    /**
     *  Builds the Gesture for the form, using either the direct or
     *  action service. 
     *
     *  @since 1.0.3
     *
     **/

    private Gesture getGesture(IRequestCycle cycle, String actionId)
    {
        String serviceName = null;

        if (isDirect())
            serviceName = IEngineService.DIRECT_SERVICE;
        else
            serviceName = IEngineService.ACTION_SERVICE;

        IEngine engine = cycle.getEngine();
        IEngineService service = engine.getService(serviceName);

        // A single service parameter is used to store the actionId.
        
        return service.buildGesture(cycle, this, new String[] { actionId });
    }

    private void writeGestureParameters(IMarkupWriter writer, Gesture g, boolean reserveOnly)
    {
        String[] parameters = g.getServiceParameters();
        int count = Tapestry.size(parameters);

        if (count == 0)
            return;

        if (!reserveOnly)
        {
            for (int i = 0; i < count; i++)
            {

                writer.beginEmpty("input");
                writer.attribute("type", "hidden");
                writer.attribute("name", IEngineService.PARAMETERS_QUERY_PARAMETER_NAME);
                writer.attribute("value", parameters[i]);
                writer.println();
            }
        }

        // Reserve the name, in case any form component has the
        // same name.

        getElementId(IEngineService.PARAMETERS_QUERY_PARAMETER_NAME);

    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        _rendering = false;
        _elementCount = 0;
        _events = null;
        _allocatorMap = null;

        super.cleanupAfterRender(cycle);

    }

    /**
     *  Clears the delegate property at the end of the request cycle.
     * 
     **/

    public void pageDetached(PageEvent event)
    {
        _delegate = null;
    }

    /**
     *  Adds this Form as a page detach listener, so that the delegate property
     *  can be cleared at the end of the request cycle.
     * 
     **/

    protected void finishLoad()
    {
        getPage().addPageDetachListener(this);
    }

    public IValidationDelegate getDelegate()
    {
        return _delegate;
    }

    public void setDelegate(IValidationDelegate delegate)
    {
        _delegate = delegate;
    }

    public void setDirect(boolean direct)
    {
        _direct = direct;
    }

    public IActionListener getListener()
    {
        return _listener;
    }

    public void setListener(IActionListener listener)
    {
        _listener = listener;
    }

    public String getMethod()
    {
        return _method;
    }

    public void setMethod(String method)
    {
        _method = method;
    }

    /**
     *  Invoked when not rendering, so it uses the stateful binding.
     *  If not bound, returns true.
     * 
     **/

    public boolean isStateful()
    {
        if (_statefulBinding == null)
            return true;

        return _statefulBinding.getBoolean();
    }

    public IBinding getStatefulBinding()
    {
        return _statefulBinding;
    }

    public void setStatefulBinding(IBinding statefulBinding)
    {
        _statefulBinding = statefulBinding;
    }

    public boolean getStatefulMode()
    {
        return _statefulMode;
    }

    public void setStatefulMode(boolean statefulMode)
    {
        _statefulMode = statefulMode;
    }

}