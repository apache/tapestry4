// Copyright 2006 The Apache Software Foundation
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
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.ServiceConstants;
import org.testng.annotations.Test;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;

/**
 * Test for {@link org.apache.tapestry.portlet.RenderService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestRenderService extends BaseComponentTestCase
{
    public void testGetLinkUnsupported()
    {
        RenderService rs = new RenderService();

        try
        {
            rs.getLink(false, null);
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Hm. Not actually the best message!
            assertEquals("Method getLink() is not supported for portlet requests.", ex.getMessage());
        }
    }

    public void testNormal() throws Exception
    {
        IRequestCycle cycle = newCycle();
        PortletRequest request = newMock(PortletRequest.class);

        PortletRenderer renderer = newMock(PortletRenderer.class);
        
        trainGetParameter(cycle, PortletConstants.PORTLET_MODE, "view");
        trainGetParameter(cycle, PortletConstants.WINDOW_STATE, "normal");

        trainGetPortletMode(request, PortletMode.VIEW);
        trainGetWindowState(request, WindowState.NORMAL);

        trainGetParameter(cycle, ServiceConstants.PAGE, "Fred");

        renderer.renderPage(cycle, "Fred");

        replay();

        RenderService rs = new RenderService();
        rs.setPortletRenderer(renderer);
        rs.setRequest(request);

        rs.service(cycle);

        verify();
    }

    public void testPortletModeChanged() throws Exception
    {
        IRequestCycle cycle = newCycle();
        PortletRequest request = newMock(PortletRequest.class);

        PortletRenderer renderer = newMock(PortletRenderer.class);
        
        trainGetParameter(cycle, PortletConstants.PORTLET_MODE, "view");
        trainGetParameter(cycle, PortletConstants.WINDOW_STATE, "normal");

        trainGetPortletMode(request, PortletMode.EDIT);
        
        PortletPageResolver resolver = newResolver(cycle, "Barney");
        
        renderer.renderPage(cycle, "Barney");
        
        replay();

        RenderService rs = new RenderService();
        rs.setPortletRenderer(renderer);
        rs.setRequest(request);
        rs.setPageResolver(resolver);

        rs.service(cycle);

        verify();
    }

    public void testWindowStateChanged() throws Exception
    {
        IRequestCycle cycle = newCycle();
        PortletRequest request = newMock(PortletRequest.class);

        PortletRenderer renderer = newMock(PortletRenderer.class);
        
        trainGetParameter(cycle, PortletConstants.PORTLET_MODE, "view");
        trainGetParameter(cycle, PortletConstants.WINDOW_STATE, "normal");

        trainGetPortletMode(request, PortletMode.VIEW);
        trainGetWindowState(request, WindowState.MAXIMIZED);
        
        PortletPageResolver resolver = newResolver(cycle, "Wilma");
        
        renderer.renderPage(cycle, "Wilma");
        
        replay();

        RenderService rs = new RenderService();
        rs.setPortletRenderer(renderer);
        rs.setRequest(request);
        rs.setPageResolver(resolver);

        rs.service(cycle);

        verify();
    }

    private PortletPageResolver newResolver(IRequestCycle cycle, String pageName)
    {
        PortletPageResolver resolver = newMock(PortletPageResolver.class);
        
        expect(resolver.getPageNameForRequest(cycle)).andReturn(pageName);
        
        return resolver;
    }

    private void trainGetWindowState(PortletRequest request, WindowState state)
    {
        expect(request.getWindowState()).andReturn(state);
    }

    private void trainGetPortletMode(PortletRequest request, PortletMode mode)
    {
        expect(request.getPortletMode()).andReturn(mode);
    }
}
