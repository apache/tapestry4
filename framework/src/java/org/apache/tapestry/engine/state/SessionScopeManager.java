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

package org.apache.tapestry.engine.state;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Manager for the 'session' scope; state objects are stored as HttpSession attributes. The
 * HttpSession is created as necessary.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class SessionScopeManager implements StateObjectPersistenceManager
{
    private HttpServletRequest _request;

    private String _applicationName;

    private String buildKey(String objectName)
    {
        return "state:" + _applicationName + ":" + objectName;
    }

    /**
     * Returns the session for the current request, creating it if necessary.
     */

    private HttpSession getSession()
    {
        return _request.getSession();
    }

    public boolean exists(String objectName)
    {
        HttpSession session = _request.getSession(false);

        if (session == null)
            return false;

        return session.getAttribute(buildKey(objectName)) != null;
    }

    public Object get(String objectName, StateObjectFactory factory)
    {
        String key = buildKey(objectName);
        HttpSession session = getSession();

        Object result = session.getAttribute(key);
        if (result == null)
        {
            result = factory.createStateObject();
            session.setAttribute(key, result);
        }

        return result;
    }

    public void store(String objectName, Object stateObject)
    {
        String key = buildKey(objectName);

        HttpSession session = getSession();

        if (stateObject == null)
            session.removeAttribute(key);
        else
            session.setAttribute(key, stateObject);
    }

    public void setApplicationName(String applicationName)
    {
        _applicationName = applicationName;
    }

    public void setRequest(HttpServletRequest request)
    {
        _request = request;
    }
}