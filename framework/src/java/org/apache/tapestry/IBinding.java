// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry;

import org.apache.hivemind.Locatable;

/**
 * A binding is the mechanism used to provide values for parameters of specific {@link IComponent}
 * instances. The component doesn't care where the required value comes from, it simply requires
 * that a value be provided when needed.
 * <p>
 * Bindings are set inside the containing component's specification or template. Bindings may be
 * invariant or dynamic (though that is irrelevant to the component). Components may also use a
 * binding to write a value back through a property to some other object (typically, another
 * component).
 * 
 * @author Howard Lewis Ship
 */

public interface IBinding extends Locatable
{
    /**
     * Returns the value of this binding. This is the essential method.
     */

    public Object getObject();

    /**
     * Returns the value for the binding after performing some basic checks.
     * <p>
     * Note: In release 3.1, the parameterName parameter was removed.
     * 
     * @param type
     *            if not null, the value must be assignable to the specific class
     * @throws BindingException
     *             if the value is not assignable to the specified type
     * @since 0.2.9
     */

    public Object getObject(Class type);

    /**
     * Returns true if the value is invariant (not changing; the same value returned each time).
     * Static and message bindings are always invariant, and
     * {@link org.apache.tapestry.binding.ExpressionBinding}s may be marked invariant (as an
     * optimization).
     * 
     * @since 2.0.3
     */

    public boolean isInvariant();

    /**
     * Updates the value of the binding, if possible.
     * 
     * @exception BindingException
     *                If the binding is read only.
     */

    public void setObject(Object value);

    /**
     * Returns the name of the component parameter for this binding.
     * 
     * @since 3.1
     */
    
    public String getParameterName();
}