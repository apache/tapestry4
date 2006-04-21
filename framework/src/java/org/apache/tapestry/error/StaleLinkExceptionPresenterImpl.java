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

import java.io.IOException;

import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.StaleLinkException;
import org.apache.tapestry.services.ResponseRenderer;

/**
 * Implementation of
 * {@link org.apache.tapestry.error.StaleLinkExceptionPresenter} that uses a
 * page to present the exception. The page must implement a property named
 * "message" of type String and should present that message to the user.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class StaleLinkExceptionPresenterImpl implements
        StaleLinkExceptionPresenter
{

    private ResponseRenderer _responseRenderer;

    private String _pageName;

    public void presentStaleLinkException(IRequestCycle cycle,
            StaleLinkException cause)
        throws IOException
    {
        IPage exceptionPage = cycle.getPage(_pageName);

        PropertyUtils.write(exceptionPage, "message", cause.getMessage());

        cycle.activate(exceptionPage);

        _responseRenderer.renderResponse(cycle);
    }

    public void setPageName(String pageName)
    {
        _pageName = pageName;
    }

    public void setResponseRenderer(ResponseRenderer responseRenderer)
    {
        _responseRenderer = responseRenderer;
    }

}
