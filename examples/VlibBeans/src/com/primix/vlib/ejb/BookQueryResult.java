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
 *  Represents a single result row from a {@link IBookQuery}.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class BookQueryResult implements Serializable
{
	/**
	 *  Column index for the Book's primary key.
	 *
	 */
	 
	public static final int PRIMARY_KEY_COLUMN = 0;
	
	/**
	 *  Column index for the book title.
	 *
	 */
	 
	public static final int TITLE_COLUMN = 1;
	
	public static final int DESCRIPTION_COLUMN = 2;
	public static final int ISBN_COLUMN = 3;
	public static final int LEND_COUNT_COLUMN = 4;
	public static final int OWNER_PK_COLUMN = 5;
	
	/**
	 *  Column index for a presentable version of the holder's name.
	 *
	 *  @see IPerson#getNaturalName()
	 *
	 */
	 
	public static final int OWNER_NAME_COLUMN = 6;
	
	public static final int HOLDER_PK_COLUMN = 7;
	public static final int HOLDER_NAME_COLUMN = 8;
	public static final int PUBLISHER_PK_COLUMN = 9;
	public static final int PUBLISHER_NAME_COLUMN = 10;
	
	/**
	 *  Number of columns in the result.
	 *
	 */
	 
	public static final int N_COLUMNS = 11;
	
	private Object[] columns;
	
	/**
	 *  Constructs a new BookQueryResult, making an internal copy of the columns passed.
	 *
	 */
	 
	public BookQueryResult(Object[] columns)
	{
		if (columns == null)
			throw new IllegalArgumentException("Must provide a non-null columns.");
		
		if (columns.length != N_COLUMNS)
			throw new IllegalArgumentException("Wrong number of columns for a BookQueryResult.");
			
		this.columns = new Object[N_COLUMNS];
		System.arraycopy(columns, 0, this.columns, 0, N_COLUMNS);	
	}
	
	private Object get(int index)
	{
		return columns[index];
	}
	
	public Object getPrimaryKey()
	{
		return get(PRIMARY_KEY_COLUMN);
	}
	
	public String getTitle()
	{
		return (String)get(TITLE_COLUMN);
	}
	
	public String getDescription()
	{
		return (String)get(DESCRIPTION_COLUMN);
	}
	
	public String getISBN()
	{
		return (String)get(ISBN_COLUMN);
	}
	
	public int getLendCount()
	{
		return ((Integer)get(LEND_COUNT_COLUMN)).intValue();
	}
	
	public Object getOwnerPrimaryKey()
	{
		return get(OWNER_PK_COLUMN);
	}
	
	public String getOwnerName()
	{
		return (String)get(OWNER_NAME_COLUMN);
	}
	
	public Object getHolderPrimaryKey()
	{
		return get(HOLDER_PK_COLUMN);
	}
	
	public String getHolderName()
	{
		return (String)get(HOLDER_NAME_COLUMN);
	}
	
	public Object getPublisherPrimaryKey()
	{
		return get(PUBLISHER_PK_COLUMN);
	}
	
	public String getPublisherName()
	{
		return (String)get(PUBLISHER_NAME_COLUMN);
	}
}