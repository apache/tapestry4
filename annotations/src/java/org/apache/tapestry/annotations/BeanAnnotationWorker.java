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
import java.lang.reflect.Method;

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.bean.LightweightBeanInitializer;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.BeanSpecification;
import org.apache.tapestry.spec.IBeanSpecification;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Creates a {@link org.apache.tapestry.spec.IBeanSpecification} from the
 * {@link org.apache.tapestry.annotations.Bean} annotation.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class BeanAnnotationWorker implements MethodAnnotationEnhancementWorker
{
    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Annotation annotation, Method method)
    {
        Bean bean = (Bean) annotation;
        String propertyName = AnnotationUtils.getPropertyName(method);

        Class beanClass = bean.value();
        if (beanClass.equals(Object.class))
            beanClass = op.getPropertyType(propertyName);

        IBeanSpecification bs = new BeanSpecification();

        // A shame to convert it to a string then back to
        // a class later, but ...

        bs.setClassName(beanClass.getName());
        bs.setPropertyName(propertyName);

        // Starting to like enums!

        bs.setLifecycle(bean.lifecycle().getBeanLifecycle());

        String initializer = bean.initializer();

        if (HiveMind.isNonBlank(initializer))
            bs.addInitializer(new LightweightBeanInitializer(initializer));

        spec.addBeanSpecification(propertyName, bs);
    }

}
