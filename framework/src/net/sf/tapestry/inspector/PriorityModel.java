//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.inspector;

import net.sf.tapestry.form.IPropertySelectionModel;
import org.apache.log4j.Priority;

/**
 *  Provides a {@link IPropertySelectionModel} for setting the priority of
 *  a {@link org.apache.log4j.Category}.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class PriorityModel implements IPropertySelectionModel
{
    private Priority[] values;

    public PriorityModel()
    {
        this(true);
    }

    public PriorityModel(boolean includeNull)
    {
        if (includeNull)
        {
            Priority[] allValues = Priority.getAllPossiblePriorities();
            values = new Priority[allValues.length + 1];
            values[0] = null;
            System.arraycopy(allValues, 0, values, 1, allValues.length);
        }
        else
        {
            values = Priority.getAllPossiblePriorities();
        }
    }

    public Object translateValue(String value)
    {
        int index = Integer.parseInt(value);

        return values[index];
    }

    public String getValue(int index)
    {
        return Integer.toString(index);
    }

    public int getOptionCount()
    {
        return values.length;
    }

    public Object getOption(int index)
    {
        return values[index];
    }

    public String getLabel(int index)
    {
        Priority option = values[index];

        if (option == null)
            return "";

        return values[index].toString();
    }
}