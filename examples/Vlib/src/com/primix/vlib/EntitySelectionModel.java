/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.vlib;

import com.primix.tapestry.*;
import com.primix.tapestry.form.*;
import java.util.*;

/**
 *  This class is used as a property selection model to select a primary key.
 *  We assume that the primary keys are integers, which makes it easy to
 *  translate between the various representations.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

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
			throw new ApplicationRuntimeException(
				"Could not convert '" + value + "' to an Integer.",
				e);
		}
	}
}