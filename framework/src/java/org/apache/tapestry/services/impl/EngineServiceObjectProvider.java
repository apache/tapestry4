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

package org.apache.tapestry.services.impl;

import org.apache.hivemind.Location;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.service.ObjectProvider;
import org.apache.tapestry.services.ServiceMap;

/**
 * Provides the implementation of the "engine-service:" prefix, were the locator is simply the name
 * of an engine service.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class EngineServiceObjectProvider implements ObjectProvider
{
    private ServiceMap _serviceMap;

    public void setServiceMap(ServiceMap serviceMap)
    {
        _serviceMap = serviceMap;
    }

    public Object provideObject(Module contributingModule, Class propertyType, String locator,
            Location location)
    {
        return _serviceMap.getService(locator);
    }
}