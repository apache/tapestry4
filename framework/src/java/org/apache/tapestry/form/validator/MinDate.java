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

import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Expects the value to be a {@link Date}, and constrains the date to follow a particular date.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */

public class MinDate extends BaseValidator
{
    private Date _minDate;

    public MinDate()
    {
    }

    public MinDate(String initializer)
    {
        super(initializer);
    }

    public void setMinDate(Date minDate)
    {
        _minDate = minDate;
    }

    public void validate(IFormComponent field, ValidationMessages messages, Object object)
            throws ValidatorException
    {
        Date date = (Date) object;

        if (date.before(_minDate))
            throw new ValidatorException(buildMessage(messages, field),
                    ValidationConstraint.TOO_SMALL);
    }

    private String buildMessage(ValidationMessages messages, IFormComponent field)
    {
        return messages.formatValidationMessage(
                getMessage(),
                ValidationStrings.DATE_TOO_EARLY,
                new Object[]
                { field.getDisplayName(), _minDate });
    }

}
