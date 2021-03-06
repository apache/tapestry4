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

package org.apache.tapestry.form.validator;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import static org.easymock.EasyMock.*;

/**
 * Base class for writing {@link org.apache.tapestry.form.validator.Validator} tests.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public abstract class BaseValidatorTestCase extends BaseComponentTestCase
{
    protected IFormComponent newField(String displayName, boolean disabled)
    {
        IFormComponent field = newField(disabled);
        
        expect(field.getDisplayName()).andReturn(displayName);
        
        return field;
    }
    
    protected IFormComponent newField(String displayName)
    {
        IFormComponent field = newMock(IFormComponent.class);
        checkOrder(field, false);
        
        expect(field.getDisplayName()).andReturn(displayName);
        
        return field;
    }

    protected IFormComponent newField(String displayName, String clientId)
    {
        IFormComponent field = newMock(IFormComponent.class);
        checkOrder(field, false);
        
        expect(field.getClientId()).andReturn(clientId).anyTimes();
        expect(field.getDisplayName()).andReturn(displayName);
        
        return field;
    }
    
    protected IFormComponent newField(String displayName, String clientId, boolean disabled)
    {
        IFormComponent field = newField(disabled);
        
        checkOrder(field, false);
        
        expect(field.getClientId()).andReturn(clientId).anyTimes();
        expect(field.getDisplayName()).andReturn(displayName);
        
        return field;
    }
    
    protected IFormComponent newField(boolean disabled)
    {
        IFormComponent field = newField();
        expect(field.isDisabled()).andReturn(disabled);
        
        return field;
    }
    
    protected IFormComponent newField()
    {
        return newMock(IFormComponent.class);
    }

    protected ValidationMessages newMessages()
    {
        return newMock(ValidationMessages.class);
    }

    protected ValidationMessages newMessages(String messageOverride, String messageKey,
            Object[] parameters, String result)
    {
        ValidationMessages messages = newMock(ValidationMessages.class);
        
        trainFormatMessage(messages, messageOverride, messageKey, parameters, result);

        return messages;
    }

    protected void trainFormatMessage(ValidationMessages messages,
            String messageOverride, String messageKey, Object[] parameters, String result)
    {
        expect(messages.formatValidationMessage(eq(messageOverride), eq(messageKey), aryEq(parameters)))
        .andReturn(result);
    }

    protected FormComponentContributorContext newContext()
    {
        return newMock(FormComponentContributorContext.class);
    }

}
