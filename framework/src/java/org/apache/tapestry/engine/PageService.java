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

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.ResponseOutputStream;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.services.ServiceConstants;

/**
 * Basic server for creating a link to another page in the application.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.9
 */

public class PageService implements IEngineService
{
    /** @since 3.1 */
    private ResponseRenderer _responseRenderer;

    /** @since 3.1 */
    private LinkFactory _linkFactory;

    public ILink getLink(IRequestCycle cycle, Object parameter)
    {
        Defense.isAssignable(parameter, String.class, "parameter");

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.SERVICE, Tapestry.PAGE_SERVICE);
        parameters.put(ServiceConstants.PAGE, parameter);

        return _linkFactory.constructLink(cycle, parameters, true);

    }

    public void service(IRequestCycle cycle, ResponseOutputStream output) throws IOException
    {
        String pageName = cycle.getParameter(ServiceConstants.PAGE);

        // At one time, the page service required a session, but that is no longer necessary.
        // Users can now bookmark pages within a Tapestry application. Pages
        // can implement validate() and throw a PageRedirectException if they don't
        // want to be accessed this way. For example, most applications have a concept
        // of a "login" and have a few pages that don't require the user to be logged in,
        // and other pages that do. The protected pages should redirect to a login page.

        cycle.activate(pageName);

        _responseRenderer.renderResponse(cycle, output);
    }

    public String getName()
    {
        return Tapestry.PAGE_SERVICE;
    }

    /** @since 3.1 */
    public void setResponseRenderer(ResponseRenderer responseRenderer)
    {
        _responseRenderer = responseRenderer;
    }

    /** @since 3.1 */
    public void setLinkFactory(LinkFactory linkFactory)
    {
        _linkFactory = linkFactory;
    }
}