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

package org.apache.tapestry.vlib.services;

import java.io.IOException;

import org.apache.tapestry.services.WebRequestServicer;
import org.apache.tapestry.services.WebRequestServicerFilter;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;
import org.apache.tapestry.web.WebSession;

/**
 * Filter, injected into the tapestry.request.WebRequestServicerPipeline configuration, that
 * optionally discards the session at the end of the request (after a logout, typically).
 * 
 * @author Howard M. Lewis Ship
 * @see org.apache.tapestry.vlib.services.ApplicationLifecycle#getDiscardSession()
 * @since 4.0
 */
public class DiscardSessionFilter implements WebRequestServicerFilter
{
    private ApplicationLifecycle _applicationLifecycle;

    public void service(WebRequest request, WebResponse response, WebRequestServicer servicer)
            throws IOException
    {
        try
        {
            servicer.service(request, response);
        }
        finally
        {
            if (_applicationLifecycle.getDiscardSession())
                discardSession(request);
        }
    }

    private void discardSession(WebRequest request)
    {
        WebSession session = request.getSession(false);

        if (session != null)
            try
            {
                session.invalidate();
            }
            catch (IllegalStateException ex)
            {
                // Ignore.
            }
    }

    public void setApplicationLifecycle(ApplicationLifecycle applicationLifecycle)
    {
        _applicationLifecycle = applicationLifecycle;
    }
}
