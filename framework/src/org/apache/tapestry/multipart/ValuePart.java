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

package org.apache.tapestry.multipart;

import java.util.ArrayList;
import java.util.List;

/**
 *  A portion of a multipart request that stores a value, or values, for
 *  a parameter.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.1
 *
 **/

public class ValuePart implements IPart
{
    private int _count;
    // Stores either String or List of String
    private Object _value;

    public ValuePart(String value)
    {
        _count = 1;
        _value = value;
    }

    public int getCount()
    {
        return _count;
    }

    /**
     *  Returns the value, or the first value (if multi-valued).
     * 
     **/

    public String getValue()
    {
        if (_count == 1)
            return (String) _value;

        List l = (List) _value;

        return (String) l.get(0);
    }

    /**
     *  Returns the values as an array of strings.  If there is only one value,
     *  it is returned wrapped as a single element array.
     * 
     **/

    public String[] getValues()
    {
        if (_count == 1)
            return new String[] {(String) _value };

        List l = (List) _value;

        return (String[]) l.toArray(new String[_count]);
    }

    public void add(String newValue)
    {
        if (_count == 1)
        {
            List l = new ArrayList();
            l.add(_value);
            l.add(newValue);

            _value = l;
            _count++;
            return;
        }

        List l = (List) _value;
        l.add(newValue);
        _count++;
    }

    /**
     *  Does nothing.
     * 
     **/

    public void cleanup()
    {
    }
}