// Copyright May 21, 2006 The Apache Software Foundation
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

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.test.AggregateArgumentsMatcher;
import org.apache.hivemind.test.ArgumentMatcher;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IDirectEvent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.IgnoreMatcher;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.dojo.IWidget;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.IScriptSource;


/**
 * Test functionality of {@link ComponentEventConnectionWorker}.
 * 
 * @author jkuhnert
 */
public class ComponentEventConnectionWorkerTest extends BaseComponentTestCase
{

    public void testEventRenderChain()
    {   
        ClassResolver resolver = new DefaultClassResolver();
        
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        IEngineService engine = (IEngineService)newMock(IEngineService.class);
        IRequestCycle cycle = newCycle();
        IScriptSource scriptSource = (IScriptSource)newMock(IScriptSource.class);
        IScript script = (IScript)newMock(IScript.class);
        
        PageRenderSupport prs = newPageRenderSupport();
        
        ILink link = (ILink)newMock(ILink.class);
        
        String compScript = "/org/apache/tapestry/ComponentEvent.script";
        String widScript = "/org/apache/tapestry/dojo/html/WidgetEvent.script";
        
        Resource compScriptResource = new ClasspathResource(resolver, compScript);
        Resource widScriptResource = new ClasspathResource(resolver, widScript);
        
        ComponentEventConnectionWorker worker = new ComponentEventConnectionWorker();
        worker.setClassResolver(resolver);
        worker.setComponentEventInvoker(invoker);
        worker.setComponentScript(compScript);
        worker.setWidgetScript(widScript);
        worker.setEventEngine(engine);
        worker.setScriptSource(scriptSource);
        
        IDirectEvent component = (IDirectEvent)newMock(IDirectEvent.class);
        IWidget widget = (IWidget)newMock(IWidget.class);
        
        assertNotNull(worker.getScript(component));
        assertEquals(compScript, worker.getScript(component).getPath());
        
        assertNotNull(worker.getScript(widget));
        assertEquals(widScript, worker.getScript(widget).getPath());
        
        // now test render
        invoker.addEventListener("comp1", new String[] {"onclick"}, "testMethod");
        
        component.getId();
        setReturnValue(component, "comp1");
        
        component.getId();
        setReturnValue(component, "comp1");
        
        trainGetLinkCheckIgnoreParameter(engine, cycle, false, new Object(), link);
        trainGetURL(link, "/some/url");
        
        cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE);
        setReturnValue(cycle, prs);
        
        scriptSource.getScript(compScriptResource);
        setReturnValue(scriptSource, script);
        
        script.execute(cycle, prs, new HashMap());
        
        ArgumentMatcher ignore = new IgnoreMatcher();
        getControl(script).setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        { null, null, ignore }));
        
        replayControls();
        
        worker.renderComponent(cycle, component);
        
        verifyControls();
        
        // test widget render
        
        invoker.addEventListener("wid1", new String[] {"onSelect"}, "testMethod");
        
        assertTrue(invoker.hasEvents("wid1"));
        
        widget.getId();
        setReturnValue(widget, "wid1");
        
        widget.getId();
        setReturnValue(widget, "wid1");
        
        trainGetLinkCheckIgnoreParameter(engine, cycle, false, new Object(), link);
        trainGetURL(link, "/some/url2");
        
        cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE);
        setReturnValue(cycle, prs);
        
        scriptSource.getScript(widScriptResource);
        setReturnValue(scriptSource, script);
        
        script.execute(cycle, prs, new HashMap());
        
        getControl(script).setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        { null, null, ignore }));
        
        replayControls();
        
        worker.renderComponent(cycle, widget);
        
        verifyControls();
    }
}
