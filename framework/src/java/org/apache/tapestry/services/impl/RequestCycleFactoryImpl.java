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

import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.IMonitor;
import org.apache.tapestry.engine.IMonitorFactory;
import org.apache.tapestry.engine.RequestCycle;
import org.apache.tapestry.engine.ServiceEncoder;
import org.apache.tapestry.engine.ServiceEncodingImpl;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.services.RequestCycleFactory;
import org.apache.tapestry.services.ServiceConstants;
import org.apache.tapestry.services.ServiceMap;
import org.apache.tapestry.util.QueryParameterMap;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class RequestCycleFactoryImpl implements RequestCycleFactory
{
    private ServiceEncoder[] _encoders;

    private IMonitorFactory _monitorFactory;

    private ServiceMap _serviceMap;

    public IRequestCycle newRequestCycle(IEngine engine, RequestContext context)
    {
        IMonitor monitor = _monitorFactory.createMonitor(context);

        QueryParameterMap parameters = extractParameters(context);

        decodeParameters(context.getRequest().getServletPath(), parameters);

        IEngineService service = findService(parameters);

        return new RequestCycle(engine, context, parameters, service, monitor);
    }

    private IEngineService findService(QueryParameterMap parameters)
    {
        String serviceName = parameters.getParameterValue(ServiceConstants.SERVICE);

        if (serviceName == null)
            serviceName = Tapestry.HOME_SERVICE;

        return _serviceMap.getService(serviceName);
    }

    /**
     * Constructs a {@link org.apache.tapestry.util.QueryParameterMap}using the parameters
     * available from the {@link org.apache.tapestry.request.RequestContext}&nbsp;(but ignoring any
     * file upload parameters!).
     */

    private QueryParameterMap extractParameters(RequestContext context)
    {
        QueryParameterMap result = new QueryParameterMap();

        String[] names = context.getParameterNames();

        for (int i = 0; i < names.length; i++)
        {
            String name = names[i];

            String[] values = context.getParameters(name);

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

    public void setServiceMap(ServiceMap serviceMap)
    {
        _serviceMap = serviceMap;
    }
}