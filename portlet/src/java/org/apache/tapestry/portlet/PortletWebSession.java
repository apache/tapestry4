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

package org.apache.tapestry.portlet;

import java.util.List;

import javax.portlet.PortletSession;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.web.WebSession;
import org.apache.tapestry.web.WebUtils;

/**
 * Adapts a {@link javax.portlet.PortletSession}as a {@link org.apache.tapestry.web.WebSession}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class PortletWebSession implements WebSession
{
    private final PortletSession _portletSession;

    public PortletWebSession(final PortletSession portletSession)
    {
        Defense.notNull(portletSession, "portletSession");

        _portletSession = portletSession;
    }

    public String getId()
    {
        return _portletSession.getId();
    }

    public boolean isNew()
    {
        return _portletSession.isNew();
    }

    public List getAttributeNames()
    {
        return WebUtils.toSortedList(_portletSession.getAttributeNames());
    }

    public Object getAttribute(String name)
    {
        return _portletSession.getAttribute(name);
    }

    public void setAttribute(String name, Object attribute)
    {
        if (attribute == null)
            _portletSession.removeAttribute(name);
        else
            _portletSession.setAttribute(name, attribute);
    }

}