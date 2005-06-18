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
import org.apache.tapestry.util.RegexpMatcher;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * {@link Validator} implementation that validates against a regular expression.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class RegExValidator extends AbstractFormComponentContributor implements Validator
{
    private RegexpMatcher _matcher = new RegexpMatcher();

    private String _expression = defaultExpression();

    /**
     * A custom message in the event of a validation failure.
     */
    private String _message;

    protected String defaultScript()
    {
        return "/org/apache/tapestry/form/validator/RegExValidator.js";
    }

    protected String defaultExpression()
    {
        return "";
    }

    /**
     * @see org.apache.tapestry.form.validator.Validator#validate(org.apache.tapestry.form.IFormComponent,
     *      ValidationMessages, java.lang.Object)
     */
    public void validate(IFormComponent field, ValidationMessages messages, Object object)
            throws ValidatorException
    {
        String string = (String) object;

        if (!_matcher.contains(_expression, string))
        {
            throw new ValidatorException(buildMessage(field), ValidationConstraint.PATTERN_MISMATCH);
        }
    }

    protected String buildMessage(IFormComponent field)
    {
        Locale locale = field.getPage().getLocale();
        String message = (_message == null) ? ValidationStrings.getMessagePattern(
                getMessageKey(),
                locale) : _message;

        return MessageFormat.format(message, new Object[]
        { field.getDisplayName(), _expression });
    }

    protected String getMessageKey()
    {
        return ValidationStrings.REGEX_MISMATCH;
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
        String expression = _matcher.getEscapedPatternString(_expression);
        String message = buildMessage(field);
        String handler = "validate_regex(event, document." + form.getName() + "." + field.getName()
                + ",'" + expression + "','" + message + "')";

        addSubmitHandler(form, handler);
    }

    /**
     * @return Returns the pattern.
     */
    public String getExpression()
    {
        return _expression;
    }

    /**
     * @param pattern
     *            The pattern to set.
     */
    public void setExpression(String expression)
    {
        _expression = expression;
    }

    /**
     * @return Returns the patternNotMatchedMessage.
     */
    public String getMessage()
    {
        return _message;
    }

    /**
     * @param patternNotMatchedMessage
     *            The patternNotMatchedMessage to set.
     */
    public void setMessage(String message)
    {
        _message = message;
    }

    public boolean getAcceptsNull()
    {
        return false;
    }
}
