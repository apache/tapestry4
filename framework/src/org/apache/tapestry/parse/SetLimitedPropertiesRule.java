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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.tapestry.Tapestry;
import org.xml.sax.Attributes;

/**
 *  Much like {@link org.apache.commons.digester.SetPropertiesRule}, but
 *  only properties that are declared will be copied; other properties
 *  will be ignored.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/
public class SetLimitedPropertiesRule extends AbstractSpecificationRule
{
    private String[] _attributeNames;
    private String[] _propertyNames;

    public SetLimitedPropertiesRule(String attributeName, String propertyName)
    {
        this(new String[] { attributeName }, new String[] { propertyName });
    }

    public SetLimitedPropertiesRule(String[] attributeNames, String[] propertyNames)
    {
        _attributeNames = attributeNames;
        _propertyNames = propertyNames;
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception
    {
        Object top = digester.peek();

        int count = attributes.getLength();

        for (int i = 0; i < count; i++)
        {
            String attributeName = attributes.getLocalName(i);

            if (Tapestry.isBlank(attributeName))
                attributeName = attributes.getQName(i);

            for (int x = 0; x < _attributeNames.length; x++)
            {
                if (_attributeNames[x].equals(attributeName))
                {
                    String value = attributes.getValue(i);
                    String propertyName = _propertyNames[x];

                    PropertyUtils.setProperty(top, propertyName, value);

                    // Terminate inner loop when attribute name is found.

                    break;
                }
            }
        }
    }

}
