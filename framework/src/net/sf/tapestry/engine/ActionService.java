//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.engine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.Gesture;
import net.sf.tapestry.IAction;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngineServiceView;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ResponseOutputStream;
import net.sf.tapestry.StaleSessionException;
import net.sf.tapestry.Tapestry;

/**
 *  A context-sensitive service related to {@link net.sf.tapestry.form.Form} 
 *  and {@link net.sf.tapestry.link.Action}.  Encodes
 *  the page, component and an action id in the service context.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.9
 *
 **/

public class ActionService extends AbstractService
{
    public Gesture buildGesture(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        if (parameters == null || parameters.length != 1)
            throw new IllegalArgumentException(Tapestry.getString("service-single-parameter", ACTION_SERVICE));

        IPage componentPage = component.getPage();
        IPage responsePage = cycle.getPage();
        int length = (componentPage == responsePage) ? 3 : 4;

        String[] serviceContext = new String[length];

        int i = 0;

        serviceContext[i++] = responsePage.getName();
        serviceContext[i++] = (String)parameters[0];

        // Because of Block/InsertBlock, the component may not be on
        // the same page as the response page and we need to make
        // allowances for this.

        if (componentPage != responsePage)
            serviceContext[i++] = componentPage.getName();

        serviceContext[i++] = component.getIdPath();

        return assembleGesture(cycle, ACTION_SERVICE, serviceContext, null, true);
    }

    public boolean service(IEngineServiceView engine, IRequestCycle cycle, ResponseOutputStream output)
        throws RequestCycleException, ServletException, IOException
    {
        IAction action = null;
        String componentPageName;
        int count = 0;

        String[] serviceContext = getServiceContext(cycle.getRequestContext());

        if (serviceContext != null)
            count = serviceContext.length;

        if (count != 3 && count != 4)
            throw new ApplicationRuntimeException(
                Tapestry.getString("AbstractEngine.action-context-parameters"));

        int i = 0;
        String pageName = serviceContext[i++];
        String targetActionId = serviceContext[i++];

        if (count == 3)
            componentPageName = pageName;
        else
            componentPageName = serviceContext[i++];

        String targetIdPath = serviceContext[i++];

        IPage page = cycle.getPage(pageName);

        IPage componentPage = cycle.getPage(componentPageName);
        IComponent component = componentPage.getNestedComponent(targetIdPath);

        try
        {
            action = (IAction) component;
        }
        catch (ClassCastException ex)
        {
            throw new RequestCycleException(
                Tapestry.getString("AbstractEngine.action-component-wrong-type", component.getExtendedId()),
                component,
                ex);
        }

        if (action.getRequiresSession())
        {
            HttpSession session = cycle.getRequestContext().getSession();

            if (session == null || session.isNew())
                throw new StaleSessionException();
        }

        // Allow the page to validate that the user is allowed to visit.  This is simple
        // protection from malicious users who hack the URLs directly, or make inappropriate
        // use of the back button. 

        // Note that we validate the page that rendered the response which (again, due to
        // Block/InsertBlock) is not necessarily the page that contains the component.

        page.validate(cycle);

        // Setup the page for the rewind, then do the rewind.

        cycle.setPage(page);
        cycle.rewindPage(targetActionId, action);

        // During the rewind, a component may change the page.  This will take
        // effect during the second render, which renders the HTML response.

        // Render the response.

        engine.renderResponse(cycle, output);

        return true;
    }

    public String getName()
    {
        return ACTION_SERVICE;
    }

}