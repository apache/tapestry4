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

import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.services.ServiceConstants;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletHomeService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
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

    private PortletPageResolver newResolver(IRequestCycle cycle, String pageName)
    {
        MockControl control = newControl(PortletPageResolver.class);
        PortletPageResolver resolver = (PortletPageResolver) control.getMock();

        resolver.getPageNameForRequest(cycle);
        control.setReturnValue(pageName);

        return resolver;
    }

    public void testIsRenderRequest() throws Exception
    {
        IRequestCycle cycle = newCycle();
        PortletRequestGlobals globals = newRequestGlobals(true);
        PortletRenderer renderer = newPortletRenderer();
        PortletPageResolver resolver = newResolver(cycle, "ZePage");

        renderer.renderPage(cycle, "ZePage");

        replayControls();

        PortletHomeService phs = new PortletHomeService();
        phs.setPageResolver(resolver);
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
        PortletPageResolver resolver = newResolver(cycle, "ZePage");

        cycle.activate("ZePage");
        renderer.renderResponse(cycle);

        replayControls();

        PortletHomeService phs = new PortletHomeService();
        phs.setResponseRenderer(renderer);
        phs.setRequestGlobals(globals);
        phs.setPageResolver(resolver);

        phs.service(cycle);

        verifyControls();
    }

    public void testGetLink()
    {
        Map parameters = new HashMap();
        parameters.put(ServiceConstants.SERVICE, Tapestry.HOME_SERVICE);

        MockControl factoryc = newControl(LinkFactory.class);
        LinkFactory factory = (LinkFactory) factoryc.getMock();

        ILink link = (ILink) newMock(ILink.class);
        IRequestCycle cycle = newCycle();

        factory.constructLink(cycle, parameters, true);
        factoryc.setReturnValue(link);

        replayControls();

        PortletHomeService phs = new PortletHomeService();
        phs.setLinkFactory(factory);

        assertSame(link, phs.getLink(cycle, null));

        verifyControls();
    }

    public void testGetLinkWithParameter()
    {
        try
        {
            new PortletHomeService().getLink(null, "PARAMETER");
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertEquals("The home service does not require a parameter object.", ex.getMessage());
        }
    }
}