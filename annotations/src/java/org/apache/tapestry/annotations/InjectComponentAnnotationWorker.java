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
import org.apache.tapestry.enhance.InjectComponentWorker;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Injects a reference to a compent.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.annotations.InjectComponent
 * @see org.apache.tapestry.enhance.InjectComponentWorker
 */
public class InjectComponentAnnotationWorker implements MethodAnnotationEnhancementWorker
{
    final InjectComponentWorker _delegate;

    InjectComponentAnnotationWorker(InjectComponentWorker delegate)
    {
        _delegate = delegate;
    }

    public InjectComponentAnnotationWorker()
    {
        this(new InjectComponentWorker());
    }

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method, Location location)
    {
        InjectComponent ic = method.getAnnotation(InjectComponent.class);
        String propertyName = AnnotationUtils.getPropertyName(method);

        _delegate.injectComponent(op, ic.value(), propertyName);
    }

}
