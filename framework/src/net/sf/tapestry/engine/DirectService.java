/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.engine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IDirect;
import net.sf.tapestry.IEngineServiceView;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ResponseOutputStream;
import net.sf.tapestry.StaleSessionException;
import net.sf.tapestry.Tapestry;

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
     *  @since 2.4
     **/

    private static final String STATEFUL_ON = "1";

    /**
     *  Encoded into URL if engine was not stateful.
     * 
     *  @since 2.4
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

    public boolean service(IEngineServiceView engine, IRequestCycle cycle, ResponseOutputStream output)
        throws RequestCycleException, ServletException, IOException
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
            throw new ApplicationRuntimeException(Tapestry.getString("DirectService.context-parameters"));

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

        // Allow the page to validate that the user is allowed to visit.  This is simple
        // protection from malicious users who hack the URLs directly, or make inappropriate
        // use of the back button. 
        // Using Block/InsertBlock, it is possible that the component is on a different page
        // than the render page.  The render page is validated, not necessaily the component
        // page.

        page.validate(cycle);
        cycle.setPage(page);

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
            throw new RequestCycleException(
                Tapestry.getString("DirectService.component-wrong-type", component.getExtendedId()),
                component,
                ex);
        }

        // Check for a StateSession only the session was stateful when
        // the Gesture was created.

        if (stateful.equals(STATEFUL_ON) && direct.isStateful())
        {
            HttpSession session = cycle.getRequestContext().getSession();

            if (session == null || session.isNew())
                throw new StaleSessionException(
                    Tapestry.getString("DirectService.stale-session-exception", direct.getExtendedId()),
                    direct.getPage());
        }

        Object[] parameters = getParameters(cycle);

        cycle.setServiceParameters(parameters);
        direct.trigger(cycle);

        // Render the response.  This will be the response page (the first element in the context)
        // unless the direct (or its delegate) changes it.

        engine.renderResponse(cycle, output);

        return true;
    }

    public String getName()
    {
        return Tapestry.DIRECT_SERVICE;
    }
}