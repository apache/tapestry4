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
import org.apache.tapestry.json.JSONLiteral;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.util.RegexpMatcher;
import org.apache.tapestry.valid.ValidationConstants;
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
public class Pattern extends BaseValidator
{
    // It is expectd that each Pattern instance will be used by a single component instance,
    // and therefore be restricted to a single thread.
    
    private RegexpMatcher _matcher = new RegexpMatcher();

    private String _pattern;

    public Pattern()
    {
    }

    public Pattern(String initializer)
    {
        super(initializer);
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
                getMessage(),
                ValidationStrings.REGEX_MISMATCH,
                new Object[]
                { _pattern, field.getDisplayName() });
    }
    
    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
        String pattern = _matcher.getEscapedPatternString(_pattern);
        
        JSONObject profile = context.getProfile();
        
        if (!profile.has(ValidationConstants.CONSTRAINTS)) {
            profile.put(ValidationConstants.CONSTRAINTS, new JSONObject());
        }
        JSONObject cons = profile.getJSONObject(ValidationConstants.CONSTRAINTS);
        
        accumulateProperty(cons, field.getClientId(), 
                new JSONLiteral("[tapestry.form.validation.isValidPattern,\""
                        + pattern + "\"]"));
        
        accumulateProfileProperty(field, profile, 
                ValidationConstants.CONSTRAINTS, buildMessage(context, field));
    }

    public void setPattern(String pattern)
    {
        _pattern = pattern;
    }

}
