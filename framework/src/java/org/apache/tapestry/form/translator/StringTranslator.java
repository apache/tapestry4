// Copyright 2005, 2006 The Apache Software Foundation
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

import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;

/**
 * A trivial {@link Translator} implementation. By default, empty text submissions are interpretted
 * as null.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class StringTranslator extends AbstractTranslator
{

    private String _empty = null;

    public StringTranslator()
    {
    }

    // Needed until HIVEMIND-134 fix is available

    public StringTranslator(String initializer)
    {
        PropertyUtils.configureProperties(this, initializer);
    }

    /**
     * @see org.apache.tapestry.form.translator.AbstractTranslator#parseText(org.apache.tapestry.form.IFormComponent,
     *      ValidationMessages, java.lang.String)
     */
    protected Object parseText(IFormComponent field, ValidationMessages messages, String text)
    {
        return text;
    }

    /**
     * @see org.apache.tapestry.form.translator.AbstractTranslator#formatObject(org.apache.tapestry.form.IFormComponent,
     *      Locale, java.lang.Object)
     */
    protected String formatObject(IFormComponent field, Locale locale, Object object)
    {
        return object.toString();
    }

    public Object getValueForEmptyInput()
    {
        return _empty;
    }

    public void setEmpty(String empty)
    {
        _empty = empty;
    }

}
