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

import org.apache.hivemind.util.Defense;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class StateObjectManagerImpl implements StateObjectManager
{
    private StateObjectPersistenceManager _persistenceManager;

    private StateObjectFactory _factory;

    private String _name;

    public StateObjectManagerImpl(String name, StateObjectFactory factory,
            StateObjectPersistenceManager persistenceManager)
    {
        Defense.notNull(name, "name");
        Defense.notNull(factory, "factory");
        Defense.notNull(persistenceManager, "persistenceManager");

        _name = name;
        _factory = factory;
        _persistenceManager = persistenceManager;
    }

    public boolean exists()
    {
        return _persistenceManager.exists(_name);
    }

    public Object get()
    {
        return _persistenceManager.get(_name, _factory);
    }

    public void store(Object stateObject)
    {
        _persistenceManager.store(_name, stateObject);
    }
}