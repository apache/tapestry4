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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IDirectEvent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.dojo.IWidget;
import org.apache.tapestry.engine.DirectEventServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.html.Body;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.internal.event.EventBoundListener;
import org.apache.tapestry.services.ComponentRenderWorker;


/**
 * Implementation that handles connecting events to listener
 * method invocations.
 * 
 * @author jkuhnert
 */
public class ComponentEventConnectionWorker implements ComponentRenderWorker
{
    /** Stored in {@link IRequestCycle} with associated forms. */
    public static final String FORM_NAME_LIST = 
        "org.apache.tapestry.services.impl.ComponentEventConnectionFormNames-";
    
    // holds mapped event listener info
    private ComponentEventInvoker _invoker;
    
    // generates links for scripts
    private IEngineService _eventEngine;
    
    // handles resolving and loading different component event 
    // connection script types
    private IScriptSource _scriptSource;
    private String _componentScript;
    private String _widgetScript;
    private String _elementScript;
    private ClassResolver _resolver;
    private ClasspathResource _componentResource;
    private ClasspathResource _widgetResource;
    private ClasspathResource _elementResource;
    
    // For event connections referencing forms that have not 
    // been rendered yet.
    private Map _deferredFormConnections = new HashMap();
    
    /** 
     * {@inheritDoc}
     */
    public void renderComponent(IRequestCycle cycle, IComponent component)
    {
        if (cycle.isRewinding() 
                || TapestryUtils.getOptionalPageRenderSupport(cycle) == null) 
            return;
        
        if (_invoker.hasEvents(component.getId()))
            linkComponent(cycle, component);
        
        if (IForm.class.isInstance(component))
            mapFormNames(cycle, (IForm)component);
        
        if (isDeferredForm(component))
            linkDeferredForm(cycle, (IForm)component);
    }
    
    void linkComponent(IRequestCycle cycle, IComponent component)
    {
        ComponentEventProperty prop = _invoker.getComponentEvents(component.getId());
        String clientId = component.getClientId();
        
        Map parms = new HashMap();
        parms.put("clientId", clientId);
        parms.put("component", component);
        
        Set events = prop.getEvents();
        Object[][] formEvents = filterFormEvents(prop, parms, cycle);
        
        if (events.size() < 1 && formEvents.length < 1)
            return;
        
        DirectEventServiceParameter dsp =
            new DirectEventServiceParameter((IDirectEvent)component, new Object[] {}, new String[] {}, false);
        
        parms.put("url", _eventEngine.getLink(false, dsp).getURL());
        parms.put("events", events);
        parms.put("formEvents", formEvents);
        
        PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, component);
        Resource resource = getScript(component);
        
