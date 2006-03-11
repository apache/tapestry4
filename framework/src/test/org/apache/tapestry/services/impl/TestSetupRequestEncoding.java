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

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.services.ServletRequestServicer;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.SetupRequestEncoding}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestSetupRequestEncoding extends HiveMindTestCase
{
    private HttpServletRequest newRequest(String encoding)
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getCharacterEncoding();
        control.setReturnValue(encoding);

        return request;
    }

    private HttpServletResponse newResponse()
    {
        return (HttpServletResponse) newMock(HttpServletResponse.class);
    }

    private ServletRequestServicer newServicer()
    {
        return (ServletRequestServicer) newMock(ServletRequestServicer.class);
    }

    public void testEncodingNotNull() throws Exception
    {
        HttpServletRequest request = newRequest("utf-8");
        HttpServletResponse response = newResponse();
        ServletRequestServicer servicer = newServicer();

        servicer.service(request, response);

        replayControls();

        SetupRequestEncoding sre = new SetupRequestEncoding();
        sre.setOutputEncoding("output-encoding");

        sre.service(request, response, servicer);

        verifyControls();
    }

    public void testEncodingNull() throws Exception
    {
        HttpServletRequest request = newRequest(null);
        HttpServletResponse response = newResponse();
        ServletRequestServicer servicer = newServicer();

        request.setCharacterEncoding("output-encoding");

        servicer.service(request, response);

        replayControls();

        SetupRequestEncoding sre = new SetupRequestEncoding();
        sre.setOutputEncoding("output-encoding");

        sre.service(request, response, servicer);

        verifyControls();
    }

    public void testUnsupportedEncoding() throws Exception
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        HttpServletResponse response = newResponse();
        ServletRequestServicer servicer = newServicer();

        Throwable t = new UnsupportedEncodingException("Bad encoding.");

        request.getCharacterEncoding();
        control.setReturnValue(null);

        request.setCharacterEncoding("output-encoding");
        control.setThrowable(t);

        replayControls();

        SetupRequestEncoding sre = new SetupRequestEncoding();
        sre.setOutputEncoding("output-encoding");

        try
        {
            sre.service(request, response, servicer);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Unable to set request character encoding to 'output-encoding': Bad encoding.",
                    ex.getMessage());
            assertSame(t, ex.getRootCause());
        }

        verifyControls();
    }

    public void testNoSuchMethodError() throws Exception
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        HttpServletResponse response = newResponse();
        ServletRequestServicer servicer = newServicer();

        Throwable t = new NoSuchMethodError();

        request.getCharacterEncoding();
        control.setReturnValue(null);

        request.setCharacterEncoding("output-encoding");
        control.setThrowable(t);

        servicer.service(request, response);

        replayControls();

        SetupRequestEncoding sre = new SetupRequestEncoding();
        sre.setOutputEncoding("output-encoding");

        sre.service(request, response, servicer);

        verifyControls();

        // Check that, after such an error, we don't even try to do it again.

        servicer.service(request, response);

        replayControls();

        sre.service(request, response, servicer);

        verifyControls();
    }
    
    public void testAbstractMethodError() throws Exception
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        HttpServletResponse response = newResponse();
        ServletRequestServicer servicer = newServicer();

        Throwable t = new AbstractMethodError();

        request.getCharacterEncoding();
        control.setReturnValue(null);

        request.setCharacterEncoding("output-encoding");
        control.setThrowable(t);

        servicer.service(request, response);

        replayControls();

        SetupRequestEncoding sre = new SetupRequestEncoding();
        sre.setOutputEncoding("output-encoding");

        sre.service(request, response, servicer);

        verifyControls();

        // Check that, after such an error, we don't even try to do it again.

        servicer.service(request, response);

        replayControls();

        sre.service(request, response, servicer);

        verifyControls();
    }    
}