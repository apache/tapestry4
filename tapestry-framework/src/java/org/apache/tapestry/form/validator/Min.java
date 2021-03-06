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
import org.apache.tapestry.form.translator.NumberTranslator;
import org.apache.tapestry.json.JSONLiteral;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.valid.ValidationConstants;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Expects the object to be a number, and checks that the value not smaller than a specified value.
 *
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class Min extends BaseValidator
{
    private double _min;

    public Min()
    {
    }

    public Min(String initializer)
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

        if (_min > value.doubleValue())
            throw new ValidatorException(buildMessage(messages, field), ValidationConstraint.TOO_SMALL);
    }

    private String getStringValue(Locale locale, IFormComponent field)
    {
        String ret = null;
        NumberTranslator translator = (NumberTranslator)super.getFieldTranslator(field, NumberTranslator.class);

        if (translator != null)
            ret = translator.format(field, locale, new Double(_min));
        else
            ret = String.valueOf(_min);

        return ret;
    }

    private String buildMessage(ValidationMessages messages, IFormComponent field)
    {
        return messages.formatValidationMessage(
                getMessage(),
                ValidationStrings.VALUE_TOO_SMALL,
                new Object[] {
                        field.getDisplayName(), getStringValue(messages.getLocale(), field)
                });
    }

    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
                                   FormComponentContributorContext context, IFormComponent field)
    {
        JSONObject profile = context.getProfile();

        if (!profile.has(ValidationConstants.CONSTRAINTS)) {
            profile.put(ValidationConstants.CONSTRAINTS, new JSONObject());
        }
        JSONObject cons = profile.getJSONObject(ValidationConstants.CONSTRAINTS);

        String minString = getStringValue(context.getLocale(), field);
        String grouping = "";

        DecimalFormatSymbols symbols = null;
        NumberTranslator translator = (NumberTranslator)super.getFieldTranslator(field, NumberTranslator.class);

        if (translator != null) {
            DecimalFormat format = translator.getDecimalFormat(context.getLocale());

            if (format.isGroupingUsed()) {

                grouping += ",separator:" + JSONObject.quote(format.getDecimalFormatSymbols().getGroupingSeparator());
                grouping += ",groupSize:" + format.getGroupingSize();
            } else {

                grouping += ",separator:\"\"";
            }
            
            symbols = format.getDecimalFormatSymbols();
        } else {

            symbols = new DecimalFormatSymbols(context.getLocale());
        }

        accumulateProperty(cons, field.getClientId(),
                           new JSONLiteral("[tapestry.form.validation.greaterThanOrEqual,"
                                           + JSONObject.quote(minString)
                                           + ",{"
                                           + "decimal:" + JSONObject.quote(symbols.getDecimalSeparator())
                                           + grouping
                                           + "}]"));

        accumulateProfileProperty(field, profile, ValidationConstants.CONSTRAINTS, buildMessage(context, field));
    }

    public void setMin(double min)
    {
        _min = min;
    }

    public double getMin()
    {
        return _min;
    }
}
