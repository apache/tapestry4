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

package org.apache.tapestry.annotations;

import java.lang.reflect.Method;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.BindingSpecification;
import org.apache.tapestry.spec.BindingType;
import org.apache.tapestry.spec.ContainedComponent;
import org.apache.tapestry.spec.IBindingSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;

/**
 * Adds a {@link org.apache.tapestry.spec.IContainedComponent} to the
 * {@link org.apache.tapestry.spec.IComponentSpecification}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.annotations.Component
 * @see org.apache.tapestry.annotations.Binding
 */
public class ComponentAnnotationWorker implements MethodAnnotationEnhancementWorker
{

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method)
    {
        Component component = method.getAnnotation(Component.class);

        String propertyName = AnnotationUtils.getPropertyName(method);

        IContainedComponent cc = new ContainedComponent();

        cc.setInheritInformalParameters(component.inheritInformalParameters());
        cc.setType(component.type());
        cc.setPropertyName(propertyName);

        String[] bindings = component.bindings();

        if (bindings.length % 2 != 0)
            throw new ApplicationRuntimeException(AnnotationMessages.oddBindings(bindings.length));

        for (int i = 0; i < bindings.length; i += 2)
        {
            String name = bindings[i];
            String value = bindings[i + 1];

            IBindingSpecification bs = new BindingSpecification();
            bs.setType(BindingType.PREFIXED);
            bs.setValue(value);

            cc.setBinding(name, bs);
        }

        String id = component.id();

        if (id.equals(""))
            id = propertyName;

        spec.addComponent(id, cc);
    }
}