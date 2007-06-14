// Copyright 2007 The Apache Software Foundation
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

package org.apache.tapestry.form.validator;

import java.util.Locale;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.form.ValidationMessagesImpl;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.checkOrder;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.validator.Identity}.
 * 
 * @since 4.1.2
 */
@Test
public class TestIdentity extends BaseValidatorTestCase
{
    public void testOK() throws Exception
    {
        IFormComponent field = newField();
        IFormComponent otherField = newField();                
        
        trainGetContainerAndComponent(field, "newPass", otherField);
        trainGetValueBinding(otherField, "pass");        
        
        ValidationMessages messages = newMessages();

        replay();

        new Identity("match=newPass").validate(field, messages, "pass");

        verify();
    }

    public void testFail()
    {
        IFormComponent field = newField();
        IFormComponent otherField = newField();                
        
        trainGetContainerAndComponent(field, "newPass", otherField);
        trainGetValueBinding(otherField, "pass");
        expect(field.getDisplayName()).andReturn("Password-1");
        expect(otherField.getDisplayName()).andReturn("Password-2");
        
        ValidationMessages messages = newMessages();
        trainIdentityMessages(messages, null, "Password-1", "Password-2", 1, "err1");        

        replay();

        try
        {
            new Identity("match=newPass").validate(field, messages, "passOTHER");
        }
        catch (ValidatorException ex)
        {
            assertEquals(ex.getMessage(), "err1");
            assertEquals(ex.getConstraint(), ValidationConstraint.CONSISTENCY);
        }
    }

    public void testFailCustomMessage()
    {
        IFormComponent field = newField();
        IFormComponent otherField = newField();                
        
        trainGetContainerAndComponent(field, "newPass", otherField);
        trainGetValueBinding(otherField, "pass");
        expect(field.getDisplayName()).andReturn("Password-1");
        expect(otherField.getDisplayName()).andReturn("Password-2");
        
        ValidationMessages messages = newMessages();
        String msgOverride = "Should differ!";
        trainIdentityMessages(messages, msgOverride, "Password-1", "Password-2", 0, msgOverride);

        replay();

        try
        {
            new Identity("differ=newPass,message=Should differ!").validate(field, messages, "pass");
        }
        catch (ValidatorException ex)
        {
            assertEquals(ex.getMessage(), msgOverride);
            assertEquals(ex.getConstraint(), ValidationConstraint.CONSISTENCY);
        }
    }

    public void test_Render_Contribution()
    {
        JSONObject json = new JSONObject();
        
        IFormComponent field = newField("Password", "pass1");
        expect(field.isDisabled()).andReturn(false);
        
        IFormComponent otherField = newField("Verify Password", "pass2");
        trainGetContainerAndComponent(field, "other", otherField);
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);        
        expect(context.getProfile()).andReturn(json);        
                
        trainIdentityMessages(context, null, "Password", "Verify Password", 1, "Fields must match");        
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        replay();
        
        new Identity("match=other").renderContribution(writer, cycle, context, field);
        
        verify();
        
        assertEquals("{\"constraints\":{\"pass1\":[[tapestry.form.validation.isEqual,\"pass2\"]]},"
                + "\"pass1\":{\"constraints\":[\"Fields must match\"]}}",
                json.toString());
    }    
    
    public void testNotRequired()
    {
        assertEquals(false, new Identity().isRequired());
    }
    
    private void trainGetContainerAndComponent(IFormComponent field, String name, IFormComponent other) {
        IComponent container = newComponent();
        expect(field.getContainer()).andReturn(container);
        expect(container.getComponent(name)).andReturn(other);        
    }
    
    private void trainGetValueBinding(IFormComponent field, String value) {
        IBinding ret = newBinding(value);
        expect(field.getBinding("value")).andReturn(ret);
    }   

    private void trainIdentityMessages(ValidationMessages messages, String msgOverride,
            String name1, String name2, int match, String result)
    {
        trainFormatMessage(messages, msgOverride, "invalid-field-equality", 
                new Object[]{ name1, new Integer(match), name2 }, 
                result);
    }    
}
