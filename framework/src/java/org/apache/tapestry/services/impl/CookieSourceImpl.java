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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.services.CookieSource;

/**
 * Implementation of the {@link org.apache.tapestry.services.CookieSource}service interface.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class CookieSourceImpl implements CookieSource
{
    private HttpServletRequest _request;

    private HttpServletResponse _response;

    public String readCookieValue(String name)
    {
        Cookie[] cookies = _request.getCookies();

        if (cookies == null)
            return null;

        for (int i = 0; i < cookies.length; i++)
        {
            if (cookies[i].getName().equals(name))
                return cookies[i].getValue();
        }

        return null;
    }

    public void writeCookieValue(String name, String value)
    {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(_request.getContextPath());

        _response.addCookie(cookie);
    }

    public void removeCookieValue(String name)
    {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath(_request.getContextPath());
        cookie.setMaxAge(0);

        _response.addCookie(cookie);
    }

    public void setRequest(HttpServletRequest request)
    {
        _request = request;
    }

    public void setResponse(HttpServletResponse response)
    {
        _response = response;
    }
}