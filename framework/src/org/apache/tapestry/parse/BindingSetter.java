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

package org.apache.tapestry.parse;

import org.apache.hivemind.impl.BaseLocatable;
import org.apache.tapestry.spec.IBindingSpecification;
import org.apache.tapestry.spec.IContainedComponent;

/**
 * Carries name and value information while the body of
 * a &lt;binding&gt; or &lt;static-binding&gt; is being parsed.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 */
class BindingSetter extends BaseLocatable
{
    private IContainedComponent _component;
    private String _name;
    private String _value;

    BindingSetter(IContainedComponent component, String name, String value)
    {
        _component = component;
        _name = name;
        _value = value;
    }

    void apply(IBindingSpecification spec)
    {
    	spec.setLocation(getLocation());
        _component.setBinding(_name, spec);
    }

    public String getValue()
    {
        return _value;
    }
}
