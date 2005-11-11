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
import org.apache.hivemind.util.ClasspathResource;
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
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * Tests for {@link org.apache.tapestry.form.FormSupportImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
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

    private IFormComponent newFormComponent(String id, String name)
    {
        IFormComponent component = (IFormComponent) newMock(IFormComponent.class);

        component.getId();
        setReturnValue(component, id);

        component.setName(name);

        return component;
    }

    private IFormComponent newFormComponent(String id, String extendedId, Location location)
    {
        IFormComponent component = (IFormComponent) newMock(IFormComponent.class);

        component.getId();
        setReturnValue(component, id);

        component.getExtendedId();
        setReturnValue(component, extendedId);

        component.getLocation();
        setReturnValue(component, location);

        return component;
    }

    private IValidationDelegate newDelegate()
    {
        return (IValidationDelegate) newMock(IValidationDelegate.class);
    }

    public void testComplexRender()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        trainGetEngine(cycle, engine);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        final IFormComponent barney1 = newFormComponent("barney", "barney");
        final IFormComponent wilma = newFormComponent("wilma", "wilma");
        final IFormComponent barney2 = newFormComponent("barney", "barney_0");

        IRender body = newComponentsRenderBody(fs, new IFormComponent[]
        { barney1, wilma, barney2 }, nested);

        form.setBody(body);

        trainRegister(support);

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

        trainHiddenBlock(writer, "fred", "barney,wilma,barney_0");

        nested.close();

        writer.end();

        trainGetFocusField(delegate, "wilma");
        trainGetFieldFocus(cycle, null);

        trainFocus(support);

        trainSetFieldFocus(cycle);

        replayControls();

        fs.render("post", render, link, null);

        verifyControls();
    }

    protected void trainHiddenBlock(IMarkupWriter writer, String serviceName, String formIds)
    {
        trainDiv(writer);

        trainHidden(writer, "formids", formIds);
        trainHidden(writer, "service", serviceName);
        trainHidden(writer, "submitmode", "");
        trainHidden(writer, FormConstants.SUBMIT_NAME_PARAMETER, "");

        writer.end();
    }

    protected void trainDiv(IMarkupWriter writer)
    {
        writer.begin("div");
        writer.attribute("style", "display:none;");
    }

    protected void trainIsRewound(IRequestCycle cycle, IForm form, boolean isRewound)
    {
        cycle.isRewound(form);
        setReturnValue(cycle, isRewound);
    }

    protected IEngine newEngine()
    {
        return (IEngine) newMock(IEngine.class);
    }

    private void trainRegister(PageRenderSupport support)
    {
        support.addExternalScript(new ClasspathResource(getClassResolver(),
                "/org/apache/tapestry/form/Form.js"));

        support.addInitializationScript("Tapestry.register_form('myform');");
    }

    private void trainFocus(PageRenderSupport support)
    {
        support.addInitializationScript("Tapestry.set_focus('wilma');");
    }

    private void trainSetFieldFocus(IRequestCycle cycle)
    {
        cycle.setAttribute(FormSupportImpl.FIELD_FOCUS_ATTRIBUTE, Boolean.TRUE);
    }

    private void trainGetFieldFocus(IRequestCycle cycle, Object value)
    {
        cycle.getAttribute(FormSupportImpl.FIELD_FOCUS_ATTRIBUTE);
        setReturnValue(cycle, value);
    }

    private void trainGetFocusField(IValidationDelegate delegate, String fieldName)
    {
        delegate.getFocusField();
        setReturnValue(delegate, fieldName);
    }

    public void testComplexRewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        IEngine engine = newEngine(getClassResolver());
        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);
        trainGetEngine(cycle, engine);
        trainGetPageRenderSupport(cycle, null);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        delegate.clear();

        trainCycleForRewind(cycle, "barney,wilma,barney_0", null);

        final IFormComponent barney1 = newFormComponent("barney", "barney");
        final IFormComponent wilma = newFormComponent("wilma", "wilma");
        final IFormComponent barney2 = newFormComponent("barney", "barney_0");

        IRender body = newComponentsRenderBody(fs, new IFormComponent[]
        { barney1, wilma, barney2 }, writer);

        form.setBody(body);

        replayControls();

        assertEquals(FormConstants.SUBMIT_NORMAL, fs.rewind());

        verifyControls();
    }

    public void testComplexSubmitEventHandler()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();
        PageRenderSupport support = newPageRenderSupport();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);
        trainGetEngine(cycle, engine);
        trainGetPageRenderSupport(cycle, support);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit1");
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit2");
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit3");
            }
        });

        trainRegister(support);

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

        trainHiddenBlock(writer, "fred", "");

        nested.close();

        writer.end();

        support
                .addInitializationScript("Tapestry.onsubmit('myform', function (event)\n{\n  mySubmit1();\n  mySubmit2();\n  mySubmit3();\n});\n");

        // Side test: what if no focus field?

        trainGetFocusField(delegate, null);

        replayControls();

        fs.render("post", render, link, null);

        verifyControls();
    }

    private void trainGetURL(ILink link, String scheme, String URL)
    {
        // This will change shortly, with the new scheme parameter passed into FormSupport.render()

        link.getURL(scheme, null, 0, null, false);
        setReturnValue(link, URL);
    }

    public void testEncodingType()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();
        PageRenderSupport support = newPageRenderSupport();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);
        trainGetEngine(cycle, engine);

        trainGetPageRenderSupport(cycle, support);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.setEncodingType("foo/bar");
            }
        });

        trainRegister(support);

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

        trainHiddenBlock(writer, "fred", "");

        nested.close();

        writer.end();

        trainGetFocusField(delegate, null);

        replayControls();

        fs.render("post", render, link, null);

        verifyControls();
    }

    public void testHiddenValues()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();
        PageRenderSupport support = newPageRenderSupport();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        trainGetEngine(cycle, engine);

        trainGetPageRenderSupport(cycle, support);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addHiddenValue("hidden1", "value1");
                fs.addHiddenValue("hidden2", "id2", "value2");
            }
        });

        trainRegister(support);

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

        trainDiv(writer);

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

        replayControls();

        fs.render("post", render, link, null);

        verifyControls();
    }

    public void testInvalidEncodingType()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();
        PageRenderSupport support = newPageRenderSupport();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        trainGetEngine(cycle, engine);

        trainGetPageRenderSupport(cycle, support);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.setEncodingType("foo/bar");
                fs.setEncodingType("zip/zap");
            }
        });

        trainRegister(support);

        trainGetParameterNames(link, new String[]
        { "service" });

        trainGetParameterValues(link, "service", new String[]
        { "fred" });

        trainGetNestedWriter(writer, nested);

        replayControls();

        try
        {
            fs.render("post", render, link, null);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Components within form SomePage/myform have requested conflicting encoding types 'foo/bar' and 'zip/zap'.",
                    ex.getMessage());
            assertSame(form, ex.getComponent());
        }

        verifyControls();
    }

    public void testRenderExtraReservedIds()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();
        PageRenderSupport support = newPageRenderSupport();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        trainGetEngine(cycle, engine);

        trainGetPageRenderSupport(cycle, support);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        final IFormComponent component = newFormComponent("action", "action_0");

        IRender body = newComponentRenderBody(fs, component, nested);

        form.setBody(body);

        trainRegister(support);

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

        trainDiv(writer);

        trainHidden(writer, "formids", "action_0");
        trainHidden(writer, "action", "fred");
        trainHidden(writer, "reservedids", "action");
        trainHidden(writer, "submitmode", "");
        trainHidden(writer, FormConstants.SUBMIT_NAME_PARAMETER, "");

        writer.end();

        nested.close();

        writer.end();

        trainGetFocusField(delegate, null);

        replayControls();

        fs.render("post", render, link, null);

        verifyControls();
    }

    public void testResetEventHandler()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();
        PageRenderSupport support = newPageRenderSupport();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        trainGetEngine(cycle, engine);

        trainGetPageRenderSupport(cycle, support);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addEventHandler(FormEventType.RESET, "myReset1");
                fs.addEventHandler(FormEventType.RESET, "myReset2");
            }
        });

        trainRegister(support);

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

        trainHiddenBlock(writer, "fred", "");

        nested.close();

        writer.end();

        support
                .addInitializationScript("Tapestry.onreset('myform', function (event)\n{\n  myReset1();\n  myReset2();\n});\n");

        trainGetFocusField(delegate, null);

        replayControls();

        fs.render("post", render, link, null);

        verifyControls();
    }

    public void testRewindExtraReservedIds()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);

        trainGetEngine(cycle, engine);

        trainGetPageRenderSupport(cycle, null);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        delegate.clear();

        trainCycleForRewind(cycle, "action_0", "action");

        final IFormComponent component = newFormComponent("action", "action_0");

        IRender body = newComponentRenderBody(fs, component, writer);

        form.setBody(body);

        replayControls();

        assertEquals(FormConstants.SUBMIT_NORMAL, fs.rewind());

        verifyControls();
    }

    public void testRewindMismatch()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);
        trainGetEngine(cycle, engine);
        trainGetPageRenderSupport(cycle, null);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

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

        replayControls();

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

        verifyControls();
    }

    public void testRewindTooLong()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);
        trainGetEngine(cycle, engine);
        trainGetPageRenderSupport(cycle, null);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

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

        replayControls();

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

        verifyControls();
    }

    public void testRewindTooShort()
    {
        Location l = newLocation();
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate, l);

        trainIsRewound(cycle, form, true);
        trainGetEngine(cycle, engine);
        trainGetPageRenderSupport(cycle, null);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

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

        replayControls();

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

        verifyControls();
    }

    public void testSimpleRender()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        trainGetEngine(cycle, engine);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        final IFormComponent component = newFormComponent("barney", "barney");

        IRender body = newComponentRenderBody(fs, component, nested);

        form.setBody(body);

        trainRegister(support);

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

        trainHiddenBlock(writer, "fred", "barney");

        nested.close();

        writer.end();

        trainGetFocusField(delegate, "barney");

        // Side test: check for another form already grabbing focus

        trainGetFieldFocus(cycle, Boolean.TRUE);

        replayControls();

        fs.render("post", render, link, null);

        verifyControls();
    }

    public void testSimpleRenderWithScheme()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        trainGetEngine(cycle, engine);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        final IFormComponent component = newFormComponent("barney", "barney");

        IRender body = newComponentRenderBody(fs, component, nested);

        form.setBody(body);

        trainRegister(support);

        trainGetParameterNames(link, new String[]
        { "service" });
        trainGetParameterValues(link, "service", new String[]
        { "fred" });

        trainGetNestedWriter(writer, nested);

        trainGetURL(link, "https", "https://foo.bar/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "https://foo.bar/app");

        writer.attribute("name", "myform");
        writer.attribute("id", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHiddenBlock(writer, "fred", "barney");

        nested.close();

        writer.end();

        trainGetFocusField(delegate, "barney");

        // Side test: check for another form already grabbing focus

        trainGetFieldFocus(cycle, Boolean.TRUE);

        replayControls();

        fs.render("post", render, link, "https");

        verifyControls();
    }

    public void testSimpleRenderWithDeferredRunnable()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        trainGetEngine(cycle, engine);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

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

        trainRegister(support);

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

        trainHiddenBlock(writer, "fred", "");

        // EasyMock can't fully verify that this gets called at the right moment, nor can we truly
        // prove (well, except by looking at the code), that the deferred runnables execute at the
        // right time.

        nested.print("DEFERRED");

        nested.close();

        writer.end();

        trainGetFocusField(delegate, null);

        replayControls();

        fs.render("post", render, link, null);

        verifyControls();
    }

    public void testSimpleRewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        IEngine engine = newEngine(getClassResolver());
        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);

        trainGetEngine(cycle, engine);

        trainGetPageRenderSupport(cycle, null);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        delegate.clear();

        trainCycleForRewind(cycle, "barney", null);

        final IFormComponent component = newFormComponent("barney", "barney");

        IRender body = newComponentRenderBody(fs, component, writer);

        form.setBody(body);

        replayControls();

        assertEquals(FormConstants.SUBMIT_NORMAL, fs.rewind());

        verifyControls();
    }

    public void testRefreshRewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        IEngine engine = newEngine(getClassResolver());
        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);

        trainGetEngine(cycle, engine);

        trainGetPageRenderSupport(cycle, null);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        delegate.clear();

        trainCycleForRewind(cycle, "refresh", "barney", null);

        final IFormComponent component = newFormComponent("barney", "barney");

        IRender body = newComponentRenderBody(fs, component, writer);

        form.setBody(body);

        replayControls();

        assertEquals(FormConstants.SUBMIT_REFRESH, fs.rewind());

        verifyControls();
    }

    public void testCancelRewind()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        IEngine engine = newEngine(getClassResolver());
        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);

        trainGetEngine(cycle, engine);

        trainGetPageRenderSupport(cycle, null);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        delegate.clear();

        trainGetParameter(cycle, FormSupportImpl.SUBMIT_MODE, "cancel");

        // Create a body, just to provie it doesn't get invoked.

        IRender body = (IRender) newMock(IRender.class);

        form.setBody(body);

        replayControls();

        assertEquals(FormConstants.SUBMIT_CANCEL, fs.rewind());

        verifyControls();
    }

    public void testSimpleRewindWithDeferredRunnable()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IValidationDelegate delegate = newDelegate();
        IEngine engine = newEngine(getClassResolver());
        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, true);

        trainGetEngine(cycle, engine);

        trainGetPageRenderSupport(cycle, null);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        delegate.clear();

        trainCycleForRewind(cycle, "", null);

        writer.print("DEFERRED");

        replayControls();

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

        assertEquals(FormConstants.SUBMIT_NORMAL, fs.rewind());

        verifyControls();
    }

    public void testSimpleSubmitEventHandler()
    {
        IMarkupWriter writer = newWriter();
        NestedMarkupWriter nested = newNestedWriter();
        IRequestCycle cycle = newCycle();
        IEngine engine = newEngine(getClassResolver());
        IValidationDelegate delegate = newDelegate();
        ILink link = newLink();
        IRender render = newRender();

        MockForm form = new MockForm(delegate);

        trainIsRewound(cycle, form, false);

        trainGetEngine(cycle, engine);

        PageRenderSupport support = newPageRenderSupport();

        trainGetPageRenderSupport(cycle, support);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter pwriter, IRequestCycle pcycle)
            {
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit()");
            }
        });

        trainRegister(support);

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

        trainHiddenBlock(writer, "fred", "");

        nested.close();

        writer.end();

        support
                .addInitializationScript("Tapestry.onsubmit('myform', function (event)\n{\n  mySubmit();\n});\n");

        trainGetFocusField(delegate, null);

        replayControls();

        fs.render("post", render, link, null);

        verifyControls();
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
}