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

package net.sf.tapestry.util;

import java.util.*;

/**
 *  Base class implementation for the {@link IPropertyHolder} interface.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class BasePropertyHolder implements IPropertyHolder
{
	private static final int MAP_SIZE = 7;
	private Map properties;

	public String getProperty(String name)
	{
		if (properties == null)
			return null;

		return (String) properties.get(name);
	}

	public void setProperty(String name, String value)
	{
		if (value == null)
		{
			removeProperty(name);
			return;
		}

		if (properties == null)
			properties = new HashMap(MAP_SIZE);

		properties.put(name, value);
	}

	public void removeProperty(String name)
	{
		if (properties == null)
			return;

		properties.remove(name);
	}

	public Collection getPropertyNames()
	{
		if (properties == null)
			return Collections.EMPTY_LIST;

		// Slightly dangerous, should wraps this as an unmodifiable, perhaps?

		return properties.keySet();
	}

}