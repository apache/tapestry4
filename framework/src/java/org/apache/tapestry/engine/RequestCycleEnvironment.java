// Copyright 2005, 2006 The Apache Software Foundation
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
import org.apache.tapestry.IEngine;
import org.apache.tapestry.record.PropertyPersistenceStrategySource;
import org.apache.tapestry.services.AbsoluteURLBuilder;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.util.QueryParameterMap;

/**
 * An object that contains all the invariant parameters to the
 * {@link org.apache.tapestry.engine.RequestCycle#RequestCycle(IEngine, QueryParameterMap, IEngineService, IMonitor, RequestCycleEnvironment)}
 * constructor.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class RequestCycleEnvironment
{
    private final Infrastructure _infrastructure;

    private final PropertyPersistenceStrategySource _strategySource;

    private final AbsoluteURLBuilder _absoluteURLBuilder;

    private final ErrorHandler _errorHandler;

    public RequestCycleEnvironment(ErrorHandler errorHandler, Infrastructure infrastructure,
            PropertyPersistenceStrategySource strategySource, AbsoluteURLBuilder absoluteURLBuilder)
    {
        _errorHandler = errorHandler;
        _infrastructure = infrastructure;
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
}