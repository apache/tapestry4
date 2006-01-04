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

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.StaleSessionException;
import org.apache.tapestry.services.ResponseRenderer;

/**
 * Used to activate a particular page to report the
 * {@link org.apache.tapestry.StaleSessionException}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class StaleSessionExceptionPresenterImpl implements StaleSessionExceptionPresenter
{
    private ResponseRenderer _responseRenderer;

    private String _pageName;

    public void presentStaleSessionException(IRequestCycle cycle, StaleSessionException cause)
            throws IOException
    {
        IPage exceptionPage = cycle.getPage(_pageName);

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
