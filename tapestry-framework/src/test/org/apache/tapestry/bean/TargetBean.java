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

package org.apache.tapestry.bean;

/**
 * Used by {@link org.apache.tapestry.bean.TestLightweightBeanInitializer} to verify that
 * initialization does occur.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TargetBean
{
    private boolean _required;

    private int _minLength;

    public int getMinLength()
    {
        return _minLength;
    }

    public void setMinLength(int minLength)
    {
        _minLength = minLength;
    }

    public boolean isRequired()
    {
        return _required;
    }

    public void setRequired(boolean required)
    {
        _required = required;
    }
}
