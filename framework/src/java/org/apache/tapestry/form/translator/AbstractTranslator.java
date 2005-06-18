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

import java.text.MessageFormat;
import java.util.Locale;

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.AbstractFormComponentContributor;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.ValidationStrings;
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
     *      java.lang.Object)
     */
    public String format(IFormComponent field, Object object)
    {
        return (object != null) ? formatObject(field, object) : "";
    }

    /**
     * @see org.apache.tapestry.form.translator.Translator#parse(org.apache.tapestry.form.IFormComponent,
     *      java.lang.String)
     */
    public Object parse(IFormComponent field, String text) throws ValidatorException
    {
        String value = _trim ? text.trim() : text;

        return HiveMind.isBlank(value) ? getEmpty() : parseText(field, value);
    }

    protected abstract String formatObject(IFormComponent field, Object object);

    protected abstract Object parseText(IFormComponent field, String text)
            throws ValidatorException;

    protected Object getEmpty()
    {
        return null;
    }

    protected String buildMessage(IFormComponent field, String key)
    {
        Locale locale = field.getPage().getLocale();

        String pattern = (_message == null) ? ValidationStrings.getMessagePattern(key, locale)
                : _message;

        String name = field.getDisplayName();

        return MessageFormat.format(pattern, getMessageParameters(locale, name));
    }

    protected Object[] getMessageParameters(Locale locale, String label)
    {
        return new Object[]
        { label };
    }

    /**
     * @see org.apache.tapestry.form.FormComponentContributor#renderContribution(org.apache.tapestry.IRequestCycle,
     *      org.apache.tapestry.form.IFormComponent)
     */
    public void renderContribution(IMarkupWriter writer, IRequestCycle cycle, FormComponentContributorContext context, IFormComponent field)
    {
        super.renderContribution(writer, cycle, context, field);

        if (_trim)
        {
            IForm form = field.getForm();

            addSubmitHandler(form, "trim(document." + form.getName() + "." + field.getName() + ")");
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
