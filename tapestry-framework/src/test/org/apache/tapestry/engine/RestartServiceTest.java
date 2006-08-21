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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import org.apache.commons.logging.Log;
import org.apache.tapestry.IRequestCycle;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Tests for {@link org.apache.tapestry.engine.RestartService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
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

        replay();

        RestartService s = new RestartService();
        s.setRequest(request);
        s.setResponse(response);
        s.setServletPath("/app");

        s.service(cycle);

        verify();
    }

    private void trainGetSession(HttpServletRequest request, boolean create, HttpSession session)
    {
        expect(request.getSession(create)).andReturn(session);
    }

    private HttpServletResponse newServletResponse()
    {
        return newMock(HttpServletResponse.class);
    }

    private HttpServletRequest newServletRequest()
    {
        return newMock(HttpServletRequest.class);
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

        replay();

        RestartService s = new RestartService();
        s.setRequest(request);
        s.setResponse(response);
        s.setServletPath("/app");

        s.service(cycle);

        verify();
    }

    private HttpSession newHttpSession()
    {
        return newMock(HttpSession.class);
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
        expectLastCall().andThrow(ex);

        log.warn("Exception thrown invalidating HttpSession.", ex);

        trainGetAbsoluteURL(cycle, "/app", "http://myserver/app");

        response.sendRedirect("http://myserver/app");

        replay();

        RestartService s = new RestartService();
        s.setRequest(request);
        s.setResponse(response);
        s.setServletPath("/app");
        s.setLog(log);

        s.service(cycle);

        verify();
    }
}
