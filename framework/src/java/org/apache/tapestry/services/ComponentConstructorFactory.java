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

package org.apache.tapestry.services;

import org.apache.tapestry.spec.IComponentSpecification;

/**
 * <code>tapestry.enhance.ComponentConstructorFactory</code> service that acts as a wrapper around
 * {@link org.apache.tapestry.enhance.EnhancementOperation}, used to take a base component class
 * and provide an enhanced subclass of it.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface ComponentConstructorFactory
{
    /**
     * Passed a component specification and the base component class name, provides back an object
     * used to instantiate instances of the component. {@link ComponentConstructor}s are internally
     * cached, repeated calls with the same specification object will yield the same result.
     * 
     * @param specification
     *            the page or component specification which directs the enhancement operation
     * @param className
     *            the name of the base component class (in some cases,
     *            {@link IComponentSpecification#getComponentClassName() is null andother code
     *            provides the default)
     * @returns a constructor used to create instances of the enhanced component class
     */

    public ComponentConstructor getComponentConstructor(IComponentSpecification specification,
            String className);
}