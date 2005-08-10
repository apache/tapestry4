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
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.TextArea}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestTextArea extends BaseFormComponentTest
{
    public void testRewind()
    {
        MockControl tfsc = newControl(TranslatedFieldSupport.class);
        TranslatedFieldSupport tfs = (TranslatedFieldSupport) tfsc.getMock();

        MockControl vfsc = newControl(ValidatableFieldSupport.class);
        ValidatableFieldSupport vfs = (ValidatableFieldSupport) vfsc.getMock();

        TextArea component = (TextArea) newInstance(TextArea.class, new Object[]
        { "translatedFieldSupport", tfs, "validatableFieldSupport", vfs });

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

        trainGetParameter(cyclec, cycle, "barney", " text area value ");

        try
        {
            tfs.parse(component, " text area value ");
            tfsc.setReturnValue("text area value");
        
            vfs.validate(component, writer, cycle, "text area value");
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        
        replayControls();

        component.render(writer, cycle);

        verifyControls();
        
        assertEquals("text area value", component.getValue());
    }

    public void testRewindTranslateFailed()
    {
        MockControl tfsc = newControl(TranslatedFieldSupport.class);
        TranslatedFieldSupport tfs = (TranslatedFieldSupport) tfsc.getMock();

        MockControl vfsc = newControl(ValidatableFieldSupport.class);
        ValidatableFieldSupport vfs = (ValidatableFieldSupport) vfsc.getMock();

        TextArea component = (TextArea) newInstance(TextArea.class, new Object[]
        { "translatedFieldSupport", tfs, "validatableFieldSupport", vfs });

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

        trainGetParameter(cyclec, cycle, "barney", " text area value ");

        ValidatorException exception = new ValidatorException("test");
        
        try
        {
            tfs.parse(component, " text area value ");
            tfsc.setThrowable(exception);
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        
        trainGetDelegate(formc, form, delegate);
        delegate.record(exception);
        
        replayControls();

        component.render(writer, cycle);
        
        verifyControls();
    }

    public void testRewindValidateFailed()
    {
        MockControl tfsc = newControl(TranslatedFieldSupport.class);
        TranslatedFieldSupport tfs = (TranslatedFieldSupport) tfsc.getMock();

        MockControl vfsc = newControl(ValidatableFieldSupport.class);
        ValidatableFieldSupport vfs = (ValidatableFieldSupport) vfsc.getMock();

        TextArea component = (TextArea) newInstance(TextArea.class, new Object[]
        { "translatedFieldSupport", tfs, "validatableFieldSupport", vfs });

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

        trainGetParameter(cyclec, cycle, "barney", " text area value ");

        ValidatorException exception = new ValidatorException("test");
        
        try
        {
            tfs.parse(component, " text area value ");
            tfsc.setReturnValue("text area value");
            
            vfs.validate(component, writer, cycle, "text area value");
            vfsc.setThrowable(exception);
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        
        trainGetDelegate(formc, form, delegate);
        delegate.record(exception);
        
        replayControls();

        component.render(writer, cycle);
        
        verifyControls();
    }

    public void testWasPrerendered()
    {
        TextArea component = (TextArea) newInstance(TextArea.class);

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
        TextArea component = (TextArea) newInstance(TextArea.class);

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

    public void testRewindDisabled()
    {
        TextArea component = (TextArea) newInstance(TextArea.class, "disabled", Boolean.TRUE);

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

        assertNull(component.getValue());

        verifyControls();
    }

    public void testRender()
    {
        MockControl tfsc = newControl(TranslatedFieldSupport.class);
        TranslatedFieldSupport tfs = (TranslatedFieldSupport) tfsc.getMock();
        
        ValidatableFieldSupport vfs = (ValidatableFieldSupport) newMock(ValidatableFieldSupport.class);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        TextArea component = (TextArea) newInstance(TextArea.class, new Object[]
        { "name", "fred", "translatedFieldSupport", tfs, "validatableFieldSupport", vfs, "value", " text area value " });

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, false);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(formc, form, false);
        trainIsRewinding(cyclec, cycle, false);

        delegate.setFormComponent(component);

        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);

        tfs.format(component, " text area value ");
        tfsc.setReturnValue("text area value");
        
        tfs.renderContributions(component, writer, cycle);
        vfs.renderContributions(component, writer, cycle);

        replayControls();

        component.render(writer, cycle);

        verifyControls();

        assertBuffer("<span class=\"prefix\"><textarea name=\"fred\" class=\"validation-delegate\">text area value</textarea></span>");
    }

    public void testRenderDisabled()
    {
        MockControl tfsc = newControl(TranslatedFieldSupport.class);
        TranslatedFieldSupport tfs = (TranslatedFieldSupport) tfsc.getMock();

        ValidatableFieldSupport vfs = (ValidatableFieldSupport) newMock(ValidatableFieldSupport.class);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        TextArea component = (TextArea) newInstance(TextArea.class, new Object[]
        { "name", "fred", "translatedFieldSupport", tfs, "validatableFieldSupport", vfs, "value", " text area value ", "disabled", Boolean.TRUE });

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, false);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(formc, form, false);
        trainIsRewinding(cyclec, cycle, false);

        delegate.setFormComponent(component);

        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);

        tfs.format(component, " text area value ");
        tfsc.setReturnValue("text area value");
        
        tfs.renderContributions(component, writer, cycle);
        vfs.renderContributions(component, writer, cycle);

        replayControls();

        component.render(writer, cycle);

        verifyControls();

        assertBuffer("<span class=\"prefix\"><textarea name=\"fred\" disabled=\"disabled\" class=\"validation-delegate\">text area value</textarea></span>");
    }

    public void testRenderWithInformalParameters()
    {
        MockControl tfsc = newControl(TranslatedFieldSupport.class);
        TranslatedFieldSupport tfs = (TranslatedFieldSupport) tfsc.getMock();

        ValidatableFieldSupport vfs = (ValidatableFieldSupport) newMock(ValidatableFieldSupport.class);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        IBinding binding = newBinding("informal-value");

        TextArea component = (TextArea) newInstance(TextArea.class, new Object[]
        { "name", "fred", "translatedFieldSupport", tfs, "validatableFieldSupport", vfs, "value", " text area value ", "specification", new ComponentSpecification() });

        component.setBinding("informal", binding);

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, false);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(formc, form, false);
        trainIsRewinding(cyclec, cycle, false);

        delegate.setFormComponent(component);

        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);

        tfs.format(component, " text area value ");
        tfsc.setReturnValue("text area value");
        
        tfs.renderContributions(component, writer, cycle);
        vfs.renderContributions(component, writer, cycle);

        replayControls();

        component.render(writer, cycle);

        verifyControls();

        assertBuffer("<span class=\"prefix\"><textarea name=\"fred\" class=\"validation-delegate\" informal=\"informal-value\">text area value</textarea></span>");
    }

    public void testRenderNullValue()
    {
        MockControl tfsc = newControl(TranslatedFieldSupport.class);
        TranslatedFieldSupport tfs = (TranslatedFieldSupport) tfsc.getMock();

        ValidatableFieldSupport vfs = (ValidatableFieldSupport) newMock(ValidatableFieldSupport.class);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl formc = newControl(IForm.class);
        IForm form = (IForm) formc.getMock();

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        TextArea component = (TextArea) newInstance(TextArea.class, new Object[]
        { "name", "fred", "translatedFieldSupport", tfs, "validatableFieldSupport", vfs });

        trainGetForm(cyclec, cycle, form);
        trainWasPrerendered(formc, form, writer, component, false);
        trainGetDelegate(formc, form, delegate);

        delegate.setFormComponent(component);

        trainGetElementId(formc, form, component, "fred");
        trainIsRewinding(formc, form, false);
        trainIsRewinding(cyclec, cycle, false);

        delegate.setFormComponent(component);

        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);
        trainGetDelegate(formc, form, delegate);

        tfs.format(component, null);
        tfsc.setReturnValue("");
        
        tfs.renderContributions(component, writer, cycle);
        vfs.renderContributions(component, writer, cycle);

        replayControls();

        component.render(writer, cycle);

        verifyControls();

        assertBuffer("<span class=\"prefix\"><textarea name=\"fred\" class=\"validation-delegate\"></textarea></span>");
    }
}
