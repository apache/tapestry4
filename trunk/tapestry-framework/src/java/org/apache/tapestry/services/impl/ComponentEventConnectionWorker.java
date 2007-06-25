// Copyright May 20, 2006 The Apache Software Foundation
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
package org.apache.tapestry.services.impl;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.PoolManageable;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.*;
import org.apache.tapestry.dojo.IWidget;
import org.apache.tapestry.engine.DirectEventServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.html.Body;
import org.apache.tapestry.internal.Component;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.internal.event.EventBoundListener;
import org.apache.tapestry.internal.event.IComponentEventInvoker;
import org.apache.tapestry.services.ComponentRenderWorker;
import org.apache.tapestry.util.ScriptUtils;

import java.util.*;


/**
 * Implementation that handles connecting events to listener
 * method invocations.
 *
 * @author jkuhnert
 */
public class ComponentEventConnectionWorker implements ComponentRenderWorker, PoolManageable
{
    /** Stored in {@link IRequestCycle} with associated forms. */
    public static final String FORM_NAME_LIST =  "org.apache.tapestry.services.impl.ComponentEventConnectionFormNames-";

    // holds mapped event listener info
    private IComponentEventInvoker _invoker;

    // generates links for scripts
    private IEngineService _eventEngine;

    // handles resolving and loading different component event 
    // connection script types
    private IScriptSource _scriptSource;

    // script path references
    private String _componentScript;
    private String _widgetScript;
    private String _elementScript;

    // resolves classpath relative resources
    private ClassResolver _resolver;

    // wrappers around resolved script templates
    private ClasspathResource _componentResource;
    private ClasspathResource _widgetResource;
    private ClasspathResource _elementResource;

    /**
     * For event connections referencing forms that have not been rendered yet.
     */
    private Map _deferredFormConnections = new HashMap(24);

    /**
     * Used to store deferred form connection information, but most importantly is used
     * to provide unique equals/hashcode semantics.
     */
    class DeferredFormConnection {

        String _formId;
        Map _scriptParms;
        Boolean _async;
        Boolean _validate;
        String _uniqueHash;
        
        public DeferredFormConnection(String formId, Map scriptParms, Boolean async,
                                      Boolean validate, String uniqueHash)
        {
            _formId = formId;
            _scriptParms = scriptParms;
            _async = async;
            _validate = validate;
            _uniqueHash = uniqueHash;
        }

        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DeferredFormConnection that = (DeferredFormConnection) o;

            if (_uniqueHash != null ? !_uniqueHash.equals(that._uniqueHash) : that._uniqueHash != null) return false;

            return true;
        }

