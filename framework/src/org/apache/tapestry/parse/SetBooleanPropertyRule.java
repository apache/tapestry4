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
 *  Sets a boolean property from an attribute of the current element.
 *  The value must be either "yes" or "no" (or not present).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/
public class SetBooleanPropertyRule extends AbstractSpecificationRule
{
    private String _attributeName;
    private String _propertyName;

    public SetBooleanPropertyRule(String attributeName, String propertyName)
    {
        _attributeName = attributeName;
        _propertyName = propertyName;
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception
    {
        String attributeValue = getValue(attributes, _attributeName);

        if (attributeValue == null)
            return;

        Boolean propertyValue = attributeValue.equals("yes") ? Boolean.TRUE : Boolean.FALSE;

        setProperty(_propertyName, propertyValue);
    }

}
