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

package org.apache.tapestry.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.impl.BaseLocatable;

/**
 * 
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class ServletDescriptor extends BaseLocatable
{
    private String _name;
    private String _className;
    private Map _inits = new HashMap();

    public String getClassName()
    {
        return _className;
    }

    public String getName()
    {
        return _name;
    }

    public void setClassName(String string)
    {
        _className = string;
    }

    public void setName(String string)
    {
        _name = string;
    }

    public void addInitParameter(String name, String value)
    {
        _inits.put(name, value);
    }

    public String getInitParameter(String name)
    {
        return (String) _inits.get(name);
    }

    public Map getInitParameters()
    {
        return _inits;
    }
}
