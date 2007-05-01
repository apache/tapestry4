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

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.*;
import org.apache.tapestry.dojo.IWidget;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.html.Body;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.internal.event.IComponentEventInvoker;
import org.apache.tapestry.internal.event.impl.ComponentEventInvoker;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import static org.easymock.EasyMock.*;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Test functionality of {@link ComponentEventConnectionWorker}.
 * 
 * @author jkuhnert
 */
@Test
public class ComponentEventConnectionWorkerTest extends BaseComponentTestCase
{
    public void test_Event_Render_Chain()
    {   
        ClassResolver resolver = new DefaultClassResolver();
        
        IComponentEventInvoker invoker = new ComponentEventInvoker();
        IEngineService engine = newMock(IEngineService.class);
        IRequestCycle cycle = newCycle();
        checkOrder(cycle, false);
        
        IComponentSpecification spec = new ComponentSpecification();
        IComponentSpecification widgetSpec = new ComponentSpecification();
        
        IScriptSource scriptSource = newMock(IScriptSource.class);
        IScript script = newMock(IScript.class);
        
        PageRenderSupport prs = newPageRenderSupport();
        
        ILink link = newMock(ILink.class);
        
        String elemScript = "/org/apache/tapestry/html/ElementEvent.script";
        String compScript = "/org/apache/tapestry/ComponentEvent.script";
        String widScript = "/org/apache/tapestry/dojo/html/WidgetEvent.script";
        
        Resource compScriptResource = new ClasspathResource(resolver, compScript);
        Resource widScriptResource = new ClasspathResource(resolver, widScript);
        Resource elemScriptResource = new ClasspathResource(resolver, elemScript);
        
        ComponentEventConnectionWorker worker = new ComponentEventConnectionWorker();
        worker.setClassResolver(resolver);
        worker.setEventInvoker(invoker);
        worker.setComponentScript(compScript);
        worker.setWidgetScript(widScript);
        worker.setElementScript(elemScript);
        worker.setEventEngine(engine);
        worker.setScriptSource(scriptSource);
        
        IDirectEvent component = newMock(IDirectEvent.class);
        IWidget widget = newMock(IWidget.class);
        
        assertNotNull(worker.getScript(component));
        assertEquals(compScript, worker.getScript(component).getPath());
        
        assertNotNull(worker.getScript(widget));
        assertEquals(widScript, worker.getScript(widget).getPath());
        
        assertNotNull(worker.getElementScript());
        assertEquals(elemScript, worker.getElementScript().getPath());
        
        // now test render
        invoker.addEventListener("comp1", spec);
        spec.addEventListener("comp1", new String[] {"onclick"}, "testMethod", null, false, true, false, false);
        spec.addElementEventListener("elementId", new String[] {"onclick"}, "testMethod", null, false, true, false);
        
        /////////////////////////////////
        
        Map elmEvents = spec.getElementEvents();
        Iterator keyIt = elmEvents.keySet().iterator();
        
        while (keyIt.hasNext()) {
            String elem = (String)keyIt.next();
            assertEquals(elem, "elementId");
            
            ComponentEventProperty prop = (ComponentEventProperty)elmEvents.get(elem);
            assertNotNull(prop);
            
            Object[][] events = worker.getEvents(prop, elem);
            
            assertNotNull(events);
            assertEquals(events.length, 1);
            assertEquals(events[0].length, 2);
        }
        
        ////////////////////////////////////////////
        
        expect(cycle.isRewinding()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.FIELD_PRERENDER)).andReturn(null);
        
        expect(component.getExtendedId()).andReturn("comp1").anyTimes();
        expect(component.getClientId()).andReturn("comp1").anyTimes();
        
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(prs).anyTimes();
        expect(component.getSpecification()).andReturn(spec);
        
        trainGetLinkCheckIgnoreParameter(engine, cycle, false, new Object(), link);
        trainGetURL(link, "/some/url");
        
        expect(scriptSource.getScript(compScriptResource)).andReturn(script);
        script.execute(eq(component), eq(cycle), eq(prs), isA(Map.class));
        
        expect(component.getSpecification()).andReturn(spec);
        
        trainGetLinkCheckIgnoreParameter(engine, cycle, false, new Object(), link);
        trainGetURL(link, "/some/url");
        
        expect(scriptSource.getScript(elemScriptResource)).andReturn(script);
        
        script.execute(eq(component), eq(cycle), eq(prs), isA(Map.class));
        
        replay();
        
        worker.renderComponent(cycle, component);
        
        verify();
        
        assertEquals(0, worker.getDefferedFormConnections().size());
        
        // test widget render
        
        invoker.addEventListener("wid1", widgetSpec);
        widgetSpec.addEventListener("wid1", new String[] {"onclick"}, "testMethod", null, false, true, false, false);
        
