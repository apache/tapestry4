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

package org.apache.tapestry.describe;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry.web.WebUtils;

/**
 * Strategy for describing an {@link javax.servlet.http.HttpServletRequest}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class HttpServletRequestStrategy implements DescribableStrategy
{

    public void describeObject(Object object, DescriptionReceiver receiver)
    {
        HttpServletRequest request = (HttpServletRequest) object;

        receiver.title("HttpServletRequest");
        receiver.property("authType", request.getAuthType());
        receiver.property("characterEncoding", request.getCharacterEncoding());
        receiver.property("contentLength", request.getContentLength());
        receiver.property("contextPath", request.getContextPath());
        receiver.property("contentType", request.getContentType());
        receiver.array("cookies", request.getCookies());
        receiver.property("locale", request.getLocale());
        receiver.property("method", request.getMethod());
        receiver.property("pathInfo", request.getPathInfo());
        receiver.property("pathTranslated", request.getPathTranslated());
        receiver.property("protocol", request.getProtocol());
        receiver.property("queryString", request.getQueryString());
        receiver.property("requestURI", request.getRequestURI());
        receiver.property("scheme", request.getScheme());
        receiver.property("secure", request.isSecure());
        receiver.property("serverName", request.getServerName());
        receiver.property("serverPort", request.getServerPort());
        receiver.property("servletPath", request.getServletPath());
        receiver.property("userPrincipal", request.getUserPrincipal());

        receiver.section("Parameters");

        List keys = WebUtils.toSortedList(request.getParameterNames());
        Iterator i = keys.iterator();
        while (i.hasNext())
        {
            String key = (String) i.next();
            String[] values = request.getParameterValues(key);

            receiver.array(key, values);
        }

        receiver.section("Headers");
        keys = WebUtils.toSortedList(request.getHeaderNames());
        i = keys.iterator();
        while (i.hasNext())
        {
            String key = (String) i.next();
            String value = request.getHeader(key);

            receiver.property(key, value);
        }

        receiver.section("Attributes");
        keys = WebUtils.toSortedList(request.getAttributeNames());
        i = keys.iterator();
        while (i.hasNext())
        {
            String key = (String) i.next();
            Object value = request.getAttribute(key);

            receiver.property(key, value);
        }
    }

}