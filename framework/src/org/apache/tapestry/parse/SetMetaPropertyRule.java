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
import org.apache.tapestry.util.IPropertyHolder;
import org.apache.tapestry.util.xml.DocumentParseException;
import org.xml.sax.Attributes;

/**
 *  Handles the &lt;property&gt; element in Tapestry specifications, which is 
 *  designed to hold meta-data about specifications.
 *  Expects the top object on the stack to be a {@link org.apache.tapestry.util.IPropertyHolder}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class SetMetaPropertyRule extends AbstractSpecificationRule
{
    private String _name;
    private String _value;

    public void begin(String namespace, String name, Attributes attributes) throws Exception
    {
        _name = getValue(attributes, "name");

        // First, get the value from the attribute, if present

        _value = getValue(attributes, "value");

    }

    public void body(String namespace, String name, String text) throws Exception
    {
        if (Tapestry.isBlank(text))
            return;

        if (_value != null)
        {
            throw new DocumentParseException(
                Tapestry.format("SpecificationParser.no-attribute-and-body", "value", name),
                getResourceLocation());
        }

        _value = text.trim();
    }

    public void end(String namespace, String name) throws Exception
    {
        if (_value == null)
            throw new DocumentParseException(
                Tapestry.format("SpecificationParser.required-extended-attribute", name, "value"),
                getResourceLocation());

        IPropertyHolder holder = (IPropertyHolder) digester.peek();

        holder.setProperty(_name, _value);

        _name = null;
        _value = null;
    }

}
