package com.primix.tapestry.spec;

import com.primix.foundation.*;
import com.primix.tapestry.*;
import java.util.*;
/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 * Defines a contained component.  This includes the information needed to
 * get the contained component's specification, as well as any bindings
 * for the component.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class ContainedComponent
{
	private String type;

	private Map bindings;

	private static final int MAP_SIZE = 3;

	/**
	*  Returns the named binding, or null if the binding does not
	*  exist.
	*
	*/

	public BindingSpecification getBinding(String name)
	{
		if (bindings == null)
			return null;

		return (BindingSpecification)bindings.get(name);
	}

	/**
	*  Returns an umodifiable <code>Collection</code>
	*  of Strings, each the name of one binding
	*  for the component.
	*
	*/

	public Collection getBindingNames()
	{
		if (bindings == null)
			return Collections.EMPTY_LIST;

		return Collections.unmodifiableCollection(bindings.keySet());
	}

	public String getType()
	{
		return type;
	}

	public void setBinding(String name, BindingSpecification spec)
	{
		if (bindings == null)
			bindings = new HashMap(MAP_SIZE);

		bindings.put(name, spec);
	}

	public void setType(String value)
	{
		type = value;
	}
}

