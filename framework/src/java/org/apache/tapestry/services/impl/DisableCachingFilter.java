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

package org.apache.tapestry.services.impl;

import java.io.IOException;

import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.HiveMind;
import org.apache.tapestry.services.ResetEventCoordinator;
import org.apache.tapestry.services.WebRequestServicer;
import org.apache.tapestry.services.WebRequestServicerFilter;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

/**
 * Filter whose job is to invoke
 * {@link org.apache.tapestry.services.ResetEventCoordinator#fireResetEvent()}after the request has
 * been processed. This filter is only contributed into the
 * tapestry.request.WebRequestServicerPipeline configuration if the
 * org.apache.tapestry.disable-caching system property is true.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class DisableCachingFilter implements WebRequestServicerFilter
{
    private ErrorLog _errorLog;

    private ResetEventCoordinator _resetEventCoordinator;

    public void service(WebRequest request, WebResponse response, WebRequestServicer servicer)
            throws IOException
    {
        try
        {
            servicer.service(request, response);
        }
        finally
        {
            fireResetEvent();
        }

    }

    private void fireResetEvent()
    {
        try
        {
            _resetEventCoordinator.fireResetEvent();
        }
        catch (Exception ex)
        {
            _errorLog.error(ImplMessages.errorResetting(ex), HiveMind.getLocation(ex), ex);
        }
    }

    public void setResetEventCoordinator(ResetEventCoordinator resetEventCoordinator)
    {
        _resetEventCoordinator = resetEventCoordinator;
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}