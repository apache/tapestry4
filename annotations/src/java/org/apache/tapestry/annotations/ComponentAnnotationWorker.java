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

import java.lang.annotation.Annotation;

import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Sets properties of the {@link org.apache.tapestry.spec.IComponentSpecification} based on the
 * {@link org.apache.tapestry.annotations.Component} annotation. In addition, marks the component as
 * deprecated if the {@link java.lang.Deprecated} annotation is present on the class.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class ComponentAnnotationWorker implements ClassAnnotationEnhancementWorker
{

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Annotation annotation)
    {
        Component component = (Component) annotation;

        spec.setAllowBody(component.allowBody());
        spec.setAllowInformalParameters(component.allowInformalParameters());

        String[] names = TapestryUtils.split(component.reservedParameters());
        for (String name : names)
            spec.addReservedParameterName(name);

        if (op.getBaseClass().isAnnotationPresent(Deprecated.class))
            spec.setDeprecated(true);
    }

}
