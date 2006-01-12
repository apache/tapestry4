// Copyright 2004, 2005, 2006 The Apache Software Foundation
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
import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.services.impl.CookieSourceImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class CookieSourceTest extends HiveMindTestCase
{
    // In seconds

    private static final int ONE_WEEK = 7 * 24 * 60 * 60;

    /** Allows Cookies to be compared (which you'd think would be a natural). */
    private static class ComparableCookie extends Cookie
    {
        public ComparableCookie(String name, String value, int maxAge)
        {
            super(name, value);
            setMaxAge(maxAge);
        }

        public boolean equals(Object obj)
        {
            Cookie c = (Cookie) obj;

            return equals(getName(), c.getName()) && equals(getValue(), c.getValue())
                    && equals(getPath(), c.getPath()) && getMaxAge() == c.getMaxAge();
        }

        private boolean equals(Object value, Object other)
        {
            return value == other || (value != null && value.equals(other));
        }
    }

    private HttpServletRequest newRequest(String[] nameValues)
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

        HttpServletRequest request = newRequest();

        request.getCookies();
        setReturnValue(request, cookies);

        return request;
    }

    protected HttpServletRequest newRequest()
    {
        return (HttpServletRequest) newMock(HttpServletRequest.class);
    }

    private void attempt(String name, String expected, String[] nameValues)
    {
        HttpServletRequest request = newRequest(nameValues);

        CookieSourceImpl cs = new CookieSourceImpl();

        cs.setRequest(request);

        replayControls();

        String actual = cs.readCookieValue(name);

        assertEquals(expected, actual);

        verifyControls();
    }

    public void testNoCookies()
    {
        attempt("foo", null, null);
    }

    public void testMatch()
    {
        attempt("fred", "flintstone", new String[]
        { "barney", "rubble", "fred", "flintstone" });
    }

    public void testNoMatch()
    {
        attempt("foo", null, new String[]
        { "bar", "baz" });
    }

    public void testWriteCookie()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        // Training

        trainGetContextPath(request, "/context");

        Cookie cookie = new ComparableCookie("foo", "bar", ONE_WEEK);
        cookie.setPath("/context/");

        response.addCookie(cookie);

        replayControls();

        CookieSourceImpl cs = new CookieSourceImpl();
        cs.setRequest(request);
        cs.setResponse(response);
        cs.setDefaultMaxAge(ONE_WEEK);

        cs.writeCookieValue("foo", "bar");

        verifyControls();
    }

    public void testWriteCookieWithMaxAge()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        // Training

        trainGetContextPath(request, "/ctx");

        Cookie cookie = new ComparableCookie("foo", "bar", -1);
        cookie.setPath("/ctx/");

        response.addCookie(cookie);

        replayControls();

        CookieSourceImpl cs = new CookieSourceImpl();
        cs.setRequest(request);
        cs.setResponse(response);
        cs.setDefaultMaxAge(ONE_WEEK);

        cs.writeCookieValue("foo", "bar", -1);

        verifyControls();
    }

    private void trainGetContextPath(HttpServletRequest request, String contextPath)
    {
        request.getContextPath();
        setReturnValue(request, contextPath);
    }

    private HttpServletResponse newResponse()
    {
        return (HttpServletResponse) newMock(HttpServletResponse.class);
    }

    public void testRemoveCookie()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        // Training

        trainGetContextPath(request, "/context");

        Cookie cookie = new ComparableCookie("foo", null, 0);
        cookie.setPath("/context/");

        response.addCookie(cookie);

        replayControls();

        CookieSourceImpl cs = new CookieSourceImpl();
        cs.setRequest(request);
        cs.setResponse(response);

        cs.removeCookieValue("foo");

        verifyControls();
    }
}