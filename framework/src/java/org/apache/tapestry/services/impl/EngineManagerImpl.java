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

import java.util.Locale;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.services.EngineFactory;
import org.apache.tapestry.services.EngineManager;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ObjectPool;

/**
 * Implementation of service {@link org.apache.tapestry.services.EngineManager}.
 * Service point tapestry.request.EngineManager.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class EngineManagerImpl implements EngineManager
{
    private ObjectPool _enginePool;

    private EngineFactory _engineFactory;

    private RequestLocaleManager _localeManager;

    public IEngine getEngineInstance()
    {
        Locale locale = _localeManager.extractLocaleForCurrentRequest();

        IEngine result = (IEngine) _enginePool.get(locale);

        // This happens when either the pool is empty, or when a session exists
        // but the engine has not been stored into it (which should never happen, and
        // probably indicates an error in the framework or the application).

        if (result == null)
            result = _engineFactory.constructNewEngineInstance(locale);

        return result;
    }

    public void storeEngineInstance(IEngine engine)
    {
        _enginePool.store(engine.getLocale(), engine);
    }

    public void setEngineFactory(EngineFactory factory)
    {
        _engineFactory = factory;
    }

    public void setEnginePool(ObjectPool pool)
    {
        _enginePool = pool;
    }

    public void setLocaleManager(RequestLocaleManager manager)
    {
        _localeManager = manager;
    }
}