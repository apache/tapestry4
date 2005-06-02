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

import java.io.IOException;

import javax.portlet.PortletRequest;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.ServiceConstants;

/**
 * Responsible for rendering out a page; a Portlet render URL is built during action processing that
 * stores the active page; this is the page that will be rendered. The render service is (typically)
 * the only service that operates during a portlet RenderRequest. All other services will be an
 * ActionRequest that (via {@link org.apache.tapestry.portlet.PortletResponseRenderer}, writes
 * query parameters to activate this service during the render request.
 * <p>
 * Problematic is is anything related to the portlet mode or window state. As per the Portlet spec,
 * when the user clicks the "help" or "edit" buttons (or the minimize, maximize, etc.), this causes
 * a new RenderRequest, but explicitly keeps the render parameters set by the most recent
 * ActionRequest. But what Tapestry needs is to detect that the mode or state has changed and select
 * a different page to render the response. So we store the mode and state in effect when the
 * ActionRequest executed as two more query parameters, and detect changes to mode and state that
 * way. If there is a change, then we ignore the page query parameter and use the
 * {@link PortletPageResolver} to figure out the correct page to display instead.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.services.impl.ResponseRendererImpl
 */
public class RenderService implements IEngineService
{
    private PortletRequest _request;

    private PortletRenderer _portletRenderer;

    private PortletPageResolver _pageResolver;

    public ILink getLink(IRequestCycle cycle, Object parameter)
    {
        throw new UnsupportedOperationException(PortletMessages.unsupportedMethod("getLink"));
    }

    public void service(IRequestCycle cycle) throws IOException
    {
        String pageName = getPageNameToRender(cycle);

        _portletRenderer.renderPage(cycle, pageName);
    }

    private String getPageNameToRender(IRequestCycle cycle)
    {
        if (isStateChange(cycle))
            return _pageResolver.getPageNameForRequest(cycle);

        return cycle.getParameter(ServiceConstants.PAGE);
    }

    /**
     * Returns true if the portlet mode or the window state has changed since. The values stored
     * previously (during an action request) are compared to the current values.
     */

    boolean isStateChange(IRequestCycle cycle)
    {
        String expectedPortletMode = cycle.getParameter(PortletConstants.PORTLET_MODE);
        String expectedWindowState = cycle.getParameter(PortletConstants.WINDOW_STATE);

        return !(_request.getPortletMode().toString().equals(expectedPortletMode) && _request
                .getWindowState().toString().equals(expectedWindowState));

    }

    public String getName()
    {
        return PortletConstants.RENDER_SERVICE;
    }

    public void setPortletRenderer(PortletRenderer portletRenderer)
    {
        _portletRenderer = portletRenderer;
    }

    public void setRequest(PortletRequest request)
    {
        _request = request;
    }

    public void setPageResolver(PortletPageResolver pageResolver)
    {
        _pageResolver = pageResolver;
    }
}