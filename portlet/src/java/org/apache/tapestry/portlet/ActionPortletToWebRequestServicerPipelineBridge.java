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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import org.apache.tapestry.services.WebRequestServicer;

/**
 * Bridges from the <code>tapestry.portlet.ActionRequestServicerPipeline</code> to the standard
 * <code>tapestry.request.WebRequestServicerPipeline</code>.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ActionPortletToWebRequestServicerPipelineBridge implements ActionRequestServicer
{
    private PortletRequestGlobals _portletRequestGlobals;

    private WebRequestServicer _webRequestServicer;

    public void service(ActionRequest request, ActionResponse response) throws IOException,
            PortletException
    {
        _portletRequestGlobals.store(request, response);

        PortletWebRequest webRequest = new PortletWebRequest(request);
        PortletWebResponse webResponse = new PortletWebResponse(response);

        try
        {
            _webRequestServicer.service(webRequest, webResponse);
        }
        catch (RuntimeException ex)
        {
            throw new PortletException(ex);
        }
    }

    public void setPortletRequestGlobals(PortletRequestGlobals portletRequestGlobals)
    {
        _portletRequestGlobals = portletRequestGlobals;
    }

    public void setWebRequestServicer(WebRequestServicer webRequestServicer)
    {
        _webRequestServicer = webRequestServicer;
    }
}