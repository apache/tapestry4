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

import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.NestedMarkupWriter;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.StaleLinkException;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.listener.ListenerInvoker;
import org.apache.tapestry.services.impl.ComponentEventInvoker;
import org.apache.tapestry.valid.IValidationDelegate;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.FormSupportImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class FormSupportTest extends BaseComponentTestCase
{

    private IRender newComponentRenderBody(final FormSupport fs, final IFormComponent component,
            final IMarkupWriter nested)
    {
        return newComponentsRenderBody(fs, new IFormComponent[]
        { component }, nested);
    }

    private IRender newComponentsRenderBody(final FormSupport fs, final IFormComponent[] component,
            final IMarkupWriter nested)
    {
        return new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                assertEquals("Writer argument must be nested instance.", nested, writer);

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
        
        trainGetId(component, id);

        component.setName(name);

        return component;
    }

    private IFormComponent newFormComponent(String id, String extendedId, Location location)
    {
        IFormComponent component = newMock(IFormComponent.class);

        trainGetId(component, id);
        trainGetExtendedId(component, extendedId);
        trainGetLocation(component, location);

        return component;
    }

    public void testCancelRewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);

        trainGetPageRenderSupport(cycle, null);

        replay();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

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

    public void testComplexRender()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

        final IFormComponent barney1 = newFormComponent("barney", "barney");
        final IFormComponent wilma = newFormComponent("wilma", "wilma");
        final IFormComponent barney2 = newFormComponent("barney", "barney_0");

        IRender body = newComponentsRenderBody(fs, new IFormComponent[]
        { barney1, wilma, barney2 }, nested);

        form.setBody(body);

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[]
        { "service" });
        trainGetParameterValues(link, "service", new String[]
        { "fred" });

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");
        writer.attribute("id", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(writer, form, "fred", "barney,wilma,barney_0");

        nested.close();

        writer.end();

        trainGetFocusField(delegate, "wilma");
        trainGetFieldFocus(cycle, null);

        trainFocus(support, form);

        trainSetFieldFocus(cycle);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void testComplexRewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        MockForm form = new MockForm(delegate);
        ListenerInvoker listenerInvoker = newMock(ListenerInvoker.class);
        
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        invoker.setListenerInvoker(listenerInvoker);
        
        trainIsRewound(cycle, form, true);
        trainGetPageRenderSupport(cycle, null);
        
        replay();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);
        
        verify();

        delegate.clear();
        
        trainCycleForRewind(cycle, "barney,wilma,barney_0", null);

        final IFormComponent barney1 = newFormComponent("barney", "barney");
        final IFormComponent wilma = newFormComponent("wilma", "wilma");
        final IFormComponent barney2 = newFormComponent("barney", "barney_0");

        IRender body = newComponentsRenderBody(fs, new IFormComponent[]
        { barney1, wilma, barney2 }, writer);

        form.setBody(body);
        form.setEventInvoker(invoker);
        
        trainExtractBrowserEvent(cycle);
        
        replay();
        
        invoker.invokeFormListeners(fs, cycle, new BrowserEvent(null, null));
        
        assertEquals(FormConstants.SUBMIT_NORMAL, fs.rewind());

        verify();
    }

    public void testComplexSubmitEventHandler()
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

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();
        
        form.setBody(new IRender()
        {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit1");
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit2");
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit3");
            }
        });

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[]
        { "service" });

        trainGetParameterValues(link, "service", new String[]
        { "fred" });

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");
        writer.attribute("id", "myform");
        
        support.addInitializationScript("Tapestry.onsubmit('myform', function (event)"
                + "\n{\n  mySubmit1();\n  mySubmit2();\n  mySubmit3();\n});\n");
        
        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(writer, form, "fred", "");

        nested.close();

        writer.end();
        
        // Side test: what if no focus field?

        trainGetFocusField(delegate, null);
        
        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void testEncodingType()
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

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.setEncodingType("foo/bar");
            }
        });

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[]
        { "service" });

        trainGetParameterValues(link, "service", new String[]
        { "fred" });

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");
        writer.attribute("name", "myform");
        writer.attribute("id", "myform");
        writer.attribute("enctype", "foo/bar");

        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(writer, form, "fred", "");

        nested.close();

        writer.end();

        trainGetFocusField(delegate, null);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void testFieldPrerenderTwice()
    {
        IFormComponent field = newField();
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        Location l = newLocation();

        trainGetExtendedId(field, "foo.bar");

        trainGetNestedWriter(writer, nested);

        field.render(nested, cycle);

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
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Field EasyMock for interface org.apache.tapestry.form.IFormComponent has already been pre-rendered. "
                            + "This exception may indicate that a FieldLabel rendered, but the corresponding field did not.",
                    ex.getMessage());

            assertSame(l, ex.getLocation());
            assertSame(field, ex.getComponent());
        }

        verify();

    }

    public void testHiddenValues()
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

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addHiddenValue("hidden1", "value1");
                fs.addHiddenValue("hidden2", "id2", "value2");
            }
        });

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[]
        { "service" });

        trainGetParameterValues(link, "service", new String[]
        { "fred" });

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");
        writer.attribute("id", "myform");

        render.render(writer, cycle);

        writer.println();

        trainDiv(writer, form);

        trainHidden(writer, "formids", "");
        trainHidden(writer, "service", "fred");
        trainHidden(writer, "submitmode", "");
        trainHidden(writer, FormConstants.SUBMIT_NAME_PARAMETER, "");
        trainHidden(writer, "hidden1", "value1");
        trainHidden(writer, "hidden2", "id2", "value2");

        writer.end();

        nested.close();

        writer.end();

        trainGetFocusField(delegate, null);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void testInvalidEncodingType()
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

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.setEncodingType("foo/bar");
                fs.setEncodingType("zip/zap");
            }
        });

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[]
        { "service" });

        trainGetParameterValues(link, "service", new String[]
        { "fred" });

        trainGetNestedWriter(writer, nested);

        replay();

        try
        {
            fs.render("post", render, link, null, null);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Components within form SomePage/myform have requested conflicting encoding types 'foo/bar' and 'zip/zap'.",
                    ex.getMessage());
            assertSame(form, ex.getComponent());
        }

        verify();
    }

    public void testRefreshRewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        MockForm form = new MockForm(delegate);
        ComponentEventInvoker invoker = org.easymock.classextension.EasyMock.createMock(ComponentEventInvoker.class);
        
        trainIsRewound(cycle, form, true);

        trainGetPageRenderSupport(cycle, null);

        replay();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

        delegate.clear();

        trainCycleForRewind(cycle, "refresh", "barney", null);

        final IFormComponent component = newFormComponent("barney", "barney");

        IRender body = newComponentRenderBody(fs, component, writer);

        form.setBody(body);
        form.setEventInvoker(invoker);
        
        trainExtractBrowserEvent(cycle);
        
        invoker.invokeFormListeners(eq(fs), eq(cycle), isA(BrowserEvent.class));
        
        replay();

        assertEquals(FormConstants.SUBMIT_REFRESH, fs.rewind());

        verify();
    }

    public void testRenderExtraReservedIds()
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

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

        final IFormComponent component = newFormComponent("action", "action_0");

        IRender body = newComponentRenderBody(fs, component, nested);

        form.setBody(body);

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[]
        { "action" });

        trainGetParameterValues(link, "action", new String[]
        { "fred" });

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");
        writer.attribute("id", "myform");

        render.render(writer, cycle);

        writer.println();

        trainDiv(writer, form);

        trainHidden(writer, "formids", "action_0");
        trainHidden(writer, "action", "fred");
        trainHidden(writer, "reservedids", "action");
        trainHidden(writer, "submitmode", "");
        trainHidden(writer, FormConstants.SUBMIT_NAME_PARAMETER, "");

        writer.end();

        nested.close();

        writer.end();

        trainGetFocusField(delegate, null);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void testResetEventHandler()
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

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addEventHandler(FormEventType.RESET, "myReset1");
                fs.addEventHandler(FormEventType.RESET, "myReset2");
            }
        });

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[]
        { "service" });

        trainGetParameterValues(link, "service", new String[]
        { "fred" });

        trainGetNestedWriter(writer, nested);
        
        trainGetURL(link, null, "/app");
        
        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");
        writer.attribute("id", "myform");

        support.addInitializationScript("Tapestry.onreset('myform', function (event)"
                + "\n{\n  myReset1();\n  myReset2();\n});\n");
        
        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(writer, form, "fred", "");

        nested.close();

        writer.end();

        trainGetFocusField(delegate, null);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void testRewindExtraReservedIds()
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

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

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

    public void testRewindMismatch()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);
        trainGetPageRenderSupport(cycle, null);

        replay();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

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
        { barney1, wilma, barney2 }, writer);

        form.setBody(body);

        replay();

        try
        {
            fs.rewind();
            unreachable();
        }
        catch (StaleLinkException ex)
        {
            assertEquals(
                    "Rewind of form SomePage/myform expected allocated id #3 to be 'pebbles', but was 'barney_0' (requested by component SomePage/barney).",
                    ex.getMessage());
            assertSame(barney2, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void testRewindTooLong()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);
        trainGetPageRenderSupport(cycle, null);

        replay();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

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
        { barney1, wilma, barney2 }, writer);

        form.setBody(body);

        replay();

        try
        {
            fs.rewind();
            unreachable();
        }
        catch (StaleLinkException ex)
        {
            assertEquals(
                    "Rewind of form SomePage/myform expected only 2 form elements, but an additional id was requested by component SomePage/barney.",
                    ex.getMessage());
            assertSame(barney2, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void testRewindTooShort()
    {
        Location l = newLocation();
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        ComponentEventInvoker invoker = 
            org.easymock.classextension.EasyMock.createMock(ComponentEventInvoker.class);
        
        MockForm form = new MockForm(delegate, l);

        trainIsRewound(cycle, form, true);
        trainGetPageRenderSupport(cycle, null);

        replay();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

        delegate.clear();

        // So, the scenario here is that component "barney" was inside
        // some kind of loop that executed twice on the render, but only once
        // on the rewind (i.e., the object was deleted in between).

        trainCycleForRewind(cycle, "barney,wilma,barney$0", null);

        final IFormComponent barney1 = newFormComponent("barney", "barney");
        final IFormComponent wilma = newFormComponent("wilma", "wilma");

        IRender body = newComponentsRenderBody(fs, new IFormComponent[]
        { barney1, wilma }, writer);

        form.setBody(body);
        form.setEventInvoker(invoker);
        
        trainExtractBrowserEvent(cycle);
        
        invoker.invokeFormListeners(eq(fs), eq(cycle), isA(BrowserEvent.class));
        
        replay();

        try
        {
            fs.rewind();
            unreachable();
        }
        catch (StaleLinkException ex)
        {
            assertEquals(
                    "Rewind of form SomePage/myform expected 1 more form elements, starting with id 'barney$0'.",
                    ex.getMessage());
            assertSame(form, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void testSimpleRender()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

        final IFormComponent component = newFormComponent("barney", "barney");

        IRender body = newComponentRenderBody(fs, component, nested);

        form.setBody(body);

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[]
        { "service" });
        trainGetParameterValues(link, "service", new String[]
        { "fred" });

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");
        writer.attribute("id", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(writer, form, "fred", "barney");

        nested.close();

        writer.end();

        trainGetFocusField(delegate, "barney");

        // Side test: check for another form already grabbing focus

        trainGetFieldFocus(cycle, Boolean.TRUE);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void testSimpleRenderWithDeferredRunnable()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

        IRender body = new IRender()
        {
            public void render(final IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addDeferredRunnable(new Runnable()
                {

                    public void run()
                    {
                        pwriter.print("DEFERRED");
                    }

                });
            }

        };
        
        form.setBody(body);

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[]
        { "service" });
        trainGetParameterValues(link, "service", new String[]
        { "fred" });

        trainGetNestedWriter(writer, nested);
        
        nested.print("DEFERRED");
        
        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");
        writer.attribute("id", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(writer, form, "fred", "");
        
        nested.close();

        writer.end();

        trainGetFocusField(delegate, null);

        replay();

        fs.render("post", render, link, null, null);

        verify();
    }

    public void testSimpleRenderWithScheme()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();
        
        MockForm form = new MockForm(delegate);
        
        trainIsRewound(cycle, form, false);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);
        
        replay();
        
        final FormSupport fs = new FormSupportImpl(writer, cycle, form);
        
        verify();

        final IFormComponent component = newFormComponent("barney", "barney");

        IRender body = newComponentRenderBody(fs, component, nested);

        form.setBody(body);

        trainRegister(support, form, "myform");

        trainGetParameterNames(link, new String[]
        { "service" });
        trainGetParameterValues(link, "service", new String[]
        { "fred" });

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, "https", "https://foo.bar/app", 443);

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "https://foo.bar/app");

        writer.attribute("name", "myform");
        writer.attribute("id", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(writer, form, "fred", "barney");

        nested.close();

        writer.end();

        trainGetFocusField(delegate, "barney");

        // Side test: check for another form already grabbing focus

        trainGetFieldFocus(cycle, Boolean.TRUE);

        replay();

        fs.render("post", render, link, "https", new Integer(443));

        verify();
    }

    public void testSimpleRewind()
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

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

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

    public void testSimpleRewindWithDeferredRunnable()
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

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();

        delegate.clear();

        trainCycleForRewind(cycle, "", null);

        trainExtractBrowserEvent(cycle);
        
        writer.print("DEFERRED");
        
        invoker.invokeFormListeners(eq(fs), eq(cycle), isA(BrowserEvent.class));
        
        replay();

        IRender body = new IRender()
        {

            public void render(final IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addDeferredRunnable(new Runnable()
                {
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

    public void testSimpleSubmitEventHandler()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);

        replay();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verify();
        
        trainRegister(support, form, "myform");
        
        trainGetParameterNames(link, new String[]
        { "service" });
        trainGetParameterValues(link, "service", new String[]
        { "fred" });
        
        trainGetNestedWriter(writer, nested);
        
        trainGetURL(link, null, "/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");
        
        writer.attribute("name", "myform");
        writer.attribute("id", "myform");
        
        form.setBody(new IRender()
        {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit()");
            }
        });
        
        support.addInitializationScript("Tapestry.onsubmit('myform', function (event)"
                + "\n{\n  mySubmit();\n});\n");
        
        render.render(writer, cycle);
        
        writer.println();

        trainHiddenBlock(writer, form, "fred", "");

        nested.close();

        writer.end();

        trainGetFocusField(delegate, null);

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

    private void trainFocus(PageRenderSupport support, IForm form)
    {
        support.addInitializationScript(form, "tapestry.form.focusField('wilma');");
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

    protected void trainHiddenBlock(IMarkupWriter writer, IForm form, String serviceName, String formIds)
    {
        trainDiv(writer, form);
        
        trainHidden(writer, "formids", formIds);
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
                + "tapestry.form.registerForm('" + formId + "');");
    }

    private void trainSetFieldFocus(IRequestCycle cycle)
    {
        cycle.setAttribute(FormSupportImpl.FIELD_FOCUS_ATTRIBUTE, Boolean.TRUE);
    }
}