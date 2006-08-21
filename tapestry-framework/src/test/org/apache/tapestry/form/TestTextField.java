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

import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.TextField}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestTextField extends BaseFormComponentTestCase
{
    public void testRewind()
    {
        TranslatedFieldSupport tfs = newMock(TranslatedFieldSupport.class);
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);

        TextField component = newInstance(TextField.class, new Object[]
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

        trainGetParameter(cycle, "barney", "10");

        Integer value = new Integer(10);
        
        try
        {
            expect(tfs.parse(component, "10")).andReturn(value);
        
            vfs.validate(component, writer, cycle, value);
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();
        
        assertEquals(value, component.getValue());
    }

    public void testRewindTranslateFailed()
    {
        TranslatedFieldSupport tfs = newMock(TranslatedFieldSupport.class);
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);

        TextField component = newInstance(TextField.class, new Object[]
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

        trainGetParameter(cycle, "barney", "10");

        ValidatorException exception = new ValidatorException("test");
        
        try
        {
            expect(tfs.parse(component, "10")).andThrow(exception);
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

        TextField component = newInstance(TextField.class, new Object[]
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

        trainGetParameter(cycle, "barney", "10");

        ValidatorException exception = new ValidatorException("test");
        
        Integer value = new Integer(10);
        
        try
        {
            expect(tfs.parse(component, "10")).andReturn(value);
            
            vfs.validate(component, writer, cycle, value);
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
        TextField component = (TextField) newInstance(TextField.class);

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
        TextField component = (TextField) newInstance(TextField.class);

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
        TextField component = (TextField) newInstance(TextField.class, "disabled", Boolean.TRUE);

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

        assertNull(PropertyUtils.read(component, "value"));

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

        TextField component = newInstance(TextField.class, new Object[]
        { "name", "fred", "translatedFieldSupport", tfs, "validatableFieldSupport", vfs, "value", new Integer(10) });
        
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
        
        expect(tfs.format(component, new Integer(10))).andReturn("10");
        
        trainGetDelegate(form, delegate);
        
        tfs.renderContributions(component, writer, cycle);
        
        trainGetDelegate(form, delegate);
        
        vfs.renderContributions(component, writer, cycle);

        trainGetDelegate(form, delegate);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();

        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" value=\"10\" class=\"validation-delegate\"/></span>");
    }

    public void testRenderHidden()
    {
        TranslatedFieldSupport tfs = newMock(TranslatedFieldSupport.class);
        
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);

        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        TextField component = newInstance(TextField.class, new Object[]
        { "name", "fred", "translatedFieldSupport", tfs, "validatableFieldSupport", vfs, "value", new Integer(10), "hidden", Boolean.TRUE });
        
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

        expect(tfs.format(component, new Integer(10))).andReturn("10");
        
        trainGetDelegate(form, delegate);
        
        tfs.renderContributions(component, writer, cycle);

        trainGetDelegate(form, delegate);
        
        vfs.renderContributions(component, writer, cycle);

        trainGetDelegate(form, delegate);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();

        assertBuffer("<span class=\"prefix\"><input type=\"password\" name=\"fred\" value=\"10\" class=\"validation-delegate\"/></span>");
    }

    public void testRenderDisabled()
    {
        TranslatedFieldSupport tfs = newMock(TranslatedFieldSupport.class);

        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);

        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        TextField component = newInstance(TextField.class, new Object[]
        { "name", "fred", "translatedFieldSupport", tfs, "validatableFieldSupport", vfs, "value", new Integer(10), "disabled", Boolean.TRUE });
        
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

        expect(tfs.format(component, new Integer(10))).andReturn("10");
        
        trainGetDelegate(form, delegate);
        
        tfs.renderContributions(component, writer, cycle);
        
        trainGetDelegate(form, delegate);
        
        vfs.renderContributions(component, writer, cycle);

        trainGetDelegate(form, delegate);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();

        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" disabled=\"disabled\" value=\"10\" class=\"validation-delegate\"/></span>");
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

        TextField component = newInstance(TextField.class, new Object[]
        { "name", "fred", "translatedFieldSupport", tfs, "validatableFieldSupport", vfs, "value", new Integer(10), "specification", new ComponentSpecification() });
        
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
        
        expect(tfs.format(component, new Integer(10))).andReturn("10");
        
        tfs.renderContributions(component, writer, cycle);
        vfs.renderContributions(component, writer, cycle);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();

        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" value=\"10\" class=\"validation-delegate\" informal=\"informal-value\"/></span>");
    }

    public void testRenderNullValue()
    {
        TranslatedFieldSupport tfs = newMock(TranslatedFieldSupport.class);

        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);

        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        TextField component = newInstance(TextField.class, new Object[]
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

        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" value=\"\" class=\"validation-delegate\"/></span>");
    }

    public void testIsRequired()
    {
        ValidatableFieldSupport support = newMock(ValidatableFieldSupport.class);

        TextField field = newInstance(TextField.class, new Object[]
        { "validatableFieldSupport", support, });
        
        expect(support.isRequired(field)).andReturn(true);

        replay();

        assertEquals(true, field.isRequired());

        verify();
    }
}
