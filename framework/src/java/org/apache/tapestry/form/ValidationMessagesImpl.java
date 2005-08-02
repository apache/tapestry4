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

package org.apache.tapestry.form;

import java.text.MessageFormat;
import java.util.Locale;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.valid.ValidationStrings;

/**
 * Wrapper around
 * {@link org.apache.tapestry.valid.ValidationStrings#getMessagePattern(String, Locale)} and
 * {@link java.text.MessageFormat#format(java.lang.String, java.lang.Object[])}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class ValidationMessagesImpl implements ValidationMessages
{
    private final IFormComponent _field;

    private final Locale _locale;

    public ValidationMessagesImpl(IFormComponent field, Locale locale)
    {
        Defense.notNull(field, "field");
        Defense.notNull(locale, "locale");

        _field = field;
        _locale = locale;
    }

    public String formatValidationMessage(String messageOverride, String messageKey,
            Object[] arguments)
    {
        String message = extractLocalizedMessage(messageOverride, messageKey);

        return MessageFormat.format(message, arguments);
    }

    private String extractLocalizedMessage(String messageOverride, String messageKey)
    {
        if (messageOverride == null)
            return ValidationStrings.getMessagePattern(messageKey, _locale);

        if (messageOverride.startsWith("%"))
        {
            String key = messageOverride.substring(1);

            return _field.getContainer().getMessages().getMessage(key);
        }

        // Otherwise, a literal string

        return messageOverride;
    }
}
