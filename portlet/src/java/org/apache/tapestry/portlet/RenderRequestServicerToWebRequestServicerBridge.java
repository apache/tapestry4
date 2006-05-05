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

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.tapestry.services.WebRequestServicer;

/**
 * Bridges from the <code>tapestry.portlet.RenderRequestServicerPipeline</code>
 * to the standard <code>tapestry.request.WebRequestServicerPipeline</code>.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class RenderRequestServicerToWebRequestServicerBridge implements
        RenderRequestServicer
{

    private PortletRequestGlobals _portletRequestGlobals;

    private WebRequestServicer _webRequestServicer;

    public void service(RenderRequest request, RenderResponse response)
        throws IOException, PortletException
    {
        _portletRequestGlobals.store(request, response);

        PortletWebRequest webRequest = new PortletWebRequest(request);
        PortletWebResponse webResponse = new RenderWebResponse(response);

        try
        {
            _webRequestServicer.service(webRequest, webResponse);
        }
        catch (RuntimeException ex)
        {
            throw new PortletException(ex);
        }
    }

    public void setPortletRequestGlobals(
            PortletRequestGlobals portletRequestGlobals)
    {
        _portletRequestGlobals = portletRequestGlobals;
    }

    public void setWebRequestServicer(WebRequestServicer webRequestServicer)
    {
        _webRequestServicer = webRequestServicer;
    }

}
