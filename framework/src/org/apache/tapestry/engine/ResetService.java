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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.ResponseOutputStream;

/**
 *  ServiceLink used to discard all cached data (templates, specifications, et cetera).
 *  This is primarily used during development.  It could be a weakness of a Tapestry
 *  application, making it susceptible to denial of service attacks, which is why
 *  it is disabled by default.  The link generated by the ResetService redisplays the
 *  current page after discarding all data.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.9
 *  @see org.apache.tapestry.IEngine#isResetServiceEnabled()
 * 
 **/

public class ResetService extends AbstractService
{

    public ILink getLink(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        if (Tapestry.size(parameters) != 0)
            throw new IllegalArgumentException(
                Tapestry.format("service-no-parameters", Tapestry.RESET_SERVICE));

        String[] context = new String[1];
        context[0] = component.getPage().getPageName();

        return constructLink(cycle, Tapestry.RESET_SERVICE, context, null, true);
    }

    public String getName()
    {
        return Tapestry.RESET_SERVICE;
    }

    public void service(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws ServletException, IOException
    {
        String[] context = getServiceContext(cycle.getRequestContext());

        if (Tapestry.size(context) != 1)
            throw new ApplicationRuntimeException(
                Tapestry.format("service-single-parameter", Tapestry.RESET_SERVICE));

        String pageName = context[0];

        if (engine.isResetServiceEnabled())
            engine.clearCachedData();

        IPage page = cycle.getPage(pageName);

        cycle.activate(page);

        // Render the same page (that contained the reset link).

        engine.renderResponse(cycle, output);
    }

}