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

import static org.easymock.EasyMock.expect;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.ServiceConstants;
import org.testng.annotations.Test;

import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletResponseRenderer}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestPortletResponseRenderer extends BaseComponentTestCase
{
    private ActionResponse newResponse()
    {
        return newMock(ActionResponse.class);
    }

    private IRequestCycle newCycle(IPage page)
    {
        IRequestCycle cycle = newCycle();
        
        expect(cycle.getPage()).andReturn(page);
        
        return cycle;
    }
    
    public void testSuccess() throws Exception
    {
        IPage page = newPage("Frodo");
        IRequestCycle cycle = newCycle(page);
        
        PortletRequest request = newMock(PortletRequest.class);
        
        expect(request.getPortletMode()).andReturn(PortletMode.VIEW);

        expect(request.getWindowState()).andReturn(WindowState.NORMAL);
        
        ActionResponse response = newResponse();

        response.setRenderParameter(ServiceConstants.SERVICE, PortletConstants.RENDER_SERVICE);
        response.setRenderParameter(ServiceConstants.PAGE, "Frodo");
        response.setRenderParameter(PortletConstants.PORTLET_MODE, "view");
        response.setRenderParameter(PortletConstants.WINDOW_STATE, "normal");

        replay();

        PortletResponseRenderer renderer = new PortletResponseRenderer();
        renderer.setResponse(response);
        renderer.setRequest(request);

        renderer.renderResponse(cycle);

        verify();
    }
}
