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

package org.apache.tapestry.engine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.tapestry.IRequestCycle;

/**
 * Tests for {@link org.apache.tapestry.engine.RestartService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class RestartServiceTest extends ServiceTestCase
{
    public void testNoSession() throws Exception
    {
        HttpServletRequest request = newServletRequest();
        HttpServletResponse response = newServletResponse();
        IRequestCycle cycle = newCycle();

        // Training

        trainGetSession(request, false, null);
        trainGetAbsoluteURL(cycle, "/app", "http://myserver/app");

        response.sendRedirect("http://myserver/app");

        replayControls();

        RestartService s = new RestartService();
        s.setRequest(request);
        s.setResponse(response);
        s.setServletPath("/app");

        s.service(cycle);

        verifyControls();
    }

    private void trainGetSession(HttpServletRequest request, boolean create, HttpSession session)
    {
        request.getSession(create);
        setReturnValue(request, session);
    }

    private HttpServletResponse newServletResponse()
    {
        return (HttpServletResponse) newMock(HttpServletResponse.class);
    }

    private HttpServletRequest newServletRequest()
    {
        return (HttpServletRequest) newMock(HttpServletRequest.class);
    }

    public void testWithSession() throws Exception
    {
        HttpServletRequest request = newServletRequest();
        HttpServletResponse response = newServletResponse();
        HttpSession session = newHttpSession();

        IRequestCycle cycle = newCycle();

        // Training

        trainGetSession(request, false, session);

        session.invalidate();

        trainGetAbsoluteURL(cycle, "/app", "http://myserver/app");

        response.sendRedirect("http://myserver/app");

        replayControls();

        RestartService s = new RestartService();
        s.setRequest(request);
        s.setResponse(response);
        s.setServletPath("/app");

        s.service(cycle);

        verifyControls();
    }

    private HttpSession newHttpSession()
    {
        return (HttpSession) newMock(HttpSession.class);
    }

    public void testErrorInvalidatingSession() throws Exception
    {
        HttpServletRequest request = newServletRequest();
        HttpServletResponse response = newServletResponse();
        HttpSession session = newHttpSession();
        Log log = newLog();
        Throwable ex = new IllegalStateException("Bad state");

        IRequestCycle cycle = newCycle();

        // Training

        trainGetSession(request, false, session);

        session.invalidate();
        setThrowable(session, ex);

        log.warn("Exception thrown invalidating HttpSession.", ex);

        trainGetAbsoluteURL(cycle, "/app", "http://myserver/app");

        response.sendRedirect("http://myserver/app");

        replayControls();

        RestartService s = new RestartService();
        s.setRequest(request);
        s.setResponse(response);
        s.setServletPath("/app");
        s.setLog(log);

        s.service(cycle);

        verifyControls();
    }
}
