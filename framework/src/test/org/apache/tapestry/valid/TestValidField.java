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

import org.apache.hivemind.Location;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.form.BaseFormComponentTest;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.MockDelegate;
import org.apache.tapestry.html.BasePage;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.valid.ValidField}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestValidField extends BaseFormComponentTest
{

    public void testWasPrerendered()
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        trainGetForm(cyclec, cycle, form);

        ValidField component = (ValidField) newInstance(ValidField.class);

        trainWasPrerendered(formc, form, writer, component, true);

        replayControls();

        component.render(writer, cycle);

        verifyControls();
    }

    /**
     * Check when the form is not rewinding, but the cycle is rewinding (a whole page rewind care of
     * the action service).
     */

    public void testFormNotRewinding()
    {
        ValidField component = (ValidField) newInstance(ValidField.class);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        MockControl delegatec = newControl(IValidationDelegate.class);
        IValidationDelegate delegate = (IValidationDelegate) delegatec.getMock();

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, false);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(formc, form, false);
        trainIsRewinding(cyclec, cycle, true);

        replayControls();

        component.render(writer, cycle);

        verifyControls();
    }

    private void trainToObject(MockControl control, IValidator validator, IFormComponent component,
            String fieldValue, Object translatedValue) throws ValidatorException
    {
        validator.toObject(component, fieldValue);

        control.setReturnValue(translatedValue);
    }

    public void testRewind() throws Exception
    {
        Object translatedValue = new Object();

        MockControl validatorc = newControl(IValidator.class);
        IValidator validator = (IValidator) validatorc.getMock();

        ValidField component = (ValidField) newInstance(ValidField.class, "validator", validator);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        MockControl delegatec = newControl(IValidationDelegate.class);
        IValidationDelegate delegate = (IValidationDelegate) delegatec.getMock();

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, false);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(formc, form, true);

        trainGetParameter(cyclec, cycle, "fred", "fred-value");

        trainGetDelegate(formc, form, delegate);

        delegate.recordFieldInputValue("fred-value");

        trainToObject(validatorc, validator, component, "fred-value", translatedValue);

        replayControls();

        component.render(writer, cycle);

        verifyControls();

        assertSame(translatedValue, component.getProperty("value"));
    }

    public void testRewindNoValidator()
    {
        Location l = newLocation();

        IBinding binding = newBinding(l);

        IPage page = (IPage) newInstance(BasePage.class);
        page.setPageName("Barney");

        ValidField component = (ValidField) newInstance(ValidField.class, new Object[]
        { "page", page, "id", "inputFred", "container", page });

        component.setBinding("validator", binding);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        MockControl delegatec = newControl(IValidationDelegate.class);
        IValidationDelegate delegate = (IValidationDelegate) delegatec.getMock();

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, false);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(formc, form, true);

        trainGetParameter(cyclec, cycle, "fred", "fred-value");

        replayControls();

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

        verifyControls();
    }

    public void testRender()
    {
        Object value = new Object();
        MockControl validatorc = newControl(IValidator.class);
        IValidator validator = (IValidator) validatorc.getMock();

        ValidField component = (ValidField) newInstance(ValidField.class, new Object[]
        { "value", value, "validator", validator });

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, false);
        trainGetDelegate(formc, form, delegate);

        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(formc, form, false);
        trainIsRewinding(cyclec, cycle, false);

        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);

        trainToString(validatorc, validator, component, value, "fred value");

        validator.isRequired();
        validatorc.setReturnValue(false);

        // Would be nice to have this do something so we could check the timing, but ...

        validator.renderValidatorContribution(component, writer, cycle);

        replayControls();

        component.render(writer, cycle);

        verifyControls();

        assertSame(component, delegate.getFormComponent());
        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" value=\"fred value\" class=\"validation-delegate\"/></span>");
    }

    private void trainToString(MockControl validatorc, IValidator validator,
            IFormComponent component, Object value, String string)
    {
        validator.toString(component, value);
        validatorc.setReturnValue(string);
    }

    public void testRenderNull()
    {
        IPage page = (IPage) newInstance(BasePage.class);

        MockControl validatorc = newControl(IValidator.class);
        IValidator validator = (IValidator) validatorc.getMock();

        ValidField component = (ValidField) newInstance(ValidField.class, new Object[]
        { "validator", validator, "page", page, "container", page });

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        page.attach(null, cycle);

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, false);
        trainGetDelegate(formc, form, delegate);

        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(formc, form, false);
        trainIsRewinding(cyclec, cycle, false);

        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);

        trainToString(validatorc, validator, component, null, null);

        validator.isRequired();
        validatorc.setReturnValue(false);

        // Would be nice to have this do something so we could check the timing, but ...

        validator.renderValidatorContribution(component, writer, cycle);

        // Short cut this here, so that it appears some other field is
        // taking the honors ...

        replayControls();

        component.render(writer, cycle);

        verifyControls();

        assertSame(component, delegate.getFormComponent());
        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" class=\"validation-delegate\"/></span>");
    }

    protected void trainGetAttribute(MockControl control, IRequestCycle cycle, String name,
            Object object)
    {
        cycle.getAttribute(name);
        control.setReturnValue(object);
    }

    private void trainIsRequired(MockControl control, IValidator validator, boolean isRequired)
    {
        validator.isRequired();
        control.setReturnValue(isRequired);
    }

    public void testRenderWithError()
    {
        Object value = new Object();
        MockControl validatorc = newControl(IValidator.class);
        IValidator validator = (IValidator) validatorc.getMock();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        ValidField component = (ValidField) newInstance(ValidField.class, new Object[]
        { "value", value, "validator", validator, "form", form, "name", "fred" });

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate(true);
        delegate.recordFieldInputValue("recorded field value");

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, false);
        trainGetDelegate(formc, form, delegate);

        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(formc, form, false);
        trainIsRewinding(cyclec, cycle, false);

        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);

        validator.isRequired();
        validatorc.setReturnValue(true);

        // Would be nice to have this do something so we could check the timing, but ...

        validator.renderValidatorContribution(component, writer, cycle);

        replayControls();

        component.render(writer, cycle);

        verifyControls();

        assertSame(component, delegate.getFormComponent());
        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" value=\"recorded field value\" class=\"validation-delegate\"/></span>");
    }

    private void trainGetName(MockControl control, IForm form, String name)
    {
        form.getName();

        control.setReturnValue(name);
    }
}
