//  Copyright 2004 The Apache Software Foundation
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.StaleSessionException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.request.ResponseOutputStream;

/**
 *  Implementation of the direct service, which encodes the page and component id in
 *  the service context, and passes application-defined parameters as well.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.9
 *
 **/

public class DirectService extends AbstractService
{
    /**
     *  Encoded into URL if engine was stateful.
     * 
     *  @since 3.0
     **/

    private static final String STATEFUL_ON = "1";

    /**
     *  Encoded into URL if engine was not stateful.
     * 
     *  @since 3.0
     **/

    private static final String STATEFUL_OFF = "0";

    public ILink getLink(IRequestCycle cycle, IComponent component, Object[] parameters)
    {

        // New since 1.0.1, we use the component to determine
        // the page, not the cycle.  Through the use of tricky
        // things such as Block/InsertBlock, it is possible 
        // that a component from a page different than
        // the response page will render.
        // In 1.0.6, we start to record *both* the render page
        // and the component page (if different), as the extended
        // context.

        IPage renderPage = cycle.getPage();
        IPage componentPage = component.getPage();

        boolean complex = renderPage != componentPage;

        String[] context = complex ? new String[4] : new String[3];

        int i = 0;

        String stateful = cycle.getEngine().isStateful() ? STATEFUL_ON : STATEFUL_OFF;

        context[i++] = stateful;

        if (complex)
            context[i++] = renderPage.getPageName();

        context[i++] = componentPage.getPageName();
        context[i++] = component.getIdPath();

        return constructLink(cycle, Tapestry.DIRECT_SERVICE, context, parameters, true);
    }

    public void service(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws ServletException, IOException
    {
        IDirect direct;
        int count = 0;
        String componentPageName;
        IPage componentPage;
        RequestContext requestContext = cycle.getRequestContext();
        String[] serviceContext = getServiceContext(requestContext);

        if (serviceContext != null)
            count = serviceContext.length;

        if (count != 3 && count != 4)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("DirectService.context-parameters"));

        boolean complex = count == 4;

        int i = 0;
        String stateful = serviceContext[i++];
        String pageName = serviceContext[i++];

        if (complex)
            componentPageName = serviceContext[i++];
        else
            componentPageName = pageName;

        String componentPath = serviceContext[i++];

        IPage page = cycle.getPage(pageName);

        cycle.activate(page);

        if (complex)
            componentPage = cycle.getPage(componentPageName);
        else
            componentPage = page;

        IComponent component = componentPage.getNestedComponent(componentPath);

        try
        {
            direct = (IDirect) component;
        }
        catch (ClassCastException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("DirectService.component-wrong-type", component.getExtendedId()),
                component,
                null,
                ex);
        }

        // Check for a StateSession only the session was stateful when
        // the Gesture was created.

        if (stateful.equals(STATEFUL_ON) && direct.isStateful())
        {
            HttpSession session = cycle.getRequestContext().getSession();

            if (session == null || session.isNew())
                throw new StaleSessionException(
                    Tapestry.format(
                        "DirectService.stale-session-exception",
                        direct.getExtendedId()),
                    direct.getPage());
        }

        Object[] parameters = getParameters(cycle);

        cycle.setServiceParameters(parameters);
        direct.trigger(cycle);

        // Render the response.  This will be the response page (the first element in the context)
        // unless the direct (or its delegate) changes it.

        engine.renderResponse(cycle, output);
    }

    public String getName()
    {
        return Tapestry.DIRECT_SERVICE;
    }
}