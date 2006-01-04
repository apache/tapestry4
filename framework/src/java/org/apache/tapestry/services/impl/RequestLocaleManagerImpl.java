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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.hivemind.service.ThreadLocale;
import org.apache.tapestry.TapestryConstants;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.services.CookieSource;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.web.WebRequest;

/**
 * Service tapestry.request.RequestLocaleManager. Identifies the Locale provided by the client
 * (either in a Tapestry-specific cookie, or interpolated from the HTTP header.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class RequestLocaleManagerImpl implements RequestLocaleManager
{
    private WebRequest _request;

    /**
     * Extracted at start of request, and used at end of request to see if locale has changed.
     * Because of this thread-specific state, the service must use the threaded service lifecycle
     * model.
     */

    private Locale _requestLocale;

    private CookieSource _cookieSource;

    private ThreadLocale _threadLocale;

    /**
     * Set from symbol org.apache.tapestry.accepted-locales, a comma-seperated list of locale names.
     * The first name is the default for requests that can't be matched against the other locale
     * names. May also be blank, in which case, whatever locale was provided in the request is
     * accepted (which is Tapestry 3.0 behavior).
     */

    private String _acceptedLocales;

    private Locale _defaultLocale;

    /**
     * Set of locale names. Incoming requests will be matched to one of these locales.
     */

    private Set _acceptedLocaleNamesSet = new HashSet();

    /**
     * Cache of Locales, keyed on locale name.
     */

    private Map _localeCache = new HashMap();

    public void initializeService()
    {
        String[] names = TapestryUtils.split(_acceptedLocales);

        if (names.length == 0)
            return;

        _defaultLocale = getLocale(names[0]);

        _acceptedLocaleNamesSet.addAll(Arrays.asList(names));

    }

    public Locale extractLocaleForCurrentRequest()
    {
        String localeName = _cookieSource.readCookieValue(TapestryConstants.LOCALE_COOKIE_NAME);

        String requestedLocale = (localeName != null) ? localeName : _request.getLocale()
                .toString();

        _requestLocale = filterRequestedLocale(requestedLocale);

        _threadLocale.setLocale(_requestLocale);

        return _requestLocale;
    }

    /**
     * Converts the request locale name into a Locale instance; applies filters (based on
     * acceptedLocales) if enabled.
     */

    Locale filterRequestedLocale(String localeName)
    {
        if (_acceptedLocaleNamesSet.isEmpty())
            return getLocale(localeName);

        while (true)
        {
            if (_acceptedLocaleNamesSet.contains(localeName))
                return getLocale(localeName);

            localeName = stripTerm(localeName);

            if (localeName.length() == 0)
                return _defaultLocale;
        }
    }

    private String stripTerm(String localeName)
    {
        int scorex = localeName.lastIndexOf('_');

        return scorex < 0 ? "" : localeName.substring(0, scorex);
    }

    public void persistLocale()
    {
        Locale locale = _threadLocale.getLocale();

        if (locale.equals(_requestLocale))
            return;

        _cookieSource.writeCookieValue(TapestryConstants.LOCALE_COOKIE_NAME, locale.toString());
    }

    Locale getLocale(String name)
    {
        Locale result = (Locale) _localeCache.get(name);

        if (result == null)
        {
            result = constructLocale(name);
            _localeCache.put(name, result);
        }

        return result;
    }

    private Locale constructLocale(String name)
    {
        String[] terms = TapestryUtils.split(name, '_');

        switch (terms.length)
        {
            case 1:
                return new Locale(terms[0], "");

            case 2:
                return new Locale(terms[0], terms[1]);

            case 3:

                return new Locale(terms[0], terms[1], terms[2]);

            default:

                throw new IllegalArgumentException();
        }
    }

    public void setCookieSource(CookieSource source)
    {
        _cookieSource = source;
    }

    public void setRequest(WebRequest request)
    {
        _request = request;
    }

    public void setThreadLocale(ThreadLocale threadLocale)
    {
        _threadLocale = threadLocale;
    }

    public void setAcceptedLocales(String acceptedLocales)
    {
        _acceptedLocales = acceptedLocales;
    }
}