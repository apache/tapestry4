// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.portlet;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.request.ResponseOutputStream;
import org.apache.tapestry.services.ServiceConstants;

/**
 * Responsible for rendering out a page; a Portlet render URL is built during action processing that
 * stores the active page; this is the page that will be rendered.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 * @see org.apache.tapestry.services.impl.ResponseRendererImpl
 */
public class RenderService implements IEngineService
{

    public ILink getLink(IRequestCycle cycle, Object parameter)
    {
        throw new UnsupportedOperationException(PortletMessages.unsupportedMethod("getLink"));
    }

    public void service(IRequestCycle cycle, ResponseOutputStream output) throws ServletException,
            IOException
    {
        String pageName = cycle.getParameter(ServiceConstants.PAGE);

        IPage page = cycle.getPage(pageName);

        IMarkupWriter writer = page.getResponseWriter(output);

        // TODO: Ensure that content type matches?
        // output.setContentType(writer.getContentType());

        boolean discard = true;

        try
        {
            cycle.renderPage(writer);

            discard = false;
        }
        finally
        {
            // Closing the writer closes its PrintWriter and a whole stack of
            // java.io objects,
            // which tend to stream a lot of output that eventually hits the
            // ResponseOutputStream. If we are discarding output anyway (due to
            // an exception
            // getting thrown during the render), we can save ourselves some
            // trouble
            // by ignoring it.

            if (discard)
                output.setDiscard(true);

            writer.close();

            if (discard)
                output.setDiscard(false);
        }

        // TODO: Trap errors and do some error reporting here?
    }

    public String getName()
    {
        return PortletConstants.RENDER_SERVICE;
    }

}