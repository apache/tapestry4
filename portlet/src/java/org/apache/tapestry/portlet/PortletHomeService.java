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
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.EngineMessages;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ResponseRenderer;
import org.apache.tapestry.services.ServiceConstants;

/**
 * Replacement for the standard home service, used by Portlets. This exists to handle the special
 * case where a Portlet render request arrives when there is not a Portlet action request prior ...
 * this can happen when a Portlet is first added to a Portal page.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PortletHomeService implements IEngineService
{
    private PortletRenderer _portletRenderer;

    private PortletRequestGlobals _requestGlobals;

    private ResponseRenderer _responseRenderer;

    private LinkFactory _linkFactory;

    private PortletPageResolver _pageResolver;

    public String getName()
    {
        return Tapestry.HOME_SERVICE;
    }

    public ILink getLink(IRequestCycle cycle, boolean post, Object parameter)
    {
        if (parameter != null)
            throw new IllegalArgumentException(EngineMessages.serviceNoParameter(this));

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.SERVICE, Tapestry.HOME_SERVICE);

        return _linkFactory.constructLink(cycle, post, parameters, true);
    }

    public void setLinkFactory(LinkFactory linkFactory)
    {
        _linkFactory = linkFactory;
    }

    public void setResponseRenderer(ResponseRenderer responseRenderer)
    {
        _responseRenderer = responseRenderer;
    }

    public void service(IRequestCycle cycle) throws IOException
    {
        String pageName = _pageResolver.getPageNameForRequest(cycle);

        if (_requestGlobals.isRenderRequest())
        {
            _portletRenderer.renderPage(cycle, pageName);
            return;
        }

        cycle.activate(pageName);

        _responseRenderer.renderResponse(cycle);
    }

    public void setPortletRenderer(PortletRenderer portletRenderer)
    {
        _portletRenderer = portletRenderer;
    }

    public void setRequestGlobals(PortletRequestGlobals requestGlobals)
    {
        _requestGlobals = requestGlobals;
    }

    public void setPageResolver(PortletPageResolver pageResolver)
    {
        _pageResolver = pageResolver;
    }
}