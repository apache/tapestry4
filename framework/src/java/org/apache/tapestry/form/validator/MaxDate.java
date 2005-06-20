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

import java.util.Date;

import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Validates that the object, a Date, is not after a set maximum.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class MaxDate implements Validator
{
    private String _message;

    private Date _maxDate;

    public MaxDate()
    {
    }

    public MaxDate(String initializer)
    {
        PropertyUtils.configureProperties(this, initializer);
    }

    /** Returns false. */
    public boolean getAcceptsNull()
    {
        return false;
    }

    public void validate(IFormComponent field, ValidationMessages messages, Object object)
            throws ValidatorException
    {
        Date date = (Date) object;

        if (date.after(_maxDate))
            throw new ValidatorException(buildMessage(messages, field),
                    ValidationConstraint.TOO_LARGE);

    }

    private String buildMessage(ValidationMessages messages, IFormComponent field)
    {
        return messages.formatValidationMessage(
                _message,
                ValidationStrings.DATE_TOO_LATE,
                new Object[]
                { field.getDisplayName(), _maxDate });
    }

    /**
     * Does nothing.
     */

    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
    }

    public void setMessage(String message)
    {
        _message = message;
    }

    public void setMaxDate(Date minDate)
    {
        _maxDate = minDate;
    }

}
