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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class SOMRegistryImpl implements StateObjectManagerRegistry
{
    private ErrorLog _errorLog;

    private Map _factoryContributions;

    private Map _applicationContributions;

    private Map _persistenceManagers;

    private Map _objectManagers = new HashMap();

    public void initializeService()
    {
        Map contributions = new HashMap();
        contributions.putAll(_factoryContributions);
        // Overwrite any duplicates with the application contributions
        contributions.putAll(_applicationContributions);

        Iterator i = contributions.values().iterator();
        while (i.hasNext())
        {
            StateObjectContribution c = (StateObjectContribution) i.next();

            String objectName = c.getName();
            String scope = c.getScope();

            StateObjectPersistenceManager pm = (StateObjectPersistenceManager) _persistenceManagers
                    .get(scope);

            if (pm == null)
            {
                _errorLog.error(
                        StateMessages.unknownScope(objectName, scope),
                        c.getLocation(),
                        null);
                continue;
            }

            StateObjectManager manager = new StateObjectManagerImpl(objectName, c.getFactory(), pm);

            _objectManagers.put(objectName, manager);

        }
    }

    public StateObjectManager get(String objectName)
    {
        StateObjectManager manager = (StateObjectManager) _objectManagers.get(objectName);

        if (manager == null)
            throw new ApplicationRuntimeException(StateMessages.unknownStateObjectName(objectName));

        return manager;
    }

    public void setApplicationContributions(Map applicationContributions)
    {
        _applicationContributions = applicationContributions;
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }

    public void setFactoryContributions(Map factoryContributions)
    {
        _factoryContributions = factoryContributions;
    }

    public void setPersistenceManagers(Map persistenceManagers)
    {
        _persistenceManagers = persistenceManagers;
    }
}