//  Copyright 2004 The Apache Software Foundation
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

import org.apache.tapestry.ILocatable;
import org.apache.tapestry.ILocationHolder;

/**
 *  Stores a binding specification, which identifies the static value
 *  or OGNL expression for the binding.  The name of the binding (which
 *  matches a bindable property of the contined component) is implicitly known.
 *
 * @author glongman@intelligentworks.com
 * @version $Id$
 */
public interface IBindingSpecification extends ILocationHolder, ILocatable
{
    public abstract BindingType getType();
    public abstract String getValue();
    public abstract void setType(BindingType type);
    public abstract void setValue(String value);
}