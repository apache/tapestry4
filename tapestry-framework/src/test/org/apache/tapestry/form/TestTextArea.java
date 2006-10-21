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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.TextArea}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestTextArea extends BaseFormComponentTestCase
{
    public void testRewind()
    {
        TranslatedFieldSupport tfs = newMock(TranslatedFieldSupport.class);
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);

        TextArea component = newInstance(TextArea.class, new Object[]
        { "translatedFieldSupport", tfs, "validatableFieldSupport", vfs });
        
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(form, component, "barney");
        trainIsRewinding(form, true);

        trainGetParameter(cycle, "barney", " text area value ");

        try
        {
            expect(tfs.parse(component, " text area value ")).andReturn("text area value");
        
            vfs.validate(component, writer, cycle, "text area value");
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();
        
        assertEquals("text area value", component.getValue());
    }

    public void testRewindTranslateFailed()
    {
        TranslatedFieldSupport tfs = newMock(TranslatedFieldSupport.class);
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);

        TextArea component = newInstance(TextArea.class, new Object[]
        { "translatedFieldSupport", tfs, "validatableFieldSupport", vfs });
        
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(form, component, "barney");
        trainIsRewinding(form, true);

        trainGetParameter(cycle, "barney", " text area value ");

        ValidatorException exception = new ValidatorException("test");
        
        try
        {
            expect(tfs.parse(component, " text area value ")).andThrow(exception);
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        
        trainGetDelegate(form, delegate);
        delegate.record(exception);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);
        
        verify();
        
        assertNull(component.getValue());
    }

    public void testRewindValidateFailed()
    {
        TranslatedFieldSupport tfs = newMock(TranslatedFieldSupport.class);
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);

        TextArea component = newInstance(TextArea.class, new Object[]
        { "translatedFieldSupport", tfs, "validatableFieldSupport", vfs });
        
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(form, component, "barney");
        trainIsRewinding(form, true);

        trainGetParameter(cycle, "barney", " text area value ");

        ValidatorException exception = new ValidatorException("test");
        
        try
        {
            expect(tfs.parse(component, " text area value ")).andReturn("text area value");
            
            vfs.validate(component, writer, cycle, "text area value");
            expectLastCall().andThrow(exception);
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        
        trainGetDelegate(form, delegate);
        delegate.record(exception);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);
        
        verify();
        
        assertNull(component.getValue());
    }

    public void testWasPrerendered()
    {
        TextArea component = (TextArea) newInstance(TextArea.class);
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newWriter();
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, true);

        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();
    }

    public void testRewindNotForm()
    {
        TextArea component = (TextArea) newInstance(TextArea.class);
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(form, component, "barney");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, true);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();
    }

    public void testRewindDisabled()
    {
        TextArea component = (TextArea) newInstance(TextArea.class, "disabled", Boolean.TRUE);
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(form, component, "barney");
        trainIsRewinding(form, true);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        assertNull(component.getValue());

        verify();
    }

    public void testRender()
    {
        TranslatedFieldSupport tfs = newMock(TranslatedFieldSupport.class);
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        TextArea component = newInstance(TextArea.class, new Object[]
        { "name", "fred", "translatedFieldSupport", tfs, "validatableFieldSupport", vfs, "value", " text area value " });
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(form, component, "fred");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, false);

        form.setFormFieldUpdating(true);
        
        delegate.setFormComponent(component);

        trainGetDelegate(form, delegate);

        expect(tfs.format(component, " text area value ")).andReturn("text area value");
        
        trainGetDelegate(form, delegate);
        
        tfs.renderContributions(component, writer, cycle);
        
        trainGetDelegate(form, delegate);
        
        vfs.renderContributions(component, writer, cycle);

        trainGetDelegate(form, delegate);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();

        assertBuffer("<span class=\"prefix\"><textarea name=\"fred\" id=\"fred\" class=\"validation-delegate\">text area value</textarea></span>");
    }

    public void testRenderDisabled()
    {
        TranslatedFieldSupport tfs = newMock(TranslatedFieldSupport.class);
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        TextArea component = newInstance(TextArea.class, new Object[]
        { "name", "fred", "translatedFieldSupport", tfs, "validatableFieldSupport", vfs, "value", " text area value ", "disabled", Boolean.TRUE });
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(form, component, "fred");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, false);

        form.setFormFieldUpdating(true);
        
        delegate.setFormComponent(component);
        
        expect(tfs.format(component, " text area value ")).andReturn("text area value");
        
        trainGetDelegate(form, delegate);
        
        tfs.renderContributions(component, writer, cycle);
        
        trainGetDelegate(form, delegate);
        
        vfs.renderContributions(component, writer, cycle);

        trainGetDelegate(form, delegate);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();

        assertBuffer("<span class=\"prefix\"><textarea name=\"fred\" disabled=\"disabled\" id=\"fred\" class=\"validation-delegate\">text area value</textarea></span>");
    }

    public void testRenderWithInformalParameters()
    {
        TranslatedFieldSupport tfs = newMock(TranslatedFieldSupport.class);
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        IBinding binding = newBinding("informal-value");

        TextArea component = newInstance(TextArea.class, new Object[]
        { "name", "fred", "translatedFieldSupport", tfs, "validatableFieldSupport", vfs, "value", " text area value ", "specification", new ComponentSpecification() });

        component.setBinding("informal", binding);
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(form, component, "fred");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, false);

        form.setFormFieldUpdating(true);
        
        delegate.setFormComponent(component);

        expect(tfs.format(component, " text area value ")).andReturn("text area value");
        
        tfs.renderContributions(component, writer, cycle);
        vfs.renderContributions(component, writer, cycle);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();

        assertBuffer("<span class=\"prefix\"><textarea name=\"fred\" id=\"fred\" class=\"validation-delegate\" informal=\"informal-value\">text area value</textarea></span>");
    }

    public void testRenderNullValue()
    {
        TranslatedFieldSupport tfs = newMock(TranslatedFieldSupport.class);
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        TextArea component = newInstance(TextArea.class, new Object[]
        { "name", "fred", "translatedFieldSupport", tfs, "validatableFieldSupport", vfs });
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(form, component, "fred");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, false);

        form.setFormFieldUpdating(true);
        
        delegate.setFormComponent(component);

        expect(tfs.format(component, null)).andReturn("");
        
        trainGetDelegate(form, delegate);
        
        tfs.renderContributions(component, writer, cycle);
        
        trainGetDelegate(form, delegate);
        
        vfs.renderContributions(component, writer, cycle);

        trainGetDelegate(form, delegate);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();

        assertBuffer("<span class=\"prefix\"><textarea name=\"fred\" id=\"fred\" class=\"validation-delegate\"></textarea></span>");
    }
}
