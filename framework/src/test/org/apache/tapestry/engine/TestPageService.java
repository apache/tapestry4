// Copyright 2004 The Apache Software Foundation
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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.ResponseOutputStream;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.services.ServiceConstants;
import org.easymock.MockControl;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPageService extends ServiceTestCase
{
    public void testGetLink()
    {
        Map parameters = new HashMap();
        parameters.put(ServiceConstants.SERVICE, Tapestry.PAGE_SERVICE);
        parameters.put(ServiceConstants.PAGE, "TargetPage");

        ILink link = newLink();

        MockControl lfc = newControl(LinkFactory.class);
        LinkFactory lf = (LinkFactory) lfc.getMock();

        IRequestCycle cycle = newRequestCycle();

        lf.constructLink(cycle, parameters, true);
        lfc.setReturnValue(link);

        replayControls();

        PageService ps = new PageService();
        ps.setLinkFactory(lf);

        assertSame(link, ps.getLink(cycle, "TargetPage"));

        verifyControls();
    }

    public void testService() throws Exception
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getParameter(ServiceConstants.PAGE);
        cyclec.setReturnValue("TargetPage");

        cycle.activate("TargetPage");

        ResponseRenderer rr = newResponseRenderer();

        ResponseOutputStream ros = new ResponseOutputStream(null);

        rr.renderResponse(cycle, ros);

        replayControls();

        PageService ps = new PageService();
        ps.setResponseRenderer(rr);

        ps.service(cycle, ros);

        verifyControls();
    }
}