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
import java.util.Iterator;
import java.util.Map;

import org.apache.hivemind.PoolManageable;

/**
 * This implementation expects to be pooled.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ApplicationStateManagerImpl implements ApplicationStateManager, PoolManageable
{

    /**
     * Keyed on application static object name, value is the current state object.
     */

    private Map _stateObjects = new HashMap();

    private StateObjectManagerRegistry _registry;

    public void activateService()
    {
    }

    public void passivateService()
    {
        _stateObjects.clear();
    }

    public boolean exists(String objectName)
    {
        return _stateObjects.containsKey(objectName) || _registry.get(objectName).exists();
    }

    public Object get(String objectName)
    {
        Object result = _stateObjects.get(objectName);

        if (result == null)
        {
            result = _registry.get(objectName).get();

            _stateObjects.put(objectName, result);
        }

        return result;
    }

    public void store(String objectName, Object stateObject)
    {
        _registry.get(objectName).store(stateObject);

        _stateObjects.put(objectName, stateObject);
    }

    public void flush()
    {
        Iterator i = _stateObjects.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();

            String objectName = (String) e.getKey();
            Object stateObject = e.getValue();

            // Slight bending of law-of-demeter

            _registry.get(objectName).store(stateObject);
        }
    }

    public void setRegistry(StateObjectManagerRegistry registry)
    {
        _registry = registry;
    }
}