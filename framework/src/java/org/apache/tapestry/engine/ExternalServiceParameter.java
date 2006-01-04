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

import org.apache.hivemind.util.Defense;

/**
 * Parameter for the {@link org.apache.tapestry.engine.ExternalService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ExternalServiceParameter
{
    private String _pageName;

    private Object[] _serviceParameters;

    public ExternalServiceParameter(String pageName)
    {
        this(pageName, null);
    }

    public ExternalServiceParameter(String pageName, Object[] serviceParameters)
    {
        Defense.notNull(pageName, "pageName");

        _pageName = pageName;
        _serviceParameters = serviceParameters;
    }

    public String getPageName()
    {
        return _pageName;
    }

    public Object[] getServiceParameters()
    {
        return _serviceParameters;
    }
}