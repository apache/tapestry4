//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.script;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.Tapestry;

/**
 *  A token that validates that an input symbol exists or is of a
 *  declared type.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

class InputSymbolToken extends AbstractToken
{
    private String _key;
    private Class _class;
    private boolean _required;

    InputSymbolToken(String key, Class clazz, boolean required, ILocation location)
    {
        super(location);

        _key = key;
        _class = clazz;
        _required = required;
    }

    public void write(StringBuffer buffer, ScriptSession session)
    {
        Object value = session.getSymbols().get(_key);

        if (_required && value == null)
            throw new ApplicationRuntimeException(
                Tapestry.format("InputSymbolToken.required", _key),
                getLocation(),
                null);

        if (value != null && _class != null && !_class.isAssignableFrom(value.getClass()))
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "InputSymbolToken.wrong-type",
                    _key,
                    value.getClass().getName(),
                    _class.getName()),
                getLocation(),
                null);
    }

}
