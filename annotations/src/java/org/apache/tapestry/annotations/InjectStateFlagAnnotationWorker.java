// Copyright 2005, 2006 The Apache Software Foundation
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

import org.apache.hivemind.Location;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.InjectSpecification;
import org.apache.tapestry.spec.InjectSpecificationImpl;

/**
 * Converts an {@link org.apache.tapestry.annotations.InjectStateFlag}
 * annotation into a {@link org.apache.tapestry.spec.InjectSpecification} (of
 * type "state-flag") attached to the component specification.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InjectStateFlagAnnotationWorker implements
        MethodAnnotationEnhancementWorker {

    public void performEnhancement(EnhancementOperation op,
            IComponentSpecification spec, Method method, Location location)
    {
        InjectStateFlag isv = method.getAnnotation(InjectStateFlag.class);

        String propertyName = AnnotationUtils.getPropertyName(method);

        InjectSpecification inject = new InjectSpecificationImpl();

        inject.setType("state-flag");
        inject.setProperty(propertyName);
        inject.setObject(isv.value());
        inject.setLocation(location);

        spec.addInjectSpecification(inject);
    }

}
