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

import java.util.HashMap;
import java.util.Map;

/**
 * Holds a utility method that converts java type names (as they might appear in source code) into
 * JVM class names.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class JavaTypeUtils
{
    /**
     * Mapping between a primitive type and its Java VM representation Used for the encoding of
     * array types
     */
    private static Map PRIMITIVE_TYPES = new HashMap();

    static
    {
        PRIMITIVE_TYPES.put("boolean", "Z");
        PRIMITIVE_TYPES.put("short", "S");
        PRIMITIVE_TYPES.put("int", "I");
        PRIMITIVE_TYPES.put("long", "J");
        PRIMITIVE_TYPES.put("float", "F");
        PRIMITIVE_TYPES.put("double", "D");
        PRIMITIVE_TYPES.put("char", "C");
        PRIMITIVE_TYPES.put("byte", "B");
    }

    private JavaTypeUtils()
    {
        // Prevent instantiation
    }

    /**
     * Translates types from standard Java format to Java VM format. For example, java.util.Locale
     * remains java.util.Locale, but int[][] is translated to [[I and java.lang.Object[] to
     * [Ljava.lang.Object;
     */
    public static String getJVMClassName(String type)
    {
        // if it is not an array, just return the type itself
        if (!type.endsWith("[]"))
            return type;

        // if it is an array, convert it to JavaVM-style format
        StringBuffer buffer = new StringBuffer();

        while (type.endsWith("[]"))
        {
            buffer.append("[");
            type = type.substring(0, type.length() - 2);
        }

        String primitiveIdentifier = (String) PRIMITIVE_TYPES.get(type);
        if (primitiveIdentifier != null)
            buffer.append(primitiveIdentifier);
        else
        {
            buffer.append("L");
            buffer.append(type);
            buffer.append(";");
        }

        return buffer.toString();
    }
}