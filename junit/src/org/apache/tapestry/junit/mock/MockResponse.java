//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.junit.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 *  Mock implementation of {@link javax.servlet.http.HttpServletResponse}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class MockResponse implements HttpServletResponse
{
    private MockRequest _request;
    private boolean _commited = false;
    private ByteArrayOutputStream _outputByteStream;
    private ServletOutputStream _outputStream;
    private List _cookies = new ArrayList();

    private String _redirectLocation;
    private String _contentType;

    private class ServletOutputStreamImpl extends ServletOutputStream
    {
        private ServletOutputStreamImpl()
        {
        }

        public void close() throws IOException
        {
            super.close();

            if (_outputByteStream != null)
                _outputByteStream.close();
        }

        public void write(byte[] b, int off, int len) throws IOException
        {
            commit();

            _outputByteStream.write(b, off, len);
        }

        public void write(byte[] b) throws IOException
        {
            commit();

            _outputByteStream.write(b);
        }

        public void write(int b) throws IOException
        {
            commit();

            _outputByteStream.write(b);
        }

        private void commit()
        {
            if (!_commited)
            {
                _commited = true;
                _outputByteStream = new ByteArrayOutputStream();
            }
        }
    }

    public MockResponse(MockRequest request)
    {
        _request = request;
    }

    public void addCookie(Cookie cookie)
    {
        _cookies.add(cookie);
    }

    public boolean containsHeader(String arg0)
    {
        return false;
    }

    public String encodeURL(String path)
    {
        return path;
    }

    public String encodeRedirectURL(String path)
    {
        return path;
    }

    public String encodeUrl(String path)
    {
        return encodeURL(path);
    }

    public String encodeRedirectUrl(String path)
    {
        return encodeRedirectURL(path);
    }

    public void sendError(int code, String message) throws IOException
    {
        if (_commited)
            throw new IllegalStateException("sendError() when committed.");
    }

    public void sendError(int code) throws IOException
    {
        sendError(code, null);
    }

    public void sendRedirect(String location) throws IOException
    {
        if (_commited)
            throw new IllegalStateException("sendRedirect() when committed.");

		if (location.endsWith("/FAIL_IO"))
			throw new IOException("Forced IOException in MockResponse.sendRedirect().");

        _redirectLocation = location;

        _commited = true;

    }

    public String getRedirectLocation()
    {
        return _redirectLocation;
    }

    public void setDateHeader(String name, long value)
    {
    }

    public void addDateHeader(String name, long value)
    {
    }

    public void setHeader(String name, String value)
    {
    }

    public void addHeader(String name, String value)
    {
    }

    public void setIntHeader(String name, int value)
    {
    }

    public void addIntHeader(String name, int value)
    {
    }

    public void setStatus(int name)
    {
    }

    public void setStatus(int name, String arg1)
    {
    }

    public String getCharacterEncoding()
    {
        return null;
    }

    public ServletOutputStream getOutputStream() throws IOException
    {
        if (_outputStream != null)
            throw new IllegalStateException("getOutputStream() invoked more than once.");

        _outputStream = new ServletOutputStreamImpl();

        return _outputStream;
    }

    public PrintWriter getWriter() throws IOException
    {
        return new PrintWriter(getOutputStream());
    }

    public void setContentLength(int arg0)
    {
    }

    public void setContentType(String contentType)
    {
    	_contentType = contentType;
    }

    public void setBufferSize(int arg0)
    {
    }

    public int getBufferSize()
    {
        return 0;
    }

    public void flushBuffer() throws IOException
    {
    }

    public void resetBuffer()
    {
    }

    public boolean isCommitted()
    {
        return _commited;
    }

    public void reset()
    {
    }

    public void setLocale(Locale arg0)
    {
    }

    public Locale getLocale()
    {
        return null;
    }

    /**
     *  Invoked by {@link org.apache.tapestry.junit.mock.MockTester} after
     *  the test is complete, to close and otherwise finish up.
     * 
     **/

    public void end() throws IOException
    {
    	// For redirects, we may never open an output stream.
    	
        if (_outputStream != null)
            _outputStream.close();
    }

    /**
     *  Converts the binary output stream back into a String.
     * 
     **/

    public String getOutputString()
    {
    	if (_outputByteStream == null)
    		return null;
    		
        try {
            String encoding = _request.getCharacterEncoding();
            return new String(_outputByteStream.toByteArray(), encoding);
        }
        catch (UnsupportedEncodingException e) {
            // use the default encoding if the provided one is unsupported
            return _outputByteStream.toString();
        }
    }

	public byte[] getResponseBytes()
	{
		return _outputByteStream.toByteArray();
	}

    public Cookie[] getCookies()
    {
        return (Cookie[]) _cookies.toArray(new Cookie[_cookies.size()]);
    }
    
    public String getContentType()
    {
        return _contentType;
    }

    public void setCharacterEncoding(String enc)
    {
    }

}
