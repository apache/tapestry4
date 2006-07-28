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

import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletHomeService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestPortletHomeService extends BaseComponentTestCase
{
    private ResponseRenderer newResponseRenderer()
    {
        return newMock(ResponseRenderer.class);
    }

    private PortletRequestGlobals newRequestGlobals(boolean isRender)
    {
        PortletRequestGlobals globals = newMock(PortletRequestGlobals.class);
        checkOrder(globals, false);
        
        expect(globals.isRenderRequest()).andReturn(isRender);
        
        return globals;
    }

    private PortletRenderer newPortletRenderer()
    {
        return newMock(PortletRenderer.class);
    }

    private PortletPageResolver newResolver(IRequestCycle cycle, String pageName)
    {
        PortletPageResolver resolver = newMock(PortletPageResolver.class);

        expect(resolver.getPageNameForRequest(cycle)).andReturn(pageName);
        
        return resolver;
    }

    public void testIsRenderRequest() throws Exception
    {
        IRequestCycle cycle = newCycle();
        PortletRequestGlobals globals = newRequestGlobals(true);
        PortletRenderer renderer = newPortletRenderer();
        PortletPageResolver resolver = newResolver(cycle, "ZePage");

        renderer.renderPage(cycle, "ZePage");

        replay();

        PortletHomeService phs = new PortletHomeService();
        phs.setPageResolver(resolver);
        phs.setPortletRenderer(renderer);
        phs.setRequestGlobals(globals);

        phs.service(cycle);

        verify();
    }

    public void testIsActionRequest() throws Exception
    {
        IRequestCycle cycle = newCycle();
        PortletRequestGlobals globals = newRequestGlobals(false);
        
        ResponseRenderer renderer = newResponseRenderer();
        
        PortletPageResolver resolver = newResolver(cycle, "ZePage");
        
        cycle.activate("ZePage");
        renderer.renderResponse(cycle);

        replay();

        PortletHomeService phs = new PortletHomeService();
        phs.setResponseRenderer(renderer);
        phs.setRequestGlobals(globals);
        phs.setPageResolver(resolver);

        phs.service(cycle);

        verify();
    }

    public void testGetLink()
    {
        Map parameters = new HashMap();
        
        LinkFactory factory = newMock(LinkFactory.class);
        ILink link = newMock(ILink.class);

        PortletHomeService phs = new PortletHomeService();
        phs.setLinkFactory(factory);
        
        expect(factory.constructLink(phs, false, parameters, true)).andReturn(link);

        replay();

        assertSame(link, phs.getLink(false, null));

        verify();
    }

    public void testGetLinkWithParameter()
    {
        try
        {
            new PortletHomeService().getLink(false, "PARAMETER");
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertEquals("The home service does not require a parameter object.", ex.getMessage());
        }
    }
}
