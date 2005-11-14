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

package org.apache.tapestry.binding;

import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;

/**
 * Creates a new instance of {@link org.apache.tapestry.IBinding}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public interface BindingFactory
{
    /**
     * Creates a new binding instance.
     * 
     * @param root
     *            the component that is the source of properties or messages (or etc.). When the
     *            path is "evaluated", the root component provides a context.
     * @param description
     *            The {@link IBinding#getDescription() description}of the binding.
     * @param expression
     *            The expression used to get (or update) a value. The interpretation of this
     *            expression is determined by the type of {@link IBinding} created by this factory.
     *            In some cases, it is simple the name of an object contained by the root component.
     *            For the common "ognl:" binding prefix, it is an OGNL expression to be evaluated on
     *            the root object.
     * @param location
     *            The location of the binding, used to report any errors related to the binding, or
     *            to the component parameter the binding is bound to.
     */
    public IBinding createBinding(IComponent root, String bindingDescription, String expression,
            Location location);
}