// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.hivemind.util.Defense;

/**
 * Adapts {@link javax.servlet.http.HttpSession}&nbsp; as
 * {@link org.apache.tapestry.web.WebSession}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ServletWebSession implements WebSession
{
    private final HttpSession _httpSession;

    public ServletWebSession(HttpSession session)
    {
        Defense.notNull(session, "session");

        _httpSession = session;
    }

    public List getAttributeNames()
    {
        return WebUtils.toSortedList(_httpSession.getAttributeNames());
    }

    public Object getAttribute(String name)
    {
        return _httpSession.getAttribute(name);
    }

    public void setAttribute(String name, Object attribute)
    {
        if (attribute == null)
            _httpSession.removeAttribute(name);
        else
            _httpSession.setAttribute(name, attribute);
    }

    public String getId()
    {
        return _httpSession.getId();
    }
}