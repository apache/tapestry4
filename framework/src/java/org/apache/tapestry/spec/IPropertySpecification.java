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

package org.apache.tapestry.spec;

import org.apache.hivemind.LocationHolder;

/**
 * Defines a transient or persistant property of a component or page. A
 * {@link org.apache.tapestry.enhance.ComponentClassFactory}uses this information to create a
 * subclass with the necessary instance variables and methods.
 * 
 * @author glongman@intelligentworks.com
 */
public interface IPropertySpecification extends LocationHolder
{
    /**
     * Returns the initial value for this property, as a binding reference. May return null if the
     * property has no initial value. The initial value is from finishLoad() and re-applied in
     * pageDetached().
     */

    public String getInitialValue();

    public String getName();

    /**
     * Returns true if {@link #getPersistence()}is null.
     */
    public boolean isPersistent();

    public String getType();

    public void setInitialValue(String initialValue);

    /**
     * Sets the name of the property. This should not be changed once this IPropertySpecification is
     * added to a {@link org.apache.tapestry.spec.IComponentSpecification}.
     */
    public void setName(String name);

    public void setType(String type);

    /**
     * A string indicating how the property is persisted.
     * 
     * @since 4.0
     */

    public void setPersistence(String persistence);

    /**
     * If null, then the property is not persistent.
     * 
     * @since 4.0
     */
    public String getPersistence();

}