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

package org.apache.tapestry.services.impl;

import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.web.InitializationParameterHolder;

/**
 * Searches for property values inside objects that implement
 * {@link org.apache.tapestry.web.InitializationParameterHolder}.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class InitializationParameterHolderPropertySource implements IPropertySource
{
    private InitializationParameterHolder _holder;

    public String getPropertyValue(String propertyName)
    {
        return _holder.getInitParameterValue(propertyName);
    }

    public void setHolder(InitializationParameterHolder holder)
    {
        _holder = holder;
    }

}