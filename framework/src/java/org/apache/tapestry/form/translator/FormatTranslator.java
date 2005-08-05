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

import java.text.Format;
import java.text.ParseException;
import java.util.Locale;

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Abstract {@link Translator} implementation for {@link java.text.Format}-based translators.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public abstract class FormatTranslator extends AbstractTranslator
{
    private String _pattern;

    protected abstract String defaultPattern();

    /**
     * @see org.apache.tapestry.form.translator.AbstractTranslator#formatObject(org.apache.tapestry.form.IFormComponent,
     *      java.lang.Object)
     */
    protected String formatObject(IFormComponent field, Object object)
    {
        // Get a new format each time, because (a) have to account for locale and (b) formatters are
        // not thread safe.

        Format format = getFormat(field.getPage().getLocale());

        return format.format(object);
    }

    /**
     * @see org.apache.tapestry.form.translator.AbstractTranslator#parseText(org.apache.tapestry.form.IFormComponent,
     *      java.lang.String)
     */
    protected Object parseText(IFormComponent field, String text) throws ValidatorException
    {
        Format format = getFormat(field.getPage().getLocale());

        try
        {
            return format.parseObject(text);
        }
        catch (ParseException e)
        {
            throw new ValidatorException(buildMessage(field, getMessageKey()), getConstraint());
        }
    }

    protected abstract ValidationConstraint getConstraint();

    protected abstract Format getFormat(Locale locale);

    protected abstract String getMessageKey();

    public String getPattern()
    {
        return _pattern;
    }

    public void setPattern(String pattern)
    {
        _pattern = pattern;
    }

    public FormatTranslator()
    {
    	_pattern = defaultPattern();
    }

    // Needed until HIVEMIND-134 fix is available
    public FormatTranslator(String initializer)
    {
        super(initializer);
        
        if (HiveMind.isBlank(_pattern))
        {
        	_pattern = defaultPattern();
        }
    }
}
