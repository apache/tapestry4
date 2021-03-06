// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.form;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.*;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.event.EventTarget;
import org.apache.tapestry.internal.event.impl.ComponentEventInvoker;
import org.apache.tapestry.listener.ListenerInvoker;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.util.IdAllocator;
import org.apache.tapestry.valid.IValidationDelegate;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.*;
import org.easymock.IAnswer;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link org.apache.tapestry.form.FormSupportImpl}.
 *
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test(sequential = true)
public class FormSupportTest extends BaseComponentTestCase {
    
    protected FormSupport newFormSupport(IMarkupWriter writer, IRequestCycle cycle, IForm form)
    {
        return new FormSupportImpl(writer, cycle, form);
    }

    private IRender newComponentRenderBody(final FormSupport fs, final IFormComponent component,
                                           final IMarkupWriter nested)
    {
        return newComponentsRenderBody(fs, new IFormComponent[] {component}, nested);
    }

    private IRender newComponentsRenderBody(final FormSupport fs, final IFormComponent[] component,
                                            final IMarkupWriter nested)
    {
        return new IRender() {
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                assertEquals(nested, writer);

                for (int i = 0; i < component.length; i++)
                    fs.getElementId(component[i]);
            }
        };
    }

    private IValidationDelegate newDelegate()
    {
        return newMock(IValidationDelegate.class);
    }

    protected IEngine newEngine()
    {
        return newMock(IEngine.class);
    }

    private IFormComponent newField()
    {
        return newMock(IFormComponent.class);
    }

    private IFormComponent newFormComponent(String id, String name)
    {
        IFormComponent component = newMock(IFormComponent.class);
        checkOrder(component, false);

        expect(component.getSpecifiedId()).andReturn(id);

        component.setName(name);
        component.setClientId(name);

        return component;
    }

    private IFormComponent newFormComponent(String id, String extendedId, Location location)
    {
        IFormComponent component = newMock(IFormComponent.class);

        expect(component.getSpecifiedId()).andReturn(id);

        trainGetExtendedId(component, extendedId);
        trainGetLocation(component, location);

        return component;
    }

    protected void trainCycleSeedEncoding(IRequestCycle cycle)
    {
        expect(cycle.encodeIdState()).andReturn("ENCODED").anyTimes();
        
        expect(cycle.getUniqueId(isA(String.class))).andAnswer(new UniqueIdAnswer()).anyTimes();
    }

    public class UniqueIdAnswer implements IAnswer<String> {

        IdAllocator _allocator = new IdAllocator();

        public String answer()
                throws Throwable
        {
            return _allocator.allocateId((String) EasyMock.getCurrentArguments()[0]);
        }
    }

    public void test_Cancel_Rewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);

        trainGetPageRenderSupport(cycle, null);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        delegate.clear();

        trainGetParameter(cycle, FormSupportImpl.SUBMIT_MODE, "cancel");

        // Create a body, just to provie it doesn't get invoked.

        IRender body = newMock(IRender.class);

        form.setBody(body);

        replay();

        assertEquals(FormConstants.SUBMIT_CANCEL, fs.rewind());

        verify();
    }

    public void test_Complex_Render()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        ResponseBuilder builder = newMock(ResponseBuilder.class);
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();
        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        
        final IFormComponent barney1 = newFormComponent("barney", "barney");
        final IFormComponent wilma = newFormComponent("wilma", "wilma");
        final IFormComponent barney2 = newFormComponent("barney", "barney_0");

        IRender body = newComponentsRenderBody(fs, new IFormComponent[] {barney1, wilma, barney2}, nested);

        form.setBody(body);

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[] {"service"});
        trainGetParameterValues(link, "service", new String[] {"fred"});

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("id", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(cycle, builder, writer, form, "fred", "barney,wilma,barney_0");

        nested.close();

        writer.end();

        trainGetFocusField(delegate, "wilma");

        expect(cycle.isFocusDisabled()).andReturn(false);

        // effectively means someone else has already claimed focus

        trainGetFieldFocus(cycle, null);

        support.addInitializationScript(form, "dojo.require(\"tapestry.form\");");
        support.addScriptAfterInitialization(form, "tapestry.form.focusField('wilma');");
        cycle.setAttribute(FormSupportImpl.FIELD_FOCUS_ATTRIBUTE, Boolean.TRUE);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void test_Complex_Rewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        MockForm form = new MockForm(delegate);
        ListenerInvoker listenerInvoker = newMock(ListenerInvoker.class);

        ComponentEventInvoker invoker = new ComponentEventInvoker();
        invoker.setInvoker(listenerInvoker);

        trainIsRewound(cycle, form, true);
        trainGetPageRenderSupport(cycle, null);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();
        
        delegate.clear();

        trainCycleForRewind(cycle, "barney,wilma,barney_0", null);
        trainCycleSeedEncoding(cycle);

        final IFormComponent barney1 = newFormComponent("barney", "barney");
        final IFormComponent wilma = newFormComponent("wilma", "wilma");
        final IFormComponent barney2 = newFormComponent("barney", "barney_0");

        IRender body = newComponentsRenderBody(fs, new IFormComponent[] {barney1, wilma, barney2}, writer);

        form.setBody(body);
        form.setEventInvoker(invoker);

        trainExtractBrowserEvent(cycle);

        replay();

        Map props = new HashMap();
        props.put("id", "bsId");
        BrowserEvent event = new BrowserEvent("onclick", new EventTarget(props));

        invoker.invokeFormListeners(fs, cycle, event);

        assertEquals(FormConstants.SUBMIT_NORMAL, fs.rewind());

        verify();
    }

    public void test_Complex_Submit_Event_Handler()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        ResponseBuilder builder = newMock(ResponseBuilder.class);
        IValidationDelegate delegate = newDelegate();
        PageRenderSupport support = newPageRenderSupport();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);
        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);

        form.setBody(new IRender() {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit1");
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit2");
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit3");
            }
        });

        trainRegister(support, form, "myform");
        trainGetParameterNames(link, new String[] {"service"});
        trainGetParameterValues(link, "service", new String[] {"fred"});

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("id", "myform");

        support.addInitializationScript(form, "Tapestry.onsubmit('myform', function (event)"
                                              + "\n{\n  mySubmit1();\n  mySubmit2();\n  mySubmit3();\n});\n");

        render.render(writer, cycle);
        writer.println();

        trainHiddenBlock(cycle, builder, writer, form, "fred", "");

        nested.close();
        writer.end();

        support.addInitializationScript(form, "dojo.require(\"tapestry.form\");");

        // Side test: what if no focus field?

        trainGetFocusField(delegate, null);

        expect(cycle.isFocusDisabled()).andReturn(false);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void test_Encoding_Type()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        ResponseBuilder builder = newMock(ResponseBuilder.class);
        IValidationDelegate delegate = newDelegate();
        PageRenderSupport support = newPageRenderSupport();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        
        form.setBody(new IRender() {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.setEncodingType("foo/bar");
            }
        });

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[] {"service"});
        trainGetParameterValues(link, "service", new String[] {"fred"});
        
        trainGetNestedWriter(writer, nested);

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("id", "myform");
        writer.attribute("enctype", "foo/bar");

        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(cycle, builder, writer, form, "fred", "");

        nested.close();

        writer.end();

        support.addInitializationScript(form, "dojo.require(\"tapestry.form\");");

        trainGetFocusField(delegate, null);

        expect(cycle.isFocusDisabled()).andReturn(false);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void test_Field_Prerender_Twice()
    {
        IFormComponent field = newField();
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        Location l = newLocation();

        ResponseBuilder builder = newMock(ResponseBuilder.class);

        trainGetExtendedId(field, "foo.bar");
        trainGetNestedWriter(writer, nested);

        expect(cycle.getAttribute(TapestryUtils.FIELD_PRERENDER)).andReturn(null);
        cycle.setAttribute(TapestryUtils.FIELD_PRERENDER, field);

        expect(cycle.getResponseBuilder()).andReturn(builder);

        builder.render(nested, field, cycle);

        cycle.removeAttribute(TapestryUtils.FIELD_PRERENDER);

        expect(nested.getBuffer()).andReturn("NESTED CONTENT");

        replay();

        FormSupport fs = new FormSupportImpl(cycle);

        fs.prerenderField(writer, field, l);

        verify();

        trainGetExtendedId(field, "foo.bar");

        replay();

        try
        {
            fs.prerenderField(writer, field, l);
            unreachable();
        }
        catch (ApplicationRuntimeException ex) {
            assertEquals(
                    "Field EasyMock for interface org.apache.tapestry.form.IFormComponent has already been pre-rendered. "
                    + "This exception may indicate that a FieldLabel rendered, but the corresponding field did not.",
                    ex.getMessage());

            assertSame(l, ex.getLocation());
            assertSame(field, ex.getComponent());
        }

        verify();

    }

    public void test_Hidden_Values()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        ResponseBuilder builder = newMock(ResponseBuilder.class);
        IValidationDelegate delegate = newDelegate();
        PageRenderSupport support = newPageRenderSupport();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);
        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        
        form.setBody(new IRender() {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addHiddenValue("hidden1", "value1");
                fs.addHiddenValue("hidden2", "id2", "value2");
            }
        });

        trainRegister(support, form, "myform");
        trainGetParameterNames(link, new String[]{"service"});
        trainGetParameterValues(link, "service", new String[] {"fred"});

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("id", "myform");

        render.render(writer, cycle);

        writer.println();

        expect(cycle.getResponseBuilder()).andReturn(builder);

        expect(builder.contains(form)).andReturn(false);

        trainDiv(writer, form);


        trainHidden(writer, "formids", "");
        trainHidden(writer, "seedids", "ENCODED");
        trainHidden(writer, "service", "fred");
        trainHidden(writer, "submitmode", "");
        trainHidden(writer, FormConstants.SUBMIT_NAME_PARAMETER, "");
        trainHidden(writer, "hidden1", "value1");
        trainHidden(writer, "hidden2", "id2", "value2");

        writer.end();

        nested.close();

        writer.end();

        support.addInitializationScript(form, "dojo.require(\"tapestry.form\");");

        trainGetFocusField(delegate, null);

        expect(cycle.isFocusDisabled()).andReturn(false);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void test_Invalid_Encoding_Type()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        PageRenderSupport support = newPageRenderSupport();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        
        form.setBody(new IRender() {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.setEncodingType("foo/bar");
                fs.setEncodingType("zip/zap");
            }
        });

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[]{"service"});
        trainGetParameterValues(link, "service", new String[]{"fred"});

        trainGetNestedWriter(writer, nested);

        replay();

        try {
            fs.render("post", render, link, null, null);
            unreachable();
        }
        catch (ApplicationRuntimeException ex) {
            assertEquals(
                    "Components within form SomePage/myform have requested conflicting encoding types 'foo/bar' and 'zip/zap'.",
                    ex.getMessage());
            assertSame(form, ex.getComponent());
        }

        verify();
    }

    public void test_Refresh_Rewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        MockForm form = new MockForm(delegate);
        ComponentEventInvoker invoker = org.easymock.classextension.EasyMock.createMock(ComponentEventInvoker.class);

        trainIsRewound(cycle, form, true);
        trainGetPageRenderSupport(cycle, null);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        delegate.clear();

        trainCycleForRewind(cycle, "refresh", "barney", null);

        final IFormComponent component = newFormComponent("barney", "barney");

        IRender body = newComponentRenderBody(fs, component, writer);

        form.setBody(body);
        form.setEventInvoker(invoker);

        trainExtractBrowserEvent(cycle);

        invoker.invokeFormListeners(eq(fs), eq(cycle), isA(BrowserEvent.class));

        delegate.clearErrors();

        replay();

        assertEquals(FormConstants.SUBMIT_REFRESH, fs.rewind());

        verify();
    }

    public void test_Render_Extra_Reserved_Ids()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        ResponseBuilder builder = newMock(ResponseBuilder.class);
        IValidationDelegate delegate = newDelegate();
        PageRenderSupport support = newPageRenderSupport();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        
        final IFormComponent component = newFormComponent("action", "action_0");

        IRender body = newComponentRenderBody(fs, component, nested);

        form.setBody(body);

        trainRegister(support, form, "myform");
        trainGetParameterNames(link, new String[]{"action"});
        trainGetParameterValues(link, "action", new String[] {"fred"});

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("id", "myform");

        render.render(writer, cycle);

        writer.println();

        expect(cycle.getResponseBuilder()).andReturn(builder);
        expect(builder.contains(form)).andReturn(false);

        trainDiv(writer, form);

        trainHidden(writer, "formids", "action_0");
        trainHidden(writer, "seedids", "ENCODED");
        trainHidden(writer, "action", "fred");
        trainHidden(writer, "reservedids", "action");
        trainHidden(writer, "submitmode", "");
        trainHidden(writer, FormConstants.SUBMIT_NAME_PARAMETER, "");

        writer.end();
        nested.close();
        writer.end();

        support.addInitializationScript(form, "dojo.require(\"tapestry.form\");");

        trainGetFocusField(delegate, null);

        expect(cycle.isFocusDisabled()).andReturn(false);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void test_Reset_Event_Handler()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        ResponseBuilder builder = newMock(ResponseBuilder.class);

        IValidationDelegate delegate = newDelegate();
        PageRenderSupport support = newPageRenderSupport();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        form.setBody(new IRender() {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addEventHandler(FormEventType.RESET, "myReset1");
                fs.addEventHandler(FormEventType.RESET, "myReset2");
            }
        });

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[] {"service"});
        trainGetParameterValues(link, "service", new String[] {"fred"});

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("id", "myform");

        support.addInitializationScript(form, "Tapestry.onreset('myform', function (event)"
                                              + "\n{\n  myReset1();\n  myReset2();\n});\n");

        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(cycle, builder, writer, form, "fred", "");

        nested.close();
        writer.end();

        support.addInitializationScript(form, "dojo.require(\"tapestry.form\");");

        trainGetFocusField(delegate, null);

        expect(cycle.isFocusDisabled()).andReturn(false);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void test_Rewind_Extra_Reserved_Ids()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        ComponentEventInvoker invoker =
                org.easymock.classextension.EasyMock.createMock(ComponentEventInvoker.class);

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);

        trainGetPageRenderSupport(cycle, null);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        delegate.clear();

        trainCycleForRewind(cycle, "action_0", "action");

        final IFormComponent component = newFormComponent("action", "action_0");

        IRender body = newComponentRenderBody(fs, component, writer);

        form.setBody(body);
        form.setEventInvoker(invoker);

        trainExtractBrowserEvent(cycle);

        invoker.invokeFormListeners(eq(fs), eq(cycle), isA(BrowserEvent.class));

        replay();

        assertEquals(FormConstants.SUBMIT_NORMAL, fs.rewind());

        verify();
    }

    public void test_Rewind_Mismatch()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);
        trainGetPageRenderSupport(cycle, null);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        Location l = newLocation();

        delegate.clear();

        // So, the scenario here is that component "pebbles" was inside
        // some kind of conditional that evaluated to true during the render,
        // but is now false on the rewind.

        trainCycleForRewind(cycle, "barney,wilma,pebbles,barney_0", null);

        final IFormComponent barney1 = newFormComponent("barney", "barney");
        final IFormComponent wilma = newFormComponent("wilma", "wilma");
        final IFormComponent barney2 = newFormComponent("barney", "SomePage/barney", l);

        IRender body = newComponentsRenderBody(fs, new IFormComponent[]
                {barney1, wilma, barney2}, writer);

        form.setBody(body);

        replay();

        try {
            fs.rewind();
            unreachable();
        }
        catch (StaleLinkException ex) {
            assertEquals(
                    "Rewind of form SomePage/myform expected allocated id #3 to be 'pebbles', but was 'barney_0' (requested by component SomePage/barney).",
                    ex.getMessage());
            assertSame(barney2, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void test_Rewind_Too_Long()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);
        trainGetPageRenderSupport(cycle, null);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        
        Location l = newLocation();
        delegate.clear();

        // So, the scenario here is that component "barney" was inside
        // some kind of loop that executed once on the render, but twice
        // on the rewind (i.e., an additional object was added in between).

        trainCycleForRewind(cycle, "barney,wilma", null);

        final IFormComponent barney1 = newFormComponent("barney", "barney");
        final IFormComponent wilma = newFormComponent("wilma", "wilma");
        final IFormComponent barney2 = newFormComponent("barney", "SomePage/barney", l);

        IRender body = newComponentsRenderBody(fs, new IFormComponent[]
                {barney1, wilma, barney2}, writer);

        form.setBody(body);

        replay();

        try {
            fs.rewind();
            unreachable();
        }
        catch (StaleLinkException ex) {
            assertEquals(
                    "Rewind of form SomePage/myform expected only 2 form elements, but an additional id was requested by component SomePage/barney.",
                    ex.getMessage());
            assertSame(barney2, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void test_Rewind_Too_Short()
    {
        Location l = newLocation();
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        ComponentEventInvoker invoker = org.easymock.classextension.EasyMock.createMock(ComponentEventInvoker.class);

        MockForm form = new MockForm(delegate, l);

        trainIsRewound(cycle, form, true);
        trainGetPageRenderSupport(cycle, null);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        delegate.clear();

        // So, the scenario here is that component "barney" was inside
        // some kind of loop that executed twice on the render, but only once
        // on the rewind (i.e., the object was deleted in between).

        trainCycleForRewind(cycle, "barney,wilma,barney$0", null);

        final IFormComponent barney1 = newFormComponent("barney", "barney");
        final IFormComponent wilma = newFormComponent("wilma", "wilma");

        IRender body = newComponentsRenderBody(fs, new IFormComponent[]{barney1, wilma}, writer);

        form.setBody(body);
        form.setEventInvoker(invoker);

        trainExtractBrowserEvent(cycle);

        invoker.invokeFormListeners(eq(fs), eq(cycle), isA(BrowserEvent.class));

        replay();

        try {
            fs.rewind();
            unreachable();
        }
        catch (StaleLinkException ex) {
            assertEquals(
                    "Rewind of form SomePage/myform expected 1 more form elements, starting with id 'barney$0'.",
                    ex.getMessage());
            assertSame(form, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void test_Simple_Render()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        ResponseBuilder builder = newMock(ResponseBuilder.class);
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        final IFormComponent component = newFormComponent("barney", "barney");

        IRender body = newComponentRenderBody(fs, component, nested);

        form.setBody(body);

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[]
                {"service"});
        trainGetParameterValues(link, "service", new String[]
                {"fred"});

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("id", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(cycle, builder, writer, form, "fred", "barney");

        nested.close();

        writer.end();

        trainGetFocusField(delegate, "barney");

        expect(cycle.isFocusDisabled()).andReturn(false);

        // Side test: check for another form already grabbing focus

        trainGetFieldFocus(cycle, null);

        support.addInitializationScript(form, "dojo.require(\"tapestry.form\");");
        support.addScriptAfterInitialization(form, "tapestry.form.focusField('barney');");
        cycle.setAttribute(FormSupportImpl.FIELD_FOCUS_ATTRIBUTE, Boolean.TRUE);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void test_Simple_Render_With_Deferred_Runnable()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        ResponseBuilder builder = newMock(ResponseBuilder.class);
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        IRender body = new IRender() {
            public void render(final IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addDeferredRunnable(new Runnable() {

                    public void run()
                    {
                        pwriter.print("DEFERRED");
                    }

                });
            }

        };

        form.setBody(body);

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[] {"service"});
        trainGetParameterValues(link, "service", new String[]{"fred"});
        
        trainGetNestedWriter(writer, nested);

        nested.print("DEFERRED");

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("id", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(cycle, builder, writer, form, "fred", "");

        nested.close();

        writer.end();

        support.addInitializationScript(form, "dojo.require(\"tapestry.form\");");

        trainGetFocusField(delegate, null);

        expect(cycle.isFocusDisabled()).andReturn(false);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void test_Simple_Render_With_Scheme()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        ResponseBuilder builder = newMock(ResponseBuilder.class);
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        final IFormComponent component = newFormComponent("barney", "barney");

        IRender body = newComponentRenderBody(fs, component, nested);

        form.setBody(body);

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[]
                {"service"});
        trainGetParameterValues(link, "service", new String[]
                {"fred"});

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, "https", "https://foo.bar/app", 443);

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "https://foo.bar/app");

        writer.attribute("id", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(cycle, builder, writer, form, "fred", "barney");

        nested.close();

        writer.end();

        support.addInitializationScript(form, "dojo.require(\"tapestry.form\");");

        trainGetFocusField(delegate, "barney");

        expect(cycle.isFocusDisabled()).andReturn(false);

        // Side test: check for another form already grabbing focus

        trainGetFieldFocus(cycle, Boolean.TRUE);

        // support.addInitializationScript(form, "tapestry.form.focusField('barney');");

        // cycle.setAttribute(FormSupportImpl.FIELD_FOCUS_ATTRIBUTE, true);

        replay();

        fs.render("post", render, link, "https", new Integer(443));

        verify();
    }

    public void test_Simple_Rewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        MockForm form = new MockForm(delegate);
        ComponentEventInvoker invoker =
                org.easymock.classextension.EasyMock.createMock(ComponentEventInvoker.class);

        trainIsRewound(cycle, form, true);

        trainGetPageRenderSupport(cycle, null);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        delegate.clear();

        trainCycleForRewind(cycle, "barney", null);

        final IFormComponent component = newFormComponent("barney", "barney");

        IRender body = newComponentRenderBody(fs, component, writer);

        form.setBody(body);
        form.setEventInvoker(invoker);

        trainExtractBrowserEvent(cycle);

        invoker.invokeFormListeners(eq(fs), eq(cycle), isA(BrowserEvent.class));

        replay();

        assertEquals(FormConstants.SUBMIT_NORMAL, fs.rewind());

        verify();
    }

    public void test_Simple_Rewind_With_Deferred_Runnable()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        MockForm form = new MockForm(delegate);
        ComponentEventInvoker invoker =
                org.easymock.classextension.EasyMock.createMock(ComponentEventInvoker.class);

        trainIsRewound(cycle, form, true);

        trainGetPageRenderSupport(cycle, null);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        delegate.clear();

        trainCycleForRewind(cycle, "", null);
        trainExtractBrowserEvent(cycle);

        writer.print("DEFERRED");

        invoker.invokeFormListeners(eq(fs), eq(cycle), isA(BrowserEvent.class));

        replay();

        IRender body = new IRender() {

            public void render(final IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addDeferredRunnable(new Runnable() {
                    public void run()
                    {
                        pwriter.print("DEFERRED");
                    }

                });
            }

        };

        form.setBody(body);
        form.setEventInvoker(invoker);

        assertEquals(FormConstants.SUBMIT_NORMAL, fs.rewind());

        verify();
    }

    public void test_Simple_Submit_Event_Handler()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        ResponseBuilder builder = newMock(ResponseBuilder.class);
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = newFormSupport(writer, cycle, form);

        verify();

        trainCycleSeedEncoding(cycle);
        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[]
                {"service"});
        trainGetParameterValues(link, "service", new String[]
                {"fred"});

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("id", "myform");

        form.setBody(new IRender() {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit()");
            }
        });

        support.addInitializationScript(form, "Tapestry.onsubmit('myform', function (event)"
                                              + "\n{\n  mySubmit();\n});\n");

        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(cycle, builder, writer, form, "fred", "");

        nested.close();

        writer.end();

        support.addInitializationScript(form, "dojo.require(\"tapestry.form\");");

        trainGetFocusField(delegate, null);

        expect(cycle.isFocusDisabled()).andReturn(false);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    private void trainCycleForRewind(IRequestCycle cycle, String allocatedIds, String reservedIds)
    {
        trainCycleForRewind(cycle, "submit", allocatedIds, reservedIds);
    }

    private void trainCycleForRewind(IRequestCycle cycle, String submitMode, String allocatedIds,
                                     String reservedIds)
    {
        trainGetParameter(cycle, FormSupportImpl.SEED_IDS, "");
        cycle.initializeIdState("");

        trainGetParameter(cycle, FormSupportImpl.SUBMIT_MODE, submitMode);
        trainGetParameter(cycle, FormSupportImpl.FORM_IDS, allocatedIds);
        trainGetParameter(cycle, FormSupportImpl.RESERVED_FORM_IDS, reservedIds);
    }

    protected void trainDiv(IMarkupWriter writer, IForm form)
    {
        writer.begin("div");
        writer.attribute("style", "display:none;");
        writer.attribute("id", form.getName() + "hidden");
    }

    private void trainGetFieldFocus(IRequestCycle cycle, Object value)
    {
        expect(cycle.getAttribute(FormSupportImpl.FIELD_FOCUS_ATTRIBUTE)).andReturn(value);
    }

    private void trainGetFocusField(IValidationDelegate delegate, String fieldName)
    {
        expect(delegate.getFocusField()).andReturn(fieldName);
    }

    private void trainGetURL(ILink link, String scheme, String URL, int port)
    {
        // This will change shortly, with the new scheme parameter passed into FormSupport.render()

        expect(link.getURL(scheme, null, port, null, false)).andReturn(URL);
    }

    private void trainGetURL(ILink link, String scheme, String URL)
    {
        trainGetURL(link, scheme, URL, 0);
    }

    private void trainHidden(IMarkupWriter writer, String name, String value)
    {
        writer.beginEmpty("input");
        writer.attribute("type", "hidden");
        writer.attribute("name", name);
        writer.attribute("value", value);
        writer.println();
    }

    private void trainHidden(IMarkupWriter writer, String name, String id, String value)
    {
        writer.beginEmpty("input");
        writer.attribute("type", "hidden");
        writer.attribute("name", name);
        writer.attribute("id", id);
        writer.attribute("value", value);
        writer.println();
    }

    protected void trainHiddenBlock(IRequestCycle cycle, ResponseBuilder builder,
                                    IMarkupWriter writer, IForm form,
                                    String serviceName, String formIds)
    {
        expect(cycle.getResponseBuilder()).andReturn(builder);

        expect(builder.contains(form)).andReturn(false);

        trainDiv(writer, form);

        trainHidden(writer, "formids", formIds);
        trainHidden(writer, "seedids", "ENCODED");
        trainHidden(writer, "service", serviceName);
        trainHidden(writer, "submitmode", "");
        trainHidden(writer, FormConstants.SUBMIT_NAME_PARAMETER, "");

        writer.end();
    }

    protected void trainIsRewound(IRequestCycle cycle, IForm form, boolean isRewound)
    {
        expect(cycle.isRewound(form)).andReturn(isRewound);
    }

    private void trainRegister(PageRenderSupport support, IForm form, String formId)
    {
        support.addInitializationScript(form, "dojo.require(\"tapestry.form\");"
                                              + "tapestry.form.registerForm(\"" + formId + "\");");
    }
}
