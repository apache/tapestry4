// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.commons.logging.Log;
import org.apache.tapestry.services.RequestExceptionReporter;
import org.apache.tapestry.util.exception.ExceptionAnalyzer;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class RequestExceptionReporterImpl implements RequestExceptionReporter
{
    private Log _log;

    private WebRequest _request;

    public void setLog(Log log)
    {
        _log = log;
    }

    public void setRequest(WebRequest request)
    {
        _request = request;
    }

    public void reportRequestException(String message, Throwable cause)
    {
        _log.warn(message, cause);

        System.err.println("\n\n**********************************************************\n\n");

        System.err.println(message);

        System.err.println("\n");

        WebSession session = _request.getSession(false);

        if (session != null)
            System.err.println("      Session id    : " + session.getId());

        System.err.println("\nExceptions:\n");

        new ExceptionAnalyzer().reportException(cause, System.err);

        System.err.println("\n**********************************************************\n");
    }

}