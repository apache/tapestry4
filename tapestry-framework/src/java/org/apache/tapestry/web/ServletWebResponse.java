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
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.util.ContentType;

/**
 * Adapts {@link javax.servlet.http.HttpServletResponse}&nbsp;as
 * {@link org.apache.tapestry.web.WebResponse}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ServletWebResponse implements WebResponse
{
    private static final Log DEFAULT_LOG = LogFactory.getLog(ServletWebResponse.class);

    private final Log _log;

    private final boolean _tomcatPatch;

    private final HttpServletResponse _servletResponse;

    private boolean _needsReset;

    private ContentType _printWriterContentType;

    public ServletWebResponse(HttpServletResponse response)
    {
        this(response, DEFAULT_LOG, Boolean.getBoolean("org.apache.tapestry.607-patch"));
    }

    /**
     * Alternate constructor used by some tests.
     */
    ServletWebResponse(HttpServletResponse response, Log log, boolean tomcatPatch)
    {
        Defense.notNull(response, "response");
        Defense.notNull(log, "log");

        _servletResponse = response;
        _log = log;
        _tomcatPatch = tomcatPatch;
    }

    public OutputStream getOutputStream(ContentType contentType)
    {
        Defense.notNull(contentType, "contentType");

        _servletResponse.setContentType(contentType.getMimeType());

        try
        {
            return _servletResponse.getOutputStream();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(WebMessages.streamOpenError(contentType, ex),
                    null, ex);
        }
    }

    public PrintWriter getPrintWriter(ContentType contentType) throws IOException
    {
        Defense.notNull(contentType, "contentType");

        if (_needsReset)
            reset();

        _needsReset = true;
        
        if (_printWriterContentType == null || ! _tomcatPatch)
        {
            _servletResponse.setContentType(contentType.toString());
            _printWriterContentType = contentType;
        }
        else
        {
            // This is a workaround for a tomcat bug; it takes effect when a page is reset so that
            // the exception page (typically) can be rendered. See TAPESTRY-607 for details.

            if (!_printWriterContentType.equals(contentType))
                _log.warn(WebMessages.contentTypeUnchanged(_printWriterContentType, contentType));
        }

        try
        {
            return _servletResponse.getWriter();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(WebMessages.writerOpenError(contentType, ex),
                    null, ex);
        }
    }

    public String encodeURL(String url)
    {
        return _servletResponse.encodeURL(url);
    }

    public void reset()
    {
        try
        {
            _servletResponse.reset();
        }
        catch (IllegalStateException ex)
        {
            _log.error(WebMessages.resetFailed(ex), ex);
        }
    }

    public void setContentLength(int length)
    {
        _servletResponse.setContentLength(length);
    }

    public String getNamespace()
    {
        return "";
    }

    public void setDateHeader(String name, long date)
    {
        _servletResponse.setDateHeader(name, date);
    }

    public void setStatus(int status)
    {
        _servletResponse.setStatus(status);
    }

    public void setHeader(String name, String value)
    {
        _servletResponse.setHeader(name, value);
    }

    public void setIntHeader(String name, int value)
    {
        _servletResponse.setIntHeader(name, value);
    }

    public void sendError(int statusCode, String message) throws IOException
    {
        _servletResponse.sendError(statusCode, message);
    }

}