        checkOrder(cycle, false);
        expect(cycle.isRewinding()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(prs).anyTimes();
        
        expect(widget.getSpecification()).andReturn(widgetSpec);
        
        expect(cycle.getAttribute(TapestryUtils.FIELD_PRERENDER)).andReturn(null);
        
        expect(widget.getExtendedId()).andReturn("wid1").anyTimes();
        expect(widget.getClientId()).andReturn("wid1").anyTimes();
        
        assert widgetSpec.getComponentEvents("wid1") != null;
        
        trainGetLinkCheckIgnoreParameter(engine, cycle, false, new Object(), link);
        trainGetURL(link, "/some/url2");
        
        expect(scriptSource.getScript(widScriptResource)).andReturn(script);
        
        script.execute(eq(widget), eq(cycle), eq(prs), isA(Map.class));
        
        replay();
        
        worker.renderComponent(cycle, widget);
        
        verify();
    }
    
    public void test_Rewind_Render()
    {
        IRequestCycle cycle = newCycle();
        
        ComponentEventConnectionWorker worker = new ComponentEventConnectionWorker();
        
        expect(cycle.isRewinding()).andReturn(true);
        
        replay();
        
        worker.renderComponent(cycle, null);
        
        verify();
    }
    
    public void test_Null_PageSupport()
    {
        IRequestCycle cycle = newCycle();
        
        ComponentEventConnectionWorker worker = new ComponentEventConnectionWorker();
        
        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(null);
        
        replay();
        
        worker.renderComponent(cycle, null);
        
        verify();
    }
    
    
    public void test_Deferred_Connection()
    {
        IComponentEventInvoker invoker = new org.apache.tapestry.internal.event.impl.ComponentEventInvoker();
        IEngineService engine = newMock(IEngineService.class);
        IRequestCycle cycle = newCycle();
        PageRenderSupport prs = newPageRenderSupport();
        
        ComponentEventConnectionWorker worker = new ComponentEventConnectionWorker();
        worker.setEventInvoker(invoker);
        worker.setEventEngine(engine);
        
        IDirectEvent component = newMock(IDirectEvent.class);
        IComponentSpecification spec = new ComponentSpecification();
        
        IDirectEvent comp1 = newMock(IDirectEvent.class);
        IComponentSpecification comp1Spec = new ComponentSpecification();
        
        IDirectEvent comp2 = newMock(IDirectEvent.class);
        IComponentSpecification comp2Spec = new ComponentSpecification();
        
        // now test render
        spec.addEventListener("comp1", new String[] {"onclick"}, "testMethod", "form1", true, true, false, false);
        invoker.addEventListener("comp1", spec);
        invoker.addFormEventListener("form1", spec);
        
        spec.addEventListener("comp2", new String[] {"onclick"}, "testAnotherMethod", "form1", true, true, false, false);
        invoker.addEventListener("comp2", spec);
        invoker.addFormEventListener("form1", spec);
        
        // render of comp1
        
        expect(cycle.isRewinding()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(prs);
        expect(cycle.getAttribute(TapestryUtils.FIELD_PRERENDER)).andReturn(null);
        
        expect(comp1.getExtendedId()).andReturn("comp1").anyTimes();
        expect(comp1.getClientId()).andReturn("comp1").anyTimes();
        
        expect(cycle.getAttribute(ComponentEventConnectionWorker.FORM_NAME_LIST + "form1")).andReturn(null);
        
        expect(comp1.getSpecification()).andReturn(comp1Spec);
        
        // render of comp2
        
        expect(cycle.isRewinding()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(prs);
        expect(cycle.getAttribute(TapestryUtils.FIELD_PRERENDER)).andReturn(null);
        
        expect(comp2.getExtendedId()).andReturn("comp2").anyTimes();
        expect(comp2.getClientId()).andReturn("comp2").anyTimes();
        
        expect(cycle.getAttribute(ComponentEventConnectionWorker.FORM_NAME_LIST + "form1")).andReturn(null);
        
        expect(comp2.getSpecification()).andReturn(comp2Spec);
        
        // render of component
        
        expect(cycle.isRewinding()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(prs);
        expect(cycle.getAttribute(TapestryUtils.FIELD_PRERENDER)).andReturn(null);
        
        expect(component.getExtendedId()).andReturn("comp").anyTimes();
        expect(component.getSpecification()).andReturn(spec);
        
        replay();
        
        worker.renderComponent(cycle, comp1);
        worker.renderComponent(cycle, comp2);
        worker.renderComponent(cycle, component);
        
        verify();
        
        assertEquals(worker.getDefferedFormConnections().size(), 1);
        
        List deferred = (List)worker.getDefferedFormConnections().get("form1");
        
        assert deferred != null;
        assertEquals(deferred.size(), 2);
        
        Object[] parms = (Object[])deferred.get(0);
        assertEquals(4, parms.length);
        
        // assert async is false
        assert (Boolean)parms[1] == true;
        
        // assert validate form is true
        assert (Boolean)parms[2] == true;
        
        Map parm = (Map)parms[0];
        
        assert parm.get("clientId") != null;
        assert parm.get("component") != null;
        assert parm.get("url") == null;
        assert parm.get("formEvents") == null;
        assert parm.get("target") == null;
        
        assertEquals("comp1", parm.get("clientId"));
        assertEquals(comp1, parm.get("component"));
        
        // test comp2 connections
        
        parms = (Object[])deferred.get(1);
        assertEquals(4, parms.length);
        
        // assert async is false
        assert (Boolean)parms[1] == true;
        
        // assert validate form is true
        assert (Boolean)parms[2] == true;
        
        parm = (Map)parms[0];
        
        assert parm.get("clientId") != null;
        assert parm.get("component") != null;
        assert parm.get("url") == null;
        assert parm.get("formEvents") == null;
        assert parm.get("target") == null;
        
        assertEquals("comp2", parm.get("clientId"));
        assertEquals(comp2, parm.get("component"));
    }
    
    
    public void test_Form_Render_Deffered()
    {
        ClassResolver resolver = new DefaultClassResolver();
        
        IComponentEventInvoker invoker = new ComponentEventInvoker();
        IEngineService engine = newMock(IEngineService.class);
        IRequestCycle cycle = newCycle();
        IScriptSource scriptSource = newMock(IScriptSource.class);
        IScript script = newMock(IScript.class);
        
        PageRenderSupport prs = newPageRenderSupport();
        
        String compScript = "/org/apache/tapestry/ComponentEvent.script";
        Resource compScriptResource = new ClasspathResource(resolver, compScript);
        
        ComponentEventConnectionWorker worker = new ComponentEventConnectionWorker();
        worker.setClassResolver(resolver);
        worker.setEventInvoker(invoker);
        worker.setComponentScript(compScript);
        worker.setEventEngine(engine);
        worker.setScriptSource(scriptSource);
        
        IDirectEvent component = newMock(IDirectEvent.class);
        IComponentSpecification spec = new ComponentSpecification();
        
        IComponent comp1 = newMock(IComponent.class);
        IComponentSpecification comp1Spec = new ComponentSpecification();
        
        IComponent comp2 = newMock(IComponent.class);
        IComponentSpecification comp2Spec = new ComponentSpecification();
        
        IForm form = newMock(IForm.class);
        IComponentSpecification formSpec = new ComponentSpecification();
        
        // now test render
        spec.addEventListener("comp1", new String[] {"onclick"}, "testMethod", "form1", false, true, false, false);
        invoker.addEventListener("comp1", spec);
        invoker.addFormEventListener("form1", spec);
        
        spec.addEventListener("comp2", new String[] {"ondoubleclick"}, "clickMethod", "form1", false, true, false, false);
        invoker.addEventListener("comp2", spec);
        invoker.addFormEventListener("form1", spec);
        
        expect(cycle.isRewinding()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(prs);
        expect(cycle.getAttribute(TapestryUtils.FIELD_PRERENDER)).andReturn(null);
        
        expect(component.getExtendedId()).andReturn("compListener").anyTimes();
        expect(component.getClientId()).andReturn("compListener").anyTimes();
        expect(component.getSpecification()).andReturn(spec);
        
        // comp1 render
        
        expect(cycle.isRewinding()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(prs);
        expect(cycle.getAttribute(TapestryUtils.FIELD_PRERENDER)).andReturn(null);
        
        expect(comp1.getExtendedId()).andReturn("comp1").anyTimes();
        expect(comp1.getClientId()).andReturn("comp1").anyTimes();
        
        expect(cycle.getAttribute(ComponentEventConnectionWorker.FORM_NAME_LIST + "form1")).andReturn(null);
        
        expect(comp1.getSpecification()).andReturn(comp1Spec).anyTimes();
        
        // comp2 render
        
        expect(cycle.isRewinding()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(prs);
        expect(cycle.getAttribute(TapestryUtils.FIELD_PRERENDER)).andReturn(null);
        
        expect(comp2.getExtendedId()).andReturn("comp2").anyTimes();
        expect(comp2.getClientId()).andReturn("comp2").anyTimes();
        
        expect(cycle.getAttribute(ComponentEventConnectionWorker.FORM_NAME_LIST + "form1")).andReturn(null);
        
        expect(comp2.getSpecification()).andReturn(comp2Spec).anyTimes();
        
        replay();
        
        worker.renderComponent(cycle, component);
        worker.renderComponent(cycle, comp1);
        worker.renderComponent(cycle, comp2);
        
        verify();
        
        assertEquals(worker.getDefferedFormConnections().size(), 1);
        assertEquals(((List)worker.getDefferedFormConnections().get("form1")).size(), 2);
        
        checkOrder(form, false);
        checkOrder(component, false);
        checkOrder(comp1, false);
        checkOrder(comp2, false);
        
        expect(cycle.isRewinding()).andReturn(false);
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(prs);
        
        expect(form.getSpecification()).andReturn(formSpec);
        
        expect(cycle.getAttribute(TapestryUtils.FIELD_PRERENDER)).andReturn(null);
        
        expect(form.getExtendedId()).andReturn("form1").anyTimes();
        
        expect(cycle.getAttribute(ComponentEventConnectionWorker.FORM_NAME_LIST + "form1")).andReturn(null);
        cycle.setAttribute(eq(ComponentEventConnectionWorker.FORM_NAME_LIST + "form1"), isA(List.class));
        
        expect(form.getName()).andReturn("form1_0").anyTimes();

        expect(comp1.getSpecification()).andReturn(comp1Spec);
        expect(comp1.getExtendedId()).andReturn("comp1").anyTimes();
        
        expect(comp2.getSpecification()).andReturn(comp2Spec);
        expect(comp2.getExtendedId()).andReturn("comp2").anyTimes();
        
        List formNames = new ArrayList();
        formNames.add("form1_0");
        
        expect(cycle.getAttribute(ComponentEventConnectionWorker.FORM_NAME_LIST + "form1")).andReturn(formNames).anyTimes();
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(prs).anyTimes();
        
        expect(scriptSource.getScript(compScriptResource)).andReturn(script);
        script.execute(eq(form), eq(cycle), eq(prs), isA(Map.class));
        
        expect(scriptSource.getScript(compScriptResource)).andReturn(script);
        script.execute(eq(form), eq(cycle), eq(prs), isA(Map.class));
        
        replay();
        
        worker.renderComponent(cycle, form);
        
        verify();
    }
    
    
    public void test_Script_Resource()
    {   
        ClassResolver resolver = new DefaultClassResolver();
        IComponentEventInvoker invoker = new org.apache.tapestry.internal.event.impl.ComponentEventInvoker();
        
        String compScript = "/org/apache/tapestry/ComponentEvent.script";
        String widScript = "/org/apache/tapestry/dojo/html/WidgetEvent.script";
        String elementScript = "/org/apache/tapestry/html/ElementEvent.script";
        
        ComponentEventConnectionWorker worker = new ComponentEventConnectionWorker();
        worker.setClassResolver(resolver);
        worker.setEventInvoker(invoker);
        worker.setComponentScript(compScript);
        worker.setWidgetScript(widScript);
        worker.setElementScript(elementScript);
        
        IDirectEvent component = newMock(IDirectEvent.class);
        IWidget widget = newMock(IWidget.class);
        
        MockControl bodyControl = MockClassControl.createControl(Body.class);
        Body body = (Body) bodyControl.getMock();
        
        assertNotNull(worker.getScript(component));
        assertEquals(compScript, worker.getScript(component).getPath());
        
        assertNotNull(worker.getScript(widget));
        assertEquals(widScript, worker.getScript(widget).getPath());
        
        assertNotNull(worker.getScript(body));
        assertEquals(compScript, worker.getScript(body).getPath());
        
        replay();
        
        verify();
    }
    
    public void test_Prerendered_Field()
    {   
        ClassResolver resolver = new DefaultClassResolver();
        
        IComponentEventInvoker invoker = new org.apache.tapestry.internal.event.impl.ComponentEventInvoker();
        IEngineService engine = newMock(IEngineService.class);
        IRequestCycle cycle = newCycle();
        checkOrder(cycle, false);
        
        PageRenderSupport prs = newPageRenderSupport();
        
        IScriptSource scriptSource = newMock(IScriptSource.class);
        
        ComponentEventConnectionWorker worker = new ComponentEventConnectionWorker();
        worker.setClassResolver(resolver);
        worker.setEventInvoker(invoker);
        worker.setEventEngine(engine);
        worker.setScriptSource(scriptSource);
        
        IDirectEvent component = newMock(IDirectEvent.class);
        IComponentSpecification spec = new ComponentSpecification();
        
        // now test render
        invoker.addEventListener("comp1", spec);
        spec.addEventListener("comp1", new String[] {"onclick"}, "testMethod", "form1", false, false, false, false);
        
        expect(cycle.isRewinding()).andReturn(false);
        
        expect(cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE)).andReturn(prs);
        
        expect(cycle.getAttribute(TapestryUtils.FIELD_PRERENDER)).andReturn(component);
        
        replay();
        
        worker.renderComponent(cycle, component);
        
        verify();
    }
}
