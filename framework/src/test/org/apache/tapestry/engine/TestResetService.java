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

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.engine.ResetService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestResetService extends ServiceTestCase
{
    private IRequestCycle newRequestCycle(IPage page)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        cycle.getPage();
        control.setReturnValue(page);

        return cycle;
    }

    public void testGetLink()
    {
        IPage page = newPage("TargetPage");
        IRequestCycle cycle = newRequestCycle(page);

        Map parameters = new HashMap();
        parameters.put(ServiceConstants.SERVICE, Tapestry.RESET_SERVICE);
        parameters.put(ServiceConstants.PAGE, "TargetPage");

        ILink link = newLink();
        LinkFactory lf = newLinkFactory(cycle, parameters, true, link);

        replayControls();

        ResetService s = new ResetService();
        s.setLinkFactory(lf);

        assertSame(link, s.getLink(cycle, null));

        verifyControls();
    }

    public void testGetLinkNonNullParameter()
    {
        ResetService s = new ResetService();

        try
        {
            s.getLink(null, "NonNullValue");
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertEquals(EngineMessages.serviceNoParameter(s), ex.getMessage());
        }
    }

}