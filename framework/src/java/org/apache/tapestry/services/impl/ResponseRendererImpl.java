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

package org.apache.tapestry.services.impl;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.request.ResponseOutputStream;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ResponseRenderer;

/**
 * Responsible for rendering a response to the client.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ResponseRendererImpl implements ResponseRenderer
{
    private RequestLocaleManager _localeManager;

    public void renderResponse(IRequestCycle cycle, ResponseOutputStream output)
            throws ServletException, IOException
    {
        _localeManager.persistLocale();

        IPage page = cycle.getPage();

        IMarkupWriter writer = page.getResponseWriter(output);

        output.setContentType(writer.getContentType());

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
    }

    public void setLocaleManager(RequestLocaleManager localeManager)
    {
        _localeManager = localeManager;
    }
}