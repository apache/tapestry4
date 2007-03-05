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
package org.apache.tapestry.internal.event.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.form.FormSupport;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.internal.event.EventBoundListener;
import org.apache.tapestry.internal.event.IComponentEventInvoker;
import org.apache.tapestry.listener.ListenerInvoker;
import org.apache.tapestry.spec.IComponentSpecification;


/**
 * Implementation of {@link IComponentEventInvoker}.
 *
 * @author jkuhnert
 */
public class ComponentEventInvoker implements IComponentEventInvoker, ResetEventListener
{
    private Map _components = new HashMap();
    
    private Map _formComponents = new HashMap();
    
    private ListenerInvoker _invoker;
    
    /**
     * {@inheritDoc}
     */
    public void invokeListeners(IComponent component, IRequestCycle cycle, BrowserEvent event)
    {
        Defense.notNull(component, "component");
        Defense.notNull(cycle, "cycle");
        Defense.notNull(event, "event");
        
        invokeComponentListeners(component, cycle, event);
        
        invokeElementListeners(component, cycle, event);
    }
    
    /**
     * {@inheritDoc}
     */
    public void invokeFormListeners(FormSupport formSupport, final IRequestCycle cycle, final BrowserEvent event)
    {
        Defense.notNull(formSupport, "formSupport");
        Defense.notNull(cycle, "cycle");
        Defense.notNull(event, "event");
        
        IForm form = formSupport.getForm();
        
        String targetId = (String)event.getTarget().get("id");
        if (targetId == null)
            return;
        
        List comps = getFormEventListeners(form.getId());
        if (comps == null)
            return;
        
        boolean disableFocus = false;
        
        for (int i=0; i < comps.size(); i++) {
            
            IComponentSpecification spec = (IComponentSpecification)comps.get(i);
            EventBoundListener[] listeners = spec.getFormEvents(form.getId(), event);
            
            IComponent target = null;
            if (spec.isPageSpecification()) {
                
                target = form.getPage();
            } else {
                
                target = findComponent(form.getPage().getComponents().values(), spec);
            }
            
            for (int e=0; e < listeners.length; e++) {
                
                // ensure ~only~ the method that targeted this event gets called!
                
                if (!targetId.startsWith(listeners[e].getComponentId()))
                    continue;
                
                // handle disabling focus 
                if (!disableFocus && !listeners[e].shouldFocusForm())
                    disableFocus = true;
                
                // defer execution until after form is done rewinding
                
                form.addDeferredRunnable(
                        new FormRunnable(target.getListeners().getListener(listeners[e].getMethodName()),
                                target,
                                cycle));
            }
        }
        
        // Form uses cycle attributes to test whether or not to focus .
        // The attribute existing at all is enough to bypass focusing.
        if (disableFocus) {
            
            cycle.disableFocus();
        }
    }
    
    void invokeComponentListeners(IComponent component, IRequestCycle cycle, BrowserEvent event)
    {
        List listeners = getEventListeners(component.getId());
        if (listeners == null)
            return;
        
        for (int i = 0; i < listeners.size(); i++) {
            
            IComponentSpecification listener = (IComponentSpecification)listeners.get(i);
            
            IComponent target = null;
            ComponentEventProperty props = null;
            
            if (listener.isPageSpecification()) {
                
                target = component.getPage();
                props = listener.getComponentEvents(component.getId());
            } else {
                
                target = findComponent(component.getPage().getComponents().values(), listener);
                props = target.getSpecification().getComponentEvents(component.getId());
            }
            if (props == null)
                continue;
            
            List clisteners = props.getEventListeners(event.getName());
            for (int e=0; e < clisteners.size(); e++) {
                
                EventBoundListener eventListener = (EventBoundListener)clisteners.get(e);
                
                _invoker.invokeListener(target.getListeners().getListener(eventListener.getMethodName()), target, cycle);
            }
            
        }
    }
    
    void invokeElementListeners(IComponent component, IRequestCycle cycle, BrowserEvent event)
    {
        String targetId = (String)event.getTarget().get("id");
        if (targetId == null)
            return;
        
        ComponentEventProperty prop = component.getSpecification().getElementEvents(targetId);
        if (prop == null)
            return;
        
        List listeners = prop.getEventListeners(event.getName());
        
        for (int i=0; i < listeners.size(); i++) {
            
            EventBoundListener listener = (EventBoundListener)listeners.get(i);
            
            _invoker.invokeListener(component.getListeners().getListener(listener.getMethodName()), component, cycle);
        }
    }
    
    IComponent findComponent(Collection comps, IComponentSpecification spec)
    {
        IComponent ret = null;
        
        Iterator it = comps.iterator();
        
        while (it.hasNext()) {
            IComponent comp = (IComponent)it.next();
            
            if (comp.getSpecification().equals(spec)) {
                ret = comp;
                break;
            }
            
            ret = findComponent(comp.getComponents().values(), spec);
            if (ret != null)
                break;
        }
        
        return ret;
    }
    
    /** Local runnable for deferred form connections. */
    class FormRunnable implements Runnable {
        
        private IActionListener _listener;
        private IComponent _component;
        private IRequestCycle _cycle;
        
        public FormRunnable(IActionListener listener, IComponent comp, IRequestCycle cycle)
        {
            _listener = listener;
            _component = comp;
            _cycle = cycle;
        }
        
        public void run()
        {
            _invoker.invokeListener(_listener, _component, _cycle);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void addEventListener(String componentId, IComponentSpecification listener)
    {
        List listeners = (List)_components.get(componentId);
        
        if (listeners == null) {
            listeners = new ArrayList();
            _components.put(componentId, listeners);
        }
        
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List getEventListeners(String componentId)
    {
        return (List)_components.get(componentId);
    }
    
    /**
     * {@inheritDoc}
     */
    public void addFormEventListener(String formId, IComponentSpecification listener)
    {
        List listeners = (List)_formComponents.get(formId);
        
        if (listeners == null) {
            listeners = new ArrayList();
            _formComponents.put(formId, listeners);
        }
        
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * {@inheritDoc}
     */
    public List getFormEventListeners(String formId)
    {
        return (List)_formComponents.get(formId);
    }
    
    /**
     * {@inheritDoc}
     */
    public void resetEventDidOccur()
    {
        _components.clear();
        _formComponents.clear();
    }
    
    /** Injected. */
    public void setInvoker(ListenerInvoker invoker)
    {
        _invoker = invoker;
    }
}
