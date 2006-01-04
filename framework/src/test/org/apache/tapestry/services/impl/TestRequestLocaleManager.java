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
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.TapestryConstants;
import org.apache.tapestry.services.CookieSource;
import org.apache.tapestry.web.WebRequest;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.RequestLocaleManagerImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestRequestLocaleManager extends HiveMindTestCase
{
    private ThreadLocale newThreadLocale()
    {
        return (ThreadLocale) newMock(ThreadLocale.class);
    }

    private ThreadLocale newThreadLocale(Locale locale)
    {
        MockControl control = newControl(ThreadLocale.class);
        ThreadLocale threadLocale = (ThreadLocale) control.getMock();

        threadLocale.getLocale();
        control.setReturnValue(locale);

        return threadLocale;
    }

    public void testSuppliedByRequest()
    {
        MockControl sourceControl = newControl(CookieSource.class);
        CookieSource source = (CookieSource) sourceControl.getMock();

        MockControl requestControl = newControl(WebRequest.class);
        WebRequest request = (WebRequest) requestControl.getMock();

        ThreadLocale tl = newThreadLocale();

        // Training

        source.readCookieValue(TapestryConstants.LOCALE_COOKIE_NAME);
        sourceControl.setReturnValue(null);

        request.getLocale();
        requestControl.setReturnValue(Locale.JAPANESE);

        tl.setLocale(Locale.JAPANESE);

        replayControls();

        RequestLocaleManagerImpl manager = new RequestLocaleManagerImpl();
        manager.setCookieSource(source);
        manager.setRequest(request);
        manager.setThreadLocale(tl);

        Locale actual = manager.extractLocaleForCurrentRequest();

        assertEquals(Locale.JAPANESE, actual);

        verifyControls();
    }

    private void attempt(String localeName, Locale expectedLocale)
    {
        MockControl sourceControl = newControl(CookieSource.class);
        CookieSource source = (CookieSource) sourceControl.getMock();

        ThreadLocale tl = newThreadLocale();

        // Training

        source.readCookieValue(TapestryConstants.LOCALE_COOKIE_NAME);
        sourceControl.setReturnValue(localeName);

        tl.setLocale(expectedLocale);

        replayControls();

        RequestLocaleManagerImpl manager = new RequestLocaleManagerImpl();
        manager.setCookieSource(source);
        manager.setThreadLocale(tl);

        Locale actual = manager.extractLocaleForCurrentRequest();

        assertEquals(expectedLocale, actual);

        verifyControls();
    }

    public void testJustLanguage()
    {
        attempt("en", Locale.ENGLISH);
    }

    public void testLanguageAndCountry()
    {
        attempt("fr_FR", Locale.FRANCE);
    }

    public void testWithVariant()
    {
        attempt("en_US_Bahstohn", new Locale("en", "US", "Bahstohn"));
    }

    public void testPersist()
    {
        Locale locale = Locale.SIMPLIFIED_CHINESE;

        CookieSource source = (CookieSource) newMock(CookieSource.class);
        ThreadLocale threadLocale = newThreadLocale(locale);

        // Training

        source.writeCookieValue(TapestryConstants.LOCALE_COOKIE_NAME, locale.toString());

        replayControls();

        RequestLocaleManagerImpl m = new RequestLocaleManagerImpl();
        m.setCookieSource(source);
        m.setThreadLocale(threadLocale);

        m.persistLocale();

        verifyControls();
    }

    public void testPersistNoChange()
    {
        MockControl sourceControl = newControl(CookieSource.class);
        CookieSource source = (CookieSource) sourceControl.getMock();

        MockControl requestControl = newControl(WebRequest.class);
        WebRequest request = (WebRequest) requestControl.getMock();

        MockControl tlc = newControl(ThreadLocale.class);
        ThreadLocale tl = (ThreadLocale) tlc.getMock();

        // Training

        source.readCookieValue(TapestryConstants.LOCALE_COOKIE_NAME);
        sourceControl.setReturnValue(null);

        request.getLocale();
        requestControl.setReturnValue(Locale.JAPANESE);

        tl.setLocale(Locale.JAPANESE);

        replayControls();

        RequestLocaleManagerImpl manager = new RequestLocaleManagerImpl();
        manager.setCookieSource(source);
        manager.setRequest(request);
        manager.setThreadLocale(tl);

        Locale actual = manager.extractLocaleForCurrentRequest();

        assertEquals(Locale.JAPANESE, actual);

        verifyControls();

        tl.getLocale();
        tlc.setReturnValue(Locale.JAPANESE);

        replayControls();

        // Should do nothing, beacuse it isn't a change.

        manager.persistLocale();

        verifyControls();
    }

    public void testGetLocaleValuesAreCached()
    {
        RequestLocaleManagerImpl manager = new RequestLocaleManagerImpl();

        Locale l1 = manager.getLocale("en");
        Locale l2 = manager.getLocale("en");

        assertSame(l1, l2);
    }

    /**
     * Test when filtering of incoming locales is disabled.
     */

    public void testFilterDisabled()
    {
        RequestLocaleManagerImpl manager = new RequestLocaleManagerImpl();

        Locale l = manager.filterRequestedLocale("en");

        assertEquals(Locale.ENGLISH, l);
    }

    /**
     * Test with filtering enabled.
     */

    public void testFilterEnabled()
    {
        RequestLocaleManagerImpl manager = new RequestLocaleManagerImpl();
        manager.setAcceptedLocales("en,fr");
        manager.initializeService();

        assertEquals(Locale.ENGLISH, manager.filterRequestedLocale("en"));
        assertEquals(Locale.ENGLISH, manager.filterRequestedLocale("en_US"));
        assertEquals(Locale.FRENCH, manager.filterRequestedLocale("fr"));
        assertEquals(Locale.FRENCH, manager.filterRequestedLocale("fr_FR"));

        // Unrecognized locales filter to the first accepted locale.

        assertEquals(Locale.ENGLISH, manager.filterRequestedLocale("foo_bar_BAZ"));
    }
}