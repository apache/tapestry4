// Copyright 2005, 2006 The Apache Software Foundation
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
import org.easymock.MockControl;

/**
 * Base class for writing {@link org.apache.tapestry.form.validator.Validator} tests.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public abstract class BaseValidatorTestCase extends BaseComponentTestCase
{
    protected IFormComponent newField(String displayName)
    {
        MockControl control = newControl(IFormComponent.class);
        IFormComponent field = (IFormComponent) control.getMock();

        field.getDisplayName();
        control.setReturnValue(displayName);

        return field;
    }

    protected IFormComponent newField(String displayName, String clientId)
    {
        MockControl control = MockControl.createNiceControl(IFormComponent.class);
        addControl(control);
        
        IFormComponent field = (IFormComponent) control.getMock();

        field.getClientId();
        control.setReturnValue(clientId);

        field.getDisplayName();
        control.setReturnValue(displayName);
        
        return field;
    }

    protected IFormComponent newField()
    {
        return (IFormComponent) newMock(IFormComponent.class);
    }

    protected ValidationMessages newMessages()
    {
        return (ValidationMessages) newMock(ValidationMessages.class);
    }

    protected ValidationMessages newMessages(String messageOverride, String messageKey,
            Object[] parameters, String result)
    {
        MockControl control = newControl(ValidationMessages.class);
        ValidationMessages messages = (ValidationMessages) control.getMock();

        trainFormatMessage(control, messages, messageOverride, messageKey, parameters, result);

        return messages;
    }

    protected void trainFormatMessage(MockControl control, ValidationMessages messages,
            String messageOverride, String messageKey, Object[] parameters, String result)
    {
        messages.formatValidationMessage(messageOverride, messageKey, parameters);
        control.setMatcher(MockControl.ARRAY_MATCHER);
        control.setReturnValue(result);
    }

    protected FormComponentContributorContext newContext()
    {
        return (FormComponentContributorContext) newMock(FormComponentContributorContext.class);
    }

}
