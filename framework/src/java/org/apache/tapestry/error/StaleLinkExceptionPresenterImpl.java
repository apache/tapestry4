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

package org.apache.tapestry.error;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.StaleLinkException;
import org.apache.tapestry.services.ResponseRenderer;

/**
 * Implementation of {@link org.apache.tapestry.error.StaleLinkExceptionPresenter} that uses a page
 * to present the exception. The page must implement a property named "message" of type String and
 * should present that message to the user.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class StaleLinkExceptionPresenterImpl implements StaleLinkExceptionPresenter
{
    private RequestExceptionReporter _requestExceptionReporter;

    private ResponseRenderer _responseRenderer;

    private String _pageName;

    public void presentStaleLinkException(IRequestCycle cycle, StaleLinkException cause)
    {
        try
        {
            IPage exceptionPage = cycle.getPage(_pageName);

            exceptionPage.setProperty("message", cause.getMessage());

            cycle.activate(exceptionPage);

            _responseRenderer.renderResponse(cycle);
        }
        catch (Throwable ex)
        {
            // Worst case scenario. The exception page itself is broken, leaving
            // us with no option but to write the cause to the output.

            _requestExceptionReporter.reportRequestException(ErrorMessages
                    .unableToProcessClientRequest(cause), cause);

            // Also, write the exception thrown when redendering the exception
            // page, so that can get fixed as well.

            _requestExceptionReporter.reportRequestException(ErrorMessages
                    .unableToPresentExceptionPage(ex), ex);

            // And throw the exception.

            throw new ApplicationRuntimeException(ex.getMessage(), ex);
        }

    }

    public void setPageName(String pageName)
    {
        _pageName = pageName;
    }

    public void setRequestExceptionReporter(RequestExceptionReporter requestExceptionReporter)
    {
        _requestExceptionReporter = requestExceptionReporter;
    }

    public void setResponseRenderer(ResponseRenderer responseRenderer)
    {
        _responseRenderer = responseRenderer;
    }

}
