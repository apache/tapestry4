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

package org.apache.tapestry.enhance;

import java.util.HashMap;
import java.util.Map;

/**
 *  @author Mindbridge
 *  @version $Id$
 *  @since 3.0
 */
public class JavaClassMapping
{

    /**
     *  Map of type (as Class), keyed on type name. 
     * 
     **/

    private Map _typeMap = new HashMap();


    {
        recordType("boolean", boolean.class);
        recordType("boolean[]", boolean[].class);

        recordType("short", short.class);
        recordType("short[]", short[].class);

        recordType("int", int.class);
        recordType("int[]", int[].class);

        recordType("long", long.class);
        recordType("long[]", long[].class);

        recordType("float", float.class);
        recordType("float[]", float[].class);

        recordType("double", double.class);
        recordType("double[]", double[].class);

        recordType("char", char.class);
        recordType("char[]", char[].class);

        recordType("byte", byte.class);
        recordType("byte[]", byte[].class);

        recordType("java.lang.Object", Object.class);
        recordType("java.lang.Object[]", Object[].class);

        recordType("java.lang.String", String.class);
        recordType("java.lang.String[]", String[].class);
    }


    public void recordType(String name, Class type)
    {
        _typeMap.put(name, type);
    }

    public Class getType(String name)
    {
        return (Class) _typeMap.get(name);
    }

}
