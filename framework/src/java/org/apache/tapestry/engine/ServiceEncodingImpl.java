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

package org.apache.tapestry.engine;

import java.util.Map;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.util.QueryParameterMap;

/**
 * Implementation of {@link org.apache.tapestry.engine.ServiceEncoding}, which adds the ability to
 * determine when the encoding has been modified.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ServiceEncodingImpl implements ServiceEncoding
{
    private String _servletPath;

    /**
     * Map of query parameter values; key is string name, value is either a string, an array of
     * strings, or null. Could have done this with subclassing rather than delegation.
     */

    private final QueryParameterMap _parameters;

    private boolean _modified;

    public boolean isModified()
    {
        return _modified;
    }

    public void resetModified()
    {
        _modified = false;
    }

    /**
     * Creates a new instance with a new map of parameters.
     */

    public ServiceEncodingImpl(String servletPath)
    {
        this(servletPath, new QueryParameterMap());
    }

    public ServiceEncodingImpl(String servletPath, Map parametersMap)
    {
        this(servletPath, new QueryParameterMap(parametersMap));
    }

    public ServiceEncodingImpl(String servletPath, QueryParameterMap parameters)
    {
        Defense.notNull(servletPath, "servletPath");
        Defense.notNull(parameters, "parameters");

        _servletPath = servletPath;

        _parameters = parameters;
    }

    public String getParameterValue(String name)
    {
        return _parameters.getParameterValue(name);
    }

    public String[] getParameterValues(String name)
    {
        return _parameters.getParameterValues(name);
    }

    public void setServletPath(String servletPath)
    {
        Defense.notNull(servletPath, "servletPath");

        _servletPath = servletPath;
        _modified = true;
    }

    public void setParameterValue(String name, String value)
    {
        _parameters.setParameterValue(name, value);

        _modified = true;
    }

    public void setParameterValues(String name, String[] values)
    {
        _parameters.setParameterValues(name, values);

        _modified = true;
    }

    public String getServletPath()
    {
        return _servletPath;
    }

    public String[] getParameterNames()
    {
        return _parameters.getParameterNames();
    }

}