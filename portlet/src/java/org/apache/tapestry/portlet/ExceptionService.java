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
import java.io.PrintWriter;

import javax.portlet.PortletURL;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.util.ContentType;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;
import org.apache.tapestry.web.WebSession;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.portlet.PortletConstants#PORTLET_EXCEPTION_MARKUP_ATTRIBUTE
 */
public class ExceptionService implements IEngineService
{
    private WebRequest _request;

    private WebResponse _response;

    private PortletRequestGlobals _globals;

    public ILink getLink(IRequestCycle cycle, Object parameter)
    {
        throw new UnsupportedOperationException(PortletMessages.unsupportedMethod("getLink"));
    }

    public void service(IRequestCycle cycle) throws IOException
    {
        WebSession session = _request.getSession(true);
        String markup = (String) session
                .getAttribute(PortletConstants.PORTLET_EXCEPTION_MARKUP_ATTRIBUTE);

        PrintWriter writer = _response.getPrintWriter(new ContentType("text/html"));

        PortletURL url = _globals.getRenderResponse().createActionURL();

        writer.println("<span class=\"portlet-msg-error\">An exception has occured.</span>");
        writer.println("<br/>");
        writer.println("<a href=\"" + url.toString() + "\">Click here to continue</a>");
        writer.print("<br/><hr/>");
        writer.println();

        writer.print(markup);
    }

    public String getName()
    {
        return PortletConstants.EXCEPTION_SERVICE;
    }

    public void setRequest(WebRequest request)
    {
        _request = request;
    }

    public void setResponse(WebResponse response)
    {
        _response = response;
    }

    public void setGlobals(PortletRequestGlobals globals)
    {
        _globals = globals;
    }
}