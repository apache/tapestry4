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

import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;

/**
 * Adapts {@link javax.servlet.http.HttpServletResponse}&nbsp;as
 * {@link org.apache.tapestry.web.WebResponse}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ServletWebResponse implements WebResponse
{
    private final HttpServletResponse _servletResponse;

    public ServletWebResponse(HttpServletResponse response)
    {
        Defense.notNull(response, "response");

        _servletResponse = response;
    }

    public OutputStream getOutputStream(String contentType)
    {
        Defense.notNull(contentType, "contentType");

        _servletResponse.setContentType(contentType);

        try
        {
            return _servletResponse.getOutputStream();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(WebMessages
                    .streamOpenError(contentType, ex), null, ex);
        }
    }

    public String encodeURL(String url)
    {
        return _servletResponse.encodeURL(url);
    }

    public void reset()
    {
        _servletResponse.reset();
    }

}