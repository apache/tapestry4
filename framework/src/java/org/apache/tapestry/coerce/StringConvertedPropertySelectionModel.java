// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.coerce;

import java.util.ArrayList;
import java.util.List;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * {@link org.apache.tapestry.form.IPropertySelectionModel} created from a comma-seperated string by
 * {@link org.apache.tapestry.coerce.StringToPropertySelectionModelConverter}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public final class StringConvertedPropertySelectionModel implements IPropertySelectionModel
{
    private static class Entry
    {
        String _label;

        String _value;

        Entry(String term)
        {
            Defense.notNull(term, "term");

            int equalx = term.indexOf('=');

            if (equalx < 0)
            {
                _label = term.trim();
                _value = _label;
            }
            else
            {
                _label = term.substring(0, equalx).trim();
                _value = term.substring(equalx + 1).trim();
            }
        }
    }

    private final List _entries;

    public StringConvertedPropertySelectionModel(String[] terms)
    {
        Defense.notNull(terms, "terms");

        _entries = new ArrayList(terms.length);

        for (int i = 0; i < terms.length; i++)
        {
            _entries.add(new Entry(terms[i]));
        }
    }

    public int getOptionCount()
    {
        return _entries.size();
    }

    private Entry getEntry(int index)
    {
        return (Entry) _entries.get(index);
    }

    public Object getOption(int index)
    {
        return getValue(index);
    }

    public String getLabel(int index)
    {
        // TODO Auto-generated method stub
        return getEntry(index)._label;
    }

    public String getValue(int index)
    {
        return getEntry(index)._value;
    }

    public Object translateValue(String value)
    {
        // Values are the same on the client and the server, so no translation needed.
        return value;
    }

}
