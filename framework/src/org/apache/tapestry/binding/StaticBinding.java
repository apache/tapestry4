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

package org.apache.tapestry.binding;

import org.apache.tapestry.ILocation;

/**
 * Stores a static (invariant) String as the value.
 *
 * <p>It may be useful to cache static bindings the way {@link FieldBinding}s are cached.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public class StaticBinding extends AbstractBinding
{
    private String _value;
    private boolean _parsedInt;
    private int _intValue;
    private boolean _parsedDouble;
    private double _doubleValue;

    public StaticBinding(String value, ILocation location)
    {
    	super(location);
    	
        _value = value;
    }

    /**
     *  Interprets the static value as an integer.
     *
     **/

    public int getInt()
    {
        if (!_parsedInt)
        {
            _intValue = Integer.parseInt(_value);
            _parsedInt = true;
        }

        return _intValue;
    }

    /**
     *  Interprets the static value as a double.
     *
     **/

    public double getDouble()
    {
        if (!_parsedDouble)
        {
            _doubleValue = Double.parseDouble(_value);
            _parsedDouble = true;
        }

        return _doubleValue;
    }

    public String getString()
    {
        return _value;
    }

    public Object getObject()
    {
        return _value;
    }

    public String toString()
    {
        return "StaticBinding[" + _value + "]";
    }
}