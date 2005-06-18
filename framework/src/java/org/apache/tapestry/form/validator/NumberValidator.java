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

import java.text.MessageFormat;
import java.util.Locale;

import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.AbstractFormComponentContributor;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * {@link Validator} implementation that validates a number against max and min values.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class NumberValidator extends AbstractFormComponentContributor implements Validator
{
    private Number _min;

    private Number _max;

    private String _tooSmallMessage;

    private String _tooLargeMessage;

    protected String defaultScript()
    {
        return "/org/apache/tapestry/form/validator/NumberValidator.js";
    }

    public boolean getAcceptsNull()
    {
        return false;
    }

    /**
     * @see org.apache.tapestry.form.validator.Validator#validate(org.apache.tapestry.form.IFormComponent,
     *      ValidationMessages, java.lang.Object)
     */
    public void validate(IFormComponent field, ValidationMessages messages, Object object)
            throws ValidatorException
    {
        Number value = (Number) object;

        if ((_min != null) && ((_min.doubleValue() - value.doubleValue()) > 0))
        {
            throw new ValidatorException(buildTooSmallMessage(field),
                    ValidationConstraint.TOO_SMALL);
        }

        if ((_max != null) && ((_max.doubleValue() - value.doubleValue()) < 0))
        {
            throw new ValidatorException(buildTooLargeMessage(field),
                    ValidationConstraint.TOO_LARGE);
        }
    }

    protected String buildTooSmallMessage(IFormComponent field)
    {
        return buildMessage(field, _tooSmallMessage, ValidationStrings.VALUE_TOO_SMALL, _min);
    }

    protected String buildTooLargeMessage(IFormComponent field)
    {
        return buildMessage(field, _tooLargeMessage, ValidationStrings.VALUE_TOO_LARGE, _max);
    }

    protected String buildMessage(IFormComponent field, String messageOverride, String key,
            Number object)
    {
        Locale locale = field.getPage().getLocale();
        String message = (messageOverride == null) ? ValidationStrings.getMessagePattern(
                key,
                locale) : messageOverride;

        return MessageFormat.format(message, new Object[]
        { field.getDisplayName(), object });
    }

    /**
     * @return Returns the max.
     */
    public Number getMax()
    {
        return _max;
    }

    /**
     * @param max
     *            The max to set.
     */
    public void setMax(Number max)
    {
        _max = max;
    }

    /**
     * @return Returns the min.
     */
    public Number getMin()
    {
        return _min;
    }

    /**
     * @param min
     *            The min to set.
     */
    public void setMin(Number min)
    {
        _min = min;
    }

    /**
     * @return Returns the numberTooLargeMessage.
     */
    public String getTooLargeMessage()
    {
        return _tooLargeMessage;
    }

    /**
     * @param message
     *            The tooLargeMessage to set.
     */
    public void setTooLargeMessage(String message)
    {
        _tooLargeMessage = message;
    }

    /**
     * @return Returns the tooSmallMessage.
     */
    public String getTooSmallMessage()
    {
        return _tooSmallMessage;
    }

    /**
     * @param message
     *            The tooSmallMessage to set.
     */
    public void setTooSmallMessage(String message)
    {
        _tooSmallMessage = message;
    }

    /**
     * @see org.apache.tapestry.form.AbstractFormComponentContributor#renderContribution(org.apache.tapestry.IMarkupWriter,
     *      org.apache.tapestry.IRequestCycle, FormComponentContributorContext,
     *      org.apache.tapestry.form.IFormComponent)
     */
    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
        super.renderContribution(writer, cycle, context, field);

        IForm form = field.getForm();
        String formName = form.getName();
        String fieldName = field.getName();

        if (_min != null)
        {
            String message = buildTooSmallMessage(field);
            String handler = "validate_min_number(event, document." + formName + "." + fieldName
                    + "," + _min + ",'" + message + "')";

            super.addSubmitHandler(form, handler);
        }

        if (_max != null)
        {
            String message = buildTooLargeMessage(field);
            String handler = "validate_max_number(event, document." + formName + "." + fieldName
                    + "," + _max + ",'" + message + "')";

            super.addSubmitHandler(form, handler);
        }
    }
}
