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

import org.xml.sax.Attributes;

/**
 *  Used to initialize a property of an object on the top of the digester stack.
 *  This should come after the {@link org.apache.commons.digester.ObjectCreateRule}
 *  (or variation) and before and property setting for the object.  Remember
 *  that rules order matters with the digester.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class InitializePropertyRule extends AbstractSpecificationRule
{
    private String _propertyName;
    private Object _value;

    public InitializePropertyRule(String propertyName, Object value)
    {
        _propertyName = propertyName;
        _value = value;
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception
    {
        setProperty(_propertyName, _value);
    }
}
