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

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.ResponseRenderer;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletHomeService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPortletHomeService extends HiveMindTestCase
{
    private ResponseRenderer newResponseRenderer()
    {
        return (ResponseRenderer) newMock(ResponseRenderer.class);
    }

    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    private PortletRequestGlobals newRequestGlobals(boolean isRender)
    {
        MockControl control = newControl(PortletRequestGlobals.class);
        PortletRequestGlobals globals = (PortletRequestGlobals) control.getMock();

        globals.isRenderRequest();
        control.setReturnValue(isRender);

        return globals;
    }

    private PortletRenderer newPortletRenderer()
    {
        return (PortletRenderer) newMock(PortletRenderer.class);
    }

    public void testIsRenderRequest() throws Exception
    {
        IRequestCycle cycle = newCycle();
        PortletRequestGlobals globals = newRequestGlobals(true);
        PortletRenderer renderer = newPortletRenderer();

        renderer.renderPage(cycle, "ZePage");

        replayControls();

        PortletHomeService phs = new PortletHomeService();
        phs.setPageName("ZePage");
        phs.setPortletRenderer(renderer);
        phs.setRequestGlobals(globals);

        phs.service(cycle);

        verifyControls();
    }

    public void testIsActionRequest() throws Exception
    {
        IRequestCycle cycle = newCycle();
        PortletRequestGlobals globals = newRequestGlobals(false);
        ResponseRenderer renderer = newResponseRenderer();

        cycle.activate("ZePage");
        renderer.renderResponse(cycle);

        replayControls();

        PortletHomeService phs = new PortletHomeService();
        phs.setResponseRenderer(renderer);
        phs.setRequestGlobals(globals);
        phs.setPageName("ZePage");

        phs.service(cycle);

        verifyControls();
    }
}