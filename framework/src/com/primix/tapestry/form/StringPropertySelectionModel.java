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

package com.primix.tapestry.form;

/**
 *  Implementation of {@link IPropertySelectionModel} that allows one String from
 *  an array of Strings to be selected as the property.
 *
 *  <p>Uses a simple index number as the value (used to represent the selected String).
 *  This assumes that the possible values for the Strings will remain constant between
 *  request cycles.
 *
 *  @version $Id$
 *  @author Howard Ship
 */

public class StringPropertySelectionModel implements IPropertySelectionModel
{
	private String[] options;

	/**
	 * Standard constructor.
	 *
	 * The options are retained (not copied).
	 */

	public StringPropertySelectionModel(String[] options)
	{
		this.options = options;
	}

	public int getOptionCount()
	{
		return options.length;
	}

	public Object getOption(int index)
	{
		return options[index];
	}

	/**
	 *  Labels match options.
	 *
	 */

	public String getLabel(int index)
	{
		return options[index];
	}

	/**
	 *  Values are indexes into the array of options.
	 *
	 */

	public String getValue(int index)
	{
		return Integer.toString(index);
	}

	public Object translateValue(String value)
	{
		int index;

		index = Integer.parseInt(value);

		return options[index];
	}

}