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
import org.apache.tapestry.IAction;
import org.apache.tapestry.IComponent;
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
 * Tests for {@link org.apache.tapestry.engine.ActionService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestActionService extends ServiceTestCase
{

    public void testGetLinkSimple()
    {
        MockControl componentc = newControl(IComponent.class);
        IComponent component = (IComponent) componentc.getMock();

        IPage page = newPage("ActivePage");

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        WebRequest request = newWebRequest(false, null);

        cycle.getPage();
        cyclec.setReturnValue(page);

        component.getPage();
        componentc.setReturnValue(page);

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.SERVICE, Tapestry.ACTION_SERVICE);

        component.getIdPath();
        componentc.setReturnValue("fred.barney");

        parameters.put(ServiceConstants.COMPONENT, "fred.barney");
        parameters.put(ServiceConstants.PAGE, "ActivePage");
        parameters.put(ServiceConstants.CONTAINER, null);
        parameters.put("action", "action-id");
        parameters.put(ServiceConstants.SESSION, null);

        ILink link = newLink();

        MockControl lfc = newControl(LinkFactory.class);
        LinkFactory lf = (LinkFactory) lfc.getMock();

        lf.constructLink(cycle, parameters, true);
        lfc.setReturnValue(link);

        replayControls();

        ActionService as = new ActionService();
        as.setLinkFactory(lf);
        as.setRequest(request);

        ActionServiceParameter p = new ActionServiceParameter(component, "action-id");

        assertSame(link, as.getLink(cycle, p));

        verifyControls();
    }

    public void testGetLinkComplex()
    {
        MockControl componentc = newControl(IComponent.class);
        IComponent component = (IComponent) componentc.getMock();
        WebRequest request = newWebRequest(false, newWebSession());

        IPage activePage = newPage("ActivePage");
        IPage componentPage = newPage("ComponentPage");

        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getPage();
        cyclec.setReturnValue(activePage);

        component.getPage();
        componentc.setReturnValue(componentPage);

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.SERVICE, Tapestry.ACTION_SERVICE);

        component.getIdPath();
        componentc.setReturnValue("fred.barney");

        parameters.put(ServiceConstants.COMPONENT, "fred.barney");
        parameters.put(ServiceConstants.PAGE, "ActivePage");
        parameters.put(ServiceConstants.CONTAINER, "ComponentPage");
        parameters.put("action", "action-id");
        parameters.put(ServiceConstants.SESSION, "T");

        ILink link = newLink();

        MockControl lfc = newControl(LinkFactory.class);
        LinkFactory lf = (LinkFactory) lfc.getMock();

        lf.constructLink(cycle, parameters, true);
        lfc.setReturnValue(link);

        replayControls();

        ActionService as = new ActionService();
        as.setLinkFactory(lf);
        as.setRequest(request);

        ActionServiceParameter p = new ActionServiceParameter(component, "action-id");

        assertSame(link, as.getLink(cycle, p));

        verifyControls();
    }

    public void testServiceSimple() throws Exception
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getParameter(ServiceConstants.COMPONENT);
        cyclec.setReturnValue("fred.barney");

        cycle.getParameter(ServiceConstants.CONTAINER);
        cyclec.setReturnValue(null);

        cycle.getParameter(ServiceConstants.PAGE);
        cyclec.setReturnValue("ActivePage");

        cycle.getParameter("action");
        cyclec.setReturnValue("action-id");

        cycle.getParameter(ServiceConstants.SESSION);
        cyclec.setReturnValue(null);

        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        cycle.getPage("ActivePage");
        cyclec.setReturnValue(page);

        cycle.activate(page);

        IAction action = (IAction) newMock(IAction.class);

        page.getNestedComponent("fred.barney");
        pagec.setReturnValue(action);

        cycle.rewindPage("action-id", action);

        ResponseRenderer rr = newResponseRenderer();

        rr.renderResponse(cycle);

        replayControls();

        ActionService as = new ActionService();
        as.setResponseRenderer(rr);

        as.service(cycle);

        verifyControls();
    }

    public void testSeviceActiveSession() throws Exception
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getParameter(ServiceConstants.COMPONENT);
        cyclec.setReturnValue("fred.barney");

        cycle.getParameter(ServiceConstants.CONTAINER);
        cyclec.setReturnValue(null);

        cycle.getParameter(ServiceConstants.PAGE);
        cyclec.setReturnValue("ActivePage");

        cycle.getParameter("action");
        cyclec.setReturnValue("action-id");

        cycle.getParameter(ServiceConstants.SESSION);
        cyclec.setReturnValue("T");

        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        cycle.getPage("ActivePage");
        cyclec.setReturnValue(page);

        cycle.activate(page);

        MockControl actionc = newControl(IAction.class);
        IAction action = (IAction) actionc.getMock();

        page.getNestedComponent("fred.barney");
        pagec.setReturnValue(action);

        action.getRequiresSession();
        actionc.setReturnValue(true);

        WebSession session = newWebSession(false);
        WebRequest request = newWebRequest(session);

        cycle.rewindPage("action-id", action);

        ResponseRenderer rr = newResponseRenderer();

        rr.renderResponse(cycle);

        replayControls();

        ActionService as = new ActionService();
        as.setResponseRenderer(rr);
        as.setRequest(request);

        as.service(cycle);

        verifyControls();
    }

    public void testServiceNotAction() throws Exception
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getParameter(ServiceConstants.COMPONENT);
        cyclec.setReturnValue("fred.barney");

        cycle.getParameter(ServiceConstants.CONTAINER);
        cyclec.setReturnValue(null);

        cycle.getParameter(ServiceConstants.PAGE);
        cyclec.setReturnValue("ActivePage");

        cycle.getParameter("action");
        cyclec.setReturnValue("action-id");

        cycle.getParameter(ServiceConstants.SESSION);
        cyclec.setReturnValue("T");

        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        cycle.getPage("ActivePage");
        cyclec.setReturnValue(page);

        cycle.activate(page);

        MockControl componentc = newControl(IComponent.class);
        IComponent component = (IComponent) componentc.getMock();

        page.getNestedComponent("fred.barney");
        pagec.setReturnValue(component);

        component.getExtendedId();
        componentc.setReturnValue("ActivePage/fred.barney");

        Location l = fabricateLocation(17);

        component.getLocation();
        componentc.setReturnValue(l);

        replayControls();

        ActionService as = new ActionService();

        try
        {
            as.service(cycle);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Component ActivePage/fred.barney does not implement the org.apache.tapestry.IAction interface.",
                    ex.getMessage());
            assertSame(component, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verifyControls();
    }

    public void testServiceStaleSession() throws Exception
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getParameter(ServiceConstants.COMPONENT);
        cyclec.setReturnValue("fred.barney");

        cycle.getParameter(ServiceConstants.CONTAINER);
        cyclec.setReturnValue(null);

        cycle.getParameter(ServiceConstants.PAGE);
        cyclec.setReturnValue("ActivePage");

        cycle.getParameter("action");
        cyclec.setReturnValue("action-id");

        cycle.getParameter(ServiceConstants.SESSION);
        cyclec.setReturnValue("T");

        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        cycle.getPage("ActivePage");
        cyclec.setReturnValue(page);

        cycle.activate(page);

        MockControl actionc = newControl(IAction.class);
        IAction action = (IAction) actionc.getMock();

        page.getNestedComponent("fred.barney");
        pagec.setReturnValue(action);

        action.getRequiresSession();
        actionc.setReturnValue(true);

        WebRequest request = newWebRequest(null);

        action.getExtendedId();
        actionc.setReturnValue("ActivePage/fred.barney");

        Location l = fabricateLocation(2);

        page.getLocation();
        pagec.setReturnValue(l);

        page.getPageName();
        pagec.setReturnValue("ActivePage");

        replayControls();

        ActionService as = new ActionService();
        as.setRequest(request);

        try
        {
            as.service(cycle);
            unreachable();
        }
        catch (StaleSessionException ex)
        {
            assertEquals(
                    "Component ActivePage/fred.barney is stateful, but the HttpSession has expired (or has not yet been created).",
                    ex.getMessage());
            assertSame(l, ex.getLocation());
            assertSame(page, ex.getPage());
            assertEquals("ActivePage", ex.getPageName());
        }

        verifyControls();
    }

    public void testServiceComplex() throws Exception
    {
        MockControl cyclec = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) cyclec.getMock();

        cycle.getParameter(ServiceConstants.COMPONENT);
        cyclec.setReturnValue("fred.barney");

        cycle.getParameter(ServiceConstants.CONTAINER);
        cyclec.setReturnValue("ComponentPage");

        cycle.getParameter(ServiceConstants.PAGE);
        cyclec.setReturnValue("ActivePage");

        cycle.getParameter("action");
        cyclec.setReturnValue("action-id");

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

        IAction action = (IAction) newMock(IAction.class);

        componentPage.getNestedComponent("fred.barney");
        componentPagec.setReturnValue(action);

        cycle.rewindPage("action-id", action);

        ResponseRenderer rr = newResponseRenderer();

        rr.renderResponse(cycle);

        replayControls();

        ActionService as = new ActionService();
        as.setResponseRenderer(rr);

        as.service(cycle);

        verifyControls();
    }
}