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
 * Stores the current Portlet request and response, for access by other services.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface PortletRequestGlobals
{
    public void store(ActionRequest request, ActionResponse response);

    public void store(RenderRequest request, RenderResponse response);

    public ActionRequest getActionRequest();

    public ActionResponse getActionResponse();

    public RenderRequest getRenderRequest();

    public RenderResponse getRenderResponse();

    /**
     * Returns true if {@link #store(RenderRequest, RenderResponse)}has been invoked, false
     * otherwise.
     */
    public boolean isRenderRequest();
}