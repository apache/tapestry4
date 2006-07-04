// Copyright 2006 The Apache Software Foundation
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
package org.apache.tapestry.portlet.multipart;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.portlet.ActionRequest;

import org.apache.hivemind.util.Defense;

/**
 * @author Raphael Jean
 */
public class UploadFormPortletParametersWrapper extends ActionRequestWrapper
{

    /**
     * Map of {@link ValuePart}&nbsp;keyed on parameter name.
     */
    private Map _parameterMap;

    public UploadFormPortletParametersWrapper(ActionRequest request,
            Map parameterMap)
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
        return "<UploadFormPortletParametersWrapper for " + getRequest() + ">";
    }

}
