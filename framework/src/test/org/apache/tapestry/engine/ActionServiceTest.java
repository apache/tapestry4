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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.IAction;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.StaleSessionException;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;

/**
 * Tests for {@link org.apache.tapestry.engine.ActionService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ActionServiceTest extends ServiceTestCase
{
    public void testGetLinkSimple()
    {
        IComponent component = newComponent();
        IPage page = newPage("ActivePage");
        IRequestCycle cycle = newCycle();
        WebRequest request = newWebRequest(false, null);
        LinkFactory lf = newLinkFactory();
        ILink link = newLink();

        trainGetPage(cycle, page);
        trainGetPage(component, page);

        Map parameters = new HashMap();

        trainGetIdPath(component, "fred.barney");

        parameters.put(ServiceConstants.COMPONENT, "fred.barney");
        parameters.put(ServiceConstants.PAGE, "ActivePage");
        parameters.put(ServiceConstants.CONTAINER, null);
        parameters.put("action", "action-id");
        parameters.put(ServiceConstants.SESSION, null);

        ActionService as = new ActionService();
        as.setLinkFactory(lf);
        as.setRequest(request);
        as.setRequestCycle(cycle);

        trainConstructLink(lf, as, false, parameters, true, link);

        replay();

        ActionServiceParameter p = new ActionServiceParameter(component, "action-id");

        assertSame(link, as.getLink(false, p));

        verify();
    }

    public void testGetLinkSimplePost()
    {
        IComponent component = newComponent();
        IPage page = newPage("ActivePage");
        IRequestCycle cycle = newCycle();
        WebRequest request = newWebRequest(false, null);
        LinkFactory lf = newLinkFactory();
        ILink link = newLink();

        trainGetPage(cycle, page);
        trainGetPage(component, page);

        Map parameters = new HashMap();

        trainGetIdPath(component, "fred.barney");

        parameters.put(ServiceConstants.COMPONENT, "fred.barney");
        parameters.put(ServiceConstants.PAGE, "ActivePage");
        parameters.put(ServiceConstants.CONTAINER, null);
        parameters.put("action", "action-id");
        parameters.put(ServiceConstants.SESSION, null);

        ActionService as = new ActionService();
        as.setLinkFactory(lf);
        as.setRequest(request);
        as.setRequestCycle(cycle);

        trainConstructLink(lf, as, true, parameters, true, link);

        replay();

        ActionServiceParameter p = new ActionServiceParameter(component, "action-id");

        assertSame(link, as.getLink(true, p));

        verify();
    }

    public void testGetLinkComplex()
    {
        IComponent component = newComponent();
        WebRequest request = newWebRequest(false, newWebSession());
        IPage activePage = newPage("ActivePage");
        IPage componentPage = newPage("ComponentPage");
        IRequestCycle cycle = newCycle();
        LinkFactory lf = newLinkFactory();
        ILink link = newLink();

        trainGetPage(cycle, activePage);
        trainGetPage(component, componentPage);

        Map parameters = new HashMap();

        trainGetIdPath(component, "fred.barney");

        parameters.put(ServiceConstants.COMPONENT, "fred.barney");
        parameters.put(ServiceConstants.PAGE, "ActivePage");
        parameters.put(ServiceConstants.CONTAINER, "ComponentPage");
        parameters.put("action", "action-id");
        parameters.put(ServiceConstants.SESSION, "T");

        ActionService as = new ActionService();
        as.setLinkFactory(lf);
        as.setRequest(request);
        as.setRequestCycle(cycle);

        trainConstructLink(lf, as, false, parameters, true, link);

        replay();

        ActionServiceParameter p = new ActionServiceParameter(component, "action-id");

        assertSame(link, as.getLink(false, p));

        verify();
    }

    public void testServiceSimple() throws Exception
    {
        IRequestCycle cycle = newCycle();
        IPage page = newPage();
        IAction action = newAction();
        ResponseRenderer rr = newResponseRenderer();

        trainGetParameter(cycle, ServiceConstants.COMPONENT, "fred.barney");
        trainGetParameter(cycle, ServiceConstants.CONTAINER, null);
        trainGetParameter(cycle, ServiceConstants.PAGE, "ActivePage");
        trainGetParameter(cycle, "action", "action-id");
        trainGetParameter(cycle, ServiceConstants.SESSION, null);

        trainGetPage(cycle, "ActivePage", page);

        cycle.activate(page);

        trainGetNestedComponent(page, "fred.barney", action);

        cycle.rewindPage("action-id", action);

        rr.renderResponse(cycle);

        replay();

        ActionService as = new ActionService();
        as.setResponseRenderer(rr);

        as.service(cycle);

        verify();
    }

    private IAction newAction()
    {
        return (IAction) newMock(IAction.class);
    }

    public void testSeviceActiveSession() throws Exception
    {
        IRequestCycle cycle = newCycle();
        IPage page = newPage();
        IAction action = newAction();
        WebSession session = newWebSession(false);
        WebRequest request = newWebRequest(session);
        ResponseRenderer rr = newResponseRenderer();

        trainGetParameter(cycle, ServiceConstants.COMPONENT, "fred.barney");
        trainGetParameter(cycle, ServiceConstants.CONTAINER, null);
        trainGetParameter(cycle, ServiceConstants.PAGE, "ActivePage");
        trainGetParameter(cycle, "action", "action-id");
        trainGetParameter(cycle, ServiceConstants.SESSION, "T");

        trainGetPage(cycle, "ActivePage", page);

        cycle.activate(page);

        trainGetNestedComponent(page, "fred.barney", action);

        trainGetRequiresSession(action, true);

        cycle.rewindPage("action-id", action);

        rr.renderResponse(cycle);

        replay();

        ActionService as = new ActionService();
        as.setResponseRenderer(rr);
        as.setRequest(request);

        as.service(cycle);

        verify();
    }

    public void testServiceNotAction() throws Exception
    {
        IRequestCycle cycle = newCycle();
        IPage page = newPage();
        IComponent component = newComponent();
        Location l = newLocation();

        trainGetParameter(cycle, ServiceConstants.COMPONENT, "fred.barney");
        trainGetParameter(cycle, ServiceConstants.CONTAINER, null);
        trainGetParameter(cycle, ServiceConstants.PAGE, "ActivePage");
        trainGetParameter(cycle, "action", "action-id");
        trainGetParameter(cycle, ServiceConstants.SESSION, "T");

        trainGetPage(cycle, "ActivePage", page);

        cycle.activate(page);

        trainGetNestedComponent(page, "fred.barney", component);

        trainGetExtendedId(component, "ActivePage/fred.barney");

        trainGetLocation(component, l);

        replay();

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

        verify();
    }

    public void testServiceStaleSession() throws Exception
    {
        IRequestCycle cycle = newCycle();
        IPage page = newPage();
        IAction action = newAction();
        WebRequest request = newWebRequest(null);
        Location l = newLocation();

        trainGetParameter(cycle, ServiceConstants.COMPONENT, "fred.barney");
        trainGetParameter(cycle, ServiceConstants.CONTAINER, null);
        trainGetParameter(cycle, ServiceConstants.PAGE, "ActivePage");
        trainGetParameter(cycle, "action", "action-id");
        trainGetParameter(cycle, ServiceConstants.SESSION, "T");

        trainGetPage(cycle, "ActivePage", page);

        cycle.activate(page);

        trainGetNestedComponent(page, "fred.barney", action);

        trainGetRequiresSession(action, true);

        trainGetExtendedId(action, "ActivePage/fred.barney");

        trainGetLocation(page, l);

        trainGetPageName(page, "ActivePage");

        replay();

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

        verify();
    }

    public void testServiceComplex() throws Exception
    {
        IRequestCycle cycle = newCycle();
        IPage activePage = newPage();
        IPage componentPage = newPage();
        IAction action = newAction();
        ResponseRenderer rr = newResponseRenderer();

        trainGetParameter(cycle, ServiceConstants.COMPONENT, "fred.barney");
        trainGetParameter(cycle, ServiceConstants.CONTAINER, "ComponentPage");
        trainGetParameter(cycle, ServiceConstants.PAGE, "ActivePage");
        trainGetParameter(cycle, "action", "action-id");
        trainGetParameter(cycle, ServiceConstants.SESSION, null);

        trainGetPage(cycle, "ActivePage", activePage);

        cycle.activate(activePage);

        trainGetPage(cycle, "ComponentPage", componentPage);

        trainGetNestedComponent(componentPage, "fred.barney", action);

        cycle.rewindPage("action-id", action);

        rr.renderResponse(cycle);

        replay();

        ActionService as = new ActionService();
        as.setResponseRenderer(rr);

        as.service(cycle);

        verify();
    }

    protected void trainGetRequiresSession(IAction action, boolean requiresSession)
    {
        expect(action.getRequiresSession()).andReturn(requiresSession);
    }
}
