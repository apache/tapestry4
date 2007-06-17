// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

/**
 * Tests {@link PropertySelection}.
 * 
 */
@Test(sequential = true)
public class TestPropertySelection extends BaseFormComponentTestCase
{
    private static final String SYSTEM_NEWLINE= (String)java.security.AccessController.doPrivileged(
            new sun.security.action.GetPropertyAction("line.separator"));
    
    public void test_Rewind()
    {
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);

        IPropertySelectionModel model = new StringPropertySelectionModel(new String[] { "One", "Two", "Three" });
        
        PropertySelection component = newInstance(PropertySelection.class, 
                new Object[]  { 
            "validatableFieldSupport", vfs,
            "model", model
        });
        
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
        
        trainGetParameter(cycle, "barney", "1");
        
        try
        {
            vfs.validate(component, writer, cycle, model.translateValue("1"));
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();
        
        assertEquals(component.getValue(), "Two");
    }
    
    public void test_Render()
    {
        ValidatableFieldSupport vfs = newMock(ValidatableFieldSupport.class);
        
        IRequestCycle cycle = newCycle();
        IForm form = newMock(IForm.class);

        IMarkupWriter writer = newBufferWriter();

        MockDelegate delegate = new MockDelegate();

        IPropertySelectionModel model = new StringPropertySelectionModel(new String[] { "One", "Two", "Three" }, 
                new boolean[] {false, false, true});
        
        PropertySelection component = newInstance(PropertySelection.class,
                                                  "id", "hannah",
                                                  "validatableFieldSupport", vfs,
                                                  "model", model,
                                                  "value", "One",
                                                  "optionRenderer", DefaultOptionRenderer.DEFAULT_INSTANCE);
        
        expect(cycle.renderStackPush(component)).andReturn(component);
        
        trainGetForm(cycle, form);
        trainWasPrerendered(form, writer, component, false);
        trainGetDelegate(form, delegate);
        
        delegate.setFormComponent(component);

        trainGetElementId(form, component, "hannah");
        trainIsRewinding(form, false);
        trainIsRewinding(cycle, false);
        
        form.setFormFieldUpdating(true);
        
        delegate.setFormComponent(component);
        vfs.renderContributions(component, writer, cycle);
        
        expect(cycle.renderStackPop()).andReturn(component);
        
        replay();

        component.render(writer, cycle);

        verify();

        assertBuffer("<span class=\"prefix\"><select name=\"hannah\" id=\"hannah\" class=\"validation-delegate\">" + SYSTEM_NEWLINE +
                "<option value=\"0\" selected=\"selected\">One</option>" + SYSTEM_NEWLINE +
                "<option value=\"1\">Two</option>" + SYSTEM_NEWLINE +
                "<option value=\"2\" disabled=\"true\">Three</option>" + SYSTEM_NEWLINE +
                "</select></span>");
    }
}
