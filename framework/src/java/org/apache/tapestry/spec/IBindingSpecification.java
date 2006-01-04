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

import org.apache.hivemind.Locatable;
import org.apache.hivemind.LocationHolder;

/**
 * Stores a binding specification, which identifies the static value or OGNL expression for the
 * binding. The name of the binding (which matches a bindable property of the contined component) is
 * implicitly known.
 * 
 * @author glongman@intelligentworks.com
 */
public interface IBindingSpecification extends LocationHolder, Locatable
{
    /**
     * Returns the type of binding specification; this is generally
     * {@link org.apache.tapestry.spec.BindingType#PREFIXED}.
     */

    public BindingType getType();

    /**
     * For a prefixed binding specification (the typical type), the value is a binding reference; a
     * string used to contruct the actual binding, and consists of a prefix (such as "ognl:" or
     * "message:") and a locator. The prefix selects a
     * {@link org.apache.tapestry.binding.BindingFactory}, and the locator is passed to the
     * factory, which uses it to construct the {@link org.apache.tapestry.IBinding}instance.
     */

    public String getValue();

    public void setType(BindingType type);

    public void setValue(String value);
}