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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.services.ServiceConstants;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.engine.ExternalService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class ExternalServiceTest extends ServiceTestCase
{
    public void test_Get_Link()
    {
        Object[] serviceParameters = new Object[0];
        LinkFactory lf = newLinkFactory();
        ILink link = newLink();

        Map parameters = new HashMap();
        parameters.put(ServiceConstants.PAGE, "ActivePage");
        parameters.put(ServiceConstants.PARAMETER, serviceParameters);

        ExternalService es = new ExternalService();
        es.setLinkFactory(lf);

        trainConstructLink(lf, es, false, parameters, true, link);

        replay();

        ExternalServiceParameter p = new ExternalServiceParameter("ActivePage", serviceParameters);

        assertSame(link, es.getLink(false, p));

        verify();
    }

    public void test_Service() throws Exception
    {
        IRequestCycle cycle = newCycle();
        IExternalPage page = newInstance(ExternalLinkPage.class, new Object[0]);
        Object[] parameters = new Object[0];
        LinkFactory lf = newLinkFactory();
        ResponseRenderer rr = newResponseRenderer();
        
        trainGetParameter(cycle, ServiceConstants.PAGE, "ActivePage");
        
        trainGetPage(cycle, "ActivePage", page);
        
        trainExtractListenerParameters(lf, cycle, parameters);
        
        cycle.setListenerParameters(parameters);
        cycle.activate(page, false);
        
        page.activateExternalPage(parameters, cycle);
        
        page.validate(cycle);
        
        rr.renderResponse(cycle);

        replay();

        ExternalService es = new ExternalService();
        es.setLinkFactory(lf);
        es.setResponseRenderer(rr);

        es.service(cycle);

        verify();
    }

    public void test_Service_Wrong_Type() throws Exception
    {

        IRequestCycle cycle = newCycle();
        IPage page = newPage();
        Location l = newLocation();

        trainGetParameter(cycle, ServiceConstants.PAGE, "ActivePage");

        trainGetPage(cycle, "ActivePage", page);

        trainGetPageName(page, "ActivePage");
        trainGetLocation(page, l);

        replay();

        ExternalService es = new ExternalService();

        try
        {
            es.service(cycle);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Page ActivePage does not implement the org.apache.tapestry.IExternalPage interface.",
                    ex.getMessage());
            assertSame(l, ex.getLocation());
            assertSame(page, ex.getComponent());
        }

        verify();
    }
}