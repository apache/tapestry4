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

package org.apache.tapestry.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.util.Defense;

/**
 * A wrapper around a Map that stores query parameter values. Map keys are strings. Map values can
 * be simple strings or array of string (or null).
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class QueryParameterMap
{
    private final Map _parameters;

    public QueryParameterMap()
    {
        this(new HashMap());
    }

    /**
     * Constructor around an existing Map whose keys and values are expected to conform expected use
     * (keys are strings, values are null, string or string array). The map passed in is retained (
     * not copied).
     */

    public QueryParameterMap(Map parameterMap)
    {
        Defense.notNull(parameterMap, "parameterMap");

        _parameters = parameterMap;
    }

    /**
     * Replaces the parameter value for the given name wit the new value (which may be null).
     */

    public void setParameterValue(String name, String value)
    {
        Defense.notNull(name, "name");

        _parameters.put(name, value);
    }

    /**
     * Replaces the parameter value for the given name wit the new list of values (which may be
     * empty or null).
     */

    public void setParameterValues(String name, String[] values)
    {
        Defense.notNull(name, "name");

        _parameters.put(name, values);
    }

    /**
     * Gets a query parameter value. If an array of values was stored, this returns the first value.
     * May return null.
     */

    public String getParameterValue(String name)
    {
        Defense.notNull(name, "name");

        Object values = _parameters.get(name);

        if (values == null || values instanceof String)
            return (String) values;

        String[] array = (String[]) values;

        return array[0];
    }

    /**
     * Returns the array of values for the specified parameter. If only a lone value was stored (via
     * {@link #setParameterValue(String, String)}, then the value is wrapped as a string array and
     * returned.
     */
    public String[] getParameterValues(String name)
    {
        Defense.notNull(name, "name");

        Object values = _parameters.get(name);

        if (values == null || values instanceof String[])
            return (String[]) values;

        String loneValue = (String) values;

        return new String[]
        { loneValue };
    }

    /**
     * Returns the names of all parameters, sorted alphabetically.
     */
    public String[] getParameterNames()
    {
        int count = _parameters.size();

        String[] result = (String[]) _parameters.keySet().toArray(new String[count]);

        Arrays.sort(result);

        return result;
    }
}