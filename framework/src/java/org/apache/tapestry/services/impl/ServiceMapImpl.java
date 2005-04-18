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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.services.ServiceMap;

/**
 * Implementation of {@link org.apache.tapestry.services.ServiceMap}
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class ServiceMapImpl implements ServiceMap, EngineServiceSource
{
    /**
     * List of {@link EngineServiceContribution}.
     */
    private List _applicationServices;

    /**
     * List of {@link EngineServiceContribution}.
     */
    private List _factoryServices;

    private ErrorLog _errorLog;

    /**
     * Map of {@link EngineServiceContribution}&nbsp;keyed on String name.
     */
    private Map _services;

    /**
     * Map of {@link org.apache.tapestry.services.impl.EngineServiceOuterProxy}, keyed on String
     * name.
     */

    private Map _proxies = new HashMap();

    public void initializeService()
    {
        Map factoryMap = buildServiceMap(_factoryServices);
        Map applicationMap = buildServiceMap(_applicationServices);

        // Add services from the applicationMap to factoryMap, overwriting
        // factoryMap entries with the same name.

        factoryMap.putAll(applicationMap);

        _services = factoryMap;
    }

    private Map buildServiceMap(List services)
    {
        Map result = new HashMap();

        Iterator i = services.iterator();
        while (i.hasNext())
        {
            EngineServiceContribution contribution = (EngineServiceContribution) i.next();
            String name = contribution.getName();

            EngineServiceContribution existing = (EngineServiceContribution) result.get(name);

            if (existing != null)
            {
                _errorLog.error(
                        ImplMessages.dupeService(name, existing),
                        existing.getLocation(),
                        null);
                continue;
            }

            result.put(name, contribution);
        }

        return result;
    }

    public synchronized IEngineService getService(String name)
    {
        IEngineService result = (IEngineService) _proxies.get(name);

        if (result == null)
        {
            result = buildProxy(name);
            _proxies.put(name, result);
        }

        return result;
    }

    /**
     * This returns the actual service, not the outer proxy.
     */

    public IEngineService resolveEngineService(String name)
    {
        EngineServiceContribution contribution = (EngineServiceContribution) _services.get(name);

        if (contribution == null)
            throw new ApplicationRuntimeException(ImplMessages.noSuchService(name));

        IEngineService service = contribution.getService();
        String serviceName = service.getName();

        if (!serviceName.equals(name))
            throw new ApplicationRuntimeException(ImplMessages.serviceNameMismatch(
                    service,
                    name,
                    serviceName), contribution.getLocation(), null);

        return service;
    }

    private IEngineService buildProxy(String name)
    {
        if (!_services.containsKey(name))
            throw new ApplicationRuntimeException(ImplMessages.noSuchService(name));

        EngineServiceOuterProxy outer = new EngineServiceOuterProxy(name);

        EngineServiceInnerProxy inner = new EngineServiceInnerProxy(name, outer, this);

        outer.installDelegate(inner);

        return outer;
    }

    public void setApplicationServices(List applicationServices)
    {
        _applicationServices = applicationServices;
    }

    public void setFactoryServices(List factoryServices)
    {
        _factoryServices = factoryServices;
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}