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

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Validator that ensures a string value does not exceed a maximum length.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class MaxLength extends BaseValidator
{
    private int _maxLength;

    public MaxLength()
    {

    }

    public MaxLength(String initializer)
    {
        super(initializer);
    }

    public void setMaxLength(int maxLength)
    {
        _maxLength = maxLength;
    }

    public void validate(IFormComponent field, ValidationMessages messages, Object object)
            throws ValidatorException
    {
        String string = (String) object;

        if (string.length() > _maxLength)
            throw new ValidatorException(buildMessage(messages, field),
                    ValidationConstraint.MAXIMUM_WIDTH);
    }

    protected String buildMessage(ValidationMessages messages, IFormComponent field)
    {
        return messages.formatValidationMessage(
                getMessage(),
                ValidationStrings.VALUE_TOO_LONG,
                new Object[]
                { new Integer(_maxLength), field.getDisplayName() });
    }

    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
        context.includeClasspathScript("/org/apache/tapestry/form/validator/StringValidator.js");
        
        StringBuffer buffer = new StringBuffer("function(event) { validate_max_length(event, ");
        buffer.append(context.getFieldDOM());
        buffer.append(", ");
        buffer.append(_maxLength);
        buffer.append(", '");
        buffer.append(buildMessage(context, field));
        buffer.append("'); }");

        context.addSubmitListener(buffer.toString());
    }

}
