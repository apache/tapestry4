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

package org.apache.tapestry.engine;

import org.apache.hivemind.ErrorHandler;
import org.apache.tapestry.record.PropertyPersistenceStrategySource;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.services.AbsoluteURLBuilder;
import org.apache.tapestry.services.Infrastructure;

/**
 * An object that contains all the invariant parameters to the
 * {@link org.apache.tapestry.engine.RequestCycle#RequestCycle(IEngine, QueryParameterMap, IEngineService, IMonitor, RequestCycleEnvironment)}
 * constructor.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class RequestCycleEnvironment
{
    private Infrastructure _infrastructure;

    private PropertyPersistenceStrategySource _strategySource;

    private AbsoluteURLBuilder _absoluteURLBuilder;

    private ErrorHandler _errorHandler;

    private RequestContext _requestContext;

    public RequestCycleEnvironment(ErrorHandler errorHandler, Infrastructure infrastructure,
            RequestContext requestContext, PropertyPersistenceStrategySource strategySource,
            AbsoluteURLBuilder absoluteURLBuilder)
    {
        _errorHandler = errorHandler;
        _infrastructure = infrastructure;
        _requestContext = requestContext;
        _strategySource = strategySource;
        _absoluteURLBuilder = absoluteURLBuilder;
    }

    public AbsoluteURLBuilder getAbsoluteURLBuilder()
    {
        return _absoluteURLBuilder;
    }

    public ErrorHandler getErrorHandler()
    {
        return _errorHandler;
    }

    public Infrastructure getInfrastructure()
    {
        return _infrastructure;
    }

    public PropertyPersistenceStrategySource getStrategySource()
    {
        return _strategySource;
    }

    public RequestContext getRequestContext()
    {
        return _requestContext;
    }
}