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

import org.apache.log4j.Level;

/**
 *  Provides a {@link IPropertySelectionModel} for setting the {@link org.apache.log4j.Level} of
 *  a {@link org.apache.log4j.Logger}.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class LevelModel implements IPropertySelectionModel
{
    private Level[] _values;

    private static final Level[] ALL_LEVELS =
        { Level.OFF, Level.FATAL, Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG, Level.ALL };

    private static final Level[] ALL_LEVELS_WITH_NULL =
        { Level.OFF, Level.FATAL, Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG, Level.ALL, null };

    public LevelModel()
    {
        this(true);
    }

    public LevelModel(boolean includeNull)
    {
        _values = includeNull ? ALL_LEVELS_WITH_NULL : ALL_LEVELS;
    }

    public Object translateValue(String value)
    {
        int index = Integer.parseInt(value);

        return _values[index];
    }

    public String getValue(int index)
    {
        return Integer.toString(index);
    }

    public int getOptionCount()
    {
        return _values.length;
    }

    public Object getOption(int index)
    {
        return _values[index];
    }

    public String getLabel(int index)
    {
        Level option = _values[index];

        if (option == null)
            return "";

        return _values[index].toString();
    }
}