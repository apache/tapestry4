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

package org.apache.tapestry.services.impl;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.ApplicationServlet;
import org.apache.tapestry.services.CookieSource;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.LocaleExtractorImpl}.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestLocaleExtractor extends HiveMindTestCase
{
    public void testSuppliedByRequest()
    {
        MockControl sourceControl = newControl(CookieSource.class);
        CookieSource source = (CookieSource) sourceControl.getMock();

        MockControl requestControl = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) requestControl.getMock();

        // Training

        source.getCookieValue(ApplicationServlet.LOCALE_COOKIE_NAME);
        sourceControl.setReturnValue(null);

        request.getLocale();
        requestControl.setReturnValue(Locale.JAPANESE);

        replayControls();

        LocaleExtractorImpl le = new LocaleExtractorImpl();
        le.setCookieSource(source);
        le.setRequest(request);

        Locale actual = le.extractLocaleForCurrentRequest();

        assertSame(Locale.JAPANESE, actual);

        verifyControls();
    }

    private void attempt(String localeName, Locale expectedLocale)
    {
        MockControl sourceControl = newControl(CookieSource.class);
        CookieSource source = (CookieSource) sourceControl.getMock();

        // Training
    
        source.getCookieValue(ApplicationServlet.LOCALE_COOKIE_NAME);
        sourceControl.setReturnValue(localeName);

		replayControls();
		
		LocaleExtractorImpl le = new LocaleExtractorImpl();
		le.setCookieSource(source);

		Locale actual = le.extractLocaleForCurrentRequest();
		
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
}
