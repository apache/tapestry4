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

package org.apache.tapestry.pageload;

import java.util.Iterator;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Used to defer connection of inherited informal bindings.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
class QueuedInheritInformalBindings implements IQueuedInheritedBinding
{
    private IComponent _component;

    QueuedInheritInformalBindings(IComponent component)
    {
        _component = component;
    }

    public void connect()
    {

        IComponent container = _component.getContainer();

        for (Iterator it = container.getBindingNames().iterator(); it.hasNext();)
        {
            String bindingName = (String) it.next();
            connectInformalBinding(container, _component, bindingName);
        }
    }

    private void connectInformalBinding(
        IComponent container,
        IComponent component,
        String bindingName)
    {
        IComponentSpecification componentSpec = component.getSpecification();
        IComponentSpecification containerSpec = container.getSpecification();

        // check if binding already exists in the component
        if (component.getBinding(bindingName) != null)
            return;

        // check if parameter is informal for the component
        if (componentSpec.getParameter(bindingName) != null
            || componentSpec.isReservedParameterName(bindingName))
            return;

        // check if parameter is informal for the container
        if (containerSpec.getParameter(bindingName) != null
            || containerSpec.isReservedParameterName(bindingName))
            return;

        // if everything passes, establish binding
        IBinding binding = container.getBinding(bindingName);
        component.setBinding(bindingName, binding);
    }
}