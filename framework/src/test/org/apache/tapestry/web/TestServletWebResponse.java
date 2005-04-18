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

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.util.ContentType;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.web.ServletWebResponse}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
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

        assertSame(stream, swr.getOutputStream(new ContentType("foo/bar")));

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
            swr.getOutputStream(new ContentType("foo/bar"));
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

    public void testGetPrintWriter() throws Exception
    {
        PrintWriter writer = new PrintWriter(new CharArrayWriter());

        MockControl control = newControl(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) control.getMock();

        response.setContentType("foo/bar");
        response.getWriter();
        control.setReturnValue(writer);

        replayControls();

        ServletWebResponse swr = new ServletWebResponse(response);

        assertSame(writer, swr.getPrintWriter(new ContentType("foo/bar")));

        verifyControls();
    }

    public void testGetSecondPrintWriter() throws Exception
    {
        PrintWriter writer1 = new PrintWriter(new CharArrayWriter());
        PrintWriter writer2 = new PrintWriter(new CharArrayWriter());

        MockControl control = newControl(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) control.getMock();

        response.setContentType("foo/bar");
        response.getWriter();
        control.setReturnValue(writer1);

        replayControls();

        ServletWebResponse swr = new ServletWebResponse(response);

        assertSame(writer1, swr.getPrintWriter(new ContentType("foo/bar")));

        verifyControls();

        response.reset();
        response.setContentType("zip/zap");
        response.getWriter();
        control.setReturnValue(writer2);

        replayControls();

        assertSame(writer2, swr.getPrintWriter(new ContentType("zip/zap")));

        verifyControls();
    }

    public void testGetPrintWriterFailure() throws Exception
    {
        MockControl control = newControl(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) control.getMock();

        Throwable t = new IOException("Simulated failure.");

        response.setContentType("foo/bar");
        response.getWriter();
        control.setThrowable(t);

        replayControls();

        ServletWebResponse swr = new ServletWebResponse(response);

        try
        {
            swr.getPrintWriter(new ContentType("foo/bar"));
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Error opening response writer for content type foo/bar: Simulated failure.",
                    ex.getMessage());
            assertSame(t, ex.getRootCause());
        }
    }

    public void testReset()
    {
        MockControl control = newControl(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) control.getMock();

        response.reset();

        replayControls();

        ServletWebResponse swr = new ServletWebResponse(response);

        swr.reset();

        verifyControls();
    }

}