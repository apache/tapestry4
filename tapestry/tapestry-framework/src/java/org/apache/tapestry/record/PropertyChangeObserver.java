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
package org.apache.tapestry.record;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.Tapestry;


/**
 * Core service that is consulted anytime a specified component property is set. This service
 * is intended to be used as a means of intercepting component property state objects so that 
 * their individual properties can be observed independently from basic set/get operations 
 * done on page/component properties.
 * 
 */
public interface PropertyChangeObserver
{

    /**
     * Invoked by the enhanced property workers any time a {@link IComponent} property is set, either
     * by an initial value binding or explicitly through an abstract setter.
     * 
     * @param component
     *          The component that this property is attached to.
     * @param property 
     *          The object to observe changes on, may be null.
     * @param propertyName
     *          The name of the property being observed - needed for doing things like calling
     *          {@link Tapestry#fireObservedChange(IComponent, String, Object)}.
     * @return Expected to return either the same exact instance passed in or one that is proxied but still
     *         maintains the original state of the object.
     */
    Object observePropertyChanges(IComponent component, Object property, String propertyName);
}
