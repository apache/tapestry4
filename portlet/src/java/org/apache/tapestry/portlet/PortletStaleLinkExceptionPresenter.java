// Copyright 2005, 2006 The Apache Software Foundation
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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.StaleLinkException;
import org.apache.tapestry.error.StaleLinkExceptionPresenter;
import org.apache.tapestry.services.ServiceConstants;

/**
 * Implementation of {@link org.apache.tapestry.error.StaleLinkExceptionPresenter} for Portlets.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PortletStaleLinkExceptionPresenter implements StaleLinkExceptionPresenter
{
    private PortletRequestGlobals _globals;

    public void presentStaleLinkException(IRequestCycle cycle, StaleLinkException cause)
            throws IOException
    {
        ActionRequest request = _globals.getActionRequest();

        request.getPortletSession(true).setAttribute(
                PortletConstants.PORTLET_EXCEPTION_MARKUP_ATTRIBUTE,
                cause.getMessage());

        ActionResponse response = _globals.getActionResponse();

        response.setRenderParameter(ServiceConstants.SERVICE, PortletConstants.EXCEPTION_SERVICE);
    }

    public void setGlobals(PortletRequestGlobals globals)
    {
        _globals = globals;
    }
}
