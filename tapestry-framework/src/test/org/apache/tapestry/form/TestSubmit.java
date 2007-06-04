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

import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.*;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.listener.ListenerInvokerTerminator;
import org.apache.tapestry.test.Creator;
import org.apache.tapestry.util.ScriptUtils;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationConstants;
import static org.easymock.EasyMock.*;
import org.testng.annotations.Test;

import java.util.*;

/**
 * Tests for {@link org.apache.tapestry.form.Submit}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestSubmit extends BaseFormComponentTestCase
{
    public void test_Prerendered()
    {
        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class);

        IForm form = newMock(IForm.class);
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, true);

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void test_Render()
    {
        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class, new Object[] {"submitType", "submit"});

        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, false);

        trainIsRewinding(cycle, false);
        
        form.setFormFieldUpdating(true);
        
        writer.beginEmpty("input");
        writer.attribute("type", "submit");
        writer.attribute("name", "fred");
        writer.attribute("id", "fred");
        writer.closeTag();

        trainIsInError(delegate, false);

        delegate.registerForFocus(submit, ValidationConstants.NORMAL_FIELD);

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void test_Render_Function_Hash()
    {
        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class, new Object[] {"submitType", "submit"});
        
        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, false);

        trainIsRewinding(cycle, false);
        
        form.setFormFieldUpdating(true);
        
        writer.beginEmpty("input");
        writer.attribute("type", "submit");
        writer.attribute("name", "fred");
        writer.attribute("id", "fred");
        writer.closeTag();

        trainIsInError(delegate, false);

        delegate.registerForFocus(submit, ValidationConstants.NORMAL_FIELD);

        replay();

        submit.renderComponent(writer, cycle);
        
        String hash = ScriptUtils.functionHash("onchange" + submit.hashCode());
        
        assertEquals(ScriptUtils.functionHash("onchange" + submit.hashCode()), hash);
        
        verify();
    }
    
    public void test_Render_Disabled()
    {
        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class, new Object[]
        { "disabled", Boolean.TRUE, "submitType", "submit" });

        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        trainGetDelegate(form, delegate);

        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, false);

        trainIsRewinding(cycle, false);

        form.setFormFieldUpdating(true);
        
        writer.beginEmpty("input");
        writer.attribute("type", "submit");
        writer.attribute("name", "fred");
        writer.attribute("disabled", "disabled");
        writer.attribute("id", "fred");
        writer.closeTag();

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void test_Render_With_Label()
    {
        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class, new Object[]
        { "label", "flintstone", "submitType", "submit" });

        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        trainGetDelegate(form, delegate);

        delegate.setFormComponent(submit);
        
        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, false);
        
        trainIsRewinding(cycle, false);

        form.setFormFieldUpdating(true);
        
        writer.beginEmpty("input");
        writer.attribute("type", "submit");
        writer.attribute("name", "fred");
        writer.attribute("value", "flintstone");
        writer.attribute("id", "fred");
        writer.closeTag();

        trainIsInError(delegate, false);

        delegate.registerForFocus(submit, ValidationConstants.NORMAL_FIELD);

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void test_SubmitType_OnClick()
    {
        IScript script = newMock(IScript.class);
        Submit submit = newInstance(Submit.class, 
                new Object[] {"submitType", "cancel", "submitScript", script});
        
        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, false);

        trainIsRewinding(cycle, false);

        form.setFormFieldUpdating(true);
        
        writer.beginEmpty("input");
        writer.attribute("type", "submit");
        writer.attribute("name", "fred");
        writer.attribute("id", "fred");
        
        expect(form.getClientId()).andReturn("formtest");
        
        writer.attribute("onClick", "tapestry.form.cancel('formtest','fred')");
        
        writer.closeTag();

        trainIsInError(delegate, false);

        delegate.registerForFocus(submit, ValidationConstants.NORMAL_FIELD);

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }
    
    public void test_SubmitType_OnClick_Disabled()
    {
        Submit submit = newInstance(Submit.class, new Object[] {
            "disabled", Boolean.TRUE
            });
        
        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, false);

        trainIsRewinding(cycle, false);

        form.setFormFieldUpdating(true);
        
        writer.beginEmpty("input");
        writer.attribute("type", "submit");
        writer.attribute("name", "fred");
        writer.attribute("disabled", "disabled");
        writer.attribute("id", "fred");
        writer.closeTag();
        
        replay();
        
        submit.renderComponent(writer, cycle);

        verify();
    }
    
    public void test_SubmitType_Script_OnClick()
    {
        IScript script = newMock(IScript.class);
        IForm form = newForm();
        IBinding binding = newMock(IBinding.class);
        
        Submit submit = newInstance(Submit.class, 
                new Object[] {"submitType", "cancel", 
            "submitScript", script, "form", form});
        
        submit.setBinding("onClick", binding);
        
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();
        
        PageRenderSupport prs = newPageRenderSupport();
        trainGetPageRenderSupport(cycle, prs);
        
        script.execute(eq(submit), eq(cycle), eq(prs), isA(Map.class));
        
        replay();
        
        submit.renderSubmitBindings(writer, cycle);

        verify();
    }
    
    public void test_Submit_Async()
    {
        List updates = new ArrayList();
        updates.add("bsComponent");
        
        IScript script = newMock(IScript.class);
        IForm form = newForm();
        IBinding binding = newMock(IBinding.class);
        
        IEngineService engine = newEngineService();
        ILink link = newMock(ILink.class);
        
        Submit submit = newInstance(Submit.class,
                                    "submitType", "cancel",
                                    "submitScript", script, "form", form,
                                    "async", true, "updateComponents", updates,
                                    "directService", engine);
        
        submit.setBinding("onClick", binding);
        
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();
        
        expect(engine.getLink(eq(true), isA(DirectServiceParameter.class))).andReturn(link);
        expect(link.getURL()).andReturn("/test/url");
        
        PageRenderSupport prs = newPageRenderSupport();
        trainGetPageRenderSupport(cycle, prs);
        
        script.execute(eq(submit), eq(cycle), eq(prs), isA(Map.class));
        
        replay();
        
        submit.renderSubmitBindings(writer, cycle);

        verify();
    }
    
    public void test_Rewinding_Disabled()
    {
        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class, new Object[]
        { "disabled", Boolean.TRUE });

        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        expect(form.getDelegate()).andReturn(delegate);
        
        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, true);

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void test_Rewind_Not_Form()
    {
        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class);

        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        expect(form.getDelegate()).andReturn(delegate);
        
        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, false);
        trainIsRewinding(cycle, true);

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void test_Rewind_Not_Trigger()
    {
        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class);

        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        expect(form.getDelegate()).andReturn(delegate);
        
        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, true);

        trainGetParameter(cycle, "fred", null);

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void test_Rewind_Triggered()
    {
        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class, new Object[]
        { "tag", "clicked" });

        IBinding binding = newBinding();
        submit.setBinding("selected", binding);

        IValidationDelegate delegate = newDelegate();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();
        IMarkupWriter writer = newWriter();

        trainGetForm(cycle, form);

        trainWasPrerendered(form, writer, submit, false);

        expect(form.getDelegate()).andReturn(delegate);

        delegate.setFormComponent(submit);

        trainGetElementId(form, submit, "fred");

        trainIsRewinding(form, true);

        trainGetParameter(cycle, "fred", "flintstone");

        replay();

        submit.renderComponent(writer, cycle);

        assertEquals("clicked", PropertyUtils.read(submit, "selected"));

        verify();
    }

    public void test_Trigger_With_Listener()
    {
        IActionListener listener = newListener();
        IForm form = newForm();
        IRequestCycle cycle = newCycle();

        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class, new Object[]
        { "listener", listener, "listenerInvoker", new ListenerInvokerTerminator() });

        listener.actionTriggered(submit, cycle);

        replay();

        submit.handleClick(cycle, form);

        verify();
    }

    public void test_Trigger_With_Action()
    {
        IActionListener action = newListener();
        MockForm form = new MockForm();
        IRequestCycle cycle = newCycle();

        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class, new Object[]
        { "action", action, "listenerInvoker",
                new ListenerInvokerTerminator() });

        replay();

        submit.handleClick(cycle, form);

        verify();

        action.actionTriggered(submit, cycle);

        replay();

        form.runDeferred();

        verify();
    }

    public void test_Trigger_With_Action_And_Single_Parameter()
    {
        IActionListener action = newListener();
        MockForm form = new MockForm();
        IRequestCycle cycle = newCycle();
        
        Object parameter = new Object();
        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class, new Object[]
        { "action", action, "parameters", parameter, "listenerInvoker",
                new ListenerInvokerTerminator() });
        
        cycle.setListenerParameters(aryEq(new Object[] { parameter }));

        replay();

        submit.handleClick(cycle, form);

        verify();

        action.actionTriggered(submit, cycle);

        replay();

        form.runDeferred();

        verify();
    }

    public void test_Trigger_With_Action_And_Multiple_Parameters()
    {
        IActionListener action = newListener();
        MockForm form = new MockForm();
        IRequestCycle cycle = newCycle();

        Collection parameters = new LinkedList();
        parameters.add("p1");
        parameters.add("p2");

        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class, new Object[]
        { "action", action, "parameters", parameters, "listenerInvoker",
                new ListenerInvokerTerminator() });

        cycle.setListenerParameters(aryEq(new Object[]{ "p1", "p2" }));

        replay();

        submit.handleClick(cycle, form);

        verify();

        action.actionTriggered(submit, cycle);

        replay();

        form.runDeferred();

        verify();
    }
    
    public void test_Trigger_With_Listener_And_Action()
    {
        IActionListener listener = newListener();
        IActionListener action = newListener();
        
        MockForm form = new MockForm();
        IRequestCycle cycle = newCycle();

        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class, new Object[]
        { "listener", listener, "action", action, "listenerInvoker",
                new ListenerInvokerTerminator() });

        listener.actionTriggered(submit, cycle);
        
        replay();

        submit.handleClick(cycle, form);

        verify();

        action.actionTriggered(submit, cycle);

        replay();

        form.runDeferred();

        verify();
    }
   
}
