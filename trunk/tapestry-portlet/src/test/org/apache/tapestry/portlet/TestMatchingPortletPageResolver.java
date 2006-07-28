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
import static org.testng.AssertJUnit.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageNotFoundException;
import org.testng.annotations.Test;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;

/**
 * Tests for {@link org.apache.tapestry.portlet.MatchingPortletPageResolver}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestMatchingPortletPageResolver extends BaseComponentTestCase
{
    
    private IRequestCycle newCycle(String pageName)
    {
        IPage page = newPage();
        
        IRequestCycle cycle = newCycle();
        checkOrder(cycle, false);
        
        expect(cycle.getPage(pageName)).andReturn(page);

        return cycle;
    }

    private PageResolverContribution newContribution(String mimeType, String portletMode,
            String windowState, String pageName)
    {
        PageResolverContribution result = new PageResolverContribution();
        result.setMimeType(mimeType);
        result.setPortletMode(portletMode);
        result.setWindowState(windowState);
        result.setPageName(pageName);

        return result;
    }

    public void testNoMatchOnContentType()
    {
        IRequestCycle cycle = newCycle();
        
        PortletRequest request = newMock(PortletRequest.class);
        
        trainContentType(request, "text/xml");

        List l = Collections.singletonList(newContribution("text/html", null, "wierd", "Wierd"));

        replay();

        MatchingPortletPageResolver resolver = new MatchingPortletPageResolver();
        resolver.setContributions(l);
        resolver.initializeService();
        resolver.setRequest(request);

        assertNull(resolver.getPageNameForRequest(cycle));

        verify();
    }

    public void testNoMatchOnPortletMode()
    {
        IRequestCycle cycle = newCycle();
        
        PortletRequest request = newMock(PortletRequest.class);

        trainPortletMode(request, "edit");
        
        List l = Collections.singletonList(newContribution(null, "view", null, "Wierd"));

        replay();

        MatchingPortletPageResolver resolver = new MatchingPortletPageResolver();
        resolver.setContributions(l);
        resolver.initializeService();
        resolver.setRequest(request);

        assertNull(resolver.getPageNameForRequest(cycle));

        verify();
    }

    public void testNoMatchOnWindowState()
    {
        IRequestCycle cycle = newCycle();

        PortletRequest request = newMock(PortletRequest.class);

        trainWindowState(request, "huge");

        List l = Collections.singletonList(newContribution(null, null, "tiny", "Wierd"));

        replay();

        MatchingPortletPageResolver resolver = new MatchingPortletPageResolver();
        resolver.setContributions(l);
        resolver.initializeService();
        resolver.setRequest(request);

        assertNull(resolver.getPageNameForRequest(cycle));

        verify();
    }

    public void testSortingCounts()
    {
        IRequestCycle cycle = newCycle("EditHuge");

        PortletRequest request = newMock(PortletRequest.class);
        
        List l = new ArrayList();
        l.add(newContribution(null, "edit", null, "EditNormal"));
        l.add(newContribution(null, "edit", "huge", "EditHuge"));
        
        trainPortletMode(request, "edit");
        trainWindowState(request, "huge");

        replay();

        MatchingPortletPageResolver resolver = new MatchingPortletPageResolver();
        resolver.setContributions(l);
        resolver.initializeService();
        resolver.setRequest(request);

        assertEquals("EditHuge", resolver.getPageNameForRequest(cycle));

        verify();
    }

    public void testPageMissing()
    {
        IPage page = newPage();
        IRequestCycle cycle = newCycle();

        PortletRequest request = newMock(PortletRequest.class);

        List l = new ArrayList();
        l.add(newContribution(null, "edit", null, "EditNormal"));
        l.add(newContribution(null, "edit", "huge", "EditHuge"));

        trainPortletMode(request, "edit");
        trainWindowState(request, "huge");

        expect(cycle.getPage("EditHuge")).andThrow(new PageNotFoundException("missing!"));
        
        trainPortletMode(request, "edit");

        expect(cycle.getPage("EditNormal")).andReturn(page);
        
        replay();

        MatchingPortletPageResolver resolver = new MatchingPortletPageResolver();
        resolver.setContributions(l);
        resolver.initializeService();
        resolver.setRequest(request);

        assertEquals("EditNormal", resolver.getPageNameForRequest(cycle));

        verify();
    }

    private void trainContentType(PortletRequest request, String contentType)
    {
        expect(request.getResponseContentType()).andReturn(contentType);
    }

    private void trainPortletMode(PortletRequest request, String modeName)
    {
        PortletMode mode = new PortletMode(modeName);

        expect(request.getPortletMode()).andReturn(mode);
    }

    private void trainWindowState(PortletRequest request, String stateName)
    {
        WindowState state = new WindowState(stateName);

        expect(request.getWindowState()).andReturn(state);
    }
}
