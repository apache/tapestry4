// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.engine;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResetEventHub;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.services.ServiceConstants;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.engine.ResetService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class ResetServiceTest extends ServiceTestCase
{
    public void testGetLink()
    {
        IRequestCycle cycle = newCycle();
        IPage page = newPage();
        LinkFactory lf = newLinkFactory();
        ILink link = newLink();

        ResetService s = new ResetService();
        s.setLinkFactory(lf);
        s.setRequestCycle(cycle);

        trainGetPage(cycle, page);
        trainGetPageName(page, "TargetPage");

        Map parameters = new HashMap();
        parameters.put(ServiceConstants.PAGE, "TargetPage");

        trainConstructLink(lf, s, false, parameters, true, link);

        replay();

        assertSame(link, s.getLink(false, null));

        verify();
    }

    public void testService() throws Exception
    {
        IRequestCycle cycle = newCycle();
        ResetEventHub hub = newMock(ResetEventHub.class);
        ResponseRenderer rr = newResponseRenderer();

        trainGetParameter(cycle, ServiceConstants.PAGE, "TargetPage");

        hub.fireResetEvent();

        cycle.activate("TargetPage");

        rr.renderResponse(cycle);

        replay();

        ResetService rs = new ResetService();
        rs.setEnabled(true);
        rs.setResetEventHub(hub);
        rs.setResponseRenderer(rr);

        rs.service(cycle);

        verify();
    }

    public void testServiceNotEnabled() throws Exception
    {
        IRequestCycle cycle = newCycle();
        ResetEventHub hub = newMock(ResetEventHub.class);
        ResponseRenderer rr = newResponseRenderer();

        trainGetParameter(cycle, ServiceConstants.PAGE, "TargetPage");

        cycle.activate("TargetPage");

        rr.renderResponse(cycle);

        replay();

        ResetService rs = new ResetService();
        rs.setEnabled(false);
        rs.setResetEventHub(hub);
        rs.setResponseRenderer(rr);

        rs.service(cycle);

        verify();

    }

    public void testGetLinkNonNullParameter()
    {
        ResetService s = new ResetService();

        try
        {
            s.getLink(false, "NonNullValue");
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertEquals(EngineMessages.serviceNoParameter(s), ex.getMessage());
        }
    }

}