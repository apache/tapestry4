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

package org.apache.tapestry.spec;

import org.apache.hivemind.impl.BaseLocatable;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class InjectStateSpecificationImpl extends BaseLocatable implements InjectStateSpecification
{
    private String _property;

    private String _objectName;

    public String getProperty()
    {
        return _property;
    }

    public void setProperty(String property)
    {
        _property = property;
    }

    public String getObjectName()
    {
        return _objectName;
    }

    public void setObjectName(String objectName)
    {
        _objectName = objectName;
    }

}