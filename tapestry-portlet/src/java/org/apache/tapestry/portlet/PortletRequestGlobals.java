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
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Stores the current Portlet request and response, for access by other
 * services.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface PortletRequestGlobals
{

    void store(ActionRequest request, ActionResponse response);

    void store(RenderRequest request, RenderResponse response);

    ActionRequest getActionRequest();

    ActionResponse getActionResponse();

    RenderRequest getRenderRequest();

    RenderResponse getRenderResponse();

    /**
     * Returns true if {@link #store(RenderRequest, RenderResponse)}has been
     * invoked, false otherwise.
     */
    boolean isRenderRequest();

    /**
     * Returns whatever is available; the ActionRequest or the PortletRequest.
     */
    PortletRequest getPortletRequest();
}
