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

import java.util.Map;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.services.impl.LinkFactoryImpl;
import org.apache.tapestry.util.QueryParameterMap;

/**
 * Extended version of {@link org.apache.tapestry.services.impl.LinkFactoryImpl}&nbsp;that can
 * create generate Portlet URLs.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PortletLinkFactoryImpl extends LinkFactoryImpl
{
    private RenderResponse _renderResponse;

    private IRequestCycle _requestCycle;

    public void setRenderResponse(RenderResponse renderResponse)
    {
        _renderResponse = renderResponse;
    }

    public ILink constructLink(IEngineService service, boolean post, Map parameters,
            boolean stateful)
    {
        finalizeParameters(service, parameters);

        PortletURL url = _renderResponse.createActionURL();

        return new PortletLink(_requestCycle, url, new QueryParameterMap(parameters), stateful);
    }

    public void setRequestCycle(IRequestCycle requestCycle)
    {
        _requestCycle = requestCycle;
    }
}
