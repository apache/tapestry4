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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import java.io.UnsupportedEncodingException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.services.ServletRequestServicer;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Tests for {@link org.apache.tapestry.services.impl.SetupRequestEncoding}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestSetupRequestEncoding extends BaseComponentTestCase
{
    private HttpServletRequest newRequest(String encoding)
    {
        HttpServletRequest request = newMock(HttpServletRequest.class);

        expect(request.getCharacterEncoding()).andReturn(encoding);

        return request;
    }

    private HttpServletResponse newResponse()
    {
        return newMock(HttpServletResponse.class);
    }

    private ServletRequestServicer newServicer()
    {
        return newMock(ServletRequestServicer.class);
    }

    public void testEncodingNotNull() throws Exception
    {
        HttpServletRequest request = newRequest("utf-8");
        HttpServletResponse response = newResponse();
        ServletRequestServicer servicer = newServicer();

        servicer.service(request, response);

        replay();

        SetupRequestEncoding sre = new SetupRequestEncoding();
        sre.setOutputEncoding("output-encoding");

        sre.service(request, response, servicer);

        verify();
    }

    public void testEncodingNull() throws Exception
    {
        HttpServletRequest request = newRequest(null);
        HttpServletResponse response = newResponse();
        ServletRequestServicer servicer = newServicer();

        request.setCharacterEncoding("output-encoding");

        servicer.service(request, response);

        replay();

        SetupRequestEncoding sre = new SetupRequestEncoding();
        sre.setOutputEncoding("output-encoding");

        sre.service(request, response, servicer);

        verify();
    }

    public void testUnsupportedEncoding() throws Exception
    {
        HttpServletRequest request = newMock(HttpServletRequest.class);

        HttpServletResponse response = newResponse();
        ServletRequestServicer servicer = newServicer();

        Throwable t = new UnsupportedEncodingException("Bad encoding.");

        expect(request.getCharacterEncoding()).andReturn(null);

        request.setCharacterEncoding("output-encoding");
        expectLastCall().andThrow(t);

        replay();

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

        verify();
    }

    public void testNoSuchMethodError() throws Exception
    {
        HttpServletRequest request = newMock(HttpServletRequest.class);

        HttpServletResponse response = newResponse();
        ServletRequestServicer servicer = newServicer();

        Throwable t = new NoSuchMethodError();

        expect(request.getCharacterEncoding()).andReturn(null);

        request.setCharacterEncoding("output-encoding");
        expectLastCall().andThrow(t);

        servicer.service(request, response);

        replay();

        SetupRequestEncoding sre = new SetupRequestEncoding();
        sre.setOutputEncoding("output-encoding");

        sre.service(request, response, servicer);

        verify();

        // Check that, after such an error, we don't even try to do it again.

        servicer.service(request, response);

        replay();

        sre.service(request, response, servicer);

        verify();
    }
    
    public void testAbstractMethodError() throws Exception
    {
        HttpServletRequest request = newMock(HttpServletRequest.class);

        HttpServletResponse response = newResponse();
        ServletRequestServicer servicer = newServicer();

        Throwable t = new AbstractMethodError();

        expect(request.getCharacterEncoding()).andReturn(null);

        request.setCharacterEncoding("output-encoding");
        expectLastCall().andThrow(t);

        servicer.service(request, response);

        replay();

        SetupRequestEncoding sre = new SetupRequestEncoding();
        sre.setOutputEncoding("output-encoding");

        sre.service(request, response, servicer);

        verify();

        // Check that, after such an error, we don't even try to do it again.

        servicer.service(request, response);

        replay();

        sre.service(request, response, servicer);

        verify();
    }    
}