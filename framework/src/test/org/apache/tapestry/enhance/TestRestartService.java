// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.enhance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.tapestry.engine.RestartService;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.services.AbsoluteURLBuilder;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.engine.RestartService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestRestartService extends TapestryTestCase
{
    public void testNoSession() throws Exception
    {
        MockControl requestControl = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) requestControl.getMock();

        HttpServletResponse response = (HttpServletResponse) newMock(HttpServletResponse.class);

        MockControl builderControl = newControl(AbsoluteURLBuilder.class);
        AbsoluteURLBuilder builder = (AbsoluteURLBuilder) builderControl.getMock();

        // Training

        request.getSession();
        requestControl.setReturnValue(null);

        request.getServletPath();
        requestControl.setReturnValue("/app");

        builder.constructURL("/app");
        builderControl.setReturnValue("http://myserver/app");

        response.sendRedirect("http://myserver/app");

        replayControls();

        RestartService s = new RestartService();
        s.setBuilder(builder);
        s.setRequest(request);
        s.setResponse(response);

        s.service(null, null);

        verifyControls();
    }

    public void testWithSession() throws Exception
    {
        MockControl requestControl = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) requestControl.getMock();

        HttpServletResponse response = (HttpServletResponse) newMock(HttpServletResponse.class);

        MockControl builderControl = newControl(AbsoluteURLBuilder.class);
        AbsoluteURLBuilder builder = (AbsoluteURLBuilder) builderControl.getMock();

        HttpSession session = (HttpSession) newMock(HttpSession.class);

        // Training

        request.getSession();
        requestControl.setReturnValue(session);

        session.invalidate();

        request.getServletPath();
        requestControl.setReturnValue("/app");

        builder.constructURL("/app");
        builderControl.setReturnValue("http://myserver/app");

        response.sendRedirect("http://myserver/app");

        replayControls();

        RestartService s = new RestartService();
        s.setBuilder(builder);
        s.setRequest(request);
        s.setResponse(response);

        s.service(null, null);

        verifyControls();
    }

    public void testErrorInvalidatingSession() throws Exception
    {
        MockControl requestControl = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) requestControl.getMock();

        HttpServletResponse response = (HttpServletResponse) newMock(HttpServletResponse.class);

        MockControl builderControl = newControl(AbsoluteURLBuilder.class);
        AbsoluteURLBuilder builder = (AbsoluteURLBuilder) builderControl.getMock();

        MockControl sessionControl = newControl(HttpSession.class);
        HttpSession session = (HttpSession) sessionControl.getMock();

        Log log = (Log) newMock(Log.class);

        IllegalStateException ex = new IllegalStateException();

        // Training

        request.getSession();
        requestControl.setReturnValue(session);

        session.invalidate();
        sessionControl.setThrowable(ex);

        log.warn("Exception thrown invalidating HttpSession.", ex);

        request.getServletPath();
        requestControl.setReturnValue("/app");

        builder.constructURL("/app");
        builderControl.setReturnValue("http://myserver/app");

        response.sendRedirect("http://myserver/app");

        replayControls();

        RestartService s = new RestartService();
        s.setBuilder(builder);
        s.setRequest(request);
        s.setResponse(response);
        s.setLog(log);

        s.service(null, null);

        verifyControls();
    }

}