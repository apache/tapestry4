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

import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.services.ServiceConstants;

/**
 * Sets render parameters on the current {@link javax.portlet.ActionResponse} that will invoke the
 * {@link org.apache.tapestry.portlet.RenderService} to render the (currently) active page. This
 * reflects the Portlet API's very clear division between processing an action and rendering a
 * response; we need to record into the implicit render URL the render service and the name of the
 * active page.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PortletResponseRenderer implements ResponseRenderer
{
    private PortletRequest _request;

    private ActionResponse _response;

    public void renderResponse(IRequestCycle cycle)
    {
        String pageName = cycle.getPage().getPageName();

        _response.setRenderParameter(ServiceConstants.SERVICE, PortletConstants.RENDER_SERVICE);
        _response.setRenderParameter(ServiceConstants.PAGE, pageName);
        _response.setRenderParameter(PortletConstants.PORTLET_MODE, _request.getPortletMode()
                .toString());
        _response.setRenderParameter(PortletConstants.WINDOW_STATE, _request.getWindowState()
                .toString());
    }

    public void setResponse(ActionResponse response)
    {
        _response = response;
    }

    public void setRequest(PortletRequest request)
    {
        _request = request;
    }
}
