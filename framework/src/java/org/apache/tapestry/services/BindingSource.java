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

package org.apache.tapestry.services;

import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;

/**
 * Used to convert a binding string (from a template or a specification) into an instance of
 * {@link IBinding}.
 * 
 * @since 3.1
 */
public interface BindingSource
{
    /**
     * Creates a new binding. The locator is used to identify the <em>type</em> of binding to
     * create as well as configure the binding instance. 
     * The locator is either a literal value (resulting in a
     * {@link org.apache.tapestry.binding.LiteralBinding literal binding}) or consists of prefix and
     * a path, i.e., <code>ognl:myProperty</code>.
     * <p>
     * When a prefix exists and is identified, it is used to select the correct
     * {@link BindingFactory}, and the remainder of the path (i.e., <code>myProperty</code)
     * is passed to the factory.  An unrecognized prefix is treated as a literal value
     * (it is often "javascript:" or "http:", etc.).
     * 
     * @param component the component for which the binding is created; the component is used
     * as a kind of context for certain types of bindings (for example, the root object when
     * evaluating OGNL expressions).
     * @param description {@link IBinding#getDescription() description} for the new binding
     * @param locator the binding to be created, possibly including a prefix to define the type
     * @param location location used to report errors in the binding
     */
    public IBinding createBinding(IComponent component, String description, String locator, Location location);
}