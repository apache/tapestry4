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
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.util.QueryParameterMap;

/**
 * Wrapper around {@link javax.portlet.PortletURL}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PortletLink implements ILink
{
    private final PortletURL _portletURL;

    private final QueryParameterMap _parameters;

    public PortletLink(PortletURL portletURL, QueryParameterMap parameters)
    {
        Defense.notNull(portletURL, "portletURL");
        Defense.notNull(parameters, "parameters");

        _portletURL = portletURL;
        _parameters = parameters;
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

        url = unencode(url);

        if (anchor != null)
            url = url + "#" + anchor;

        return url;
    }

    /**
     * The PortletURL class returns a url that's already XML-escaped, ready for inclusion directly
     * into the response stream. However, the IMarkupWriter expects to do that encoding too ... and
     * double encoding is bad. So we back out the most likely encoding (convert '&amp;amp;' to just
     * '&amp;').
     */

    private String unencode(String url)
    {
        StringBuffer buffer = new StringBuffer(url.length());
        String text = url;

        while (true)
        {
            int ampx = text.indexOf("&amp;");

            if (ampx < 0)
                break;

            // Take up to and including the '&'

            buffer.append(text.substring(0, ampx + 1));

            text = text.substring(ampx + 5);
        }

        buffer.append(text);

        return buffer.toString();
    }

    private void loadParameters()
    {
        String[] names = _parameters.getParameterNames();

        for (int i = 0; i < names.length; i++)
        {
            String name = names[i];
            String[] values = _parameters.getParameterValues(name);

            if (values != null)
                _portletURL.setParameter(name, values);
        }
    }

    public String getURL(String scheme, String server, int port, String anchor,
            boolean includeParameters)
    {
        // Ignore scheme, server and port ... those are under the control of the portlet container.

        return getURL(anchor, includeParameters);
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