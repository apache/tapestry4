package com.primix.tapestry.components;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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
 *  Used by a {@link PropertySelection} to provide labels for options.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public interface IPropertySelectionModel
{
	/**
	 *  Returns the number of possible options.
	 *
	 */
	 
	public int getOptionCount();
	
	/**
	 *  Returns one possible option.
	 *
	 */
	 
	public Object getOption(int index);
	
	/**
	 *  Returns the label for an option.  It is the responsibility of the
	 *  adaptor to make this value localized.
	 *
	 */
	 
	public String getLabel(int index);
}