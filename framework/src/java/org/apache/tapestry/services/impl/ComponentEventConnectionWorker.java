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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IDirectEvent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.dojo.IWidget;
import org.apache.tapestry.engine.DirectEventServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.html.Body;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.services.ComponentRenderWorker;


/**
 * Implementation that handles connecting events to listener
 * method invocations.
 * 
 * @author jkuhnert
 */
public class ComponentEventConnectionWorker implements ComponentRenderWorker
{
    private ComponentEventInvoker _invoker;
    
    private IEngineService _eventEngine;
    
    private IScriptSource _scriptSource;
    
    private String _componentScript;
    
    private String _widgetScript;
    
    private String _elementScript;
    
    private ClassResolver _resolver;
    
    private ClasspathResource _componentResource;
    private ClasspathResource _widgetResource;
    private ClasspathResource _elementResource;
    
    /** 
     * {@inheritDoc}
     */
    public void renderComponent(IRequestCycle cycle, IComponent component)
    {
        if (!_invoker.hasEvents(component.getId()))
            return;
        
        ComponentEventProperty prop = _invoker.getComponentEvents(component.getId());
        
        Map parms = new HashMap();
        DirectEventServiceParameter dsp =
            new DirectEventServiceParameter((IDirectEvent)component, new Object[] {}, new String[] {}, false);
        
        parms.put("component", component);
        parms.put("url", _eventEngine.getLink(false, dsp).getURL());
        parms.put("events", prop.getEvents());
        
        PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, component);
        
        Resource resource = getScript(component);
        
        _scriptSource.getScript(resource).execute(cycle, prs, parms);
    }
    
    /**
     * {@inheritDoc}
     */
    public void renderBody(IRequestCycle cycle, Body component)
    {
        if (!_invoker.hasElementEvents())
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
            
            _scriptSource.getScript(resource).execute(cycle, prs, parms);
            
            parms.clear();
        }
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
