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
import org.apache.tapestry.IAction;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.StaleSessionException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.ResponseOutputStream;

/**
 *  A context-sensitive service related to {@link org.apache.tapestry.form.Form} 
 *  and {@link org.apache.tapestry.link.ActionLink}.  Encodes
 *  the page, component and an action id in the service context.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.9
 *
 **/

public class ActionService extends AbstractService
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
        if (parameters == null || parameters.length != 1)
            throw new IllegalArgumentException(
                Tapestry.format("service-single-parameter", Tapestry.ACTION_SERVICE));

        String stateful = cycle.getEngine().isStateful() ? STATEFUL_ON : STATEFUL_OFF;
        IPage componentPage = component.getPage();
        IPage responsePage = cycle.getPage();

        boolean complex = (componentPage != responsePage);

        String[] serviceContext = new String[complex ? 5 : 4];

        int i = 0;

        serviceContext[i++] = stateful;
        serviceContext[i++] = responsePage.getPageName();
        serviceContext[i++] = (String) parameters[0];

        // Because of Block/InsertBlock, the component may not be on
        // the same page as the response page and we need to make
        // allowances for this.

        if (complex)
            serviceContext[i++] = componentPage.getPageName();

        serviceContext[i++] = component.getIdPath();

        return constructLink(cycle, Tapestry.ACTION_SERVICE, serviceContext, null, true);
    }

    public void service(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws ServletException, IOException
    {
        IAction action = null;
        String componentPageName;
        int count = 0;

        String[] serviceContext = getServiceContext(cycle.getRequestContext());

        if (serviceContext != null)
            count = serviceContext.length;

        if (count != 4 && count != 5)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("ActionService.context-parameters"));

        boolean complex = count == 5;

        int i = 0;
        String stateful = serviceContext[i++];
        String pageName = serviceContext[i++];
        String targetActionId = serviceContext[i++];

        if (complex)
            componentPageName = serviceContext[i++];
        else
            componentPageName = pageName;

        String targetIdPath = serviceContext[i++];

        IPage page = cycle.getPage(pageName);

		// Setup the page for the rewind, then do the rewind.

		cycle.activate(page);
		
        IPage componentPage = cycle.getPage(componentPageName);
        IComponent component = componentPage.getNestedComponent(targetIdPath);

        try
        {
            action = (IAction) component;
        }
        catch (ClassCastException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("ActionService.component-wrong-type", component.getExtendedId()),
                component,
                null,
                ex);
        }

        // Only perform the stateful check if the application was stateful
        // when the URL was rendered.

        if (stateful.equals(STATEFUL_ON) && action.getRequiresSession())
        {
            HttpSession session = cycle.getRequestContext().getSession();

            if (session == null || session.isNew())
                throw new StaleSessionException();
        }

        cycle.rewindPage(targetActionId, action);

        // During the rewind, a component may change the page.  This will take
        // effect during the second render, which renders the HTML response.

        // Render the response.

        engine.renderResponse(cycle, output);
    }

    public String getName()
    {
        return Tapestry.ACTION_SERVICE;
    }

}