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
import org.apache.hivemind.test.AggregateArgumentsMatcher;
import org.apache.hivemind.test.ArgumentMatcher;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IDirectEvent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IgnoreMatcher;
import org.apache.tapestry.StaleSessionException;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;

/**
 * Tests for {@link org.apache.tapestry.engine.DirectService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class DirectEventServiceTest extends ServiceTestCase
{
    protected IDirectEvent newDirect()
    {
        return (IDirectEvent) newMock(IDirectEvent.class);
    }

    public void testGetLinkOnSamePage()
    {
        IPage page = newPage("ThePage");
        IDirectEvent c = newDirect();
        IRequestCycle cycle = newCycle();
        WebRequest request = newWebRequest(false, null);
        LinkFactory lf = newLinkFactory();
        ILink link = newLink();

        trainGetPage(cycle, page);
        trainGetPage(c, page);
        trainGetIdPath(c, "fred.barney");

        Object[] serviceParameters = new Object[0];

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.PAGE, "ThePage");
        parameters.put(ServiceConstants.COMPONENT, "fred.barney");
        parameters.put(ServiceConstants.CONTAINER, null);
        parameters.put(ServiceConstants.SESSION, null);
        parameters.put(ServiceConstants.PARAMETER, serviceParameters);

        DirectEventService ds = new DirectEventService();

        ds.setLinkFactory(lf);
        ds.setRequest(request);
        ds.setRequestCycle(cycle);

        trainConstructLink(lf, ds, false, parameters, true, link);

        replayControls();

        assertSame(link, ds.getLink(false, new DirectEventServiceParameter(c, serviceParameters)));

        verifyControls();
    }

    public void testGetLinkOnSamePageForPost()
    {
        IPage page = newPage("ThePage");
        IDirectEvent c = newDirect();
        IRequestCycle cycle = newCycle();
        WebRequest request = newWebRequest(false, null);
        LinkFactory lf = newLinkFactory();
        ILink link = newLink();

        trainGetPage(cycle, page);
        trainGetPage(c, page);
        trainGetIdPath(c, "fred.barney");

        Object[] serviceParameters = new Object[0];

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.PAGE, "ThePage");
        parameters.put(ServiceConstants.COMPONENT, "fred.barney");
        parameters.put(ServiceConstants.CONTAINER, null);
        parameters.put(ServiceConstants.SESSION, null);
        parameters.put(ServiceConstants.PARAMETER, serviceParameters);

        DirectEventService ds = new DirectEventService();

        ds.setLinkFactory(lf);
        ds.setRequest(request);
        ds.setRequestCycle(cycle);

        trainConstructLink(lf, ds, true, parameters, true, link);

        replayControls();

        assertSame(link, ds.getLink(true, new DirectEventServiceParameter(c, serviceParameters)));

        verifyControls();
    }

    public void testGetLinkOnSamePageStateful()
    {
        IPage page = newPage("ThePage");
        IDirectEvent c = newDirect();
        IRequestCycle cycle = newCycle();
        WebSession session = newWebSession();
        WebRequest request = newWebRequest(false, session);
        LinkFactory lf = newLinkFactory();
        ILink link = newLink();

        trainGetPage(cycle, page);
        trainGetPage(c, page);
        trainGetIdPath(c, "fred.barney");

        Object[] serviceParameters = new Object[0];

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.PAGE, "ThePage");
        parameters.put(ServiceConstants.COMPONENT, "fred.barney");
        parameters.put(ServiceConstants.CONTAINER, null);
        parameters.put(ServiceConstants.SESSION, "T");
        parameters.put(ServiceConstants.PARAMETER, serviceParameters);

        DirectEventService ds = new DirectEventService();
        ds.setLinkFactory(lf);
        ds.setRequest(request);
        ds.setRequestCycle(cycle);

        trainConstructLink(lf, ds, false, parameters, true, link);

        replayControls();

        assertSame(link, ds.getLink(false, new DirectEventServiceParameter(c, serviceParameters)));

        verifyControls();
    }

    public void testGetLinkOnDifferentPage()
    {
        IPage page = newPage("ActivePage");
        IPage componentPage = newPage("ComponentPage");
        IDirectEvent c = newDirect();
        IRequestCycle cycle = newCycle();
        WebRequest request = newWebRequest(false, null);
        LinkFactory lf = newLinkFactory();
        ILink link = newLink();

        trainGetPage(cycle, page);
        trainGetPage(c, componentPage);
        trainGetIdPath(c, "fred.barney");

        Object[] serviceParameters = new Object[0];

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.PAGE, "ActivePage");
        parameters.put(ServiceConstants.COMPONENT, "fred.barney");
        parameters.put(ServiceConstants.CONTAINER, "ComponentPage");
        parameters.put(ServiceConstants.SESSION, null);
        parameters.put(ServiceConstants.PARAMETER, serviceParameters);

        DirectEventService ds = new DirectEventService();
        ds.setLinkFactory(lf);
        ds.setRequest(request);
        ds.setRequestCycle(cycle);

        trainConstructLink(lf, ds, false, parameters, true, link);

        replayControls();

        assertSame(link, ds.getLink(false, new DirectEventServiceParameter(c, serviceParameters)));

        verifyControls();
    }

    public void testServiceSimple() throws Exception
    {
        Object[] parameters = new Object[0];
        IRequestCycle cycle = newCycle();
        IPage page = newPage();
        IDirectEvent d = newDirect();
        LinkFactory lf = newLinkFactory();
        ResponseRenderer rr = newResponseRenderer();

        trainGetParameter(cycle, ServiceConstants.COMPONENT, "fred.barney");
        trainGetParameter(cycle, ServiceConstants.CONTAINER, null);
        trainGetParameter(cycle, ServiceConstants.PAGE, "ActivePage");
        trainGetParameter(cycle, ServiceConstants.SESSION, null);

        trainGetPage(cycle, "ActivePage", page);
        cycle.activate(page);

        trainGetNestedComponent(page, "fred.barney", d);

        trainExtractListenerParameters(lf, cycle, parameters);

        trainExtractBrowserEvent(cycle);
        
        cycle.setListenerParameters(parameters);
        
        ArgumentMatcher ignore = new IgnoreMatcher();
        getControl(cycle).setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        {ignore }));
        
        d.triggerEvent(cycle, new BrowserEvent(null, null));
        
        getControl(d).setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        { null, ignore }));
        
        rr.renderResponse(cycle);
        
        replayControls();

        DirectEventService ds = new DirectEventService();
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
        Object[] parameters = new Object[0];
        IRequestCycle cycle = newCycle();
        IPage page = newPage();
        IPage componentPage = newPage();
        IDirectEvent d = newDirect();
        LinkFactory lf = newLinkFactory();
        ResponseRenderer rr = newResponseRenderer();

        trainGetParameter(cycle, ServiceConstants.COMPONENT, "fred.barney");
        trainGetParameter(cycle, ServiceConstants.CONTAINER, "ComponentPage");
        trainGetParameter(cycle, ServiceConstants.PAGE, "ActivePage");
        trainGetParameter(cycle, ServiceConstants.SESSION, null);

        trainGetPage(cycle, "ActivePage", page);

        cycle.activate(page);

        trainGetPage(cycle, "ComponentPage", componentPage);

        trainGetNestedComponent(componentPage, "fred.barney", d);

        trainExtractListenerParameters(lf, cycle, parameters);

        trainExtractBrowserEvent(cycle);
        
        cycle.setListenerParameters(parameters);
        
        ArgumentMatcher ignore = new IgnoreMatcher();
        getControl(cycle).setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        {ignore }));
        
        d.triggerEvent(cycle, new BrowserEvent(null, null));
        
        getControl(d).setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        { null, ignore }));

        rr.renderResponse(cycle);

        replayControls();

        DirectEventService ds = new DirectEventService();
        ds.setLinkFactory(lf);
        ds.setResponseRenderer(rr);

        ds.service(cycle);

        verifyControls();
    }

    public void testServiceNotDirect() throws Exception
    {
        IRequestCycle cycle = newCycle();
        IPage page = newPage();
        IComponent c = newComponent();
        Location l = newLocation();

        trainGetParameter(cycle, ServiceConstants.COMPONENT, "fred.barney");
        trainGetParameter(cycle, ServiceConstants.CONTAINER, null);
        trainGetParameter(cycle, ServiceConstants.PAGE, "ActivePage");
        trainGetParameter(cycle, ServiceConstants.SESSION, null);

        trainGetPage(cycle, "ActivePage", page);
        cycle.activate(page);

        trainGetNestedComponent(page, "fred.barney", c);

        trainGetExtendedId(c, "ActivePage/fred.barney");
        trainGetLocation(c, l);

        replayControls();

        DirectEventService ds = new DirectEventService();

        try
        {
            ds.service(cycle);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Component ActivePage/fred.barney does not implement the org.apache.tapestry.IDirectEvent interface.",
                    ex.getMessage());
            assertSame(c, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verifyControls();
    }

    public void testSessionActiveAndRequired() throws Exception
    {
        Object[] parameters = new Object[0];
        IRequestCycle cycle = newCycle();
        IPage page = newPage();
        IDirectEvent d = newDirect();
        WebSession session = newWebSession(false);
        WebRequest request = newWebRequest(session);
        LinkFactory lf = newLinkFactory();
        ResponseRenderer rr = newResponseRenderer();

        trainGetParameter(cycle, ServiceConstants.COMPONENT, "fred.barney");
        trainGetParameter(cycle, ServiceConstants.CONTAINER, null);
        trainGetParameter(cycle, ServiceConstants.PAGE, "ActivePage");
        trainGetParameter(cycle, ServiceConstants.SESSION, "T");

        trainGetPage(cycle, "ActivePage", page);
        cycle.activate(page);

        trainGetNestedComponent(page, "fred.barney", d);

        trainIsStateful(d, true);

        trainExtractListenerParameters(lf, cycle, parameters);

        trainExtractBrowserEvent(cycle);
        
        cycle.setListenerParameters(parameters);
        
        ArgumentMatcher ignore = new IgnoreMatcher();
        getControl(cycle).setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        {ignore }));
        
        d.triggerEvent(cycle, new BrowserEvent(null, null));
        
        getControl(d).setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        { null, ignore }));

        rr.renderResponse(cycle);

        replayControls();

        DirectEventService ds = new DirectEventService();
        ds.setLinkFactory(lf);
        ds.setResponseRenderer(rr);
        ds.setRequest(request);

        ds.service(cycle);

        verifyControls();
    }
    
    public void testStaleSession() throws Exception
    {
        IRequestCycle cycle = newCycle();
        IPage page = newPage();
        IDirectEvent d = newDirect();
        WebRequest request = newWebRequest(null);
        Location l = newLocation();

        trainGetParameter(cycle, ServiceConstants.COMPONENT, "fred.barney");
        trainGetParameter(cycle, ServiceConstants.CONTAINER, null);
        trainGetParameter(cycle, ServiceConstants.PAGE, "ActivePage");
        trainGetParameter(cycle, ServiceConstants.SESSION, "T");

        trainGetPage(cycle, "ActivePage", page);
        cycle.activate(page);

        trainGetNestedComponent(page, "fred.barney", d);

        trainIsStateful(d, true);

        trainGetExtendedId(d, "ActivePage/fred.barney");
        trainGetLocation(page, l);
        trainGetPageName(page, "ActivePage");

        replayControls();

        DirectEventService ds = new DirectEventService();
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
    
    public void testNoBrowserEvent() throws Exception
    {
        Object[] parameters = new Object[0];
        IRequestCycle cycle = newCycle();
        IPage page = newPage();
        IDirectEvent d = newDirect();
        LinkFactory lf = newLinkFactory();
        ResponseRenderer rr = newResponseRenderer();

        trainGetParameter(cycle, ServiceConstants.COMPONENT, "fred.barney");
        trainGetParameter(cycle, ServiceConstants.CONTAINER, null);
        trainGetParameter(cycle, ServiceConstants.PAGE, "ActivePage");
        trainGetParameter(cycle, ServiceConstants.SESSION, null);

        trainGetPage(cycle, "ActivePage", page);
        cycle.activate(page);

        trainGetNestedComponent(page, "fred.barney", d);

        trainExtractListenerParameters(lf, cycle, parameters);

        cycle.getParameter(BrowserEvent.NAME);
        setReturnValue(cycle, null);
        
        replayControls();
        
        DirectEventService ds = new DirectEventService();
        ds.setLinkFactory(lf);
        ds.setResponseRenderer(rr);
        
        try {
            ds.service(cycle);
            unreachable();
        } catch (ApplicationRuntimeException e) {
            assertExceptionSubstring(e, "no browser event was found");
        }
        
        verifyControls();
    }
    
    protected void trainIsStateful(IDirectEvent direct, boolean isStateful)
    {
        direct.isStateful();
        setReturnValue(direct, isStateful);
    }
    
    protected void trainExtractBrowserEvent(IRequestCycle cycle)
    {
        cycle.getParameter(BrowserEvent.NAME);
        setReturnValue(cycle, "onClick");
        
        cycle.getParameter(BrowserEvent.NAME);
        setReturnValue(cycle, "onClick");
        
        cycle.getParameter(BrowserEvent.TYPE);
        setReturnValue(cycle, "click");
        cycle.getParameters(BrowserEvent.KEYS);
        setReturnValue(cycle, null);
        cycle.getParameter(BrowserEvent.CHAR_CODE);
        setReturnValue(cycle, null);
        cycle.getParameter(BrowserEvent.PAGE_X);
        setReturnValue(cycle, "123");
        cycle.getParameter(BrowserEvent.PAGE_Y);
        setReturnValue(cycle, "1243");
        cycle.getParameter(BrowserEvent.LAYER_X);
        setReturnValue(cycle, null);
        cycle.getParameter(BrowserEvent.LAYER_Y);
        setReturnValue(cycle, null);
        
        cycle.getParameter(BrowserEvent.TARGET + "." + BrowserEvent.TARGET_ATTR_ID);
        setReturnValue(cycle, "element1");
    }
}