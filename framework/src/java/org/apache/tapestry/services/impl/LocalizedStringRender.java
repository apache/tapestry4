// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.services.impl;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.parse.LocalizationToken;
import org.apache.tapestry.parse.TextToken;

/**
 * A class used with invisible localizations. Constructed from a {@link TextToken}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

public class LocalizedStringRender implements IRender
{
    private IComponent _component;

    private String _key;

    private Map _attributes;

    private String _value;

    private boolean _raw;

    public LocalizedStringRender(IComponent component, LocalizationToken token)
    {
        _component = component;
        _key = token.getKey();
        _raw = token.isRaw();
        _attributes = token.getAttributes();
    }

    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (cycle.isRewinding())
            return;

        if (_attributes != null)
        {
            writer.begin("span");

            Iterator i = _attributes.entrySet().iterator();

            while (i.hasNext())
            {
                Map.Entry entry = (Map.Entry) i.next();
                String attributeName = (String) entry.getKey();
                String attributeValue = (String) entry.getValue();

                writer.attribute(attributeName, attributeValue);
            }
        }

        if (_value == null)
            _value = _component.getMessages().getMessage(_key);

        if (_raw)
            writer.printRaw(_value);
        else
            writer.print(_value);

        if (_attributes != null)
            writer.end();
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("component", _component);
        builder.append("key", _key);
        builder.append("raw", _raw);
        builder.append("attributes", _attributes);

        return builder.toString();
    }

}