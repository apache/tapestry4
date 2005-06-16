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

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.translator.Translator;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.TextField}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestTextField extends BaseFormComponentTest
{
    public void testBind()
    {
        MockControl translatorc = newControl(Translator.class);
        Translator translator = (Translator) translatorc.getMock();
        
        MockControl validatableFieldSupportc = newControl(ValidatableFieldSupport.class);
        ValidatableFieldSupport validatableFieldSupport = (ValidatableFieldSupport) validatableFieldSupportc.getMock();
        
        TextField component = (TextField) newInstance(TextField.class, new Object[] { "translator", translator, "validatableFieldSupport", validatableFieldSupport, "name", "barney" });

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        IMarkupWriter writer = newWriter();

        trainGetParameter(cyclec, cycle, "barney", "value");
        
        try
        {
            validatableFieldSupport.bind(component, writer, cycle, "value");
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        
        replayControls();

        try
        {
            component.bind(writer, cycle);
        }
        catch (ValidatorException e)
        {
            unreachable();
        }

        verifyControls();
    }
    
    public void testWriteValue()
    {
        IBinding binding = newBinding();
        
        TextField component = (TextField) newInstance(TextField.class);

        component.setBinding("value", binding);
        
        binding.setObject(new Integer(10));
        
        replayControls();
        
        component.writeValue(new Integer(10));
        
        verifyControls();
    }
    
    public void testReadValue()
    {
        MockControl bindingc = newControl(IBinding.class);
        IBinding binding = (IBinding) bindingc.getMock();
        
        TextField component = (TextField) newInstance(TextField.class);

        component.setBinding("value", binding);

        binding.getObject();
        bindingc.setReturnValue(new Integer(10));
        
        replayControls();
        
        Object value = component.readValue();
        
        verifyControls();
        
        assertEquals(new Integer(10), value);
    }

    public void testGetSubmittedValue()
    {
        TextField component = (TextField) newInstance(TextField.class, "name", "fred");
        
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();
        
        cycle.getParameter("fred");
        cyclec.setReturnValue("10");
        
        replayControls();
        
        String value = component.getSubmittedValue(cycle);
        
        verifyControls();
        
        assertEquals("10", value);
    }
    
    public void testWasPrerendered()
    {
        TextField component = (TextField) newInstance(TextField.class);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, true);

        replayControls();

        component.render(writer, cycle);

        verifyControls();
    }

    public void testRewindNotForm()
    {
        TextField component = (TextField) newInstance(TextField.class);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, false);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(formc, form, component, "barney");
        trainIsRewinding(formc, form, false);
        trainIsRewinding(cyclec, cycle, true);

        replayControls();

        component.render(writer, cycle);

        verifyControls();
    }

    public void testRewindingForm()
    {
        MockControl translatorc = newControl(Translator.class);
        Translator translator = (Translator) translatorc.getMock();
        
        MockControl requirableFieldSupportc = newControl(RequirableFieldSupport.class);
        RequirableFieldSupport requirableFieldSupport = (RequirableFieldSupport) requirableFieldSupportc.getMock();
        
        TextField component = (TextField) newInstance(TextField.class, new Object[] { "translator", translator, "requirableFieldSupport", requirableFieldSupport });

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, false);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(formc, form, component, "barney");
        trainIsRewinding(formc, form, true);
        requirableFieldSupport.rewind(component, writer, cycle);
        
        replayControls();

        component.render(writer, cycle);

        verifyControls();
    }
    
    public void testRewindFormDisabled()
    {
        TextField component = (TextField) newInstance(TextField.class, "disabled", Boolean.TRUE);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newWriter();

        IValidationDelegate delegate = newDelegate();

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, false);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(formc, form, component, "barney");
        trainIsRewinding(formc, form, true);

        replayControls();

        component.render(writer, cycle);

        assertNull(component.getProperty("value"));

        verifyControls();
    }

    public void testRender()
    {
        MockControl translatorc = newControl(Translator.class);
        Translator translator = (Translator) translatorc.getMock();
        
        MockControl requirableFieldSupportc = newControl(RequirableFieldSupport.class);
        RequirableFieldSupport requirableFieldSupport = (RequirableFieldSupport) requirableFieldSupportc.getMock();
        
        MockControl validatableFieldSupportc = newControl(ValidatableFieldSupport.class);
        ValidatableFieldSupport validatableFieldSupport = (ValidatableFieldSupport) validatableFieldSupportc.getMock();
        
        TextField component = (TextField) newInstance(TextField.class, new Object[]
        { "value", "text area value", "translator", translator, "requirableFieldSupport", requirableFieldSupport, "validatableFieldSupport", validatableFieldSupport });

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        
        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, false);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(formc, form, false);
        trainIsRewinding(cyclec, cycle, false);

        validatableFieldSupport.render(component, writer, cycle);
        requirableFieldSupport.render(component, writer, cycle);
        
        replayControls();

        component.render(writer, cycle);

        verifyControls();
    }

    public void testRenderValue()
    {
        MockControl translatorc = newControl(Translator.class);
        Translator translator = (Translator) translatorc.getMock();
        
        MockControl validatableFieldSupportc = newControl(ValidatableFieldSupport.class);
        ValidatableFieldSupport validatableFieldSupport = (ValidatableFieldSupport) validatableFieldSupportc.getMock();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        
        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();
        
        TextField component = (TextField) newInstance(TextField.class, new Object[]
        { "name", "fred", "translator", translator, "validatableFieldSupport", validatableFieldSupport, "form", form });

        delegate.setFormComponent(component);
        
        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);
        
        validatableFieldSupport.renderContributions(component, writer, cycle);
        
        replayControls();

        component.render(writer, cycle, "10");

        verifyControls();
        
        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" value=\"10\" class=\"validation-delegate\"/></span>");
    }

    public void testRenderHidden()
    {
        MockControl translatorc = newControl(Translator.class);
        Translator translator = (Translator) translatorc.getMock();
        
        MockControl validatableFieldSupportc = newControl(ValidatableFieldSupport.class);
        ValidatableFieldSupport validatableFieldSupport = (ValidatableFieldSupport) validatableFieldSupportc.getMock();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        
        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();
        
        TextField component = (TextField) newInstance(TextField.class, new Object[]
        { "name", "fred", "translator", translator, "validatableFieldSupport", validatableFieldSupport, "form", form, "hidden", Boolean.TRUE });

        delegate.setFormComponent(component);
        
        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);
        
        validatableFieldSupport.renderContributions(component, writer, cycle);
        
        replayControls();

        component.render(writer, cycle, "10");

        verifyControls();
        
        assertBuffer("<span class=\"prefix\"><input type=\"password\" name=\"fred\" value=\"10\" class=\"validation-delegate\"/></span>");
    }

    public void testRenderDisabled()
    {
        MockControl translatorc = newControl(Translator.class);
        Translator translator = (Translator) translatorc.getMock();
        
        MockControl validatableFieldSupportc = newControl(ValidatableFieldSupport.class);
        ValidatableFieldSupport validatableFieldSupport = (ValidatableFieldSupport) validatableFieldSupportc.getMock();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        
        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();
        
        TextField component = (TextField) newInstance(TextField.class, new Object[]
        { "name", "fred", "translator", translator, "validatableFieldSupport", validatableFieldSupport, "form", form, "disabled", Boolean.TRUE });

        delegate.setFormComponent(component);
        
        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);
        
        validatableFieldSupport.renderContributions(component, writer, cycle);
        
        replayControls();

        component.render(writer, cycle, "10");

        verifyControls();
        
        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" disabled=\"disabled\" value=\"10\" class=\"validation-delegate\"/></span>");
    }

    public void testRenderWithInformalParameters()
    {
        MockControl translatorc = newControl(Translator.class);
        Translator translator = (Translator) translatorc.getMock();
        
        MockControl validatableFieldSupportc = newControl(ValidatableFieldSupport.class);
        ValidatableFieldSupport validatableFieldSupport = (ValidatableFieldSupport) validatableFieldSupportc.getMock();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        
        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();
        
        IBinding binding = newBinding("informal-value");
        
        TextField component = (TextField) newInstance(TextField.class, new Object[]
        { "name", "fred", "translator", translator, "validatableFieldSupport", validatableFieldSupport, "form", form, "specification", new ComponentSpecification() });

        component.setBinding("informal", binding);
        
        delegate.setFormComponent(component);
        
        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);
        
        validatableFieldSupport.renderContributions(component, writer, cycle);
        
        replayControls();

        component.render(writer, cycle, "10");

        verifyControls();
        
        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" value=\"10\" class=\"validation-delegate\" informal=\"informal-value\"/></span>");
    }

    public void testRenderNullValue()
    {
        MockControl translatorc = newControl(Translator.class);
        Translator translator = (Translator) translatorc.getMock();
        
        MockControl validatableFieldSupportc = newControl(ValidatableFieldSupport.class);
        ValidatableFieldSupport validatableFieldSupport = (ValidatableFieldSupport) validatableFieldSupportc.getMock();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();
        
        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();
        
        TextField component = (TextField) newInstance(TextField.class, new Object[]
        { "name", "fred", "translator", translator, "validatableFieldSupport", validatableFieldSupport, "form", form });

        delegate.setFormComponent(component);
        
        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);
        
        validatableFieldSupport.renderContributions(component, writer, cycle);
        
        replayControls();

        component.render(writer, cycle, null);

        verifyControls();
        
        assertBuffer("<span class=\"prefix\"><input type=\"text\" name=\"fred\" class=\"validation-delegate\"/></span>");
    }
}
