// Copyright 2004, 2005 The Apache Software Foundation
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
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.enhance.EnhanceUtils;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.enhance.EnhancementWorker;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IPropertySpecification;


/**
 * Performs runtime checks on persistent properties to ensure that objects being
 * managed by competing bytecode enhancement libraries (such as Hibernate) aren't
 * proxied.
 */
public class ComponentPropertyProxyWorker implements EnhancementWorker
{
    
    private List<String> _excludedPackages;
    
    /**
     * {@inheritDoc}
     */
    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec)
    {
        Iterator i = spec.getPropertySpecificationNames().iterator();

        while(i.hasNext())
        {
            String name = (String) i.next();
            IPropertySpecification ps = spec.getPropertySpecification(name);

            checkProxy(op, ps);
        }
    }

    void checkProxy(EnhancementOperation op, IPropertySpecification ps)
    {
        ps.setProxyChecked(true);
        
        if (!ps.isPersistent())
            return;
        
        Class propertyType = EnhanceUtils.extractPropertyType(op, ps.getName(), ps.getType());
        if (propertyType == null)
            return;
        
        if (!EnhanceUtils.canProxyPropertyType(propertyType))
            return;
        
        Annotation[] annotations = propertyType.getAnnotations();
        
        for (int i = 0; i < annotations.length; i++) {
            if (isExcluded(annotations[i]))
                return;
        }
        
        ps.setCanProxy(true);
    }
    
    boolean isExcluded(Annotation annotation)
    {
        for (String match : _excludedPackages) {
            
            if (annotation.annotationType().getName().indexOf(match) > -1)
                return true;
        }
        
        return false;
    }
    
    public void setExcludedPackages(List packages)
    {
        _excludedPackages = packages;
    }
}
