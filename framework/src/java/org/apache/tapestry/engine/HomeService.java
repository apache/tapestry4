// Copyright 2004 The Apache Software Foundation
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

import javax.servlet.ServletException;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.ResponseOutputStream;
import org.apache.tapestry.services.ResponseRenderer;

/**
 * An implementation of the home service that renders the Home page. This is the most likely
 * candidate for overriding ... for example, to select the page to render based on known information
 * about the user (stored as a cookie).
 * 
 * @author Howard Lewis Ship
 * @since 1.0.9
 */

public class HomeService extends AbstractService
{
    /** @since 3.1 */
    private ResponseRenderer _responseRenderer;

    public ILink getLink(IRequestCycle cycle, Object parameter)
    {
        if (parameter != null)
            throw new IllegalArgumentException(EngineMessages.serviceNoParameter(this));

        return constructLink(cycle, Tapestry.HOME_SERVICE, null, null, true);
    }

    public void service(IRequestCycle cycle, ResponseOutputStream output) throws ServletException,
            IOException
    {
        cycle.activate(IEngine.HOME_PAGE);

        _responseRenderer.renderResponse(cycle, output);
    }

    public String getName()
    {
        return Tapestry.HOME_SERVICE;
    }

    /** @since 3.1 */
    public void setResponseRenderer(ResponseRenderer responseRenderer)
    {
        _responseRenderer = responseRenderer;
    }
}