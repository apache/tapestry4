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
 *  A light-weight, read-only version of the {@link IPublisher} bean.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

package com.primix.vlib.ejb;

import java.io.*;

public class Publisher implements Serializable
{
	private Integer primaryKey;
	private String name;
	
	public Publisher(Integer primaryKey, String name)
	{
		this.primaryKey = primaryKey;
		this.name = name;
	}
	
	public Integer getPrimaryKey()
	{
		return primaryKey;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		StringBuffer buffer;
		
		buffer = new StringBuffer("Publisher[");
		buffer.append(primaryKey);
		buffer.append(' ');
		buffer.append(name);
		buffer.append(']');
		
		return buffer.toString();
	}
}
