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

package org.apache.tapestry.param;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;

/**
 *  Implements {@link IParameterConnector} for object parameters.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.3
 *
 **/

public class ObjectParameterConnector extends AbstractParameterConnector
{
    private Class _requiredType;

    protected ObjectParameterConnector(
        IComponent component,
        String parameterName,
        IBinding binding,
        Class requiredType)
    {
        super(component, parameterName, binding);

        _requiredType = requiredType;
    }

    /**
     *  Sets the parameter property to null.
     * 
     **/

    public void setParameter(IRequestCycle cycle)
    {
        if (shouldSetPropertyValue(cycle))
            setPropertyValue(getBindingValue(_requiredType));
    }
}