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

package org.apache.tapestry.form.translator;

import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.json.JSONLiteral;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.valid.ValidationConstants;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;

/**
 * A {@link java.text.SimpleDateFormat}-based {@link Translator} implementation.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class DateTranslator extends FormatTranslator
{
    private boolean _lenient=true;
    
    public DateTranslator()
    {
    }
    
    // Needed until HIVEMIND-134 fix is available
    public DateTranslator(String initializer)
    {
        PropertyUtils.configureProperties(this, initializer);
    }
    
    /**
     * @see org.apache.tapestry.form.translator.FormatTranslator#defaultPattern()
     */
    protected String defaultPattern()
    {
        return "MM/dd/yyyy";
    }
    
    /**
     * @see org.apache.tapestry.form.translator.FormatTranslator#getFormat(java.util.Locale)
     */
    protected Format getFormat(Locale locale)
    {
        return getDateFormat(locale);
    }
    
    public SimpleDateFormat getDateFormat(Locale locale)
    {
        SimpleDateFormat ret = new SimpleDateFormat(getPattern(), new DateFormatSymbols(locale));
        ret.setLenient(_lenient);
        
        return ret;
    }
    
    /**
     * @see org.apache.tapestry.form.translator.FormatTranslator#getMessageKey()
     */
    protected String getMessageKey()
    {
        return ValidationStrings.INVALID_DATE;
    }
    
    /**
     * @see org.apache.tapestry.form.translator.AbstractTranslator#getMessageParameters(java.util.Locale,
     *      java.lang.String)
     */
    protected Object[] getMessageParameters(Locale locale, String label)
    {
        String pattern = getDateFormat(locale).toLocalizedPattern().toUpperCase(locale);
        
        return new Object[] { label, pattern };
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
        super.renderContribution(writer, cycle, context, field);
        
        String message = buildMessage(context, field, getMessageKey());
        
        JSONObject profile = context.getProfile();
        if (!profile.has(ValidationConstants.CONSTRAINTS)) {
            profile.put(ValidationConstants.CONSTRAINTS, new JSONObject());
        }
        
        JSONObject cons = profile.getJSONObject(ValidationConstants.CONSTRAINTS);
        
        context.addInitializationScript(field, "dojo.require(\"tapestry.form.datetime\");");
        
        accumulateProperty(cons, field.getClientId(), 
                new JSONLiteral("[tapestry.form.datetime.isValidDate,{"
                        + "datePattern:" 
                        + JSONObject.quote(getPattern())
                        + (isLenient() ? "" : ",strict:true")
                        + "}]"));
        
        accumulateProfileProperty(field, profile, ValidationConstants.CONSTRAINTS, message);
    }
    
    /**
     * @see org.apache.tapestry.form.translator.FormatTranslator#getConstraint()
     */
    protected ValidationConstraint getConstraint()
    {
        return ValidationConstraint.DATE_FORMAT;
    }
    
    public void setLenient(boolean value)
    {
        _lenient = value;
    }
    
    public boolean isLenient()
    {
        return _lenient;
    }
}
