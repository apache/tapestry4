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
 * Outer proxy for engine services. The inner proxy resolves the engine service name to a engine
 * service implementation and installed it into the outer proxy as a delegate. Although HiveMind
 * does provide a similar system of inner and outer delegates, Tapestry's engine-service:
 * {@link org.apache.tapestry.services.impl.EngineServiceObjectProvider}&nbsp;object provider can
 * cause exceptions (recurive service build) when attempting to link two services together. This
 * extra layer of proxying resolves that issue.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class EngineServiceOuterProxy implements IEngineService
{
    private final String _serviceName;

    private IEngineService _delegate;

    public EngineServiceOuterProxy(String serviceName)
    {
        Defense.notNull(serviceName, "serviceName");

        _serviceName = serviceName;
    }

    void installDelegate(IEngineService delegate)
    {
        _delegate = delegate;
    }

    IEngineService getDelegate()
    {
        return _delegate;
    }

    public ILink getLink(IRequestCycle cycle, Object parameter)
    {
        return _delegate.getLink(cycle, parameter);
    }

    public void service(IRequestCycle cycle) throws IOException
    {
        _delegate.service(cycle);
    }

    public String getName()
    {
        return _serviceName;
    }

    public String toString()
    {
        return ImplMessages.engineServiceOuterProxyToString(_serviceName);
    }

}