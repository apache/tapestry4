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
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.describe.DescriptionReceiver;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;
import org.apache.tapestry.web.WebUtils;

/**
 * Implementation of {@link org.apache.tapestry.web.WebRequest}that adapts a
 * {@link PortletRequest).
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class PortletWebRequest implements WebRequest
{
    private final PortletRequest _portletRequest;

    private WebSession _webSession;

    public PortletWebRequest(PortletRequest portletRequest)
    {
        Defense.notNull(portletRequest, "portletRequest");

        _portletRequest = portletRequest;
    }

    public List getParameterNames()
    {
        return WebUtils.toSortedList(_portletRequest.getParameterNames());
    }

    public String getParameterValue(String parameterName)
    {
        return _portletRequest.getParameter(parameterName);
    }

    public String[] getParameterValues(String parameterName)
    {
        return _portletRequest.getParameterValues(parameterName);
    }

    public String getContextPath()
    {
        return _portletRequest.getContextPath();
    }

    public WebSession getSession(boolean create)
    {
        if (_webSession != null)
            return _webSession;

        PortletSession session = _portletRequest.getPortletSession(create);

        if (session != null)
            _webSession = new PortletWebSession(session);

        return _webSession;
    }

    public String getScheme()
    {
        return _portletRequest.getScheme();
    }

    public String getServerName()
    {
        return _portletRequest.getServerName();
    }

    public int getServerPort()
    {
        return _portletRequest.getServerPort();
    }

    /**
     * Returns "&lt;PortletRequest&gt;", because portlets don't have a notion of request URI.
     */

    public String getRequestURI()
    {
        return "<PortletRequest>";
    }

    public void forward(String URL)
    {
        unsupported("forward");
    }

    public String getActivationPath()
    {
        return "";
    }

    public List getAttributeNames()
    {
        return WebUtils.toSortedList(_portletRequest.getAttributeNames());
    }

    public Object getAttribute(String name)
    {
        return _portletRequest.getAttribute(name);
    }

    public void setAttribute(String name, Object attribute)
    {
        if (attribute == null)
            _portletRequest.removeAttribute(name);
        else
            _portletRequest.setAttribute(name, attribute);
    }

    protected final void unsupported(String methodName)
    {
        throw new UnsupportedOperationException(PortletMessages.unsupportedMethod(methodName));
    }

    public void describeTo(DescriptionReceiver receiver)
    {
        receiver.describeAlternate(_portletRequest);
    }

    public Locale getLocale()
    {
        return _portletRequest.getLocale();
    }
}