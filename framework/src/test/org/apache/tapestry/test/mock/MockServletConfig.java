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

package org.apache.tapestry.test.mock;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * An implementation of {@link javax.servlet.ServletConfig} used
 * for Mock testing. 
 *
 * @author Howard Lewis Ship
 * @since 4.0
 */

public class MockServletConfig implements ServletConfig, InitParameterHolder
{
    private String _name;
    private ServletContext _context;
    private Map _initParameters = new HashMap();

    public MockServletConfig(String name, ServletContext context)
    {
        _name = name;
        _context = context;
    }

    public String getInitParameter(String name)
    {
        return (String) _initParameters.get(name);
    }

    public Enumeration getInitParameterNames()
    {
        return Collections.enumeration(_initParameters.keySet());
    }

    public ServletContext getServletContext()
    {
        return _context;
    }

    public String getServletName()
    {
        return _name;
    }

    public void setInitParameter(String name, String value)
    {
        _initParameters.put(name, value);
    }

}
