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

package org.apache.tapestry.spec;

import org.apache.hivemind.impl.BaseLocatable;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InjectSpecificationImpl extends BaseLocatable implements InjectSpecification
{
    private String _property;

    private String _object;

    private String _type;

    public String getObject()
    {
        return _object;
    }

    public void setObject(String object)
    {
        _object = object;
    }

    public String getProperty()
    {
        return _property;
    }

    public void setProperty(String name)
    {
        _property = name;
    }

    public String getType()
    {
        return _type;
    }

    public void setType(String type)
    {
        _type = type;
    }
}