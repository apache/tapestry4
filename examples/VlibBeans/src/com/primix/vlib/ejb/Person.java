package com.primix.vlib.ejb;

import java.io.*;

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
 *  A light-weight, read-only version of the {@link IPerson} bean.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class Person implements Serializable
{
	private Object[] columns;
	
	public static final int PRIMARY_KEY_COLUMN = 0;
	public static final int FIRST_NAME_COLUMN = 1;
	public static final int LAST_NAME_COLUMN = 2;
	public static final int EMAIL_COLUMN = 3;
	
	public static final int N_COLUMNS = 4;
	
	public Person(Object[] columns)
	{
		if (columns == null)
			throw new IllegalArgumentException("Must provide a non-null columns.");
		
		if (columns.length != N_COLUMNS)
			throw new IllegalArgumentException("Wrong number of columns for a Person.");
			
		this.columns = new Object[N_COLUMNS];
		System.arraycopy(columns, 0, this.columns, 0, N_COLUMNS);	
	}
	
	public Integer getPrimaryKey()
	{
		return (Integer)columns[PRIMARY_KEY_COLUMN];
	}
	
	public String getFirstName()
	{
		return (String)columns[FIRST_NAME_COLUMN];
	}
	
	public String getLastName()
	{
		return (String)columns[LAST_NAME_COLUMN];
	}
	
	public String getEmail()
	{
		return (String)columns[EMAIL_COLUMN];
	}
	
	public String getNaturalName()
	{
		if (columns[FIRST_NAME_COLUMN] == null)
			return (String)columns[LAST_NAME_COLUMN];
		
		return (String)columns[FIRST_NAME_COLUMN] + " " + (String)columns[LAST_NAME_COLUMN];
	}
	
	public String toString()
	{
		StringBuffer buffer;
		
		buffer = new StringBuffer("Person[");
		
		if (columns[FIRST_NAME_COLUMN] != null)
		{
			buffer.append((String)columns[FIRST_NAME_COLUMN]);
			buffer.append(' ');
		}
		
		buffer.append((String)columns[LAST_NAME_COLUMN]);
		buffer.append(']');
		
		return buffer.toString();
	}
}
