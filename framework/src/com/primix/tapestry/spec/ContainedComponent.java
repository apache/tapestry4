/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2002 by Howard Lewis Ship
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

package com.primix.tapestry.spec;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines a contained component.  This includes the information needed to
 * get the contained component's specification, as well as any bindings
 * for the component.
 *
 * @author Howard Ship
 * @version $Id$
 **/

public class ContainedComponent
{
	private String type;

	private String copyOf;

	protected Map bindings;

	private static final int MAP_SIZE = 3;

	/**
	 *  Returns the named binding, or null if the binding does not
	 *  exist.
	 *
	 **/

	public BindingSpecification getBinding(String name)
	{
		if (bindings == null)
			return null;

		return (BindingSpecification) bindings.get(name);
	}

	/**
	 *  Returns an umodifiable <code>Collection</code>
	 *  of Strings, each the name of one binding
	 *  for the component.
	 *
	 **/

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

	/**
	 * 	Sets the String Id of the component being copied from.
	 *  For use by IDE tools like Spindle.
	 * 
	 *  @since 1.0.9
	 **/

	public void setCopyOf(String id)
	{
		copyOf = id;
	}

	/**
	 * 	Returns the id of the component being copied from.
	 *  For use by IDE tools like Spindle.
	 * 
	 *  @since 1.0.9
	 **/

	public String getCopyOf()
	{
		return copyOf;
	}
}