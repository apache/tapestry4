//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.engine;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;

import org.apache.tapestry.IMessages;
import org.apache.tapestry.Tapestry;

/**
 *  Implementation of {@link org.apache.tapestry.IMessages}.  This is basically
 *  a wrapper around an instance of {@link Properties}.  This ensures
 *  that the properties are, in fact, read-only (which ensures that
 *  they don't have to be synchronized).
 *
 *  @author Howard Lewis Ship
 *  @since 2.0.4
 *
 **/

public class ComponentMessages implements IMessages
{
    private Properties _properties;
    private Locale _locale;

    public ComponentMessages(Locale locale, Properties properties)
    {
        _locale = locale;
        _properties = properties;
    }

    public String getMessage(String key, String defaultValue)
    {
        return _properties.getProperty(key, defaultValue);
    }

    public String getMessage(String key)
    {
        String result = _properties.getProperty(key);

        if (result == null)
            result = "[" + key.toUpperCase() + "]";

        return result;
    }

    public String format(String key, Object argument1, Object argument2, Object argument3)
    {
        return format(key, new Object[] { argument1, argument2, argument3 });
    }

    public String format(String key, Object argument1, Object argument2)
    {
        return format(key, new Object[] { argument1, argument2 });
    }

    public String format(String key, Object argument)
    {
        return format(key, new Object[] { argument });
    }

    public String format(String key, Object[] arguments)
    {
        String pattern = getMessage(key);

        // This ugliness is mandated for JDK 1.3 compatibility, which has a bug 
        // in MessageFormat ... the
        // pattern is applied in the constructor, using the system default Locale,
        // regardless of what locale is later specified!
        // It appears that the problem does not exist in JDK 1.4.

        MessageFormat messageFormat = new MessageFormat("");
        messageFormat.setLocale(_locale);
        messageFormat.applyPattern(pattern);

        int count = Tapestry.size(arguments);

        for (int i = 0; i < count; i++)
        {
            if (arguments[i] instanceof Throwable)
            {
                Throwable t = (Throwable) arguments[i];
                String message = t.getMessage();

                if (Tapestry.isNonBlank(message))
                    arguments[i] = message;
                else
                    arguments[i] = t.getClass().getName();
            }
        }

        return messageFormat.format(arguments);
    }

}
