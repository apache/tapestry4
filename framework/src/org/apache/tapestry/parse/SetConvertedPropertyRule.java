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

import java.util.Map;

import org.xml.sax.Attributes;

/**
 *  Rule that applies a conversion of a string value from an attribute into
 *  an object value before assigning it to the property.  This is used
 *  to translate values from strings to
 *  {@link org.apache.commons.lang.enum.Enum}s.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class SetConvertedPropertyRule extends AbstractSpecificationRule
{
    private Map _map;
    private String _attributeName;
    private String _propertyName;

    public SetConvertedPropertyRule(Map map, String attributeName, String propertyName)
    {
        _map = map;
        _attributeName = attributeName;
        _propertyName = propertyName;
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception
    {
        String attributeValue = getValue(attributes, _attributeName);
        if (attributeValue == null)
            return;

        Object propertyValue = _map.get(attributeValue);

        // Check for null here?

        setProperty(_propertyName, propertyValue);
    }

}
