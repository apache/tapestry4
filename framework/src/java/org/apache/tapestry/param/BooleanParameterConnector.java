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

package org.apache.tapestry.param;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;

/**
 *   Connector for boolean parameters.
 * 
 *   @see IBinding#getBoolean()
 * 
 *   @author Howard Lewis Ship
 *   @since 2.0.3
 * 
 **/

public class BooleanParameterConnector extends AbstractParameterConnector
{

    protected BooleanParameterConnector(IComponent component, String parameterName, IBinding binding)
    {
        super(component, parameterName, binding);
    }

    /**
     *  Invokes {@link IBinding#getBoolean()}, which always
     *  returns true or false (there is no concept of a null
     *  value).
     * 
     **/

    public void setParameter(IRequestCycle cycle)
    {
        if (shouldSetPropertyValue(cycle))
        {
            boolean value = getBinding().getBoolean();

            setPropertyValue(value ? Boolean.TRUE : Boolean.FALSE);
        }
    }

}
