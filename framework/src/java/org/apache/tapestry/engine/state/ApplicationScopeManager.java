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

import java.util.HashMap;
import java.util.Map;

/**
 * The application scope is for objects that are global to all users and all sessions.
 * Traditionally, that's stored in the servlet context, but there's no advantage to doing that over
 * just using a Map.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ApplicationScopeManager implements StateObjectPersistenceManager
{
    private Map _objects = new HashMap();

    public synchronized boolean exists(String objectName)
    {
        return false;
    }

    public synchronized Object get(String objectName, StateObjectFactory factory)
    {
        Object result = _objects.get(objectName);

        if (result == null)
        {
            result = factory.createStateObject();
            _objects.put(objectName, result);
        }

        return result;
    }

    public synchronized void store(String objectName, Object stateObject)
    {
        _objects.put(objectName, stateObject);
    }

}