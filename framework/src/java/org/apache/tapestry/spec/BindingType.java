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

import org.apache.commons.lang.enum.Enum;

/**
 * Defines the different types of bindings possible for a component. These are used in the
 * {@link IBindingSpecification} and ultimately used to create an instance of
 * {@link org.apache.tapestry.IBinding}.
 * 
 * @author Howard Lewis Ship
 */

public final class BindingType extends Enum
{
    /**
     * Indicates that the binding value is a prefixed locator, ready for use
     * with {@link org.apache.tapestry.services.BindingSource}.
     */

    public static final BindingType PREFIXED = new BindingType("PREFIXED");

    public static final BindingType INHERITED = new BindingType("INHERITED");

    /**
     * Indicates a {@link org.apache.tapestry.binding.ListenerBinding}, a specialized kind of
     * binding that encapsulates a component listener as a script. Uses a subclass of
     * {@link BindingSpecification},{@link ListenerBindingSpecification}.
     * {@link IListenerBindingSpecification}.
     * 
     * @since 3.0
     */

    public static final BindingType LISTENER = new BindingType("LISTENER");

    private BindingType(String name)
    {
        super(name);
    }

}