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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.HiveMind;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.services.ServiceMap;

/**
 * Implementation of {@link org.apache.tapestry.services.ServiceMap}
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class ServiceMapImpl implements ServiceMap
{
    private List _applicationServices;

    private List _factoryServices;

    private ErrorLog _errorLog;

    /**
     * Map of {@link IEngineService}keyed on String name.
     */
    private Map _services;

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
            IEngineService s = (IEngineService) i.next();
            String name = s.getName();

            IEngineService existing = (IEngineService) result.get(name);

            if (existing != null)
            {
                _errorLog.error(
                        ImplMessages.dupeService(name, existing),
                        HiveMind.getLocation(s),
                        null);
                continue;
            }

            result.put(name, s);
        }

        return result;
    }

    public IEngineService getService(String name)
    {
        IEngineService result = (IEngineService) _services.get(name);

        if (result == null)
            throw new ApplicationRuntimeException(ImplMessages.noSuchService(name));

        return result;
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