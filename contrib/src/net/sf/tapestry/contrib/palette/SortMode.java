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

package net.sf.tapestry.contrib.palette;

import com.primix.tapestry.util.*;

/**
 *  Defines different sorting strategies for the {@link Palette} component.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class SortMode extends Enum
{
	/**
	 *  Sorting is not relevant and no sort controls should be visible.
	 *
	 */
	
	public static final SortMode NONE = new SortMode("NONE");
	
	/**
	 * Options should be sorted by their label.
	 *
	 */
	
	public static final SortMode LABEL = new SortMode("LABEL");
	
	/**
	 *  Options should be sorted by thier value.
	 *
	 */
	
	public static final SortMode VALUE = new SortMode("VALUE");
	
	/**
	 *  The user controls sort order; additional controls are added
	 *  to allow the user to control the order of options in the
	 *  selected list.
	 *
	 */
	
	public static final SortMode USER = new SortMode("USER");
	
	private SortMode(String enumerationId)
	{
		super(enumerationId);
	}
	
	private Object readResolve()
	{
		return getSingleton();
	}
}

