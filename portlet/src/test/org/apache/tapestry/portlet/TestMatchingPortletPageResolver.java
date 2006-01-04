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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageNotFoundException;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.portlet.MatchingPortletPageResolver}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestMatchingPortletPageResolver extends HiveMindTestCase
{
    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    private IPage newPage()
    {
        return (IPage) newMock(IPage.class);
    }

    private IRequestCycle newCycle(String pageName)
    {
        IPage page = newPage();

        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        cycle.getPage(pageName);
        control.setReturnValue(page);

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

        MockControl requestc = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) requestc.getMock();

        trainContentType(requestc, request, "text/xml");

        List l = Collections.singletonList(newContribution("text/html", null, "wierd", "Wierd"));

        replayControls();

        MatchingPortletPageResolver resolver = new MatchingPortletPageResolver();
        resolver.setContributions(l);
        resolver.initializeService();
        resolver.setRequest(request);

        assertNull(resolver.getPageNameForRequest(cycle));

        verifyControls();
    }

    public void testNoMatchOnPortletMode()
    {
        IRequestCycle cycle = newCycle();

        MockControl requestc = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) requestc.getMock();

        trainPortletMode(requestc, request, "edit");

        List l = Collections.singletonList(newContribution(null, "view", null, "Wierd"));

        replayControls();

        MatchingPortletPageResolver resolver = new MatchingPortletPageResolver();
        resolver.setContributions(l);
        resolver.initializeService();
        resolver.setRequest(request);

        assertNull(resolver.getPageNameForRequest(cycle));

        verifyControls();
    }

    public void testNoMatchOnWindowState()
    {
        IRequestCycle cycle = newCycle();

        MockControl requestc = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) requestc.getMock();

        trainWindowState(requestc, request, "huge");

        List l = Collections.singletonList(newContribution(null, null, "tiny", "Wierd"));

        replayControls();

        MatchingPortletPageResolver resolver = new MatchingPortletPageResolver();
        resolver.setContributions(l);
        resolver.initializeService();
        resolver.setRequest(request);

        assertNull(resolver.getPageNameForRequest(cycle));

        verifyControls();
    }

    public void testSortingCounts()
    {
        IRequestCycle cycle = newCycle("EditHuge");

        MockControl requestc = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) requestc.getMock();

        List l = new ArrayList();
        l.add(newContribution(null, "edit", null, "EditNormal"));
        l.add(newContribution(null, "edit", "huge", "EditHuge"));

        trainPortletMode(requestc, request, "edit");
        trainWindowState(requestc, request, "huge");

        replayControls();

        MatchingPortletPageResolver resolver = new MatchingPortletPageResolver();
        resolver.setContributions(l);
        resolver.initializeService();
        resolver.setRequest(request);

        assertEquals("EditHuge", resolver.getPageNameForRequest(cycle));

        verifyControls();
    }

    public void testPageMissing()
    {
        IPage page = newPage();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl requestc = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) requestc.getMock();

        List l = new ArrayList();
        l.add(newContribution(null, "edit", null, "EditNormal"));
        l.add(newContribution(null, "edit", "huge", "EditHuge"));

        trainPortletMode(requestc, request, "edit");
        trainWindowState(requestc, request, "huge");

        cycle.getPage("EditHuge");
        cyclec.setThrowable(new PageNotFoundException("missing!"));

        trainPortletMode(requestc, request, "edit");

        cycle.getPage("EditNormal");
        cyclec.setReturnValue(page);

        replayControls();

        MatchingPortletPageResolver resolver = new MatchingPortletPageResolver();
        resolver.setContributions(l);
        resolver.initializeService();
        resolver.setRequest(request);

        assertEquals("EditNormal", resolver.getPageNameForRequest(cycle));

        verifyControls();
    }

    private void trainContentType(MockControl control, PortletRequest request, String contentType)
    {
        request.getResponseContentType();
        control.setReturnValue(contentType);
    }

    private void trainPortletMode(MockControl control, PortletRequest request, String modeName)
    {
        PortletMode mode = new PortletMode(modeName);

        request.getPortletMode();
        control.setReturnValue(mode);
    }

    private void trainWindowState(MockControl control, PortletRequest request, String stateName)
    {
        WindowState state = new WindowState(stateName);

        request.getWindowState();
        control.setReturnValue(state);
    }
}