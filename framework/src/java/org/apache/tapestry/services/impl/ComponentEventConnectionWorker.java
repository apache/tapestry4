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
    
    private ClassResolver _resolver;
    
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
    
    Resource getScript(IComponent component)
    {
        if (IWidget.class.isInstance(component))
            return new ClasspathResource(_resolver, _widgetScript);
        
        return new ClasspathResource(_resolver, _componentScript);
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
