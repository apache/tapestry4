// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.enhance;

import java.lang.reflect.Constructor;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.services.ComponentConstructor;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ComponentConstructorImpl implements ComponentConstructor
{
    private Location _location;

    private Constructor _constructor;

    private Object[] _parameters;

    /**
     * News instance of this class.
     * 
     * @param c
     *            the constructor method to invoke
     * @param parameters
     *            the parameters to pass to the constructor. These are retained, not copied.
     */

    public ComponentConstructorImpl(Constructor c, Object[] parameters, Location location)
    {
        _constructor = c;
        _parameters = parameters;
        _location = location;
    }

    public Class getComponentClass()
    {
        return _constructor.getDeclaringClass();
    }

    public Object newInstance()
    {
        try
        {
            return _constructor.newInstance(_parameters);
        }
        catch (Throwable ex)
        {
            throw new ApplicationRuntimeException(EnhanceMessages.instantiationFailure(
                    _constructor,
                    ex), null, _location, ex);
        }
    }

}