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

package org.apache.tapestry.services.impl;

import java.util.Locale;

import org.apache.hivemind.util.Defense;
import org.apache.hivemind.util.LocalizedNameGenerator;
import org.apache.tapestry.engine.IPropertySource;

/**
 * Wraps around a {@link org.apache.tapestry.engine.IPropertySource}to query a series of localized
 * keys.
 * <p>
 * This is much simpler than the old {@link org.apache.tapestry.util.LocalizedPropertySource}, and
 * allows the locale to be specified on a thread-safe, per-invocation basis.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class LocalizedPropertySource
{
    private IPropertySource _source;

    public LocalizedPropertySource(IPropertySource source)
    {
        Defense.notNull(source, "source");

        _source = source;
    }

    /**
     * Get the property from the source, trying different variations of propertyName (adding
     * suffixes).
     * 
     * @param propertyName
     *            the base property name to search with.
     * @param local
     *            the Locale used to generate suffixes (may be null).
     */

    public String getPropertyValue(String propertyName, Locale locale)
    {
        Defense.notNull(propertyName, "propertyName");

        LocalizedNameGenerator g = new LocalizedNameGenerator(propertyName, locale, "");

        while (g.more())
        {
            String localizedName = g.next();

            String result = _source.getPropertyValue(localizedName);

            if (result != null)
                return result;
        }

        return null;
    }
}