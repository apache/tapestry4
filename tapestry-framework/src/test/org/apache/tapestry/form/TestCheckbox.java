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
import org.apache.tapestry.valid.ValidationConstants;
import org.apache.tapestry.valid.ValidatorException;
import org.testng.annotations.Test;

/**
 * Tests for the {@link org.apache.tapestry.form.Checkbox} component.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestCheckbox extends BaseFormComponentTestCase
{
    public void testRenderChecked()
    {
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        
        Checkbox cb = newInstance(Checkbox.class, new Object[]
        { "name", "assignedName", "value", Boolean.TRUE, "validatableFieldSupport", vfs });
        
        IForm form = newMock(IForm.class);
        
        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycle();

        IValidationDelegate delegate = newDelegate();
        
        expect(cycle.renderStackPush(cb)).andReturn(cb);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, cb, false);
        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(cb);
        
        trainGetElementId(form, cb, "barney");
        
        trainIsRewinding(form, false);
        
        expect(cycle.isRewinding()).andReturn(false);
        
        form.setFormFieldUpdating(true);
        
        trainGetDelegate(form, delegate);
        
        delegate.writePrefix(writer, cycle, cb, null);
        
        vfs.renderContributions(cb, writer, cycle);
        
        trainGetDelegate(form, delegate);
        
        delegate.writeSuffix(writer, cycle, cb, null);
        
        expect(delegate.isInError()).andReturn(false);
        
        delegate.registerForFocus(cb, ValidationConstants.NORMAL_FIELD);
        
        expect(cycle.renderStackPop()).andReturn(cb);
        
        replay();

        cb.render(writer, cycle);

        verify();

        assertBuffer("<input type=\"checkbox\" name=\"barney\" checked=\"checked\"/>");
    }

    public void testRenderDisabled()
    {
        IForm form = newMock(IForm.class);
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        
        Checkbox cb = newInstance(Checkbox.class, new Object[]
        { "name", "assignedName", "disabled", Boolean.TRUE, 
            "form",  form, "validatableFieldSupport", vfs});
        
        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycle();
        
        IValidationDelegate delegate = newDelegate();
        
        trainGetDelegate(form, delegate);
        delegate.writePrefix(writer, cycle, cb, null);
        
        vfs.renderContributions(cb, writer, cycle);
        
        trainGetDelegate(form, delegate);
        
        delegate.writeSuffix(writer, cycle, cb, null);
        
        replay();

        cb.renderFormComponent(writer, cycle);

        verify();

        assertBuffer("<input type=\"checkbox\" name=\"assignedName\" disabled=\"disabled\"/>");
    }

    public void testRenderInformalParameters()
    {
        IForm form = newMock(IForm.class);
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        
        Checkbox cb = newInstance(Checkbox.class, new Object[]
        { "name", "assignedName", "value", Boolean.TRUE, "specification",
                new ComponentSpecification(),
                "form",  form, "validatableFieldSupport", vfs});
        
        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycle();
        
        IBinding binding = newBinding("informal-value");

        cb.setBinding("informal", binding);
        
        IValidationDelegate delegate = newDelegate();
        
        trainGetDelegate(form, delegate);
        delegate.writePrefix(writer, cycle, cb, null);
        
        vfs.renderContributions(cb, writer, cycle);
        
        delegate.writeSuffix(writer, cycle, cb, null);
        
        replay();

        cb.renderFormComponent(writer, cycle);

        verify();

        assertBuffer("<input type=\"checkbox\" name=\"assignedName\" checked=\"checked\" informal=\"informal-value\"/>");
    }

    public void testRenderWithId()
    {
        IForm form = newMock(IForm.class);
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        
        Checkbox cb = newInstance(Checkbox.class, new Object[]
        { "idParameter", "foo", "name", "assignedName", "value", Boolean.TRUE,
            "form",  form, "validatableFieldSupport", vfs});
        
        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycle();
        
        IValidationDelegate delegate = newDelegate();
        trainGetDelegate(form, delegate);
        
        // IRequestCycle cycle = newCycleGetUniqueId("foo", "foo$unique");
        
        delegate.writePrefix(writer, cycle, cb, null);
        
        expect(cycle.getUniqueId("foo")).andReturn("foo$unique");
        
        vfs.renderContributions(cb, writer, cycle);
        
        trainGetDelegate(form, delegate);
        
        delegate.writeSuffix(writer, cycle, cb, null);
        
        replay();

        cb.renderFormComponent(writer, cycle);

        verify();

        assertBuffer("<input type=\"checkbox\" name=\"assignedName\" checked=\"checked\" id=\"foo$unique\"/>");
    }

    public void testSubmitNull()
    {
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
    	
        Checkbox cb = newInstance(Checkbox.class, new Object[]
        { "value", Boolean.TRUE, "name", "checkbox", "validatableFieldSupport", vfs });

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycleGetParameter("checkbox", null);

        try
        {
	        vfs.validate(cb, writer, cycle, null);
        }
        catch (ValidatorException e)
        {
        	unreachable();
        }
    	
        replay();

        cb.rewindFormComponent(writer, cycle);
        
        verify();

        assertEquals(false, cb.getValue());
    }

    public void testSubmitValidateFailed()
    {
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        IForm form = newMock(IForm.class);
        
        IValidationDelegate delegate = newDelegate();
        
        Checkbox cb = newInstance(Checkbox.class, new Object[]
        { "form", form, "value", Boolean.FALSE, "name", "checkbox", "validatableFieldSupport", vfs });

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycleGetParameter("checkbox", "foo");

        ValidatorException exception = new ValidatorException("failed");
        
        try
        {
	        vfs.validate(cb, writer, cycle, "foo");
            expectLastCall().andThrow(exception);
	    }
        catch (ValidatorException e)
        {
        	unreachable();
        }

        expect(form.getDelegate()).andReturn(delegate);
        
        delegate.record(exception);

        replay();

        cb.rewindFormComponent(writer, cycle);
        
        verify();
        
        assertEquals(false, cb.getValue());
    }

    public void testSubmitNonNull()
    {
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        
        Checkbox cb = newInstance(Checkbox.class, new Object[]
        { "value", Boolean.FALSE, "name", "checkbox", "validatableFieldSupport", vfs });

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycleGetParameter("checkbox", "foo");

        try
        {
        	vfs.validate(cb, writer, cycle, "foo");
        }
        catch (ValidatorException e)
        {
        	unreachable();
        }
        
        replay();

        cb.rewindFormComponent(writer, cycle);

        verify();

        assertEquals(true, cb.getValue());
    }
}
