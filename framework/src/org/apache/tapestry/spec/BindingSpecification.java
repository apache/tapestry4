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

/**
 *  Stores a binding specification, which identifies the static value
 *  or OGNL expression for the binding.  The name of the binding (which
 *  matches a bindable property of the contined component) is implicitly known.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class BindingSpecification extends BaseLocatable implements IBindingSpecification
{
    private BindingType _type;
    private String _value;
	
    public BindingType getType()
    {
        return _type;
    }

    public String getValue()
    {
        return _value;
    }

    public void setType(BindingType type)
    {
        _type = type;
    }

    public void setValue(String value)
    {
        _value = value;
    }
}