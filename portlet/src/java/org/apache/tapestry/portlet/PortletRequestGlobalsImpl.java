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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Implementation of the <code>tapestry.portlet.PortletRequestGlobals</code> service, which uses
 * the threaded service lifecycle model.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class PortletRequestGlobalsImpl implements PortletRequestGlobals
{
    private ActionRequest _actionRequest;

    private ActionResponse _actionResponse;

    private RenderResponse _renderResponse;

    private RenderRequest _renderRequest;

    public void store(ActionRequest request, ActionResponse response)
    {
        _actionRequest = request;
        _actionResponse = response;
    }

    public void store(RenderRequest request, RenderResponse response)
    {
        _renderRequest = request;
        _renderResponse = response;
    }

    public ActionRequest getActionRequest()
    {
        return _actionRequest;
    }

    public ActionResponse getActionResponse()
    {
        return _actionResponse;
    }

    public RenderRequest getRenderRequest()
    {
        return _renderRequest;
    }

    public RenderResponse getRenderResponse()
    {
        return _renderResponse;
    }
}