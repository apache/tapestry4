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
import org.apache.hivemind.HiveMind;
import org.apache.hivemind.Location;
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
 * @see Component
 */
public class ComponentAnnotationWorker implements MethodAnnotationEnhancementWorker
{

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method, Location location)
    {
        Component component = method.getAnnotation(Component.class);

        String propertyName = AnnotationUtils.getPropertyName(method);
        String type = component.type();
        String copyOf = component.copyOf();
        boolean hasCopyOf = HiveMind.isNonBlank(copyOf);
        
        if (hasCopyOf)
        {
            if (HiveMind.isNonBlank(type))
                throw new ApplicationRuntimeException(AnnotationMessages.bothTypeAndCopyOf(propertyName));
            type = null;
        }
        else
        {
            if (type.equals(""))
            {
                Class retTypeClazz = method.getReturnType();
                type = resolveComponentType(retTypeClazz);
            }
            copyOf = null;
        }
        
        IContainedComponent cc = new ContainedComponent();

        cc.setInheritInformalParameters(component.inheritInformalParameters());
        cc.setType(type);
        cc.setCopyOf(copyOf);
        cc.setPropertyName(propertyName);
        cc.setLocation(location);

        for (String binding : component.bindings())
        {
            addBinding(cc, binding, location);
        }

        for (String binding : component.inheritedBindings())
        {
            addInheritedBinding(cc, binding, location);
        }

        String id = component.id();

        if (id.equals(""))
            id = propertyName;
        
        spec.addComponent(id, cc);
        
        if (hasCopyOf)
        {
            IContainedComponent source = spec.getComponent(copyOf);
            if (source != null)                
                AnnotationUtils.copyBindings(source, cc);
        }
    }
    
    protected String resolveComponentType(Class retTypeClass)
    {
        return retTypeClass.getSimpleName();
    }

    void addBinding(IContainedComponent component, String binding, Location location)
    {
        int equalsx = binding.indexOf('=');

        if (equalsx < 1)
            invalidBinding(binding);

        if (equalsx + 1 >= binding.length())
            invalidBinding(binding);

        String name = binding.substring(0, equalsx).trim();
        String value = binding.substring(equalsx + 1).trim();

        IBindingSpecification bs = new BindingSpecification();
        bs.setType(BindingType.PREFIXED);
        bs.setValue(value);
        bs.setLocation(location);

        component.setBinding(name, bs);
    }

    /**
     * @since 4.1.4
     */
    void addInheritedBinding(IContainedComponent component, String binding, Location location)
    {
        int equalsx = binding.indexOf('=');
        String name;
        String containerName;

        if (equalsx < 0)
        {
            name = binding.trim();
            containerName = name;
        }
        else
        {
            name = binding.substring(0, equalsx).trim();
            containerName = binding.substring(equalsx + 1).trim();
        }

        IBindingSpecification bs = new BindingSpecification();
        bs.setType(BindingType.INHERITED);
        bs.setValue(containerName);
        bs.setLocation(location);

        component.setBinding(name, bs);
    }

    protected void invalidBinding(String binding)
    {
        throw new ApplicationRuntimeException(AnnotationMessages.bindingWrongFormat(binding));
    }
}
