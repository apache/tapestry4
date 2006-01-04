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

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IPropertySpecification;
import org.apache.tapestry.spec.PropertySpecification;

/**
 * Looks for {@link org.apache.tapestry.annotations.InitialValue} annotations on methods that don't
 * have a {@link org.apache.tapestry.annotations.Persist} annotation (that's handled by
 * {@link org.apache.tapestry.annotations.PersistAnnotationWorker}); adds an
 * {@link org.apache.tapestry.spec.IPropertySpecification} for the property, so that its initial
 * value may be set.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InitialValueAnnotationWorker implements SecondaryAnnotationWorker
{
    /**
     * Returns true if the method has the InitialValue annotation.
     */
    public boolean canEnhance(Method method)
    {
        return method.getAnnotation(InitialValue.class) != null;
    }

    public void peformEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method, Resource classResource)
    {
        InitialValue iv = method.getAnnotation(InitialValue.class);

        if (iv == null)
            return;

        if (method.getAnnotation(Persist.class) != null)
            return;

        Location location = AnnotationUtils.buildLocationForAnnotation(method, iv, classResource);

        String propertyName = AnnotationUtils.getPropertyName(method);

        // Define a transient property

        IPropertySpecification pspec = new PropertySpecification();

        pspec.setName(propertyName);
        pspec.setLocation(location);
        pspec.setInitialValue(iv.value());

        spec.addPropertySpecification(pspec);
    }
}
