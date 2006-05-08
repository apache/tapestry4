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

import java.util.Collection;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.multipart.UploadPart;
import org.apache.tapestry.valid.ValidationConstants;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * {@link org.apache.tapestry.form.validator.Validator} that ensures a value was supplied.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class Required extends BaseValidator
{
    public Required()
    {
    }

    public Required(String initializer)
    {
        super(initializer);
    }

    public boolean getAcceptsNull()
    {
        return true;
    }

    public void validate(IFormComponent field, ValidationMessages messages, Object object)
            throws ValidatorException
    {
        if ((object == null)
                || (String.class.isInstance(object) && (((String) object).length() == 0))
                || (Collection.class.isInstance(object) && ((Collection) object).isEmpty())
                || (UploadPart.class.isInstance(object) && ((UploadPart) object).getSize() < 1))
        {
            String message = buildMessage(messages, field);
            throw new ValidatorException(message, ValidationConstraint.REQUIRED);
        }
    }

    private String buildMessage(ValidationMessages messages, IFormComponent field)
    {
        return messages.formatValidationMessage(
                getMessage(),
                ValidationStrings.REQUIRED_FIELD,
                new Object[]
                { field.getDisplayName() });
    }

    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
        context.registerForFocus(ValidationConstants.REQUIRED_FIELD);

        StringBuffer buffer = new StringBuffer("function(event) { Tapestry.require_field(event, '");
        buffer.append(field.getClientId());
        buffer.append("', ");
        buffer.append(TapestryUtils.enquote(buildMessage(context, field)));
        buffer.append("); }");

        context.addSubmitHandler(buffer.toString());
    }

    /**
     * Returns true, that's what Required means!
     */
    public boolean isRequired()
    {
        return true;
    }
}
