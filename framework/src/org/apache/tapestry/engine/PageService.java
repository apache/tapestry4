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

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.request.ResponseOutputStream;

/**
 *  Basic server for creating a link to another page in the application.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.9
 *
 **/

public class PageService extends AbstractService
{

    public ILink getLink(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        if (Tapestry.size(parameters) != 1)
            throw new IllegalArgumentException(
                Tapestry.format("service-single-parameter", Tapestry.PAGE_SERVICE));

        return constructLink(cycle, Tapestry.PAGE_SERVICE, new String[] {(String) parameters[0]}, null, true);

    }

    public void service(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws ServletException, IOException
    {
        RequestContext context = cycle.getRequestContext();
        String[] serviceContext = getServiceContext(context);

        if (Tapestry.size(serviceContext) != 1)
            throw new ApplicationRuntimeException(
                Tapestry.format("service-single-parameter", Tapestry.PAGE_SERVICE));

        String pageName = serviceContext[0];

        // At one time, the page service required a session, but that is no longer necessary.
        // Users can now bookmark pages within a Tapestry application.  Pages
        // can implement validate() and throw a PageRedirectException if they don't
        // want to be accessed this way.  For example, most applications have a concept
        // of a "login" and have a few pages that don't require the user to be logged in,
        // and other pages that do.  The protected pages should redirect to a login page.

        IPage page = cycle.getPage(pageName);

        cycle.activate(page);

        engine.renderResponse(cycle, output);
    }

    public String getName()
    {
        return Tapestry.PAGE_SERVICE;
    }

}