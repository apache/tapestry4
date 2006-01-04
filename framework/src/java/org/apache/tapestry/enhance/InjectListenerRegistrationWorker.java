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

package org.apache.tapestry.enhance;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Checks to see if the component implements a specific interface, and adds code to the component's
 * finishLoad() method to register (with the page) for such notifications.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InjectListenerRegistrationWorker implements EnhancementWorker
{
    private Class _listenerInterface;

    private String _registerMethodName;

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec)
    {

        if (op.implementsInterface(_listenerInterface))
        {
            op.extendMethodImplementation(
                    IComponent.class,
                    EnhanceUtils.FINISH_LOAD_SIGNATURE,
                    "getPage()." + _registerMethodName + "(this);");
        }
    }

    public void setListenerInterface(Class listenerInterface)
    {
        _listenerInterface = listenerInterface;
    }

    public void setRegisterMethodName(String registerMethodName)
    {
        _registerMethodName = registerMethodName;
    }
}