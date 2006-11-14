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

import java.util.Locale;

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.AbstractFormComponentContributor;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.valid.ValidationConstants;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Abstract {@link Translator} implementation that provides default behavior for trimming, null
 * object, and empty text handling.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public abstract class AbstractTranslator extends AbstractFormComponentContributor implements
        Translator
{
    private boolean _trim;

    private String _message;

    public AbstractTranslator()
    {
    }

    // Needed until HIVEMIND-134 fix is available
    public AbstractTranslator(String initializer)
    {
        super(initializer);
    }

    /**
     * @see org.apache.tapestry.form.translator.Translator#format(org.apache.tapestry.form.IFormComponent,
     *      Locale, java.lang.Object)
     */
    public String format(IFormComponent field, Locale locale, Object object)
    {
        if (object == null)
            return "";

        return formatObject(field, locale, object);
    }

    /**
     * @see org.apache.tapestry.form.translator.Translator#parse(org.apache.tapestry.form.IFormComponent,
     *      ValidationMessages, java.lang.String)
     */
    public Object parse(IFormComponent field, ValidationMessages messages, String text)
            throws ValidatorException
    {
        String value = text == null ? null : (_trim ? text.trim() : text);

        return HiveMind.isBlank(value) ? getValueForEmptyInput()
                : parseText(field, messages, value);
    }

    protected abstract String formatObject(IFormComponent field, Locale locale, Object object);

    protected abstract Object parseText(IFormComponent field, ValidationMessages messages,
            String text) throws ValidatorException;

    /**
     * The value to be used when the value supplied in the request is blank (null or empty). The
     * default value is null, but some subclasses may override.
     * 
     * @see #parse(IFormComponent, ValidationMessages, String)
     * @return null, subclasses may override
     */
    protected Object getValueForEmptyInput()
    {
        return null;
    }

    protected String buildMessage(ValidationMessages messages, IFormComponent field, String key)
    {
        String label = field.getDisplayName();
        
        Object[] parameters = getMessageParameters(messages.getLocale(), label);
        
        return messages.formatValidationMessage(_message, key, parameters);
    }

    protected Object[] getMessageParameters(Locale locale, String label)
    {
        return new Object[] { label };
    }

    /**
     * @see org.apache.tapestry.form.FormComponentContributor#renderContribution(IMarkupWriter, IRequestCycle, FormComponentContributorContext, IFormComponent)
     */
    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
            FormComponentContributorContext context, IFormComponent field)
    {
        super.renderContribution(writer, cycle, context, field);
        
        if (_trim) {
            JSONObject profile = context.getProfile();
            
            accumulateProperty(profile, ValidationConstants.TRIM, field.getClientId());
        }
    }

    public boolean isTrim()

    {
        return _trim;
    }

    public void setTrim(boolean trim)
    {
        _trim = trim;
    }

    public String getMessage()
    {
        return _message;
    }

    public void setMessage(String message)
    {
        _message = message;
    }
}
