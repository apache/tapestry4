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

package org.apache.tapestry.services.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.service.ClassFactory;
import org.apache.tapestry.enhance.EnhancedClassValidator;
import org.apache.tapestry.enhance.EnhancementOperationImpl;
import org.apache.tapestry.enhance.EnhancementWorker;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.services.ComponentConstructor;
import org.apache.tapestry.services.ComponentConstructorFactory;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Implementation of the {@link org.apache.tapestry.services.ComponentConstructorFactory}service
 * interface.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ComponentConstructorFactoryImpl implements ComponentConstructorFactory,
        ResetEventListener
{
    private ClassFactory _classFactory;

    private ClassResolver _classResolver;

    private EnhancedClassValidator _validator;

    private EnhancementWorker _chain;

    /**
     * Map of {@link org.apache.tapestry.services.ComponentConstructor}keyed on
     * {@link org.apache.tapestry.spec.IComponentSpecification}.
     */

    private Map _cachedConstructors = Collections.synchronizedMap(new HashMap());

    public void resetEventDidOccur()
    {
        _cachedConstructors.clear();
    }

    public ComponentConstructor getComponentConstructor(IComponentSpecification specification,
            String className)
    {
        ComponentConstructor result = (ComponentConstructor) _cachedConstructors.get(specification);

        if (result == null)
        {
            Class baseClass = _classResolver.findClass(className);

            EnhancementOperationImpl eo = new EnhancementOperationImpl(_classResolver,
                    specification, baseClass, _classFactory);

            // Invoking on the chain is the same as invoking on every
            // object in the chain (because method performEnhancement() is type void).

            _chain.performEnhancement(eo, specification);

            result = eo.getConstructor();

            // TODO: This should be optional to work around that IBM JVM bug.
            // Also, to some degree, it should be passed into EnhancementOperationImpl,
            // as it generally only needs to be done if a enhanced class
            // is fabricated.

            _validator.validate(baseClass, result.getComponentClass(), specification);

            _cachedConstructors.put(specification, result);
        }

        return result;
    }

    public void setClassFactory(ClassFactory classFactory)
    {
        _classFactory = classFactory;
    }

    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }

    public void setValidator(EnhancedClassValidator validator)
    {
        _validator = validator;
    }

    public void setChain(EnhancementWorker chain)
    {
        _chain = chain;
    }
}