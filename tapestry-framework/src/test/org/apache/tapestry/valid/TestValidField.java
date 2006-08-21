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

package org.apache.tapestry.valid;

import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;

import org.apache.hivemind.Location;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.BaseFormComponentTestCase;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.MockDelegate;
import org.apache.tapestry.html.BasePage;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.valid.ValidField}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestValidField extends BaseFormComponentTestCase
{

    public void testWasPrerendered()
    {
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newWriter();
        
        ValidField component = (ValidField) newInstance(ValidField.class);
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        
        trainWasPrerendered(form, writer, component, true);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();
    }

    /**
     * Check when the form is not rewinding, but the cycle is rewinding (a whole page rewind care of
     * the action service).
     */

    public void testFormNotRewinding()
    {
        ValidField component = (ValidField) newInstance(ValidField.class);

        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newWriter();
        IValidationDelegate delegate = newDelegate();
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(form, component, "fred");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, true);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();
    }

    private void trainToObject(IValidator validator, IFormComponent component,
            String fieldValue, Object translatedValue) throws ValidatorException
    {
        expect(validator.toObject(component, fieldValue)).andReturn(translatedValue);
    }

    public void testRewind() throws Exception
    {
        Object translatedValue = new Object();
        
        IValidator validator = newMock(IValidator.class);

        ValidField component = (ValidField) newInstance(ValidField.class, "validator", validator);

        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(form, component, "fred");
        trainIsRewinding(form, true);

        trainGetParameter(cycle, "fred", "fred-value");

        trainGetDelegate(form, delegate);

        delegate.recordFieldInputValue("fred-value");

        trainToObject(validator, component, "fred-value", translatedValue);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();

        assertSame(translatedValue, PropertyUtils.read(component, "value"));
    }

    public void testRewindNoValidator()
    {   
        IPage page = (IPage) newInstance(BasePage.class);
        page.setPageName("Barney");
        
        ValidField component = newInstance(ValidField.class, new Object[]
        { "page", page, "id", "inputFred", "container", page });
        
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);
        
        IMarkupWriter writer = newWriter();
        
        IValidationDelegate delegate = newDelegate();
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        
        Location l = newLocation();
        IBinding binding = newBinding(l);
        
        component.setBinding("validator", binding);
        
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(component);
        
        trainGetElementId(form, component, "fred");
        trainIsRewinding(form, true);
        
        trainGetParameter(cycle, "fred", "fred-value");

        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        try
        {
            component.render(writer, cycle);
            unreachable();
        }
        catch (BindingException ex)
        {
            assertEquals(
                    "Value for parameter 'validator' in component Barney/inputFred is null, and a non-null value is required.",
                    ex.getMessage());
            assertSame(l, ex.getLocation());
            assertSame(binding, ex.getBinding());
        }

        verify();
    }

    public void testRender()
    {
        Object value = new Object();
        
        IValidator validator = newMock(IValidator.class);

        ValidField component = newInstance(ValidField.class, new Object[]
        { "value", value, "validator", validator });

        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);
        checkOrder(form, false);
        
        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);

        trainGetElementId(form, component, "fred");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, false);

        form.setFormFieldUpdating(true);
        
        trainToString(validator, component, value, "fred value");

        expect(validator.isRequired()).andReturn(false);

        // Would be nice to have this do something so we could check the timing, but ...

        validator.renderValidatorContribution(component, writer, cycle);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();

        assertSame(component, delegate.getFormComponent());
        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" value=\"fred value\" class=\"validation-delegate\"/></span>");
    }

    private void trainToString(IValidator validator,
            IFormComponent component, Object value, String string)
    {
        expect(validator.toString(component, value)).andReturn(string);
    }

    public void testRenderNull()
    {
        IPage page = (IPage) newInstance(BasePage.class);
        IValidator validator = newMock(IValidator.class);

        ValidField component = newInstance(ValidField.class, new Object[]
        { "validator", validator, "page", page, "container", page });
        
        IRequestCycle cycle = newCycle();
        
        page.attach(null, cycle);
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        IForm form = newMock(IForm.class);
        checkOrder(form, false);

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);

        trainGetElementId(form, component, "fred");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, false);
        
        form.setFormFieldUpdating(true);
        
        trainToString(validator, component, null, null);

        expect(validator.isRequired()).andReturn(false);

        // Would be nice to have this do something so we could check the timing, but ...

        validator.renderValidatorContribution(component, writer, cycle);

        // Short cut this here, so that it appears some other field is
        // taking the honors ...
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();

        assertSame(component, delegate.getFormComponent());
        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" class=\"validation-delegate\"/></span>");
    }

    public void testRenderWithError()
    {
        Object value = new Object();
        
        IValidator validator = newMock(IValidator.class);
        
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);
        checkOrder(form, false);
        
        ValidField component = newInstance(ValidField.class, new Object[]
        { "value", value, "validator", validator, "form", form, "name", "fred" });
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate(true);
        delegate.recordFieldInputValue("recorded field value");

        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);
        
        trainGetElementId(form, component, "fred");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, false);

        form.setFormFieldUpdating(true);
        
        expect(validator.isRequired()).andReturn(true);

        // Would be nice to have this do something so we could check the timing, but ...

        validator.renderValidatorContribution(component, writer, cycle);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();

        assertSame(component, delegate.getFormComponent());
        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" value=\"recorded field value\" class=\"validation-delegate\"/></span>");
    }
}
