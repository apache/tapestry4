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

package org.apache.tapestry.services.impl;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.Constants;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.services.EngineManager;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.RequestServicer;
import org.apache.tapestry.spec.IApplicationSpecification;

/**
 * The terminatior for the <code>tapestry.RequestProcessor</code> pipeline,
 * this service is responsible for locating the correct engine instance and
 * letting it to the rest of the request.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class InvokeEngineTerminator implements RequestServicer
{
    private HttpServlet _servlet;
    private EngineManager _engineManager;
    private IApplicationSpecification _specification;
    private Infrastructure _infrastructure;

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        IEngine engine = _engineManager.getEngineInstance();

        request.setAttribute(Constants.INFRASTRUCTURE_KEY, _infrastructure);

        RequestContext context = new RequestContext(_servlet, request, response, _specification);

        try
        {
            engine.service(context);
        }
        finally
        {
            _engineManager.storeEngineInstance(engine);

            context.cleanup();
        }

    }

    public void setEngineManager(EngineManager manager)
    {
        _engineManager = manager;
    }

    public void setServlet(HttpServlet servlet)
    {
        _servlet = servlet;
    }

    public void setSpecification(IApplicationSpecification specification)
    {
        _specification = specification;
    }

    public void setInfrastructure(Infrastructure infrastructure)
    {
        _infrastructure = infrastructure;
    }

}
