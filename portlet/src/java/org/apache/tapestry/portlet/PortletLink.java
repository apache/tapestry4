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

import javax.portlet.PortletURL;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.util.QueryParameterMap;

/**
 * Wrapper around {@link javax.portlet.PortletURL}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class PortletLink implements ILink
{
    private final IRequestCycle _cycle;

    private final PortletURL _portletURL;

    private final boolean _stateful;

    private final QueryParameterMap _parameters;

    public PortletLink(IRequestCycle cycle, PortletURL portletURL, QueryParameterMap parameters,
            boolean stateful)
    {
        Defense.notNull(cycle, "cycle");
        Defense.notNull(portletURL, "portletURL");
        Defense.notNull(parameters, "parameters");

        _cycle = cycle;
        _portletURL = portletURL;
        _parameters = parameters;
        _stateful = stateful;
    }

    public String getURL()
    {
        return getURL(null, true);
    }

    public String getURL(String anchor, boolean includeParameters)
    {
        if (includeParameters)
            loadParameters();

        String url = _portletURL.toString();

        if (_stateful)
            url = _cycle.encodeURL(url);

        if (anchor != null)
            url = url + "#" + anchor;

        return url;
    }

    private void loadParameters()
    {
        String[] names = _parameters.getParameterNames();

        for (int i = 0; i < names.length; i++)
        {
            String name = names[i];
            String[] values = _parameters.getParameterValues(name);

            _portletURL.setParameter(name, values);
        }
    }

    public String getAbsoluteURL()
    {
        throw new UnsupportedOperationException(PortletMessages.unsupportedMethod("getAbsoluteURL"));
    }

    public String getAbsoluteURL(String scheme, String server, int port, String anchor,
            boolean includeParameters)
    {
        throw new UnsupportedOperationException(PortletMessages.unsupportedMethod("getAbsoluteURL"));
    }

    public String[] getParameterNames()
    {
        return _parameters.getParameterNames();
    }

    public String[] getParameterValues(String name)
    {
        return _parameters.getParameterValues(name);
    }

}