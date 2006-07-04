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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.util.ContentType;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.web.ServletWebResponse}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class ServletWebResponseTest extends BaseComponentTestCase
{
    private static class MockServletOutputStream extends ServletOutputStream
    {
        public void write(int b) throws IOException
        {
        }
    }

    public void testGetOutputStream() throws Exception
    {
        HttpServletResponse response = newResponse();

        ServletOutputStream stream = new MockServletOutputStream();

        response.setContentType("foo/bar");
        expect(response.getOutputStream()).andReturn(stream);

        replay();

        ServletWebResponse swr = new ServletWebResponse(response);

        assertSame(stream, swr.getOutputStream(new ContentType("foo/bar")));

        verify();
    }

    public void testGetOutputStreamFailure() throws Exception
    {
        HttpServletResponse response = newResponse();

        Throwable t = new IOException("Simulated failure.");

        response.setContentType("foo/bar");
        expect(response.getOutputStream()).andThrow(t);

        replay();

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

        HttpServletResponse response = newResponse();

        response.setContentType("foo/bar");
        
        trainGetWriter(response, writer);

        replay();

        ServletWebResponse swr = new ServletWebResponse(response);

        assertSame(writer, swr.getPrintWriter(new ContentType("foo/bar")));

        verify();
    }

    private void trainGetWriter(HttpServletResponse response, PrintWriter writer) throws IOException
    {
        expect(response.getWriter()).andReturn(writer);
    }
    
    

    public void testGetSecondPrintWriter() throws Exception
    {
        PrintWriter writer1 = new PrintWriter(new CharArrayWriter());
        PrintWriter writer2 = new PrintWriter(new CharArrayWriter());

        HttpServletResponse response = newResponse();

        response.setContentType("foo/bar");
        
        trainGetWriter(response, writer1);
        
        replay();

        ServletWebResponse swr = new ServletWebResponse(response);

        assertSame(writer1, swr.getPrintWriter(new ContentType("foo/bar")));

        verify();

        response.reset();
        response.setContentType("biff/bazz");
        
        trainGetWriter(response, writer2);
        
        replay();

        assertSame(writer2, swr.getPrintWriter(new ContentType("biff/bazz")));

        verify();
    }
    
    public void testGetSecondPrintWriterTomcatPatch() throws Exception
    {
        PrintWriter writer1 = new PrintWriter(new CharArrayWriter());
        PrintWriter writer2 = new PrintWriter(new CharArrayWriter());

        HttpServletResponse response = newResponse();
        Log log = newLog();

        response.setContentType("foo/bar");
        
        trainGetWriter(response, writer1);
        
        replay();

        ServletWebResponse swr = new ServletWebResponse(response, log, true);

        assertSame(writer1, swr.getPrintWriter(new ContentType("foo/bar")));

        verify();

        response.reset();

        trainGetWriter(response, writer2);
        
        replay();

        assertSame(writer2, swr.getPrintWriter(new ContentType("foo/bar")));

        verify();
    }
    
    public void testGetSecondPrintWriterDifferentContentTypeTomcatPatch() throws Exception
    {
        PrintWriter writer1 = new PrintWriter(new CharArrayWriter());
        PrintWriter writer2 = new PrintWriter(new CharArrayWriter());

        HttpServletResponse response = newResponse();
        Log log = newLog();

        response.setContentType("foo/bar");
        
        trainGetWriter(response, writer1);
        
        replay();

        ServletWebResponse swr = new ServletWebResponse(response, log, true);

        assertSame(writer1, swr.getPrintWriter(new ContentType("foo/bar")));

        verify();

        response.reset();
        
        log.warn("Unable to change response content type from 'foo/bar' to 'biff/bazz' (following a reset). See Tapestry issue TAPESTRY-607.");
        
        trainGetWriter(response, writer2);
        
        replay();

        assertSame(writer2, swr.getPrintWriter(new ContentType("biff/bazz")));

        verify();
    }    

    public void testGetPrintWriterFailure() throws Exception
    {
        HttpServletResponse response = newResponse();

        Throwable t = new IOException("Simulated failure.");

        response.setContentType("foo/bar");
        expect(response.getWriter()).andThrow(t);

        replay();

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
        HttpServletResponse response = newResponse();

        response.reset();

        replay();

        ServletWebResponse swr = new ServletWebResponse(response);

        swr.reset();

        verify();
    }

    private HttpServletResponse newResponse()
    {
        return newMock(HttpServletResponse.class);
    }

    public void testSetHeaderMethods() throws Exception
    {
        HttpServletResponse response = newResponse();

        response.setHeader("fie", "fie");
        response.setDateHeader("expires", -1);
        response.setIntHeader("size", 33);
        response.sendError(99, "foo!");

        replay();

        ServletWebResponse swr = new ServletWebResponse(response);

        swr.setHeader("fie", "fie");
        swr.setDateHeader("expires", -1);
        swr.setIntHeader("size", 33);
        swr.sendError(99, "foo!");

        verify();

    }

}