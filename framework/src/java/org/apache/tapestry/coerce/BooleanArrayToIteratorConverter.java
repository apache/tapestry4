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

package org.apache.tapestry.coerce;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.coerce.TypeConverter;

/**
 * Converts primitive arrays of booleans to a Iterator (of a List of ) Boolean.
 * 
 * @author Laurent ETIEMBLE, Howard M. Lewis Ship
 * @since 4.0
 */
public class BooleanArrayToIteratorConverter implements TypeConverter
{

    public Object convertValue(Object value)
    {
        boolean[] booleans = (boolean[]) value;

        List list = new ArrayList(booleans.length);

        for (int i = 0; i < booleans.length; i++)
        {
            boolean b = booleans[i];

            list.add(b ? Boolean.TRUE : Boolean.FALSE);
        }

        return list.iterator();
    }

}
