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

package org.apache.tapestry.contrib.palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;

/**
 * One of the two columns in a Palette component: the left column lists
 * available options, the right column lists the selected columns.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 */
public class PaletteColumn implements IRender
{
    private String _name;
    private int _rows;
    private List _options = new ArrayList();

    private static class ValueComparator implements Comparator
    {
        public int compare(Object o1, Object o2)
        {
            PaletteOption option1 = (PaletteOption) o1;
            PaletteOption option2 = (PaletteOption) o2;

            return option1.getValue().compareTo(option2.getValue());
        }
    }

    private static class LabelComparator implements Comparator
    {
        public int compare(Object o1, Object o2)
        {
            PaletteOption option1 = (PaletteOption) o1;
            PaletteOption option2 = (PaletteOption) o2;

            return option1.getLabel().compareTo(option2.getLabel());
        }
    }

	/**
	 * @param name the name of the column (the name attribute of the &lt;select&gt;)
	 * @param rows the number of visible rows (the size attribute of the &lt;select&gt;)
	 */
	public PaletteColumn(String name, int rows)
	{
		_name = name;
		_rows = rows;
	}

    public void addOption(PaletteOption option)
    {
        _options.add(option);
    }

    /**
     * Sorts the options by value (the hidden value for the option
     * that represents the object value). This should be invoked
     * before rendering this PaletteColumn.
     */
    public void sortByValue()
    {
        Collections.sort(_options, new ValueComparator());
    }

    /**
     * Sorts the options by the label visible to the user. This should be invoked
     * before rendering this PaletteColumn.
     */
    public void sortByLabel()
    {
        Collections.sort(_options, new LabelComparator());
    }

    /**
     * Renders the &lt;select&gt; and &lt;option&gt; tags for
     * this column.
     */
    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        writer.begin("select");
        writer.attribute("multiple", "multiple");
        writer.attribute("name", _name);
        writer.attribute("size", _rows);
        writer.println();

        int count = _options.size();
        for (int i = 0; i < count; i++)
        {
            PaletteOption o = (PaletteOption) _options.get(i);

            o.render(writer, cycle);
        }

        writer.end();
    }

}
