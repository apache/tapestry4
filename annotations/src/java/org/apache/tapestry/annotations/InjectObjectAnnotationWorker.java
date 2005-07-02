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
import org.apache.tapestry.enhance.InjectObjectWorker;
import org.apache.tapestry.services.InjectedValueProvider;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Performs injection of objects, in much the same way as the &lt;inject&gt; element in a
 * specification.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.enhance.InjectObjectWorker
 * @see org.apache.tapestry.annotations.InjectObject
 */

public class InjectObjectAnnotationWorker implements MethodAnnotationEnhancementWorker
{
    final InjectObjectWorker _delegate;

    public InjectObjectAnnotationWorker()
    {
        this(new InjectObjectWorker());
    }

    InjectObjectAnnotationWorker(InjectObjectWorker delegate)
    {
        _delegate = delegate;
    }

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method, Location location)
    {
        InjectObject io = method.getAnnotation(InjectObject.class);

        String object = io.value();

        String propertyName = AnnotationUtils.getPropertyName(method);

        _delegate.injectObject(op, object, propertyName, null);
    }

    public void setProvider(InjectedValueProvider provider)
    {
        _delegate.setProvider(provider);
    }
}
