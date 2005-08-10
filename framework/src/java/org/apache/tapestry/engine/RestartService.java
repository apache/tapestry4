// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.engine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.services.AbsoluteURLBuilder;
import org.apache.tapestry.services.LinkFactory;
import org.apache.tapestry.services.ServiceConstants;

/**
 * Restarts the Tapestry application. This is normally reserved for dealing with catastrophic
 * failures of the application. Discards the {@link javax.servlet.http.HttpSession}, if any, and
 * redirects to the Tapestry application servlet URL (invoking the {@link HomeService}).
 * 
 * @author Howard Lewis Ship
 * @since 1.0.9
 */

public class RestartService implements IEngineService
{
    /** @since 4.0 */
    private Log _log;

    /** @since 4.0 */
    private HttpServletRequest _request;

    /** @since 4.0 */
    private HttpServletResponse _response;

    /** @since 4.0 */
    private AbsoluteURLBuilder _builder;

    /** @since 4.0 */
    private LinkFactory _linkFactory;

    /** @since 4.0 */
    private String _servletPath;

    public ILink getLink(IRequestCycle cycle, boolean post, Object parameter)
    {
        if (parameter != null)
            throw new IllegalArgumentException(EngineMessages.serviceNoParameter(this));

        Map parameters = new HashMap();

        parameters.put(ServiceConstants.SERVICE, Tapestry.RESTART_SERVICE);

        return _linkFactory.constructLink(cycle, post, parameters, true);
    }

    public void service(IRequestCycle cycle) throws IOException
    {
        HttpSession session = _request.getSession(false);

        if (session != null)
        {
            try
            {
                session.invalidate();
            }
            catch (IllegalStateException ex)
            {
                _log.warn("Exception thrown invalidating HttpSession.", ex);

                // Otherwise, ignore it.
            }
        }

        String url = _builder.constructURL(cycle.getAbsoluteURL(_servletPath));

        _response.sendRedirect(url);
    }

    public String getName()
    {
        return Tapestry.RESTART_SERVICE;
    }

    /** @since 4.0 */
    public void setLog(Log log)
    {
        _log = log;
    }

    /** @since 4.0 */
    public void setRequest(HttpServletRequest request)
    {
        _request = request;
    }

    /** @since 4.0 */
    public void setBuilder(AbsoluteURLBuilder builder)
    {
        _builder = builder;
    }

    /** @since 4.0 */
    public void setResponse(HttpServletResponse response)
    {
        _response = response;
    }

    /** @since 4.0 */
    public void setLinkFactory(LinkFactory linkFactory)
    {
        _linkFactory = linkFactory;
    }

    /** @since 4.0 */
    public void setServletPath(String servletPath)
    {
        _servletPath = servletPath;
    }
}