        _scriptSource.getScript(resource).execute(cycle, prs, parms);
    }
    
    /**
     * {@inheritDoc}
     */
    public void renderBody(IRequestCycle cycle, Body component)
    {
        if (cycle.isRewinding() || !_invoker.hasElementEvents())
            return;
        
        Map parms = new HashMap();
        DirectEventServiceParameter dsp =
            new DirectEventServiceParameter(component, new Object[] {}, new String[] {}, false);
        String url = _eventEngine.getLink(false, dsp).getURL();
        
        PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, component);
        Resource resource = getScript(component);
        
        Map elements = _invoker.getElementEvents();
        Iterator keys = elements.keySet().iterator();
        
        // build our list of targets / events
        while (keys.hasNext()) {
            String target = (String)keys.next();
            
            ComponentEventProperty prop = (ComponentEventProperty)elements.get(target);
            
            parms.put("target", target);
            parms.put("url", url);
            parms.put("events", prop.getEvents());
            parms.put("formEvents", filterFormEvents(prop, parms, cycle));
            
            _scriptSource.getScript(resource).execute(cycle, prs, parms);
            
            parms.clear();
        }
        
        // just in case
        _deferredFormConnections.clear();
    }
    
    void mapFormNames(IRequestCycle cycle, IForm form)
    {
        List names = (List)cycle.getAttribute(FORM_NAME_LIST + form.getId());
        
        if (names == null) {
            names = new ArrayList();
            cycle.setAttribute(FORM_NAME_LIST + form.getId(), names);
        }
        
        names.add(form.getName());
    }
    
    void linkDeferredForm(IRequestCycle cycle, IForm form)
    {
        List deferred = (List)_deferredFormConnections.remove(form.getId());
        
        for (int i=0; i < deferred.size(); i++) {
            
            Object[] val = (Object[])deferred.get(i);
            
            Map scriptParms = (Map)val[0];
            IComponent component = (IComponent)scriptParms.get("component");
            
            ComponentEventProperty props = _invoker.getComponentEvents(component.getId());
            Object[][] formEvents = buildFormEvents(cycle, form.getId(), props.getFormEvents());
            
            // don't want any events accidently connected again
            scriptParms.remove("events");
            scriptParms.put("formEvents", formEvents);
            
            // execute script
            PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, component);
            Resource resource = getScript(component);
            
            _scriptSource.getScript(resource).execute(cycle, prs, scriptParms);
        }
    }
    
    Object[][] buildFormEvents(IRequestCycle cycle, String formId, Set events)
    {
        List formNames = (List)cycle.getAttribute(FORM_NAME_LIST + formId);
        List retval = new ArrayList();
        
        Iterator it = events.iterator();
        while (it.hasNext()) {
            
            String event = (String)it.next();
            retval.add(new Object[]{event, formNames});
            
        }
        
        return (Object[][])retval.toArray(new Object[retval.size()][2]);
    }
    
    Resource getScript(IComponent component)
    {
        if (IWidget.class.isInstance(component)) {
            
            if (_widgetResource == null) 
                _widgetResource = new ClasspathResource(_resolver, _widgetScript);
            
            return _widgetResource;
        }
        
        if (Body.class.isInstance(component)) {
            
            if (_elementResource == null) 
                _elementResource = new ClasspathResource(_resolver, _elementScript);
            
            return _elementResource;
        }
        
        if (_componentResource == null) 
            _componentResource = new ClasspathResource(_resolver, _componentScript);
        
        return _componentResource;
    }
    
    boolean isDeferredForm(IComponent component)
    {
        if (IForm.class.isInstance(component) 
                && _deferredFormConnections.get(((IForm)component).getId()) != null)
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
     * @param scriptParms
     * @param cycle
     * @return
     */
    Object[][] filterFormEvents(ComponentEventProperty prop, Map scriptParms, IRequestCycle cycle)
    {
        Set events = prop.getFormEvents();
        
        if (events.size() < 1) 
            return new Object[0][0];
        
        List retval = new ArrayList();
        
        Iterator it = events.iterator();
        while (it.hasNext()) {
            
            String event = (String)it.next();
            Iterator lit = prop.getFormEventListeners(event).iterator();
            
            while (lit.hasNext()) {
                EventBoundListener listener = (EventBoundListener)lit.next();
                
                String formId = listener.getFormId();
                List formNames = (List)cycle.getAttribute(FORM_NAME_LIST + formId);
                
                // defer connection until form is rendered
                if (formNames == null) {
                    
                    deferFormConnection(formId, scriptParms);
                    continue;
                }
                
                // form has been rendered so go ahead
                retval.add(new Object[] {event, formNames});
            }
        }
        
        return (Object[][])retval.toArray(new Object[retval.size()][2]);
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
     * @param formId
     * @param scriptParms
     */
    void deferFormConnection(String formId, Map scriptParms)
    {
        List deferred = (List)_deferredFormConnections.get(formId);
        
        if (deferred == null) {
            
            deferred = new ArrayList();
            _deferredFormConnections.put(formId, deferred);
        }
        
        deferred.add(new Object[] {scriptParms});
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
    public void setComponentEventInvoker(ComponentEventInvoker invoker)
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
