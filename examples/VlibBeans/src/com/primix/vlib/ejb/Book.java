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

package com.primix.vlib.ejb;

import java.io.*;

/**
 *  Represents a single result row from a {@link IBookQuery}.  In effect,
 *  this is a light-wieght, serializable, read-only version of an {@link IBook}
 *  bean, plus it contains the owner and holder name (which means we don't
 *  have to go find the correct {@link IPerson} to dig out the name).
 *
 *  <p>This is provided for efficient access when doing various queries.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class Book implements Serializable
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
	public static final int OWNER_PK_COLUMN = 4;
	
	/**
	 *  Column index for a presentable version of the holder's name.
	 *
	 *  @see IPerson#getNaturalName()
	 *
	 */
	 
	public static final int OWNER_NAME_COLUMN = 5;
	
	public static final int HOLDER_PK_COLUMN = 6;
	public static final int HOLDER_NAME_COLUMN = 7;
	public static final int PUBLISHER_PK_COLUMN = 8;
	public static final int PUBLISHER_NAME_COLUMN = 9;
	public static final int AUTHOR_COLUMN = 10;
    
	public static final int HIDDEN_COLUMN = 11;
	public static final int LENDABLE_COLUMN = 12;
	/**
	 *  Number of columns in the result.
	 *
	 */
	 
	public static final int N_COLUMNS = 13;
	
	private Object[] columns;
	
	/**
	 *  Constructs a new BookQueryResult, making an internal copy of the columns passed.
	 *
	 */
	 
	public Book(Object[] columns)
	{
		if (columns == null)
			throw new IllegalArgumentException("Must provide a non-null columns.");
		
		if (columns.length != N_COLUMNS)
			throw new IllegalArgumentException("Wrong number of columns for a Book.");
			
		this.columns = new Object[N_COLUMNS];
		System.arraycopy(columns, 0, this.columns, 0, N_COLUMNS);	
	}
	
	private Object get(int index)
	{
		return columns[index];
	}
	
	public Integer getPrimaryKey()
	{
		return (Integer)get(PRIMARY_KEY_COLUMN);
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

	public Integer getOwnerPrimaryKey()
	{
		return (Integer)get(OWNER_PK_COLUMN);
	}
	
	public String getOwnerName()
	{
		return (String)get(OWNER_NAME_COLUMN);
	}
	
	public Integer getHolderPrimaryKey()
	{
		return (Integer)get(HOLDER_PK_COLUMN);
	}
	
	public String getHolderName()
	{
		return (String)get(HOLDER_NAME_COLUMN);
	}
	
	public Integer getPublisherPrimaryKey()
	{
		return (Integer)get(PUBLISHER_PK_COLUMN);
	}
	
	public String getPublisherName()
	{
		return (String)get(PUBLISHER_NAME_COLUMN);
	}
	
	public String getAuthor()
	{
		return (String)get(AUTHOR_COLUMN);
	}
	
	public String toString()
	{
		StringBuffer buffer;
		
		buffer = new StringBuffer("Book[");
		buffer.append(get(PRIMARY_KEY_COLUMN));
		buffer.append(' ');
		buffer.append(get(TITLE_COLUMN));
		buffer.append(']');
		
		return buffer.toString();
	}

    /**
     *  Returns true if the book is borrowed; that is, if its holder doesn't
     *  match its owner.
     *
     */

    public boolean isBorrowed()
    {
        return ! get(HOLDER_PK_COLUMN).equals(get(OWNER_PK_COLUMN));
    }
	
	public boolean isHidden()
	{
		return getBit(HIDDEN_COLUMN);
	}
	
	public boolean isLendable()
	{
		return getBit(LENDABLE_COLUMN);
	}
	
	private boolean getBit(int column)
	{
		Boolean b = (Boolean)get(column);
		
		return b.booleanValue();
	}
}
