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

import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.xml.DocumentParseException;
import org.xml.sax.Attributes;

/**
 *  Sets a property from an extended attribute.  An extended attribute
 *  is a value that may either be specified inside an XML attribute or,
 *  if the attribute is not present, in the body of the element.
 *  It is not allowed that the value be specified in both places.
 *  The value may be optional or required.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class SetExtendedPropertyRule extends AbstractSpecificationRule
{
    private String _attributeName;
    private String _propertyName;
    private boolean _required;

    private boolean _valueSet;

    public SetExtendedPropertyRule(String attributeName, String propertyName, boolean required)
    {
        _attributeName = attributeName;
        _propertyName = propertyName;
        _required = required;
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception
    {
        String value = getValue(attributes, _attributeName);

        if (value != null)
        {
            setProperty(_propertyName, value);
            _valueSet = true;
        }
    }

    public void body(String namespace, String name, String text) throws Exception
    {
        if (Tapestry.isBlank(text))
            return;

        if (_valueSet)
        {
            throw new DocumentParseException(
                Tapestry.format(
                    "SpecificationParser.no-attribute-and-body",
                    _attributeName,
                    name),
                getResourceLocation());
        }

        setProperty(_propertyName, text.trim());
        _valueSet = true;
    }

    public void end(String namespace, String name) throws Exception
    {
        if (!_valueSet && _required)
            throw new DocumentParseException(
                Tapestry.format(
                    "SpecificationParser.required-extended-attribute",
                    name,
                    _attributeName),
                getResourceLocation());

        _valueSet = false;
    }

}
