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

package org.apache.tapestry.coerce;

import org.apache.hivemind.HiveMind;

/**
 * Converts a String to a Boolean. Strings that do contain non-whitespace are considered true. As a
 * special case, the string "false" evaluates to false (this allow boolean component parameters to
 * be bound to "false" instead of "ognl:false" and still operate as expected).
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class StringToBooleanConverter implements TypeConverter
{

    public Object convertValue(Object value)
    {
        String s = (String) value;

        boolean b = HiveMind.isNonBlank(s) && !s.equals("false");

        // JDK 1.4 has a Boolean.valueOf(), but we're trying for 1.3 compatibility

        return b ? Boolean.TRUE : Boolean.FALSE;
    }

}