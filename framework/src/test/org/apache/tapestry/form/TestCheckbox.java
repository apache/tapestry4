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
import static org.testng.AssertJUnit.assertEquals;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Tests for the {@link org.apache.tapestry.form.Checkbox} component.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestCheckbox extends BaseFormComponentTestCase
{
    public void testRenderChecked()
    {
        Checkbox cb = (Checkbox) newInstance(Checkbox.class, new Object[]
        { "name", "assignedName", "value", Boolean.TRUE });

        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycle();

        replay();

        cb.renderFormComponent(writer, cycle);

        verify();

        assertBuffer("<input type=\"checkbox\" name=\"assignedName\" checked=\"checked\"/>");
    }

    public void testRenderDisabled()
    {
        Checkbox cb = (Checkbox) newInstance(Checkbox.class, new Object[]
        { "name", "assignedName", "disabled", Boolean.TRUE });

        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycle();

        replay();

        cb.renderFormComponent(writer, cycle);

        verify();

        assertBuffer("<input type=\"checkbox\" name=\"assignedName\" disabled=\"disabled\"/>");
    }

    public void testRenderInformalParameters()
    {
        Checkbox cb = (Checkbox) newInstance(Checkbox.class, new Object[]
        { "name", "assignedName", "value", Boolean.TRUE, "specification",
                new ComponentSpecification() });

        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycle();

        IBinding binding = newBinding("informal-value");

        cb.setBinding("informal", binding);

        replay();

        cb.renderFormComponent(writer, cycle);

        verify();

        assertBuffer("<input type=\"checkbox\" name=\"assignedName\" checked=\"checked\" informal=\"informal-value\"/>");
    }

    public void testRenderWithId()
    {
        Checkbox cb = (Checkbox) newInstance(Checkbox.class, new Object[]
        { "idParameter", "foo", "name", "assignedName", "value", Boolean.TRUE });

        IMarkupWriter writer = newBufferWriter();
        IRequestCycle cycle = newCycleGetUniqueId("foo", "foo$unique");

        replay();

        cb.renderFormComponent(writer, cycle);

        verify();

        assertBuffer("<input type=\"checkbox\" name=\"assignedName\" checked=\"checked\" id=\"foo$unique\"/>");
    }

    public void testSubmitNull()
    {
        ValidatableFieldSupport vfs = (ValidatableFieldSupport) newMock(ValidatableFieldSupport.class);
    	
        Checkbox cb = (Checkbox) newInstance(Checkbox.class, new Object[]
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
        
        Checkbox cb = (Checkbox) newInstance(Checkbox.class, new Object[]
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
        ValidatableFieldSupport vfs = (ValidatableFieldSupport) newMock(ValidatableFieldSupport.class);
        
        Checkbox cb = (Checkbox) newInstance(Checkbox.class, new Object[]
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
