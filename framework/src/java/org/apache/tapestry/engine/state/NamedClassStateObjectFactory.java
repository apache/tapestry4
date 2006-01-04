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
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.impl.BaseLocatable;

/**
 * Used to instantiate the a state object from a configurable class name.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class NamedClassStateObjectFactory extends BaseLocatable implements StateObjectFactory
{
    private ClassResolver _classResolver;

    private String _className;

    public Object createStateObject()
    {
        try
        {
            Class c = _classResolver.findClass(_className);

            return c.newInstance();
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(StateMessages.unableToInstantiateObject(
                    _className,
                    ex), getLocation(), ex);
        }
    }

    public void setClassName(String className)
    {
        _className = className;
    }
    
    public String getClassName()
    {
        return _className;
    }
    
    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }

 
}