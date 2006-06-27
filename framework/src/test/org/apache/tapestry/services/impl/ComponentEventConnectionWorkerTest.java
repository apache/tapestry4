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

import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IDirectEvent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.dojo.IWidget;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.html.Body;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;


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
        invoker.addEventListener("comp1", new String[] {"onclick"}, 
                "testMethod", null, false);
        
        expect(cycle.isRewinding()).andReturn(false);
        
        expect(component.getId()).andReturn("comp1").anyTimes();
        expect(component.getClientId()).andReturn("comp1").anyTimes();
        
        trainGetLinkCheckIgnoreParameter(engine, cycle, false, new Object(), link);
        trainGetURL(link, "/some/url");
        
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE))
        .andReturn(prs).anyTimes();
        
        expect(scriptSource.getScript(compScriptResource)).andReturn(script);
        
        script.execute(eq(cycle), eq(prs), isA(Map.class));
        
        replay();
        
        worker.renderComponent(cycle, component);
        
        verify();
        
        assertEquals(0, worker.getDefferedFormConnections().size());
        
        // test widget render
        
        invoker.addEventListener("wid1", new String[] {"onSelect"}, "testMethod",
                null, false);
        
        assertTrue(invoker.hasEvents("wid1"));
        
        expect(cycle.isRewinding()).andReturn(false);
     
        expect(widget.getId()).andReturn("wid1").anyTimes();
        expect(widget.getClientId()).andReturn("wid1").anyTimes();
        
        trainGetLinkCheckIgnoreParameter(engine, cycle, false, new Object(), link);
        trainGetURL(link, "/some/url2");
        
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE))
        .andReturn(prs).anyTimes();
        
        expect(scriptSource.getScript(widScriptResource)).andReturn(script);
        
        script.execute(eq(cycle), eq(prs), isA(Map.class));
        
        replay();
        
        worker.renderComponent(cycle, widget);
        
        verify();
    }
    
    public void testRewindRender()
    {
        IRequestCycle cycle = newCycle();
        
        ComponentEventConnectionWorker worker = new ComponentEventConnectionWorker();
        
        expect(cycle.isRewinding()).andReturn(true);
        
        replay();
        
        worker.renderComponent(cycle, null);
        
        verify();
    }
    
    public void testNullPageSupport()
    {
        IRequestCycle cycle = newCycle();
        
        ComponentEventConnectionWorker worker = new ComponentEventConnectionWorker();
        
        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(null);
        
        replay();
        
        worker.renderComponent(cycle, null);
        
        verify();
    }
    
    public void testDeferredConnection()
    {
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        IEngineService engine = (IEngineService)newMock(IEngineService.class);
        IRequestCycle cycle = newCycle();
        PageRenderSupport prs = newPageRenderSupport();
        
        ComponentEventConnectionWorker worker = new ComponentEventConnectionWorker();
        worker.setComponentEventInvoker(invoker);
        worker.setEventEngine(engine);
        
        IDirectEvent component = (IDirectEvent)newMock(IDirectEvent.class);
        
        // now test render
        invoker.addEventListener("comp1", new String[] {"onclick"}, 
                "testMethod", "form1", false);
        
        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(prs);
        
        expect(component.getId()).andReturn("comp1").anyTimes();
        expect(component.getClientId()).andReturn("comp1").anyTimes();
        
        expect(cycle.getAttribute(ComponentEventConnectionWorker.FORM_NAME_LIST + "form1")).andReturn(null);
        
        replay();
        
        worker.renderComponent(cycle, component);
        
        verify();
        
        assertEquals(1, worker.getDefferedFormConnections().size());
        
        List deferred = (List)worker.getDefferedFormConnections().get("form1");
        assertNotNull(deferred);
        assertEquals(1, deferred.size());
        
        Object[] parms = (Object[])deferred.get(0);
        assertEquals(1, parms.length);
        Map parm = (Map)parms[0];
        
        assertNotNull(parm.get("clientId"));
        assertNotNull(parm.get("component"));
        assertNull(parm.get("url"));
        assertNull(parm.get("formEvents"));
        assertNull(parm.get("target"));
        
        assertEquals("comp1", parm.get("clientId"));
        assertEquals(component, parm.get("component"));
    }
    
    public void testFormRenderDeffered()
    {
        ClassResolver resolver = new DefaultClassResolver();
        
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        IEngineService engine = (IEngineService)newMock(IEngineService.class);
        IRequestCycle cycle = newCycle();
        IScriptSource scriptSource = (IScriptSource)newMock(IScriptSource.class);
        IScript script = (IScript)newMock(IScript.class);
        
        PageRenderSupport prs = newPageRenderSupport();
        
        String compScript = "/org/apache/tapestry/ComponentEvent.script";
        Resource compScriptResource = new ClasspathResource(resolver, compScript);
        
        ComponentEventConnectionWorker worker = new ComponentEventConnectionWorker();
        worker.setClassResolver(resolver);
        worker.setComponentEventInvoker(invoker);
        worker.setComponentScript(compScript);
        worker.setEventEngine(engine);
        worker.setScriptSource(scriptSource);
        
        IDirectEvent component = (IDirectEvent)newMock(IDirectEvent.class);
        IForm form = (IForm)newMock(IForm.class);
        
        // now test render
        invoker.addEventListener("comp1", new String[] {"onclick"}, 
                "testMethod", "form1", false);
        
        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(prs);
        
        expect(component.getId()).andReturn("comp1").anyTimes();
        expect(component.getClientId()).andReturn("comp1").anyTimes();
        
        expect(cycle.getAttribute(ComponentEventConnectionWorker.FORM_NAME_LIST + "form1")).andReturn(null);
        
        replay();
        
        worker.renderComponent(cycle, component);
        
        verify();
        
        assertEquals(1, worker.getDefferedFormConnections().size());
        
        checkOrder(form, false);
        checkOrder(component, false);
        
        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(prs);
        
        expect(form.getId()).andReturn("form1").anyTimes();
        
        expect(cycle.getAttribute(ComponentEventConnectionWorker.FORM_NAME_LIST + "form1")).andReturn(null);
        
        cycle.setAttribute(eq(ComponentEventConnectionWorker.FORM_NAME_LIST + "form1"), 
                isA(List.class));
        
        expect(form.getName()).andReturn("form1_0").anyTimes();
        
        expect(component.getId()).andReturn("comp1").anyTimes();
        
        List formNames = new ArrayList();
        formNames.add("form1_0");
        
        expect(cycle.getAttribute(ComponentEventConnectionWorker.FORM_NAME_LIST + "form1"))
        .andReturn(formNames);
        
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE))
        .andReturn(prs);
        
        expect(scriptSource.getScript(compScriptResource)).andReturn(script);
        
        script.execute(eq(cycle), eq(prs), isA(Map.class));
        
        replay();
        
        worker.renderComponent(cycle, form);
        
        verify();
    }
    
    public void testScriptResource()
    {   
        ClassResolver resolver = new DefaultClassResolver();
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        
        String compScript = "/org/apache/tapestry/ComponentEvent.script";
        String widScript = "/org/apache/tapestry/dojo/html/WidgetEvent.script";
        String elementScript = "/org/apache/tapestry/html/ElementEvent.script";
        
        ComponentEventConnectionWorker worker = new ComponentEventConnectionWorker();
        worker.setClassResolver(resolver);
        worker.setComponentEventInvoker(invoker);
        worker.setComponentScript(compScript);
        worker.setWidgetScript(widScript);
        worker.setElementScript(elementScript);
        
        IDirectEvent component = (IDirectEvent)newMock(IDirectEvent.class);
        IWidget widget = (IWidget)newMock(IWidget.class);
        MockControl bodyControl = MockClassControl.createControl(Body.class);
        Body body = (Body) bodyControl.getMock();
        
        assertNotNull(worker.getScript(component));
        assertEquals(compScript, worker.getScript(component).getPath());
        
        assertNotNull(worker.getScript(widget));
        assertEquals(widScript, worker.getScript(widget).getPath());
        
        assertNotNull(worker.getScript(body));
        assertEquals(elementScript, worker.getScript(body).getPath());
        
        replay();
        
        verify();
    }
}
