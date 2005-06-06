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

import java.text.MessageFormat;

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Default {@link RequirableFieldSupport} implementation.  This implementation generates
 * calls to a static javascript function during render if client-side validation is enabled.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class RequirableFieldSupportImpl implements RequirableFieldSupport
{
    /**
     * @see org.apache.tapestry.form.RequirableFieldSupport#render(org.apache.tapestry.form.RequirableField, org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    public void render(RequirableField component, IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = component.getForm();
        
        if (component.isRequired() && form.isClientValidationEnabled())
        {
            String function = "require(document." + form.getName() + "." + component.getName() + ",'" + buildRequiredMessage(component) + "')";
            
            form.addEventHandler(FormEventType.SUBMIT, function);
        }
    }

    /**
     * @see org.apache.tapestry.form.RequirableFieldSupport#rewind(org.apache.tapestry.form.RequirableField, org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    public void rewind(RequirableField component, IMarkupWriter writer, IRequestCycle cycle)
    {
        try
        {
            String value = component.getSubmittedValue(cycle);
            
            if (component.isRequired() && HiveMind.isBlank(value))
            {
                throw new ValidatorException(buildRequiredMessage(component), ValidationConstraint.REQUIRED);
            }
            
            component.bind(writer, cycle);
        }
        catch (ValidatorException e)
        {
            component.getForm().getDelegate().record(e);
        }
    }
    
    protected String buildRequiredMessage(RequirableField component)
    {
        return MessageFormat.format(component.getRequiredMessage(), new Object[] { component.getDisplayName() });
    }
}
