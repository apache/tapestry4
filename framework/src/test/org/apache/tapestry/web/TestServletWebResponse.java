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

package org.apache.tapestry.web;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.web.ServletWebResponse;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.web.ServletWebResponse}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestServletWebResponse extends HiveMindTestCase
{
    private static class MockServletOutputStream extends ServletOutputStream
    {
        public void write(int b) throws IOException
        {
        }
    }

    public void testGetOutputStream() throws Exception
    {
        MockControl control = newControl(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) control.getMock();

        ServletOutputStream stream = new MockServletOutputStream();

        response.setContentType("foo/bar");
        response.getOutputStream();
        control.setReturnValue(stream);

        replayControls();

        ServletWebResponse swr = new ServletWebResponse(response);

        assertSame(stream, swr.getOutputStream("foo/bar"));

        verifyControls();
    }

    public void testGetOutputStreamFailure() throws Exception
    {
        MockControl control = newControl(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) control.getMock();

        Throwable t = new IOException("Simulated failure.");

        response.setContentType("foo/bar");
        response.getOutputStream();
        control.setThrowable(t);

        replayControls();

        ServletWebResponse swr = new ServletWebResponse(response);

        try
        {
            swr.getOutputStream("foo/bar");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Error opening response stream for content type foo/bar: Simulated failure.",
                    ex.getMessage());
            assertSame(t, ex.getRootCause());
        }

    }

}