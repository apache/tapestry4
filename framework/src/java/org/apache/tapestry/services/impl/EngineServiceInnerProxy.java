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

import java.io.IOException;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 * Inner proxy that actually resolves the engine service using the
 * {@link org.apache.tapestry.services.impl.EngineServiceSource}, then replaces itself in the outer
 * proxy.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.services.impl.EngineServiceOuterProxy
 */
public class EngineServiceInnerProxy implements IEngineService
{
    private final String _serviceName;

    private final EngineServiceOuterProxy _outerProxy;

    private final EngineServiceSource _source;

    public EngineServiceInnerProxy(String serviceName, EngineServiceOuterProxy outerProxy,
            EngineServiceSource source)
    {
        Defense.notNull(serviceName, "serviceName");
        Defense.notNull(outerProxy, "outerProxy");
        Defense.notNull(source, "source");

        _serviceName = serviceName;
        _outerProxy = outerProxy;
        _source = source;
    }

    public String toString()
    {
        return ImplMessages.engineServiceInnerProxyToString(_serviceName);
    }

    private IEngineService resolve()
    {
        IEngineService service = _source.resolveEngineService(_serviceName);

        _outerProxy.installDelegate(service);

        return service;
    }

    public synchronized ILink getLink(IRequestCycle cycle, Object parameter)
    {
        return resolve().getLink(cycle, parameter);
    }

    public synchronized void service(IRequestCycle cycle) throws IOException
    {
        resolve().service(cycle);
    }

    public String getName()
    {
        return _serviceName;
    }

}