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

package org.apache.tapestry.services.impl;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.ErrorHandler;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.IMonitor;
import org.apache.tapestry.engine.IMonitorFactory;
import org.apache.tapestry.engine.RequestCycle;
import org.apache.tapestry.engine.RequestCycleEnvironment;
import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncodingImpl;
import org.apache.tapestry.record.PropertyPersistenceStrategySource;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.services.AbsoluteURLBuilder;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.RequestCycleFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.services.ServiceMap;
import org.apache.tapestry.util.QueryParameterMap;
import org.apache.tapestry.web.WebRequest;

/**
 * Service that creates instances of {@link org.apache.tapestry.IRequestCycle}on behalf of an
 * engine.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class RequestCycleFactoryImpl implements RequestCycleFactory
{
    private ServiceEncoder[] _encoders;

    private IMonitorFactory _monitorFactory;

    private PropertyPersistenceStrategySource _strategySource;

    private ErrorHandler _errorHandler;

    private Infrastructure _infrastructure;

    private AbsoluteURLBuilder _absoluteURLBuilder;

    private RequestCycleEnvironment _environment;

    private HttpServletRequest _servletRequest;

    private HttpServletResponse _servletResponse;

    public void initializeService()
    {
        RequestContext context = new RequestContext(_servletRequest, _servletResponse);

        _environment = new RequestCycleEnvironment(_errorHandler, _infrastructure, context,
                _strategySource, _absoluteURLBuilder);
    }

    public IRequestCycle newRequestCycle(IEngine engine)
    {
        WebRequest request = _infrastructure.getRequest();

        IMonitor monitor = _monitorFactory.createMonitor(request);

        QueryParameterMap parameters = extractParameters(request);

        decodeParameters(request.getActivationPath(), parameters);

        String serviceName = findService(parameters);

        return new RequestCycle(engine, parameters, serviceName, monitor, _environment);
    }

    private String findService(QueryParameterMap parameters)
    {
        String serviceName = parameters.getParameterValue(ServiceConstants.SERVICE);

        return serviceName == null ? Tapestry.HOME_SERVICE : serviceName;
    }

    /**
     * Constructs a {@link org.apache.tapestry.util.QueryParameterMap}using the parameters
     * available from the {@link org.apache.tapestry.request.RequestContext}&nbsp;(but ignoring any
     * file upload parameters!).
     */

    private QueryParameterMap extractParameters(WebRequest request)
    {
        QueryParameterMap result = new QueryParameterMap();

        Iterator i = request.getParameterNames().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();

            String[] values = request.getParameterValues(name);

            if (values.length == 1)
                result.setParameterValue(name, values[0]);
            else
                result.setParameterValues(name, values);
        }

        return result;
    }

    private void decodeParameters(String servletPath, QueryParameterMap map)
    {
        ServiceEncodingImpl se = new ServiceEncodingImpl(servletPath, map);

        for (int i = 0; i < _encoders.length; i++)
        {
            _encoders[i].decode(se);

            if (se.isModified())
                return;
        }
    }

    public void setEncoders(ServiceEncoder[] encoders)
    {
        _encoders = encoders;
    }

    public void setMonitorFactory(IMonitorFactory monitorFactory)
    {
        _monitorFactory = monitorFactory;
    }

    public void setStrategySource(PropertyPersistenceStrategySource strategySource)
    {
        _strategySource = strategySource;
    }

    public void setErrorHandler(ErrorHandler errorHandler)
    {
        _errorHandler = errorHandler;
    }

    public void setInfrastructure(Infrastructure infrastructure)
    {
        _infrastructure = infrastructure;
    }

    public void setAbsoluteURLBuilder(AbsoluteURLBuilder absoluteURLBuilder)
    {
        _absoluteURLBuilder = absoluteURLBuilder;
    }

    public void setServletRequest(HttpServletRequest servletRequest)
    {
        _servletRequest = servletRequest;
    }

    public void setServletResponse(HttpServletResponse servletResponse)
    {
        _servletResponse = servletResponse;
    }
}