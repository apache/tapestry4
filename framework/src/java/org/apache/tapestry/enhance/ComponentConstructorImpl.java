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

package org.apache.tapestry.enhance;

import java.lang.reflect.Constructor;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.services.ComponentConstructor;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ComponentConstructorImpl implements ComponentConstructor
{
    private Location _location;

    private Constructor _constructor;

    private Object[] _parameters;

    private String _classFabString;

    /**
     * News instance of this class.
     * 
     * @param constructor
     *            the constructor method to invoke
     * @param parameters
     *            the parameters to pass to the constructor. These are retained, not copied.
     * @param classFabString
     *            a string representing the generated class information, used for exception
     *            reporting
     * @param location
     *            the location, used for exception reporting
     */

    public ComponentConstructorImpl(Constructor constructor, Object[] parameters,
            String classFabString, Location location)
    {
        Defense.notNull(constructor, "constructor");
        _constructor = constructor;
        _parameters = parameters;
        _classFabString = classFabString;
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
            Object result = _constructor.newInstance(_parameters);

            // Unlikely to generate an error if we can get through it once, so it's
            // safe to release the big classFabString

            _classFabString = null;

            return result;
        }
        catch (Throwable ex)
        {
            throw new ApplicationRuntimeException(EnhanceMessages.instantiationFailure(
                    _constructor,
                    _parameters,
                    _classFabString,
                    ex), null, _location, ex);
        }
    }

}