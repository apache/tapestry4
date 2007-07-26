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
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.InjectSpecification;
import org.apache.tapestry.spec.InjectSpecificationImpl;

/**
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class InjectScriptAnnotationWorker implements MethodAnnotationEnhancementWorker
{

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method, Location location)
    {
        InjectScript annotation = method.getAnnotation(InjectScript.class);

        String propertyName = AnnotationUtils.getPropertyName(method);

        InjectSpecification is = new InjectSpecificationImpl();

        is.setProperty(propertyName);
        is.setType("script");
        is.setObject(annotation.value());
        is.setLocation(location);

        spec.addInjectSpecification(is);
    }

}
