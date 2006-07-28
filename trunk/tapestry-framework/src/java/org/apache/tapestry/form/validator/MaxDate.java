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

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.form.translator.DateTranslator;
import org.apache.tapestry.json.JSONLiteral;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.util.Strftime;
import org.apache.tapestry.valid.ValidationConstants;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Validates that the object, a Date, is not after a set maximum.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class MaxDate extends BaseValidator
{
    private Date _maxDate;

    public MaxDate()
    {
    }

    public MaxDate(String initializer)
    {
        super(initializer);
    }

    public void validate(IFormComponent field, ValidationMessages messages, Object object)
            throws ValidatorException
    {
        Date date = (Date) object;
        DateTranslator translator = (DateTranslator) getFieldTranslator(field, DateTranslator.class);
        
        if (date.after(_maxDate))
            throw new ValidatorException(buildMessage(messages, field, translator),
                    ValidationConstraint.TOO_LARGE);
        
    }
    
    private String buildMessage(ValidationMessages messages, IFormComponent field, 
            DateTranslator translator)
    {
        return messages.formatValidationMessage(
                getMessage(),
                ValidationStrings.DATE_TOO_LATE,
                new Object[]
                { field.getDisplayName(), 
                    (translator != null) ? 
                            translator.format(field, messages.getLocale(), _maxDate)
                            : _maxDate});
    }
    
    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
        // TODO: This is a little hacky, but validators need to be able to cooperate
        // with translators during client side validation as well
        DateTranslator translator = (DateTranslator) getFieldTranslator(field, DateTranslator.class);
        if (translator == null)
            return;
        
        JSONObject profile = context.getProfile();
        
        context.addInitializationScript(field, "dojo.require(\"tapestry.form.datetime\");");
        
        if (!profile.has(ValidationConstants.CONSTRAINTS)) {
            profile.put(ValidationConstants.CONSTRAINTS, new JSONObject());
        }
        JSONObject cons = profile.getJSONObject(ValidationConstants.CONSTRAINTS);
        
        accumulateProperty(cons, field.getClientId(), 
                new JSONLiteral("[tapestry.form.datetime.isValidDate,{"
                        + "max:" 
                        + JSONObject.quote(translator.format(field, context.getLocale(), _maxDate))
                        + ","
                        + "format:" 
                        + JSONObject.quote(Strftime.convertToPosixFormat(translator.getPattern()))
                        + "}]"));
        
        accumulateProfileProperty(field, profile, 
                ValidationConstants.CONSTRAINTS, buildMessage(context, field, translator));
    }
    
    public void setMaxDate(Date minDate)
    {
        _maxDate = minDate;
    }

}
