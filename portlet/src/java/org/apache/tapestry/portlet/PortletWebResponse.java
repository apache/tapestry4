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

package org.apache.tapestry.portlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.portlet.PortletResponse;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebResponse;

/**
 * Adapts {@link javax.portlet.PortletResponse}. as {@link org.apache.tapestry.web.WebResponse}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class PortletWebResponse implements WebResponse
{
    private final PortletResponse _portletResponse;

    public PortletWebResponse(PortletResponse portletResponse)
    {
        Defense.notNull(portletResponse, "portletResponse");

        _portletResponse = portletResponse;
    }

    public OutputStream getOutputStream(ContentType contentType) throws IOException
    {
        unsupported("getOutputStream");

        return null;
    }

    public PrintWriter getPrintWriter(ContentType contentType) throws IOException
    {
        unsupported("getPrintWriter");

        return null;
    }

    public String encodeURL(String url)
    {
        return _portletResponse.encodeURL(url);
    }

    public void reset()
    {
        unsupported("reset");
    }

    public void setContentLength(int contentLength)
    {
        unsupported("setContentLength");
    }

    protected final void unsupported(String methodName)
    {
        throw new UnsupportedOperationException(PortletMessages.unsupportedMethod(methodName));
    }
}