        public int hashCode()
        {
            return (_uniqueHash != null ? _uniqueHash.hashCode() : 0);
        }
    }

    public void activateService()
    {
        _deferredFormConnections.clear();
    }

    public void passivateService()
    {
    }

    /**
     * {@inheritDoc}
     */
    public void renderComponent(IRequestCycle cycle, IComponent component)
    {
        if (cycle.isRewinding())
            return;

        if (Component.class.isInstance(component) && !((Component)component).hasEvents() && !IForm.class.isInstance(component))
            return;

        if (TapestryUtils.getOptionalPageRenderSupport(cycle) == null)
            return;

        // Don't render fields being pre-rendered, otherwise we'll render twice
        IComponent field = (IComponent)cycle.getAttribute(TapestryUtils.FIELD_PRERENDER);
        if (field != null && field == component)
            return;

        linkComponentEvents(cycle, component);

        linkElementEvents(cycle, component);

        if (IForm.class.isInstance(component))
            mapFormNames(cycle, (IForm)component);

        if (isDeferredForm(component))
            linkDeferredForm(cycle, (IForm)component);
    }

    void linkComponentEvents(IRequestCycle cycle, IComponent component)
    {
        ComponentEventProperty[] props = _invoker.getEventPropertyListeners(component.getExtendedId());
        if (props == null)
            return;

        for (int i=0; i < props.length; i++) {

            String clientId = component.getClientId();

            Map parms = new HashMap();
            parms.put("clientId", clientId);
            parms.put("component", component);

            Object[][] events = getEvents(props[i], clientId);
            Object[][] formEvents = filterFormEvents(props[i], parms, cycle);

            if (events.length < 1 && formEvents.length < 1)
                continue;

            DirectEventServiceParameter dsp =
              new DirectEventServiceParameter((IDirectEvent)component, new Object[] {}, new String[] {}, false);

            parms.put("url", _eventEngine.getLink(false, dsp).getURL());
            parms.put("events", events);
            parms.put("formEvents", formEvents);

            PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, component);
            Resource resource = getScript(component);

            _scriptSource.getScript(resource).execute(component, cycle, prs, parms);
        }
    }

    void linkElementEvents(IRequestCycle cycle, IComponent component)
    {
        if (!component.getSpecification().hasElementEvents())
            return;

        DirectEventServiceParameter dsp =
          new DirectEventServiceParameter((IDirectEvent)component, new Object[] {}, new String[] {}, false);

        String url = _eventEngine.getLink(false, dsp).getURL();

        PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, component);
        Resource resource = getElementScript();

        Map elements = component.getSpecification().getElementEvents();
        Iterator keys = elements.keySet().iterator();

        // build our list of targets / events
        while (keys.hasNext()) {

            Map parms = new HashMap();

            String target = (String)keys.next();

            ComponentEventProperty prop = (ComponentEventProperty)elements.get(target);

            parms.put("component", component);
            parms.put("target", target);
            parms.put("url", url);
            parms.put("events", getEvents(prop, target));
            parms.put("formEvents", filterFormEvents(prop, parms, cycle));

            _scriptSource.getScript(resource).execute(component, cycle, prs, parms);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void renderBody(IRequestCycle cycle, Body component)
    {
        if (cycle.isRewinding())
            return;

        renderComponent(cycle, component);

        // just in case
        _deferredFormConnections.clear();
    }

    void mapFormNames(IRequestCycle cycle, IForm form)
    {
        List names = (List)cycle.getAttribute(FORM_NAME_LIST + form.getExtendedId());

        if (names == null) {
            names = new ArrayList();

            cycle.setAttribute(FORM_NAME_LIST + form.getExtendedId(), names);
        }

        names.add(form.getName());
    }

    void linkDeferredForm(IRequestCycle cycle, IForm form)
    {
        List deferred = (List)_deferredFormConnections.remove(form.getExtendedId());

        for (int i=0; i < deferred.size(); i++)
        {
            DeferredFormConnection fConn = (DeferredFormConnection)deferred.get(i);
            Map scriptParms = fConn._scriptParms;

            // don't want any events accidently connected again
            scriptParms.remove("events");

            IComponent component = (IComponent)scriptParms.get("component");

            // fire off element based events first

            linkElementEvents(cycle, component);

            ComponentEventProperty[] props = _invoker.getEventPropertyListeners(component.getExtendedId());
            if (props == null)
                continue;

            for (int e=0; e < props.length; e++) {

                Object[][] formEvents = buildFormEvents(cycle, form.getExtendedId(),
                                                        props[e].getFormEvents(), fConn._async,
                                                        fConn._validate, fConn._uniqueHash);

                scriptParms.put("formEvents", formEvents);

                // execute script

                PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, component);
                Resource resource = getScript(component);

                _scriptSource.getScript(resource).execute(form, cycle, prs, scriptParms);
            }
        }
    }

    /**
     * Generates a two dimensional array containing the event name in the first
     * index and a unique hashcode for the event binding in the second.
     *
     * @param prop The component event properties object the events are managed in.
     * @return A two dimensional array containing all events, or empty array if none exist.
     */
    Object[][] getEvents(ComponentEventProperty prop, String clientId)
    {
        Set events = prop.getEvents();
        List ret = new ArrayList();

        Iterator it = events.iterator();
        while (it.hasNext())
        {
            String event = (String)it.next();

            int hash = 0;
            List listeners = prop.getEventListeners(event);

            for (int i=0; i < listeners.size(); i++)
                hash += listeners.get(i).hashCode();

            ret.add(new Object[]{ event, ScriptUtils.functionHash(event + hash + clientId) });
        }

        return (Object[][])ret.toArray(new Object[ret.size()][2]);
    }

    Object[][] buildFormEvents(IRequestCycle cycle, String formId, Set events,
                               Boolean async, Boolean validate, Object uniqueHash)
    {
        List formNames = (List)cycle.getAttribute(FORM_NAME_LIST + formId);
        List retval = new ArrayList();

        Iterator it = events.iterator();

        while (it.hasNext())
        {
            String event = (String)it.next();

            retval.add(new Object[]{event, formNames, async, validate,
                                    ScriptUtils.functionHash(new String(uniqueHash + event)) });
        }

        return (Object[][])retval.toArray(new Object[retval.size()][5]);
    }

    Resource getScript(IComponent component)
    {
        if (IWidget.class.isInstance(component)) {

            if (_widgetResource == null)
                _widgetResource = new ClasspathResource(_resolver, _widgetScript);

            return _widgetResource;
        }

        if (_componentResource == null)
            _componentResource = new ClasspathResource(_resolver, _componentScript);

        return _componentResource;
    }

    Resource getElementScript()
    {
        if (_elementResource == null)
            _elementResource = new ClasspathResource(_resolver, _elementScript);

        return _elementResource;
    }

    boolean isDeferredForm(IComponent component)
    {
        if (IForm.class.isInstance(component)
            && _deferredFormConnections.get(((IForm)component).getExtendedId()) != null)
            return true;

        return false;
    }

    /**
     * For each form event attempts to find a rendered form name list that corresponds
     * to the actual client ids that the form can be connected to. If the form hasn't been
     * rendered yet the events will be filtered out and deferred for execution <i>after</i>
     * the form has rendererd.
     *
     * @param prop
     *          The configured event properties.
     * @param scriptParms
     *          The parameters to eventually be passed in to the javascript tempate.
     * @param cycle
     *          The current cycle.
     *
     * @return A set of events that can be connected now because the form has already rendered.
     */
    Object[][] filterFormEvents(ComponentEventProperty prop, Map scriptParms, IRequestCycle cycle)
    {
        Set events = prop.getFormEvents();

        if (events.size() < 1)
            return new Object[0][0];

        List retval = new ArrayList();

        Iterator it = events.iterator();
        while (it.hasNext())
        {
            String event = (String)it.next();
            Iterator lit = prop.getFormEventListeners(event).iterator();

            while (lit.hasNext())
            {
                EventBoundListener listener = (EventBoundListener)lit.next();

                String formId = listener.getFormId();
                List formNames = (List)cycle.getAttribute(FORM_NAME_LIST + formId);

                // defer connection until form is rendered
                if (formNames == null)
                {
                    deferFormConnection(formId, scriptParms,
                                        listener.isAsync(),
                                        listener.isValidateForm(),
                                        ScriptUtils.functionHash(listener));

                    // re-looping over the same property -> event listener list would
                    // result in duplicate bindings so break out 
                    break;
                }

                // form has been rendered so go ahead
                retval.add(new Object[] {
                  event, formNames,
                  Boolean.valueOf(listener.isAsync()),
                  Boolean.valueOf(listener.isValidateForm()),
                  ScriptUtils.functionHash(listener)
                });
            }
        }

        return (Object[][])retval.toArray(new Object[retval.size()][5]);
    }

    /**
     * Temporarily stores the data needed to perform script evaluations that
     * connect a component event to submitting a particular form that hasn't
     * been rendered yet. We can't reliably connect to a form until its name has
     * been set by a render, which could happen multiple times if it's in a list.
     *
     * <p>
     * The idea here is that when the form actually ~is~ rendered we will look for 
     * any pending deferred operations and run them while also clearing out our
     * deferred list.
     * </p>
     *
     * @param formId The form to defer event connection for.
     * @param scriptParms The initial map of parameters for the connection @Script component.
     * @param async Whether or not the action taken should be asynchronous.
     * @param validate Whether or not the form should have client side validation run befor submitting.
     * @param uniqueHash Represents a hashcode() value that will help make client side function name
     *                  unique.
     */
    void deferFormConnection(String formId, Map scriptParms,
                             boolean async, boolean validate, String uniqueHash)
    {
        List deferred = (List)_deferredFormConnections.get(formId);
        if (deferred == null)
        {
            deferred = new ArrayList();
            _deferredFormConnections.put(formId, deferred);
        }
        
        DeferredFormConnection connection = new DeferredFormConnection(formId, scriptParms, Boolean.valueOf(async),
                                                                       Boolean.valueOf(validate), uniqueHash);
        
        if (!deferred.contains(connection))
            deferred.add(connection);
    }

    // for testing
    Map getDefferedFormConnections()
    {
        return _deferredFormConnections;
    }

    /**
     * Sets the invoker to use/manage event connections.
     * @param invoker
     */
    public void setEventInvoker(IComponentEventInvoker invoker)
    {
        _invoker = invoker;
    }

    /**
     * Sets the engine service that will be used to construct callback
     * URL references to invoke the specified components event listener.
     *
     * @param eventEngine
     */
    public void setEventEngine(IEngineService eventEngine)
    {
        _eventEngine = eventEngine;
    }

    /**
     * The javascript that will be used to connect the component
     * to its configured events. (if any)
     * @param script
     */
    public void setComponentScript(String script)
    {
        _componentScript = script;
    }

    /**
     * The javascript that will be used to connect the widget component
     * to its configured events. (if any)
     * @param script
     */
    public void setWidgetScript(String script)
    {
        _widgetScript = script;
    }

    /**
     * The javascript that connects html elements to direct
     * listener methods.
     * @param script
     */
    public void setElementScript(String script)
    {
        _elementScript = script;
    }

    /**
     * The service that parses script files.
     * @param scriptSource
     */
    public void setScriptSource(IScriptSource scriptSource)
    {
        _scriptSource = scriptSource;
    }

    public void setClassResolver(ClassResolver resolver)
    {
        _resolver = resolver;
    }
}
