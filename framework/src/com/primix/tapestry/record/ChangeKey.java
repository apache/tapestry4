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

package com.primix.tapestry.record;

import java.io.Serializable;

/**
 * Used to identify a property change.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class ChangeKey
{
	String componentPath;
	String propertyName;

	public ChangeKey(String componentPath, String propertyName)
	{
		this.componentPath = componentPath;
		this.propertyName = propertyName;
	}

	public boolean equals(Object object)
	{
		boolean propertyNameIdentical;
		boolean componentPathIdentical;

		if (object == null)
			return false;

		if (this == object)
			return true;

		try
		{
			ChangeKey other = (ChangeKey) object;

			propertyNameIdentical = propertyName == other.propertyName;
			componentPathIdentical = componentPath == other.componentPath;

			if (propertyNameIdentical && componentPathIdentical)
				return true;

			// If not identical, then see if equal. If not equal, then
			// we don't equal the other key.

			if (!propertyNameIdentical)
				if (!propertyName.equals(other.propertyName))
					return false;

			// If this far, that propertyName is equal

			if (componentPathIdentical)
				return true;

			// If one's null and the other isn't then they can't
			// be equal.

			if (componentPath == null || other.componentPath == null)
				return false;

			// So it comes down to this ... are the two (non-null)
			// componentPath's equal?

			return componentPath.equals(other.componentPath);
		}
		catch (ClassCastException e)
		{
			return false;
		}
	}

	public String getComponentPath()
	{
		return componentPath;
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	/**
	*
	*  Returns the propertyName's hash code, xor'ed with the
	*  componentPath hash code (if componentPath is non-null).
	*
	*/

	public int hashCode()
	{
		int result;

		result = propertyName.hashCode();

		if (componentPath != null)
			result ^= componentPath.hashCode();

		return result;
	}
}