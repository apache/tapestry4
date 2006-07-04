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
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.ServiceConstants;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletResponseRenderer}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestPortletResponseRenderer extends HiveMindTestCase
{
    private ActionResponse newResponse()
    {
        return (ActionResponse) newMock(ActionResponse.class);
    }

    private IPage newPage(String name)
    {
        MockControl control = newControl(IPage.class);
        IPage page = (IPage) control.getMock();

        page.getPageName();
        control.setReturnValue(name);

        return page;
    }

    private IRequestCycle newCycle(IPage page)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        cycle.getPage();
        control.setReturnValue(page);

        return cycle;
    }

    public void testSuccess() throws Exception
    {
        IPage page = newPage("Frodo");
        IRequestCycle cycle = newCycle(page);

        MockControl requestc = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) requestc.getMock();

        request.getPortletMode();
        requestc.setReturnValue(PortletMode.VIEW);

        request.getWindowState();
        requestc.setReturnValue(WindowState.NORMAL);

        ActionResponse response = newResponse();

        response.setRenderParameter(ServiceConstants.SERVICE, PortletConstants.RENDER_SERVICE);
        response.setRenderParameter(ServiceConstants.PAGE, "Frodo");
        response.setRenderParameter(PortletConstants.PORTLET_MODE, "view");
        response.setRenderParameter(PortletConstants.WINDOW_STATE, "normal");

        replayControls();

        PortletResponseRenderer renderer = new PortletResponseRenderer();
        renderer.setResponse(response);
        renderer.setRequest(request);

        renderer.renderResponse(cycle);

        verifyControls();
    }
}