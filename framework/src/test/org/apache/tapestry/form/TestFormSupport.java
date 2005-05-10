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
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.FormSupport;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.NestedMarkupWriter;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.StaleLinkException;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.valid.IValidationDelegate;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.FormSupportImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestFormSupport extends HiveMindTestCase
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
        MockControl componentc = newControl(IFormComponent.class);
        final IFormComponent component = (IFormComponent) componentc.getMock();

        component.getId();
        componentc.setReturnValue(id);

        component.setName(name);

        return component;
    }

    private IFormComponent newFormComponent(String id, String extendedId, Location location)
    {
        MockControl componentc = newControl(IFormComponent.class);
        final IFormComponent component = (IFormComponent) componentc.getMock();

        component.getId();
        componentc.setReturnValue(id);

        component.getExtendedId();
        componentc.setReturnValue(extendedId);

        component.getLocation();
        componentc.setReturnValue(location);

        return component;
    }

    private IValidationDelegate newDelegate()
    {
        return (IValidationDelegate) newMock(IValidationDelegate.class);
    }

    private IMarkupWriter newWriter()
    {
        return (IMarkupWriter) newMock(IMarkupWriter.class);
    }

    private NestedMarkupWriter newNestedWriter()
    {
        return (NestedMarkupWriter) newMock(NestedMarkupWriter.class);
    }

    public void testComplexRender()
    {
        MockControl writerc = newControl(IMarkupWriter.class);
        IMarkupWriter writer = (IMarkupWriter) writerc.getMock();

        NestedMarkupWriter nested = newNestedWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockForm form = new MockForm();

        cycle.isRewound(form);
        cyclec.setReturnValue(false);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        final IFormComponent barney1 = newFormComponent("barney", "barney");
        final IFormComponent wilma = newFormComponent("wilma", "wilma");
        final IFormComponent barney2 = newFormComponent("barney", "barney$0");

        IRender body = newComponentsRenderBody(fs, new IFormComponent[]
        { barney1, wilma, barney2 }, nested);

        form.setBody(body);

        MockControl linkc = newControl(ILink.class);
        ILink link = (ILink) linkc.getMock();

        IRender render = (IRender) newMock(IRender.class);

        link.getParameterNames();
        linkc.setReturnValue(new String[]
        { "service" });

        link.getParameterValues("service");
        linkc.setReturnValue(new String[]
        { "fred" });

        writer.getNestedWriter();
        writerc.setReturnValue(nested);

        link.getURL(null, false);
        linkc.setReturnValue("/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHidden(writer, "formids", "barney,wilma,barney$0");
        trainHidden(writer, "service", "fred");

        nested.close();

        writer.end();

        replayControls();

        fs.render("post", render, link);

        verifyControls();
    }

    public void testComplexRewind()
    {
        IMarkupWriter writer = newWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate);

        cycle.isRewound(form);
        cyclec.setReturnValue(true);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        delegate.clear();

        trainCycleForRewind(cyclec, cycle, "barney,wilma,barney$0", null);

        final IFormComponent barney1 = newFormComponent("barney", "barney");
        final IFormComponent wilma = newFormComponent("wilma", "wilma");
        final IFormComponent barney2 = newFormComponent("barney", "barney$0");

        IRender body = newComponentsRenderBody(fs, new IFormComponent[]
        { barney1, wilma, barney2 }, writer);

        form.setBody(body);

        replayControls();

        fs.rewind();

        verifyControls();
    }

    public void testComplexSubmitEventHandler()
    {
        MockControl writerc = newControl(IMarkupWriter.class);
        IMarkupWriter writer = (IMarkupWriter) writerc.getMock();

        NestedMarkupWriter nested = newNestedWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockForm form = new MockForm();

        cycle.isRewound(form);
        cyclec.setReturnValue(false);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit1");
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit2");
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit3");
            }
        });

        MockControl linkc = newControl(ILink.class);
        ILink link = (ILink) linkc.getMock();

        IRender render = (IRender) newMock(IRender.class);

        link.getParameterNames();
        linkc.setReturnValue(new String[]
        { "service" });

        link.getParameterValues("service");
        linkc.setReturnValue(new String[]
        { "fred" });

        writer.getNestedWriter();
        writerc.setReturnValue(nested);

        link.getURL(null, false);
        linkc.setReturnValue("/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHidden(writer, "formids", "");
        trainHidden(writer, "service", "fred");

        nested.close();

        writer.end();

        trainForPageSupport(
                cyclec,
                cycle,
                "document.myform.onsubmit = function ()\n{\n  return mySubmit1() &&\n    mySubmit2() &&\n    mySubmit3();\n}\n");

        replayControls();

        fs.render("post", render, link);

        verifyControls();
    }

    public void testEncodingType()
    {
        MockControl writerc = newControl(IMarkupWriter.class);
        IMarkupWriter writer = (IMarkupWriter) writerc.getMock();

        NestedMarkupWriter nested = newNestedWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockForm form = new MockForm();

        cycle.isRewound(form);
        cyclec.setReturnValue(false);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                fs.setEncodingType("foo/bar");
            }
        });

        MockControl linkc = newControl(ILink.class);
        ILink link = (ILink) linkc.getMock();

        IRender render = (IRender) newMock(IRender.class);

        link.getParameterNames();
        linkc.setReturnValue(new String[]
        { "service" });

        link.getParameterValues("service");
        linkc.setReturnValue(new String[]
        { "fred" });

        writer.getNestedWriter();
        writerc.setReturnValue(nested);

        link.getURL(null, false);
        linkc.setReturnValue("/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");
        writer.attribute("name", "myform");
        writer.attribute("enctype", "foo/bar");

        render.render(writer, cycle);

        writer.println();

        trainHidden(writer, "formids", "");
        trainHidden(writer, "service", "fred");

        nested.close();

        writer.end();

        replayControls();

        fs.render("post", render, link);

        verifyControls();
    }

    public void testHiddenValues()
    {
        MockControl writerc = newControl(IMarkupWriter.class);
        IMarkupWriter writer = (IMarkupWriter) writerc.getMock();

        NestedMarkupWriter nested = newNestedWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockForm form = new MockForm();

        cycle.isRewound(form);
        cyclec.setReturnValue(false);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                fs.addHiddenValue("hidden1", "value1");
                fs.addHiddenValue("hidden2", "id2", "value2");
            }
        });

        MockControl linkc = newControl(ILink.class);
        ILink link = (ILink) linkc.getMock();

        IRender render = (IRender) newMock(IRender.class);

        link.getParameterNames();
        linkc.setReturnValue(new String[]
        { "service" });

        link.getParameterValues("service");
        linkc.setReturnValue(new String[]
        { "fred" });

        writer.getNestedWriter();
        writerc.setReturnValue(nested);

        link.getURL(null, false);
        linkc.setReturnValue("/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHidden(writer, "formids", "");
        trainHidden(writer, "service", "fred");
        trainHidden(writer, "hidden1", "value1");
        trainHidden(writer, "hidden2", "id2", "value2");

        nested.close();

        writer.end();

        replayControls();

        fs.render("post", render, link);

        verifyControls();
    }

    public void testInvalidEncodingType()
    {
        MockControl writerc = newControl(IMarkupWriter.class);
        IMarkupWriter writer = (IMarkupWriter) writerc.getMock();

        NestedMarkupWriter nested = newNestedWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockForm form = new MockForm();

        cycle.isRewound(form);
        cyclec.setReturnValue(false);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                fs.setEncodingType("foo/bar");
                fs.setEncodingType("zip/zap");
            }
        });

        MockControl linkc = newControl(ILink.class);
        ILink link = (ILink) linkc.getMock();

        IRender render = (IRender) newMock(IRender.class);

        link.getParameterNames();
        linkc.setReturnValue(new String[]
        { "service" });

        link.getParameterValues("service");
        linkc.setReturnValue(new String[]
        { "fred" });

        writer.getNestedWriter();
        writerc.setReturnValue(nested);

        replayControls();

        try
        {
            fs.render("post", render, link);
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
        MockControl writerc = newControl(IMarkupWriter.class);
        IMarkupWriter writer = (IMarkupWriter) writerc.getMock();

        NestedMarkupWriter nested = newNestedWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockForm form = new MockForm();

        cycle.isRewound(form);
        cyclec.setReturnValue(false);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        final IFormComponent component = newFormComponent("action", "action$0");

        IRender body = newComponentRenderBody(fs, component, nested);

        form.setBody(body);

        MockControl linkc = newControl(ILink.class);
        ILink link = (ILink) linkc.getMock();

        IRender render = (IRender) newMock(IRender.class);

        link.getParameterNames();
        linkc.setReturnValue(new String[]
        { "action" });

        link.getParameterValues("action");
        linkc.setReturnValue(new String[]
        { "fred" });

        writer.getNestedWriter();
        writerc.setReturnValue(nested);

        link.getURL(null, false);
        linkc.setReturnValue("/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHidden(writer, "formids", "action$0");
        trainHidden(writer, "action", "fred");
        trainHidden(writer, "reservedids", "action");

        nested.close();

        writer.end();

        replayControls();

        fs.render("post", render, link);

        verifyControls();
    }

    public void testResetEventHandler()
    {
        MockControl writerc = newControl(IMarkupWriter.class);
        IMarkupWriter writer = (IMarkupWriter) writerc.getMock();

        NestedMarkupWriter nested = newNestedWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockForm form = new MockForm();

        cycle.isRewound(form);
        cyclec.setReturnValue(false);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                fs.addEventHandler(FormEventType.RESET, "myReset1");
                fs.addEventHandler(FormEventType.RESET, "myReset2");
            }
        });

        MockControl linkc = newControl(ILink.class);
        ILink link = (ILink) linkc.getMock();

        IRender render = (IRender) newMock(IRender.class);

        link.getParameterNames();
        linkc.setReturnValue(new String[]
        { "service" });

        link.getParameterValues("service");
        linkc.setReturnValue(new String[]
        { "fred" });

        writer.getNestedWriter();
        writerc.setReturnValue(nested);

        link.getURL(null, false);
        linkc.setReturnValue("/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHidden(writer, "formids", "");
        trainHidden(writer, "service", "fred");

        nested.close();

        writer.end();

        trainForPageSupport(
                cyclec,
                cycle,
                "document.myform.onreset = function ()\n{\n  myReset1();\n  myReset2();\n}\n");

        replayControls();

        fs.render("post", render, link);

        verifyControls();
    }

    public void testRewindExtraReservedIds()
    {
        IMarkupWriter writer = newWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate);

        cycle.isRewound(form);
        cyclec.setReturnValue(true);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        delegate.clear();

        trainCycleForRewind(cyclec, cycle, "action$0", "action");

        final IFormComponent component = newFormComponent("action", "action$0");

        IRender body = newComponentRenderBody(fs, component, writer);

        form.setBody(body);

        replayControls();

        fs.rewind();

        verifyControls();
    }

    public void testRewindMismatch()
    {

        IMarkupWriter writer = newWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate);

        cycle.isRewound(form);
        cyclec.setReturnValue(true);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        Location l = newLocation();

        delegate.clear();

        // So, the scenario here is that component "pebbles" was inside
        // some kind of conditional that evaluated to true during the render,
        // but is now false on the rewind.

        trainCycleForRewind(cyclec, cycle, "barney,wilma,pebbles,barney$0", null);

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
                    "Rewind of form SomePage/myform expected allocated id #3 to be 'pebbles', but was 'barney$0' (requested by component SomePage/barney).",
                    ex.getMessage());
            assertSame(barney2, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verifyControls();
    }

    public void testRewindTooLong()
    {
        IMarkupWriter writer = newWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate);

        cycle.isRewound(form);
        cyclec.setReturnValue(true);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        Location l = newLocation();

        delegate.clear();

        // So, the scenario here is that component "barney" was inside
        // some kind of loop that executed once on the render, but twice
        // on the rewind (i.e., an additional object was added in between).

        trainCycleForRewind(cyclec, cycle, "barney,wilma", null);

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

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate, l);

        cycle.isRewound(form);
        cyclec.setReturnValue(true);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        delegate.clear();

        // So, the scenario here is that component "barney" was inside
        // some kind of loop that executed twice on the render, but only once
        // on the rewind (i.e., the object was deleted in between).

        trainCycleForRewind(cyclec, cycle, "barney,wilma,barney$0", null);

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
        MockControl writerc = newControl(IMarkupWriter.class);
        IMarkupWriter writer = (IMarkupWriter) writerc.getMock();

        NestedMarkupWriter nested = newNestedWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockForm form = new MockForm();

        cycle.isRewound(form);
        cyclec.setReturnValue(false);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        final IFormComponent component = newFormComponent("barney", "barney");

        IRender body = newComponentRenderBody(fs, component, nested);

        form.setBody(body);

        MockControl linkc = newControl(ILink.class);
        ILink link = (ILink) linkc.getMock();

        IRender render = (IRender) newMock(IRender.class);

        link.getParameterNames();
        linkc.setReturnValue(new String[]
        { "service" });

        link.getParameterValues("service");
        linkc.setReturnValue(new String[]
        { "fred" });

        writer.getNestedWriter();
        writerc.setReturnValue(nested);

        link.getURL(null, false);
        linkc.setReturnValue("/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHidden(writer, "formids", "barney");
        trainHidden(writer, "service", "fred");

        nested.close();

        writer.end();

        replayControls();

        fs.render("post", render, link);

        verifyControls();
    }

    public void testSimpleRenderWithDeferredRunnable()
    {
        MockControl writerc = newControl(IMarkupWriter.class);
        IMarkupWriter writer = (IMarkupWriter) writerc.getMock();

        NestedMarkupWriter nested = newNestedWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockForm form = new MockForm();

        cycle.isRewound(form);
        cyclec.setReturnValue(false);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        IRender body = new IRender()
        {

            public void render(final IMarkupWriter writer, IRequestCycle cycle)
            {
                fs.addDeferredRunnable(new Runnable()
                {

                    public void run()
                    {
                        writer.print("DEFERRED");
                    }

                });
            }

        };

        form.setBody(body);

        MockControl linkc = newControl(ILink.class);
        ILink link = (ILink) linkc.getMock();

        IRender render = (IRender) newMock(IRender.class);

        link.getParameterNames();
        linkc.setReturnValue(new String[]
        { "service" });

        link.getParameterValues("service");
        linkc.setReturnValue(new String[]
        { "fred" });

        writer.getNestedWriter();
        writerc.setReturnValue(nested);

        link.getURL(null, false);
        linkc.setReturnValue("/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHidden(writer, "formids", "");
        trainHidden(writer, "service", "fred");

        // EasyMock can't fully verify that this gets called at the right moment, nor can we truly
        // prove (well, except by looking at the code), that the deferred runnables execute at the
        // right time.

        nested.print("DEFERRED");

        nested.close();

        writer.end();

        replayControls();

        fs.render("post", render, link);

        verifyControls();
    }

    public void testSimpleRewind()
    {
        IMarkupWriter writer = newWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate);

        cycle.isRewound(form);
        cyclec.setReturnValue(true);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        delegate.clear();

        trainCycleForRewind(cyclec, cycle, "barney", null);

        final IFormComponent component = newFormComponent("barney", "barney");

        IRender body = newComponentRenderBody(fs, component, writer);

        form.setBody(body);

        replayControls();

        fs.rewind();

        verifyControls();
    }

    public void testSimpleRewindWithDeferredRunnable()
    {
        IMarkupWriter writer = newWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        IValidationDelegate delegate = newDelegate();

        MockForm form = new MockForm(delegate);

        cycle.isRewound(form);
        cyclec.setReturnValue(true);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        delegate.clear();
        
        trainCycleForRewind(cyclec, cycle, "", null);

        writer.print("DEFERRED");

        replayControls();

        IRender body = new IRender()
        {

            public void render(final IMarkupWriter writer, IRequestCycle cycle)
            {
                fs.addDeferredRunnable(new Runnable()
                {
                    public void run()
                    {
                        writer.print("DEFERRED");
                    }

                });
            }

        };

        form.setBody(body);

        fs.rewind();

        verifyControls();
    }

    public void testSimpleSubmitEventHandler()
    {
        MockControl writerc = newControl(IMarkupWriter.class);
        IMarkupWriter writer = (IMarkupWriter) writerc.getMock();

        NestedMarkupWriter nested = newNestedWriter();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockForm form = new MockForm();

        cycle.isRewound(form);
        cyclec.setReturnValue(false);

        replayControls();

        final FormSupport fs = new FormSupportImpl(writer, cycle, form);

        verifyControls();

        form.setBody(new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                fs.addEventHandler(FormEventType.SUBMIT, "mySubmit");
            }
        });

        MockControl linkc = newControl(ILink.class);
        ILink link = (ILink) linkc.getMock();

        IRender render = (IRender) newMock(IRender.class);

        link.getParameterNames();
        linkc.setReturnValue(new String[]
        { "service" });

        link.getParameterValues("service");
        linkc.setReturnValue(new String[]
        { "fred" });

        writer.getNestedWriter();
        writerc.setReturnValue(nested);

        link.getURL(null, false);
        linkc.setReturnValue("/app");

        writer.begin("form");
        writer.attribute("method", "post");
        writer.attribute("action", "/app");

        writer.attribute("name", "myform");

        render.render(writer, cycle);

        writer.println();

        trainHidden(writer, "formids", "");
        trainHidden(writer, "service", "fred");

        nested.close();

        writer.end();

        trainForPageSupport(cyclec, cycle, "document.myform.onsubmit = mySubmit;\n");

        replayControls();

        fs.render("post", render, link);

        verifyControls();
    }

    private void trainCycleForRewind(MockControl cyclec, IRequestCycle cycle, String allocatedIds,
            String reservedIds)
    {
        cycle.getParameter(FormSupportImpl.FORM_IDS);
        cyclec.setReturnValue(allocatedIds);

        cycle.getParameter(FormSupportImpl.RESERVED_FORM_IDS);
        cyclec.setReturnValue(reservedIds);
    }

    private void trainForPageSupport(MockControl cyclec, IRequestCycle cycle, String initialization)
    {
        PageRenderSupport prs = (PageRenderSupport) newMock(PageRenderSupport.class);

        prs.addInitializationScript(initialization);

        cycle.getAttribute(TapestryUtils.PAGE_RENDER_SUPPORT_ATTRIBUTE);
        cyclec.setReturnValue(prs);
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