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
import org.apache.tapestry.spec.IParameterSpecification;

/**
 * Used to convert a binding string (from a template or a specification) into an instance of
 * {@link IBinding}.
 * 
 * @since 4.0
 */
public interface BindingSource
{
    /**
     * Creates a new binding. The locator is used to identify the <em>type</em> of binding to
     * create as well as configure the binding instance. The locator is either a literal value
     * (resulting in a {@link org.apache.tapestry.binding.LiteralBinding literal binding}) or
     * consists of prefix and a path, i.e., <code>ognl:myProperty</code>.
     * <p>
     * When a prefix exists and is identified, it is used to select the correct
     * {@link BindingFactory}, and the remainder of the path (i.e., <code>myProperty</code>)
     * is passed to the factory.  An unrecognized prefix is treated as a literal value
     * (it is often "javascript:" or "http:", etc.).
     * 
     * @param component
     *          The component for which the binding is created; the component is used
     *          as a kind of context for certain types of bindings (for example, the root object when
     *          evaluating OGNL expressions).
     * @param description
                {@link IBinding#getDescription() description} for the new binding
     * @param reference
     *          The binding reference used to create the binding, possibly including a prefix to define the type.
     *          If the reference does not include a prefix, then  the defaultBindingType is used as the prefix
     * @param defaultBindingType
     *          Binding type to use when no prefix is provided in the reference
     *          or doesn't match a known binding factory.
     * @param location
     *          Location used to report errors in the binding.
     *
     * @return A binding instance.
     */
    IBinding createBinding(IComponent component, String description, String reference,
            String defaultBindingType, Location location);

    /**
     * Just like {@link #createBinding(org.apache.tapestry.IComponent, String, String, String, org.apache.hivemind.Location)} - except
     * this version takes an additional parameter of type {@link IParameterSpecification} for those bindings that have a matching
     * parameter.
     * 
     * @param component
     *          The component for which the binding is created; the component is used
     *          as a kind of context for certain types of bindings (for example, the root object when
     *          evaluating OGNL expressions).
     * @param parameter
     *          Optional argument of the parameter this binding is being bound for - may be null.
     * @param description
                {@link IBinding#getDescription() description} for the new binding
     * @param reference
     *          The binding reference used to create the binding, possibly including a prefix to define the type.
     *          If the reference does not include a prefix, then  the defaultBindingType is used as the prefix
     * @param defaultBindingType
     *          Binding type to use when no prefix is provided in the reference
     *          or doesn't match a known binding factory.
     * @param location
     *          Location used to report errors in the binding.
     *
     * @return A binding instance.
     */
    IBinding createBinding(IComponent component, IParameterSpecification parameter, String description, String reference,
            String defaultBindingType, Location location);
}
