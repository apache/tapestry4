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

package org.apache.tapestry.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.request.RequestContext}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestRequestContext extends HiveMindTestCase
{
    private HttpSession newSession()
    {
        return (HttpSession) newMock(HttpSession.class);
    }

    private HttpServletRequest newRequest(boolean create, HttpSession session)
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getSession(create);
        control.setReturnValue(session);

        return request;
    }

    private HttpServletRequest newRequest()
    {
        return (HttpServletRequest) newMock(HttpServletRequest.class);
    }

    private HttpServletResponse newResponse()
    {
        return (HttpServletResponse) newMock(HttpServletResponse.class);
    }

    public void testGetParameter()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();
        HttpServletResponse response = newResponse();

        String value = "VALUE";

        request.getParameter("myparam");
        control.setReturnValue(value);

        replayControls();

        RequestContext rc = new RequestContext(request, response);

        assertSame(value, rc.getParameter("myparam"));

        verifyControls();
    }

    public void testGetParameters()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();
        HttpServletResponse response = newResponse();

        String[] values =
        { "this", "that" };

        request.getParameterValues("myparam");
        control.setReturnValue(values);

        replayControls();

        RequestContext rc = new RequestContext(request, response);

        assertSame(values, rc.getParameters("myparam"));

        verifyControls();
    }

    public void testGetParameterNames()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();
        HttpServletResponse response = newResponse();

        List names = new ArrayList();
        names.add("fred");
        names.add("barney");

        Enumeration e = Collections.enumeration(names);

        request.getParameterNames();
        control.setReturnValue(e);

        replayControls();

        RequestContext rc = new RequestContext(request, response);

        String[] pnames = rc.getParameterNames();

        assertListsEqual(new String[]
        { "fred", "barney" }, pnames);

        verifyControls();
    }

    public void testGetRequestAndResponse()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        replayControls();

        RequestContext rc = new RequestContext(request, response);

        assertSame(request, rc.getRequest());
        assertSame(response, rc.getResponse());

        verifyControls();
    }

    public void testGetAttribute()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();
        HttpServletResponse response = newResponse();

        Object attribute = new Object();

        request.getAttribute("myattr");
        control.setReturnValue(attribute);

        replayControls();

        RequestContext rc = new RequestContext(request, response);

        assertSame(attribute, rc.getAttribute("myattr"));

        verifyControls();
    }

    public void testGetSession()
    {
        HttpSession session = newSession();
        HttpServletRequest request = newRequest(false, session);
        HttpServletResponse response = newResponse();

        replayControls();

        RequestContext rc = new RequestContext(request, response);

        assertSame(session, rc.getSession());

        verifyControls();
    }

    public void testCreateSession()
    {
        HttpSession session = newSession();
        HttpServletRequest request = newRequest(true, session);
        HttpServletResponse response = newResponse();

        replayControls();

        RequestContext rc = new RequestContext(request, response);

        assertSame(session, rc.createSession());

        verifyControls();
    }
}