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

package org.apache.tapestry.vlib;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 *  This class is used as a property selection model to select a primary key.
 *  We assume that the primary keys are integers, which makes it easy to
 *  translate between the various representations.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class EntitySelectionModel implements IPropertySelectionModel
{
    private static class Entry
    {
        Integer primaryKey;
        String label;

        Entry(Integer primaryKey, String label)
        {
            this.primaryKey = primaryKey;
            this.label = label;
        }

    }

    private static final int LIST_SIZE = 20;

    private List entries = new ArrayList(LIST_SIZE);

    public void add(Integer key, String label)
    {
        Entry entry;

        entry = new Entry(key, label);
        entries.add(entry);
    }

    public int getOptionCount()
    {
        return entries.size();
    }

    private Entry get(int index)
    {
        return (Entry) entries.get(index);
    }

    public Object getOption(int index)
    {
        return get(index).primaryKey;
    }

    public String getLabel(int index)
    {
        return get(index).label;
    }

    public String getValue(int index)
    {
        Integer primaryKey;

        primaryKey = get(index).primaryKey;

        if (primaryKey == null)
            return "";

        return primaryKey.toString();
    }

    public Object translateValue(String value)
    {
        if (value.equals(""))
            return null;

        try
        {
            return new Integer(value);
        }
        catch (NumberFormatException e)
        {
            throw new ApplicationRuntimeException("Could not convert '" + value + "' to an Integer.", e);
        }
    }
}