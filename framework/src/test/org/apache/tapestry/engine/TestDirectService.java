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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.StaleSessionException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.engine.DirectService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestDirectService extends ServiceTestCase
{
    public void testGetLinkOnSamePage()
    {
        IPage page = newPage("ThePage");

        MockControl cc = newControl(IDirect.class);
        IDirect c = (IDirect) cc.getMock();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        WebRequest request = newWebRequest(false, null);

        cycle.getPage();
        cyclec.setReturnValue(page);

        c.getPage();
        cc.setReturnValue(page);

        c.getIdPath();
        cc.setReturnValue("fred.barney");

        Object[] serviceParameters = new Object[0];

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.SERVICE, Tapestry.DIRECT_SERVICE);
        parameters.put(ServiceConstants.PAGE, "ThePage");
        parameters.put(ServiceConstants.COMPONENT, "fred.barney");
        parameters.put(ServiceConstants.CONTAINER, null);
        parameters.put(ServiceConstants.SESSION, null);
        parameters.put(ServiceConstants.PARAMETER, serviceParameters);

        MockControl lfc = newControl(LinkFactory.class);
        LinkFactory lf = (LinkFactory) lfc.getMock();

        ILink link = newLink();

        lf.constructLink(cycle, parameters, true);
        lfc.setReturnValue(link);

        replayControls();

        DirectService ds = new DirectService();

        ds.setLinkFactory(lf);
        ds.setRequest(request);

        assertSame(link, ds.getLink(cycle, new DirectServiceParameter(c, serviceParameters)));

        verifyControls();
    }

    public void testGetLinkOnSamePageStateful()
    {
        IPage page = newPage("ThePage");
        WebRequest request = newWebRequest(false, newWebSession());

        MockControl cc = newControl(IDirect.class);
        IDirect c = (IDirect) cc.getMock();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getPage();
        cyclec.setReturnValue(page);

        c.getPage();
        cc.setReturnValue(page);

        c.getIdPath();
        cc.setReturnValue("fred.barney");

        Object[] serviceParameters = new Object[0];

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.SERVICE, Tapestry.DIRECT_SERVICE);
        parameters.put(ServiceConstants.PAGE, "ThePage");
        parameters.put(ServiceConstants.COMPONENT, "fred.barney");
        parameters.put(ServiceConstants.CONTAINER, null);
        parameters.put(ServiceConstants.SESSION, "T");
        parameters.put(ServiceConstants.PARAMETER, serviceParameters);

        MockControl lfc = newControl(LinkFactory.class);
        LinkFactory lf = (LinkFactory) lfc.getMock();

        ILink link = newLink();

        lf.constructLink(cycle, parameters, true);
        lfc.setReturnValue(link);

        replayControls();

        DirectService ds = new DirectService();
        ds.setLinkFactory(lf);
        ds.setRequest(request);

        assertSame(link, ds.getLink(cycle, new DirectServiceParameter(c, serviceParameters)));

        verifyControls();
    }

    public void testGetLinkOnDifferentPage()
    {
        IPage page = newPage("ActivePage");
        IPage componentPage = newPage("ComponentPage");
        WebRequest request = newWebRequest(false, null);
        MockControl cc = newControl(IDirect.class);
        IDirect c = (IDirect) cc.getMock();

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getPage();
        cyclec.setReturnValue(page);

        c.getPage();
        cc.setReturnValue(componentPage);

        c.getIdPath();
        cc.setReturnValue("fred.barney");

        Object[] serviceParameters = new Object[0];

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.SERVICE, Tapestry.DIRECT_SERVICE);
        parameters.put(ServiceConstants.PAGE, "ActivePage");
        parameters.put(ServiceConstants.COMPONENT, "fred.barney");
        parameters.put(ServiceConstants.CONTAINER, "ComponentPage");
        parameters.put(ServiceConstants.SESSION, null);
        parameters.put(ServiceConstants.PARAMETER, serviceParameters);

        MockControl lfc = newControl(LinkFactory.class);
        LinkFactory lf = (LinkFactory) lfc.getMock();

        ILink link = newLink();

        lf.constructLink(cycle, parameters, true);
        lfc.setReturnValue(link);

        replayControls();

        DirectService ds = new DirectService();
        ds.setLinkFactory(lf);
        ds.setRequest(request);

        assertSame(link, ds.getLink(cycle, new DirectServiceParameter(c, serviceParameters)));

        verifyControls();
    }

    public void testServiceSimple() throws Exception
    {
        Object[] serviceParameters = new Object[0];

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getParameter(ServiceConstants.COMPONENT);
        cyclec.setReturnValue("fred.barney");

        cycle.getParameter(ServiceConstants.CONTAINER);
        cyclec.setReturnValue(null);

        cycle.getParameter(ServiceConstants.PAGE);
        cyclec.setReturnValue("ActivePage");

        cycle.getParameter(ServiceConstants.SESSION);
        cyclec.setReturnValue(null);

        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        cycle.getPage("ActivePage");
        cyclec.setReturnValue(page);

        cycle.activate(page);

        MockControl dc = newControl(IDirect.class);
        IDirect d = (IDirect) dc.getMock();

        page.getNestedComponent("fred.barney");
        pagec.setReturnValue(d);

        MockControl lfc = newControl(LinkFactory.class);
        LinkFactory lf = (LinkFactory) lfc.getMock();

        lf.extractServiceParameters(cycle);
        lfc.setReturnValue(serviceParameters);

        cycle.setServiceParameters(serviceParameters);

        d.trigger(cycle);

        ResponseRenderer rr = newResponseRenderer();

        rr.renderResponse(cycle);

        replayControls();

        DirectService ds = new DirectService();
        ds.setLinkFactory(lf);
        ds.setResponseRenderer(rr);

        ds.service(cycle);

        verifyControls();
    }

    /**
     * The complex case is where the component is contained on a different page than the active (at
     * the time of render) page.
     */
    public void testServiceComplex() throws Exception
    {
        Object[] serviceParameters = new Object[0];

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getParameter(ServiceConstants.COMPONENT);
        cyclec.setReturnValue("fred.barney");

        cycle.getParameter(ServiceConstants.CONTAINER);
        cyclec.setReturnValue("ComponentPage");

        cycle.getParameter(ServiceConstants.PAGE);
        cyclec.setReturnValue("ActivePage");

        cycle.getParameter(ServiceConstants.SESSION);
        cyclec.setReturnValue(null);

        MockControl activePagec = newControl(IPage.class);
        IPage activePage = (IPage) activePagec.getMock();

        cycle.getPage("ActivePage");
        cyclec.setReturnValue(activePage);

        cycle.activate(activePage);

        MockControl componentPagec = newControl(IPage.class);
        IPage componentPage = (IPage) componentPagec.getMock();

        cycle.getPage("ComponentPage");
        cyclec.setReturnValue(componentPage);

        MockControl dc = newControl(IDirect.class);
        IDirect d = (IDirect) dc.getMock();

        componentPage.getNestedComponent("fred.barney");
        componentPagec.setReturnValue(d);

        MockControl lfc = newControl(LinkFactory.class);
        LinkFactory lf = (LinkFactory) lfc.getMock();

        lf.extractServiceParameters(cycle);
        lfc.setReturnValue(serviceParameters);

        cycle.setServiceParameters(serviceParameters);

        d.trigger(cycle);

        ResponseRenderer rr = newResponseRenderer();

        rr.renderResponse(cycle);

        replayControls();

        DirectService ds = new DirectService();
        ds.setLinkFactory(lf);
        ds.setResponseRenderer(rr);

        ds.service(cycle);

        verifyControls();
    }

    public void testServiceNotDirect() throws Exception
    {
        Location l = fabricateLocation(7);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getParameter(ServiceConstants.COMPONENT);
        cyclec.setReturnValue("fred.barney");

        cycle.getParameter(ServiceConstants.CONTAINER);
        cyclec.setReturnValue(null);

        cycle.getParameter(ServiceConstants.PAGE);
        cyclec.setReturnValue("ActivePage");

        cycle.getParameter(ServiceConstants.SESSION);
        cyclec.setReturnValue(null);

        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        cycle.getPage("ActivePage");
        cyclec.setReturnValue(page);

        cycle.activate(page);

        MockControl cc = newControl(IComponent.class);
        IComponent c = (IComponent) cc.getMock();

        page.getNestedComponent("fred.barney");
        pagec.setReturnValue(c);

        c.getExtendedId();
        cc.setReturnValue("ActivePage/fred.barney");

        c.getLocation();
        cc.setReturnValue(l);

        replayControls();

        DirectService ds = new DirectService();

        try
        {
            ds.service(cycle);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Component ActivePage/fred.barney does not implement the org.apache.tapestry.IDirect interface.",
                    ex.getMessage());
            assertSame(c, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verifyControls();
    }

    public void testSessionActiveAndRequired() throws Exception
    {
        Object[] serviceParameters = new Object[0];

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getParameter(ServiceConstants.COMPONENT);
        cyclec.setReturnValue("fred.barney");

        cycle.getParameter(ServiceConstants.CONTAINER);
        cyclec.setReturnValue(null);

        cycle.getParameter(ServiceConstants.PAGE);
        cyclec.setReturnValue("ActivePage");

        cycle.getParameter(ServiceConstants.SESSION);
        cyclec.setReturnValue("T");

        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        cycle.getPage("ActivePage");
        cyclec.setReturnValue(page);

        cycle.activate(page);

        MockControl dc = newControl(IDirect.class);
        IDirect d = (IDirect) dc.getMock();

        page.getNestedComponent("fred.barney");
        pagec.setReturnValue(d);

        d.isStateful();
        dc.setReturnValue(true);

        WebSession session = newWebSession(false);
        WebRequest request = newWebRequest(session);

        MockControl lfc = newControl(LinkFactory.class);
        LinkFactory lf = (LinkFactory) lfc.getMock();

        lf.extractServiceParameters(cycle);
        lfc.setReturnValue(serviceParameters);

        cycle.setServiceParameters(serviceParameters);

        d.trigger(cycle);

        ResponseRenderer rr = newResponseRenderer();

        rr.renderResponse(cycle);

        replayControls();

        DirectService ds = new DirectService();
        ds.setLinkFactory(lf);
        ds.setResponseRenderer(rr);
        ds.setRequest(request);

        ds.service(cycle);

        verifyControls();
    }

    public void testStaleSession() throws Exception
    {
        Location l = fabricateLocation(99);

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getParameter(ServiceConstants.COMPONENT);
        cyclec.setReturnValue("fred.barney");

        cycle.getParameter(ServiceConstants.CONTAINER);
        cyclec.setReturnValue(null);

        cycle.getParameter(ServiceConstants.PAGE);
        cyclec.setReturnValue("ActivePage");

        cycle.getParameter(ServiceConstants.SESSION);
        cyclec.setReturnValue("T");

        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        cycle.getPage("ActivePage");
        cyclec.setReturnValue(page);

        cycle.activate(page);

        MockControl dc = newControl(IDirect.class);
        IDirect d = (IDirect) dc.getMock();

        page.getNestedComponent("fred.barney");
        pagec.setReturnValue(d);

        d.isStateful();
        dc.setReturnValue(true);

        WebRequest request = newWebRequest(null);

        d.getExtendedId();
        dc.setReturnValue("ActivePage/fred.barney");

        page.getLocation();
        pagec.setReturnValue(l);

        page.getPageName();
        pagec.setReturnValue("ActivePage");

        replayControls();

        DirectService ds = new DirectService();
        ds.setRequest(request);

        try
        {
            ds.service(cycle);
            unreachable();
        }
        catch (StaleSessionException ex)
        {
            assertEquals(
                    "Component ActivePage/fred.barney is stateful, but the HttpSession has expired (or has not yet been created).",
                    ex.getMessage());
            assertSame(page, ex.getComponent());
            assertSame(l, ex.getLocation());
            assertEquals("ActivePage", ex.getPageName());
            assertSame(page, ex.getPage());
        }

        verifyControls();
    }
}