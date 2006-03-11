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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
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

/**
 * Implementation of the direct service, which encodes the page and component id in the service
 * context, and passes application-defined parameters as well.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.9
 */

public class DirectService implements IEngineService
{
    /** @since 4.0 */
    protected ResponseRenderer _responseRenderer;

    /** @since 4.0 */
    protected LinkFactory _linkFactory;

    /** @since 4.0 */
    protected WebRequest _request;

    /** @since 4.0 */
    private IRequestCycle _requestCycle;

    public ILink getLink(boolean post, Object parameter)
    {
        Defense.isAssignable(parameter, DirectServiceParameter.class, "parameter");

        DirectServiceParameter dsp = (DirectServiceParameter) parameter;

        IComponent component = dsp.getDirect();

        // New since 1.0.1, we use the component to determine
        // the page, not the cycle. Through the use of tricky
        // things such as Block/InsertBlock, it is possible
        // that a component from a page different than
        // the response page will render.
        // In 1.0.6, we start to record *both* the render page
        // and the component page (if different).

        IPage activePage = _requestCycle.getPage();
        IPage componentPage = component.getPage();

        Map parameters = new HashMap();

        boolean stateful = _request.getSession(false) != null;

        parameters.put(ServiceConstants.PAGE, activePage.getPageName());
        parameters.put(ServiceConstants.COMPONENT, component.getIdPath());
        parameters.put(ServiceConstants.CONTAINER, componentPage == activePage ? null
                : componentPage.getPageName());
        parameters.put(ServiceConstants.SESSION, stateful ? "T" : null);
        parameters.put(ServiceConstants.PARAMETER, dsp.getServiceParameters());

        return _linkFactory.constructLink(this, post, parameters, true);
    }

    public void service(IRequestCycle cycle) throws IOException
    {
        String componentId = cycle.getParameter(ServiceConstants.COMPONENT);
        String componentPageName = cycle.getParameter(ServiceConstants.CONTAINER);
        String activePageName = cycle.getParameter(ServiceConstants.PAGE);
        boolean activeSession = cycle.getParameter(ServiceConstants.SESSION) != null;

        IPage page = cycle.getPage(activePageName);

        cycle.activate(page);

        IPage componentPage = componentPageName == null ? page : cycle.getPage(componentPageName);

        IComponent component = componentPage.getNestedComponent(componentId);

        IDirect direct = null;

        try
        {
            direct = (IDirect) component;
        }
        catch (ClassCastException ex)
        {
            throw new ApplicationRuntimeException(EngineMessages.wrongComponentType(
                    component,
                    IDirect.class), component, null, ex);
        }

        // Check for a StaleSession only when the session was stateful when
        // the link was created.

        if (activeSession && direct.isStateful())
        {
            WebSession session = _request.getSession(false);

            if (session == null || session.isNew())
                throw new StaleSessionException(EngineMessages.requestStateSession(direct),
                        componentPage);
        }

        Object[] parameters = _linkFactory.extractListenerParameters(cycle);

        triggerComponent(cycle, direct, parameters);

        // Render the response. This will be the active page
        // unless the direct component (or its delegate) changes it.

        _responseRenderer.renderResponse(cycle);
    }

    /** @since 4.0 */

    protected void triggerComponent(IRequestCycle cycle, IDirect direct, Object[] parameters)
    {
        cycle.setListenerParameters(parameters);

        direct.trigger(cycle);
    }

    public String getName()
    {
        return Tapestry.DIRECT_SERVICE;
    }

    /** @since 4.0 */
    public void setResponseRenderer(ResponseRenderer responseRenderer)
    {
        _responseRenderer = responseRenderer;
    }

    /** @since 4.0 */
    public void setLinkFactory(LinkFactory linkFactory)
    {
        _linkFactory = linkFactory;
    }

    /** @since 4.0 */
    public void setRequest(WebRequest request)
    {
        _request = request;
    }

    /** @since 4.0 */
    public void setRequestCycle(IRequestCycle requestCycle)
    {
        _requestCycle = requestCycle;
    }
}