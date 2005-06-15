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

import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.enhance.InjectAssetWorker;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Injects an asset.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.annotations.InjectAsset
 * @see org.apache.tapestry.enhance.InjectAssetWorker
 */
public class InjectAssetAnnotationWorker implements MethodAnnotationEnhancementWorker
{
    InjectAssetWorker _delegate;

    InjectAssetAnnotationWorker(InjectAssetWorker delegate)
    {
        _delegate = delegate;
    }

    public InjectAssetAnnotationWorker()
    {
        this(new InjectAssetWorker());
    }

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method)
    {
        InjectAsset as = method.getAnnotation(InjectAsset.class);
        
        String propertyName = AnnotationUtils.getPropertyName(method);

        _delegate.injectAsset(op, as.value(), propertyName);
    }

}
