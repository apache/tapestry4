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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.StaleLinkException;
import org.apache.tapestry.error.RequestExceptionReporter;
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

    private RequestExceptionReporter _requestExceptionReporter;

    public void presentStaleLinkException(IRequestCycle cycle, StaleLinkException cause)
    {
        try
        {
            ActionRequest request = _globals.getActionRequest();

            request.getPortletSession(true).setAttribute(
                    PortletConstants.PORTLET_EXCEPTION_MARKUP_ATTRIBUTE,
                    cause.getMessage());

            ActionResponse response = _globals.getActionResponse();

            response.setRenderParameter(
                    ServiceConstants.SERVICE,
                    PortletConstants.EXCEPTION_SERVICE);
        }
        catch (Exception ex)
        {
            // Worst case scenario. The exception page itself is broken, leaving
            // us with no option but to write the cause to the output.

            // Also, write the exception thrown when redendering the exception
            // page, so that can get fixed as well.

            _requestExceptionReporter.reportRequestException(PortletMessages
                    .errorReportingException(ex), ex);

            // And throw the exception.

            throw new ApplicationRuntimeException(ex.getMessage(), ex);
        }
    }

    public void setGlobals(PortletRequestGlobals globals)
    {
        _globals = globals;
    }

    public void setRequestExceptionReporter(RequestExceptionReporter requestExceptionReporter)
    {
        _requestExceptionReporter = requestExceptionReporter;
    }

}
