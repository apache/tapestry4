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

import org.apache.hivemind.service.ThreadLocale;
import org.apache.tapestry.TapestryConstants;
import org.apache.tapestry.services.CookieSource;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.util.StringSplitter;
import org.apache.tapestry.web.WebRequest;

/**
 * Identifies the Locale provided by the client (either in a Tapestry-specific cookie, or
 * interpolated from the HTTP header. TODO: Add the ability to "filter down" Locales down to a
 * predifined set (specified using some form of HiveMInd configuration).
 * 
 * @author Howard Lewis Ship
 * @since 3.1
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

    public Locale extractLocaleForCurrentRequest()
    {
        String localeName = _cookieSource.readCookieValue(TapestryConstants.LOCALE_COOKIE_NAME);

        _requestLocale = (localeName != null) ? getLocale(localeName) : _request.getLocale();

        _threadLocale.setLocale(_requestLocale);

        return _requestLocale;
    }

    public void persistLocale()
    {
        Locale locale = _threadLocale.getLocale();

        if (locale.equals(_requestLocale))
            return;

        _cookieSource.writeCookieValue(TapestryConstants.LOCALE_COOKIE_NAME, locale.toString());
    }

    private Locale getLocale(String name)
    {
        // There used to be a cache of Locale (keyed on name), but since this service is
        // threaded, there's no point (short of making it static, which is too ugly for words).
        // Instead, we should have a LocaleCache service for that purpose. Have to balance
        // cost of invoking that service vs. the cost of creating new Locale instances all the time.

        return constructLocale(name);
    }

    private Locale constructLocale(String name)
    {
        StringSplitter splitter = new StringSplitter('_');
        String[] terms = splitter.splitToArray(name);

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
}