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

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.listener.ListenerInvokerTerminator;
import org.apache.tapestry.test.Creator;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationConstants;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.Submit}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestSubmit extends BaseFormComponentTestCase
{
    public void testPrerendered()
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

    public void testRender()
    {
        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class);

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
        writer.closeTag();

        trainIsInError(delegate, false);

        delegate.registerForFocus(submit, ValidationConstants.NORMAL_FIELD);

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void testRenderDisabled()
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
        writer.closeTag();

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void testRenderWithLabel()
    {
        Creator creator = new Creator();
        Submit submit = (Submit) creator.newInstance(Submit.class, new Object[]
        { "label", "flintstone" });

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
        writer.closeTag();

        trainIsInError(delegate, false);

        delegate.registerForFocus(submit, ValidationConstants.NORMAL_FIELD);

        replay();

        submit.renderComponent(writer, cycle);

        verify();
    }

    public void testRewindingDisabled()
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

    public void testRewindNotForm()
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

    public void testRewindNotTrigger()
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

    public void testRewindTriggered()
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

    public void testTriggerWithListener()
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

    public void testTriggerWithAction()
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

    public void testTriggerWithActionAndSingleParameter()
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

    public void testTriggerWithDActionAndMultipleParameters()
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
    
    public void testTriggerWithListenerAndAction()
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
