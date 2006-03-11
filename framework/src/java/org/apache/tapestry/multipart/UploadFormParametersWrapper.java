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

package org.apache.tapestry.multipart;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.hivemind.util.Defense;

/**
 * {@link javax.servlet.http.HttpServletRequest}&nbsp; wrapper that provides access to the form
 * field values uploaded in a multipart request.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class UploadFormParametersWrapper extends HttpServletRequestWrapper
{
    /**
     * Map of {@link ValuePart}&nbsp;keyed on parameter name.
     */
    private Map _parameterMap;

    /**
     * @param parameterMap
     *            a map whose keys are parameter names and whose values are arrays of Strings.
     */
    public UploadFormParametersWrapper(HttpServletRequest request, Map parameterMap)
    {
        super(request);

        Defense.notNull(parameterMap, "parameterMap");

        _parameterMap = Collections.unmodifiableMap(parameterMap);
    }

    public String getParameter(String name)
    {
        String[] values = getParameterValues(name);

        return (values == null || values.length == 0) ? null : values[0];
    }

    public Map getParameterMap()
    {
        return _parameterMap;
    }

    public Enumeration getParameterNames()
    {
        return Collections.enumeration(_parameterMap.keySet());
    }

    public String[] getParameterValues(String name)
    {
        return (String[]) _parameterMap.get(name);
    }

    public String toString()
    {
        return "<UploadFormPartWrapper for " + getRequest() + ">";
    }
}