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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.CookieSourceImpl}.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestCookieSource extends HiveMindTestCase
{
    private HttpServletRequest setupRequest(String[] nameValues)
    {
        Cookie[] cookies = null;

        if (nameValues != null)
        {

            List l = new ArrayList();

            for (int i = 0; i < nameValues.length; i += 2)
            {
                String name = nameValues[i];
                String value = nameValues[i + 1];

                Cookie c = new Cookie(name, value);

                l.add(c);
            }

            cookies = (Cookie[]) l.toArray(new Cookie[l.size()]);
        }

        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getCookies();
        control.setReturnValue(cookies);

        return request;
    }

    private void attempt(String name, String expected, String[] nameValues)
    {
        HttpServletRequest request = setupRequest(nameValues);

        CookieSourceImpl cs = new CookieSourceImpl();

        cs.setRequest(request);

        replayControls();

        String actual = cs.getCookieValue(name);

        assertEquals(expected, actual);

        verifyControls();
    }

    public void testNoCookies()
    {
        attempt("foo", null, null);
    }

    public void testMatch()
    {
        attempt("fred", "flintstone", new String[] { "barney", "rubble", "fred", "flintstone" });
    }

    public void testNoMatch()
    {
        attempt("foo", null, new String[] { "bar", "baz" });
    }
}
