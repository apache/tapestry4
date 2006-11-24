// Copyright Jul 9, 2006 The Apache Software Foundation
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
package org.apache.tapestry.link;

import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.html.Body;
import org.apache.tapestry.services.ComponentRenderWorker;
import org.apache.tapestry.util.ScriptUtils;


/**
 * Manages connecting {@link IDirect} components that also implement
 * the {@link ILinkComponent} interface to JavaScript asynchronous IO operations
 * when any of the shared parameters of {@link IDirect} are specified that relate
 * to asynchronous operations.
 * 
 * @author jkuhnert
 */
public class DirectLinkWorker implements ComponentRenderWorker
{
    // Parses/manages script template
    private IScriptSource _scriptSource;
    
    // Used to resolve classpath relative resources
    private ClasspathResource _script;
    
    // Class resolver
    private ClassResolver _resolver;
    
    // The path to the javascript template we will use to connect links to IO operations
    private String _scriptPath;
    
    /**
     * Default constructor, does nothing.
     */
    public DirectLinkWorker()
    {
    }
    
    /** 
     * {@inheritDoc}
     */
    public void renderComponent(IRequestCycle cycle, IComponent component)
    {
        if (cycle.isRewinding())
            return;
        
        // must implement both interfaces
        if (!ILinkComponent.class.isInstance(component)
                || !IDirect.class.isInstance(component)
                || ( ((ILinkComponent)component).isDisabled()))
            return;
        
        IDirect direct = (IDirect)component;
        
        // check for dynamic parameters
        if (!direct.isAsync() && !direct.isJson() 
                && (direct.getUpdateComponents() == null 
                || direct.getUpdateComponents().size() <= 0))
            return;
        
        PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, component);
        
        if (prs == null)
            return;
        
        Map parms = new HashMap();
        
        parms.put("component", component);
        parms.put("json", Boolean.valueOf(direct.isJson()));
        parms.put("key", ScriptUtils.functionHash("onclick" + component.hashCode()));
        
        // execute script template
        
        _scriptSource.getScript(_script).execute(component, cycle, prs, parms);
    }
    
    /** 
     * {@inheritDoc}
     */
    public void renderBody(IRequestCycle cycle, Body component)
    {
    }
    
    /**
     * Needs to be invoked to initialize resources used. 
     */
    public void initialize()
    {
        Defense.notNull(_resolver, "Classpath Resolver");
        Defense.notNull(_scriptPath, "Script path");
        
        _script = new ClasspathResource(_resolver, _scriptPath);
    }
    
    public void setScriptSource(IScriptSource source)
    {
        _scriptSource = source;
    }
    
    public void setScript(String path)
    {
        _scriptPath = path;
    }
    
    /**
     * Auto-Wire injected.
     * @param resolver
     */
    public void setClassResolver(ClassResolver resolver)
    {
        _resolver = resolver;
    }
}
