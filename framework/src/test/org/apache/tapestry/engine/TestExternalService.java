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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.ResponseOutputStream;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.services.ServiceConstants;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.engine.ExternalService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestExternalService extends ServiceTestCase
{
    private LinkFactory newLinkFactory(IRequestCycle cycle, Object[] serviceParameters)
    {
        MockControl control = newControl(LinkFactory.class);
        LinkFactory lf = (LinkFactory) control.getMock();

        lf.extractServiceParameters(cycle);
        control.setReturnValue(serviceParameters);

        return lf;
    }

    public void testGetLink()
    {
        Object[] serviceParameters = new Object[0];

        Map parameters = new HashMap();
        parameters.put(ServiceConstants.SERVICE, Tapestry.EXTERNAL_SERVICE);
        parameters.put(ServiceConstants.PAGE, "ActivePage");
        parameters.put(ServiceConstants.PARAMETER, serviceParameters);

        MockControl lfc = newControl(LinkFactory.class);
        LinkFactory lf = (LinkFactory) lfc.getMock();

        ILink link = (ILink) newMock(ILink.class);

        IRequestCycle cycle = (IRequestCycle) newMock(IRequestCycle.class);

        lf.constructLink(cycle, parameters, true);
        lfc.setReturnValue(link);

        replayControls();

        ExternalService es = new ExternalService();
        es.setLinkFactory(lf);

        ExternalServiceParameter p = new ExternalServiceParameter("ActivePage", serviceParameters);

        assertSame(link, es.getLink(cycle, p));

        verifyControls();
    }

    public void testService() throws Exception
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        IExternalPage page = (IExternalPage) newMock(IExternalPage.class);

        Object[] serviceParameters = new Object[0];

        cycle.getParameter(ServiceConstants.PAGE);
        cyclec.setReturnValue("ActivePage");

        cycle.getPage("ActivePage");
        cyclec.setReturnValue(page);

        LinkFactory lf = newLinkFactory(cycle, serviceParameters);

        cycle.setServiceParameters(serviceParameters);
        cycle.activate(page);
        page.activateExternalPage(serviceParameters, cycle);

        ResponseOutputStream ros = new ResponseOutputStream(null);

        ResponseRenderer rr = (ResponseRenderer) newMock(ResponseRenderer.class);

        rr.renderResponse(cycle, ros);

        replayControls();

        ExternalService es = new ExternalService();
        es.setLinkFactory(lf);
        es.setResponseRenderer(rr);

        es.service(cycle, ros);

        verifyControls();
    }

    public void testServiceWrongType() throws Exception
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        cycle.getParameter(ServiceConstants.PAGE);
        cyclec.setReturnValue("ActivePage");

        cycle.getPage("ActivePage");
        cyclec.setReturnValue(page);

        page.getPageName();
        pagec.setReturnValue("ActivePage");

        Location l = fabricateLocation(17);

        page.getLocation();
        pagec.setReturnValue(l);

        ResponseOutputStream ros = new ResponseOutputStream(null);

        replayControls();

        ExternalService es = new ExternalService();

        try
        {
            es.service(cycle, ros);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Page ActivePage does not implement the org.apache.tapestry.IExternalPage interface.", ex
                    .getMessage());
            assertSame(l, ex.getLocation());
            assertSame(page, ex.getComponent());
        }

        verifyControls();
    }
}