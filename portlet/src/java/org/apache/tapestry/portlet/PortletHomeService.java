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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.HomeService;
import org.apache.tapestry.request.ResponseOutputStream;

/**
 * Replacement for the standard home service, used by Portlets. This exists to handle the special
 * case where a Portlet render request arrives when there is not a Portlet action request prior ...
 * this can happen when a Portlet is first added to a Portal page.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class PortletHomeService extends HomeService
{
    private PortletRenderer _portletRenderer;

    private PortletRequestGlobals _requestGlobals;

    public void service(IRequestCycle cycle, ResponseOutputStream output) throws IOException
    {
        if (_requestGlobals.isRenderRequest())
        {
            String pageName = getPageName();

            _portletRenderer.renderPage(cycle, pageName, output);

            return;
        }

        super.service(cycle, output);
    }

    public void setPortletRenderer(PortletRenderer portletRenderer)
    {
        _portletRenderer = portletRenderer;
    }

    public void setRequestGlobals(PortletRequestGlobals requestGlobals)
    {
        _requestGlobals = requestGlobals;
    }
}