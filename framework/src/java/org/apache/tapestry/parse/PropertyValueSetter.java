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

package org.apache.tapestry.parse;

import org.apache.tapestry.util.IPropertyHolder;

/**
 * Used to hold a property name and property value, and an
 * {@link org.apache.tapestry.util.IPropertyHolder}, during parsing.
 *
 * @author Howard Lewis Ship
 */

class PropertyValueSetter
{
    private IPropertyHolder _holder;
    private String _propertyName;
    private String _propertyValue;

    PropertyValueSetter(IPropertyHolder holder, String propertyName, String propertyValue)
    {
        _holder = holder;
        _propertyName = propertyName;
        _propertyValue = propertyValue;
    }

    public void applyValue(String value)
    {
        _holder.setProperty(_propertyName, value);
    }

    public String getPropertyValue()
    {
        return _propertyValue;
    }

}
