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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry.services.CookieSource;

/**
 * Implementation of the {@link org.apache.tapestry.services.CookieSource}
 * service interface.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class CookieSourceImpl implements CookieSource
{
    private HttpServletRequest _request;

    public String getCookieValue(String name)
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

    public void setRequest(HttpServletRequest request)
    {
        _request = request;
    }

}
