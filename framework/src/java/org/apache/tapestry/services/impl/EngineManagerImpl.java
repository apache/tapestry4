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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.services.EngineFactory;
import org.apache.tapestry.services.EngineManager;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.services.ObjectPool;

/**
 * Implementation of service {@link org.apache.tapestry.services.EngineManager}.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class EngineManagerImpl implements EngineManager
{
    private ObjectPool _enginePool;
    private HttpServletRequest _request;
    private String _servletName;
    private String _engineKey;
    private EngineFactory _engineFactory;
    private RequestLocaleManager _localeManager;

    static final String ENGINE_KEY_PREFIX = "org.apache.tapestry.engine:";

    public void initializeService()
    {
        _engineKey = ENGINE_KEY_PREFIX + _servletName;
    }

    public IEngine getEngineInstance()
    {
        HttpSession session = getSession();
        IEngine result = null;

        if (session != null)
        {
            result = (IEngine) session.getAttribute(_engineKey);

            if (result != null)
                return result;
        }

        Locale locale = _localeManager.extractLocaleForCurrentRequest();

        result = (IEngine) _enginePool.get(locale);

        // This happens when either the pool is empty, or when a session exists
        // but the engine has not been stored into it (which should never happen, and
        // probably indicates an error in the framework or the application).

        if (result == null)
            result = _engineFactory.constructNewEngineInstance(locale);

        return result;
    }

    private HttpSession getSession()
    {
        return _request.getSession(false);
    }

    public void storeEngineInstance(IEngine engine)
    {
        HttpSession session = getSession();

        if (session == null)
        {
            _enginePool.store(engine.getLocale(), engine);
            return;
        }

        // TODO: We've lost the optimizations for only storing the engine when dirty.
        // However, since (I believe) in 3.1, the engine will no longer be session persistent,
        // this is OK.

        session.setAttribute(_engineKey, engine);
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

    public void setRequest(HttpServletRequest request)
    {
        _request = request;
    }

    public void setServletName(String string)
    {
        _servletName = string;
    }
}
