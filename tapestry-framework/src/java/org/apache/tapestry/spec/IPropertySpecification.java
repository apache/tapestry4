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
 * Defines a transient or persistant property of a component or page.
 * 
 * @author glongman@intelligentworks.com
 */
public interface IPropertySpecification extends LocationHolder
{

    /**
     * Returns the initial value for this property, as a binding reference. May
     * return null if the property has no initial value. The initial value is
     * from finishLoad() and re-applied in pageDetached().
     */

    String getInitialValue();

    String getName();

    /**
     * Returns true if {@link #getPersistence()}is null.
     */
    boolean isPersistent();

    String getType();

    void setInitialValue(String initialValue);

    /**
     * Sets the name of the property. This should not be changed once this
     * IPropertySpecification is added to a
     * {@link org.apache.tapestry.spec.IComponentSpecification}.
     */
    void setName(String name);

    void setType(String type);
    
    /**
     * Checks if this property has previously had it's type information examined to
     * determine if it is elligable for proxying. Meaning {@link #canProxy()} should
     * be a real value.
     * 
     * @return True if the proxy type has been checked, false otherwise.
     */
    boolean isProxyChecked();
    
    /**
     * Sets the state of this property so that it is known whether or not the type
     * it represents has been checked as being compatible with proxying or not.
     * 
     * @param checked
     */
    void setProxyChecked(boolean checked);
    
    /**
     * Checks if this parameter can be proxied. 
     * 
     * @return True if the type can be proxied, false otherwise.
     */
    boolean canProxy();
    
    /**
     * Sets whether or not this property can be proxied.
     * 
     * @param canProxy
     */
    void setCanProxy(boolean canProxy);
    
    /**
     * A string indicating how the property is persisted.
     * 
     * @since 4.0
     */

    void setPersistence(String persistence);

    /**
     * If null, then the property is not persistent.
     * 
     * @since 4.0
     */
    String getPersistence();

}
