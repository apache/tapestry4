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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.impl.BaseLocatable;

/**
 * Simple state object factory that instantiates an instance of a class.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class InstantiateClassStateObjectFactory extends BaseLocatable implements StateObjectFactory
{
    private Class _objectClass;

    public Object createStateObject()
    {
        try
        {
            return _objectClass.newInstance();
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(StateMessages.unableToInstantiateObject(
                    _objectClass,
                    ex), getLocation(), ex);
        }
    }

    public void setObjectClass(Class objectClass)
    {
        _objectClass = objectClass;
    }
}