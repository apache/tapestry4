// Copyright 2004 The Apache Software Foundation
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry.ApplicationServlet;
import org.apache.tapestry.services.CookieSource;
import org.apache.tapestry.services.LocaleExtractor;
import org.apache.tapestry.util.StringSplitter;

/**
 * Identifies the Locale provided by the client (either in a Tapestry-specific
 * cookie, or interpolated from the HTTP header.
 * 
 * TODO: Add the ability to "filter down" Locales down to a predifined set
 * (specified using some form of HiveMInd configuration).
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class LocaleExtractorImpl implements LocaleExtractor
{
    private HttpServletRequest _request;
    private CookieSource _cookieSource;
    private Map _localeMap = Collections.synchronizedMap(new HashMap());

    public Locale extractLocaleForCurrentRequest()
    {
        String localeName = _cookieSource.getCookieValue(ApplicationServlet.LOCALE_COOKIE_NAME);

        if (localeName != null)
            return getLocale(localeName);

        return _request.getLocale();
    }

    private Locale getLocale(String name)
    {
        Locale result = (Locale) _localeMap.get(name);

        if (result == null)
        {
            result = constructLocale(name);
            _localeMap.put(name, result);
        }

        return result;
    }

    private Locale constructLocale(String name)
    {
        StringSplitter splitter = new StringSplitter('_');
        String[] terms = splitter.splitToArray(name);

        switch (terms.length)
        {
            case 1 :
                return new Locale(terms[0], "");

            case 2 :
                return new Locale(terms[0], terms[1]);

            case 3 :

                return new Locale(terms[0], terms[1], terms[2]);

            default :

                throw new IllegalArgumentException();
        }
    }
    
    public void setCookieSource(CookieSource source)
    {
        _cookieSource = source;
    }

    public void setRequest(HttpServletRequest request)
    {
        _request = request;
    }

}
