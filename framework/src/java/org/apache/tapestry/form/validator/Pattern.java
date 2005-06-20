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

import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.util.RegexpMatcher;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Validates a user input string against a regular expression pattern.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.util.RegexpMatcher
 */
public class Pattern implements Validator
{
    // TODO: Possible thread safety issue if the validator
    // is shared across threads, because the matcher
    // will be too.
    private RegexpMatcher _matcher = new RegexpMatcher();

    private String _pattern;

    private String _message;

    public Pattern()
    {
    }

    public Pattern(String initializer)
    {
        PropertyUtils.configureProperties(this, initializer);
    }

    public void validate(IFormComponent field, ValidationMessages messages, Object object)
            throws ValidatorException
    {
        String input = (String) object;

        if (!_matcher.matches(_pattern, input))
            throw new ValidatorException(buildMessage(messages, field),
                    ValidationConstraint.PATTERN_MISMATCH);
    }

    private String buildMessage(ValidationMessages messages, IFormComponent field)
    {
        return messages.formatValidationMessage(
                _message,
                ValidationStrings.REGEX_MISMATCH,
                new Object[]
                { field.getDisplayName() });
    }

    public boolean getAcceptsNull()
    {
        return false;
    }

    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
        context.includeClasspathScript("/org/apache/tapestry/form/validator/RegExValidator.js");

        String pattern = _matcher.getEscapedPatternString(_pattern);
        String message = buildMessage(context, field);

        StringBuffer buffer = new StringBuffer("function(event) { validate_regexp(event, ");
        buffer.append(context.getFieldDOM());
        buffer.append(", '");
        buffer.append(pattern);
        buffer.append("', '");
        buffer.append(message);
        buffer.append("'); }");

        context.addSubmitListener(buffer.toString());
    }

    public void setMessage(String message)
    {
        _message = message;
    }

    public void setPattern(String pattern)
    {
        _pattern = pattern;
    }

}
