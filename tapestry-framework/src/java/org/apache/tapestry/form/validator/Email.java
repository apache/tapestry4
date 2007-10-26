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
 * Validates that the user input, a string, is an email address (by checking it against a regular
 * expression).
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class Email extends BaseValidator
{
    public static final String TLD_PATTERN = "arpa|aero|biz|com|coop|edu|gov|info|int|mil|museum|name|net|"
            + "org|pro|travel|xxx|jobs|mobi|post|"
            + "ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|"
            + "bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|"
            + "ec|ee|eg|er|eu|es|et|fi|fj|fk|fm|fo|fr|ga|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|"
            + "gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kr|kw|ky|kz|"
            + "la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|"
            + "my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|"
            + "re|ro|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sk|sl|sm|sn|sr|st|su|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tm|"
            + "tn|to|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw";
    public static final String DOMAIN_PATTERN = "([0-9a-z]([-0-9a-z]{0,61}[0-9a-z])?\\.)+" + "(" + TLD_PATTERN + ")";
    public static final String USERNAME_PATTERN = "([-/!\\#$*?=_+&'\\da-z]+[.])*[-/!\\#$*?=_+&'\\da-z]+";
    public static final String PATTERN = "^(?i)"+ USERNAME_PATTERN + "@" + "(" + DOMAIN_PATTERN + ")$";
                
    // TODO: Possible thread safety issue if the validator
    // is shared across threads, because the matcher
    // will be too.
    
    private RegexpMatcher _matcher = new RegexpMatcher();
    
    public Email()
    {
    }
    
    public Email(String initializer)
    {
        super(initializer);
    }
    
    public void validate(IFormComponent field, ValidationMessages messages, Object object)
            throws ValidatorException
    {
        String input = (String) object;

        if (!_matcher.matches(PATTERN, input))
            throw new ValidatorException(buildMessage(messages, field),
                    ValidationConstraint.EMAIL_FORMAT);
    }

    private String buildMessage(ValidationMessages messages, IFormComponent field)
    {
        return messages.formatValidationMessage(
                getMessage(),
                ValidationStrings.INVALID_EMAIL,
                new Object[]
                { field.getDisplayName() });
    }
    
    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
        context.addInitializationScript(field, "dojo.require(\"dojo.validate.web\");");
        
        JSONObject profile = context.getProfile();
        
        if (!profile.has(ValidationConstants.CONSTRAINTS)) {
            profile.put(ValidationConstants.CONSTRAINTS, new JSONObject());
        }
        JSONObject cons = profile.getJSONObject(ValidationConstants.CONSTRAINTS);
        
        accumulateProperty(cons, field.getClientId(),
                new JSONLiteral("[tapestry.form.validation.isEmailAddress,false,true]"));
        
        accumulateProfileProperty(field, profile, 
                ValidationConstants.CONSTRAINTS, buildMessage(context, field));
    }
}
