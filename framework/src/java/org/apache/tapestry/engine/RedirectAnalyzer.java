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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.RequestContext;

/**
 * Used by {@link org.apache.tapestry.engine.AbstractEngine} when
 * processing a {@link org.apache.tapestry.RedirectException}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */
class RedirectAnalyzer
{
    private boolean _internal;

    private String _location;

    public RedirectAnalyzer(String location)
    {
        if (Tapestry.isBlank(location))
        {
            _location = "";
            _internal = true;

            return;
        }

        _location = location;

        _internal = !(location.startsWith("/") || location.indexOf("://") > 0);
    }

    public void process(IRequestCycle cycle)
    {
        RequestContext context = cycle.getRequestContext();

        if (_internal)
            forward(context);
        else
            redirect(context);
    }

    private void forward(RequestContext context)
    {
        HttpServletRequest request = context.getRequest();
        HttpServletResponse response = context.getResponse();

        RequestDispatcher dispatcher = request.getRequestDispatcher("/" + _location);

        if (dispatcher == null)
            throw new ApplicationRuntimeException(Tapestry.format(
                    "AbstractEngine.unable-to-find-dispatcher",
                    _location));

        try
        {
            dispatcher.forward(request, response);
        }
        catch (ServletException ex)
        {
            throw new ApplicationRuntimeException(Tapestry.format(
                    "AbstractEngine.unable-to-forward",
                    _location), ex);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(Tapestry.format(
                    "AbstractEngine.unable-to-forward",
                    _location), ex);
        }
    }

    private void redirect(RequestContext context)
    {
        HttpServletResponse response = context.getResponse();

        String finalURL = response.encodeRedirectURL(_location);

        try
        {
            response.sendRedirect(finalURL);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(Tapestry.format(
                    "AbstractEngine.unable-to-redirect",
                    _location), ex);
        }
    }

}