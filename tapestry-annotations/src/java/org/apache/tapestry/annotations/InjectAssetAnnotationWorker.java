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
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.enhance.EnhancementWorker;
import org.apache.tapestry.enhance.InjectAssetWorker;
import org.apache.tapestry.spec.IAssetSpecification;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Injects an asset.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.annotations.InjectAsset
 * @see org.apache.tapestry.enhance.InjectAssetWorker
 */
public class InjectAssetAnnotationWorker implements EnhancementWorker
{
    InjectAssetWorker _delegate;
    
    private ClassResolver _classResolver;
    
    InjectAssetAnnotationWorker(InjectAssetWorker delegate)
    {
        _delegate = delegate;
    }

    public InjectAssetAnnotationWorker()
    {
        this(new InjectAssetWorker());
    }
    
    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec)
    {
        Class clazz = op.getBaseClass();
        
        Resource classResource = newClassResource(clazz);
        
        for (Method m : clazz.getMethods())
        {
            if (m.getAnnotation(InjectAsset.class) != null) {
                
                performEnhancement(op, spec, m, 
                        AnnotationUtils.buildLocationForAnnotation(
                        m,
                        m.getAnnotation(InjectAsset.class),
                        classResource));
            }
        }
    }
    
    private ClasspathResource newClassResource(Class clazz)
    {
        return new ClasspathResource(_classResolver, clazz.getName().replace('.', '/'));
    }
    
    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec, Method method, Location location)
    {
        InjectAsset as = method.getAnnotation(InjectAsset.class);
        
        IAssetSpecification asset = spec.getAsset(as.value());
        if (asset == null) {
            
            throw new ApplicationRuntimeException(AnnotationMessages.unknownAsset(as.value(), location));
        }
        
        String propertyName = AnnotationUtils.getPropertyName(method);
        
        _delegate.injectAsset(op, as.value(), propertyName, location);
    }

    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }
}
