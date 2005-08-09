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
 * Validates that the input value is not larger than a particular maximum value.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class Max extends BaseValidator
{
    private double _max;

    public Max()
    {
    }

    public Max(String initializer)
    {
        super(initializer);
    }

    /**
     * Does comparison based on the {@link Number#doubleValue()}.
     */

    public void validate(IFormComponent field, ValidationMessages messages, Object object)
            throws ValidatorException
    {
        Number value = (Number) object;

        if (value.doubleValue() > _max)
            throw new ValidatorException(buildMessage(messages, field),
                    ValidationConstraint.TOO_LARGE);
    }

    private String buildMessage(ValidationMessages messages, IFormComponent field)
    {
        return messages.formatValidationMessage(
                getMessage(),
                ValidationStrings.VALUE_TOO_LARGE,
                new Object[]
                { field.getDisplayName(), new Double(_max) });
    }

    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
        context.includeClasspathScript("/org/apache/tapestry/form/validator/NumberValidator.js");

        String message = buildMessage(context, field);

        StringBuffer buffer = new StringBuffer("function(event) { validate_max_number(event, ");
        buffer.append(context.getFieldDOM());
        buffer.append(", ");
        buffer.append(_max);
        buffer.append(", '");
        buffer.append(ValidatorUtils.escapeReservedCharacters(message));
        buffer.append("'); }");

        context.addSubmitListener(buffer.toString());
    }

    public void setMax(double max)
    {
        _max = max;
    }

}
