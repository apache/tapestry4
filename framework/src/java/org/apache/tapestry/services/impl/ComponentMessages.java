// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.services.impl;

import java.util.Locale;
import java.util.Properties;

import org.apache.hivemind.impl.AbstractMessages;
import org.apache.hivemind.util.Defense;

/**
 * Implementation of {@link org.apache.hivemind.Messages}. This is basically a wrapper around an
 * instance of {@link Properties}. This ensures that the properties are, in fact, read-only (which
 * ensures that they don't have to be synchronized).
 * 
 * @author Howard Lewis Ship
 * @since 2.0.4
 */

public class ComponentMessages extends AbstractMessages
{
    private final Properties _properties;

    private final Locale _locale;

    public ComponentMessages(Locale locale, Properties properties)
    {
        Defense.notNull(locale, "locale");
        Defense.notNull(properties, "properties");

        _locale = locale;
        _properties = properties;
    }

    protected String findMessage(String key)
    {
        return _properties.getProperty(key);
    }

    protected Locale getLocale()
    {
        return _locale;
    }
}