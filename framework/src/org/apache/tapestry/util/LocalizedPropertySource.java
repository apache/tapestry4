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

package org.apache.tapestry.util;

import java.util.Locale;

import org.apache.tapestry.engine.IPropertySource;

/**
 *  A PropertySource extending the DelegatingPropertySources and adding the 
 *  capability of searching for localized versions of the desired property.
 *  Useful when peoperties related to localization are needed.
 * 
 *  @author mindbridge
 *  @version $Id$
 *  @since 3.0
 */
public class LocalizedPropertySource extends DelegatingPropertySource
{
    private Locale _locale;
    
    /**
     *  Creates a LocalizedPropertySource with the default locale
     */
    public LocalizedPropertySource()
    {
        this(Locale.getDefault());
    }

    /**
     *  Creates a LocalizedPropertySource with the provided locale
     */
    public LocalizedPropertySource(Locale locale)
    {
        super();
        setLocale(locale);
    }

    /**
     *  Creates a LocalizedPropertySource with the default locale and the provided delegate
     */
    public LocalizedPropertySource(IPropertySource delegate)
    {
        this(Locale.getDefault(), delegate);
    }

    /**
     *  Creates a LocalizedPropertySource with the provided locale and delegate
     */
    public LocalizedPropertySource(Locale locale, IPropertySource delegate)
    {
        super(delegate);
        setLocale(locale);
    }


    /**
     * @return the locale currently used 
     */
    public Locale getLocale()
    {
        return _locale;
    }

    /**
     * @param locale the locale currently used
     */
    public void setLocale(Locale locale)
    {
        _locale = locale;
    }

    
    /**
     *  Examines the properties localized using the provided locale in the order
     *  of more specific to more general and returns the first that has a value. 
     *  @see org.apache.tapestry.util.DelegatingPropertySource#getPropertyValue(java.lang.String)
     */
    public String getPropertyValue(String propertyName)
    {
        LocalizedNameGenerator generator = new LocalizedNameGenerator(propertyName, getLocale(), "");

        while (generator.more())
        {
            String candidateName = generator.next();

            String value = super.getPropertyValue(candidateName); 
            if (value != null)
                return value;
        }

        return null;
    }

}
