/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.vlib.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *  Represents a single result row from a {@link IBookQuery}.  In effect,
 *  this is a light-wieght, serializable, read-only version of an {@link IBook}
 *  bean, plus it contains the owner and holder name (which means we don't
 *  have to go find the correct {@link IPerson} to dig out the name).
 *
 *  <p>This is provided for efficient access when doing various queries.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Book implements Serializable
{
    private static final long serialVersionUID = -3423550323411938995L;
    
    /**
     *  Column index for the Book's primary key.
     *
     **/

    public static final int ID_COLUMN = 0;

    /**
     *  Column index for the book title.
     *
     **/

    public static final int TITLE_COLUMN = 1;

    public static final int DESCRIPTION_COLUMN = 2;
    public static final int ISBN_COLUMN = 3;
    public static final int OWNER_ID_COLUMN = 4;

    /**
     *  Column index for a presentable version of the holder's name.
     *
     *  @see IPerson#getNaturalName()
     *
     **/

    public static final int OWNER_NAME_COLUMN = 5;

    public static final int HOLDER_ID_COLUMN = 6;
    public static final int HOLDER_NAME_COLUMN = 7;
    public static final int PUBLISHER_ID_COLUMN = 8;
    public static final int PUBLISHER_NAME_COLUMN = 9;
    public static final int AUTHOR_COLUMN = 10;

    public static final int HIDDEN_COLUMN = 11;
    public static final int LENDABLE_COLUMN = 12;
    public static final int DATE_ADDED_COLUMN = 13;

    /**
     *  Number of columns in the result.
     *
     **/

    public static final int N_COLUMNS = 14;

    private Object[] columns;

    /**
     *  Constructs a new BookQueryResult, making an internal copy of the columns passed.
     *
     **/

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

    public Integer getId()
    {
        return (Integer) get(ID_COLUMN);
    }

    public String getTitle()
    {
        return (String) get(TITLE_COLUMN);
    }

    public String getDescription()
    {
        return (String) get(DESCRIPTION_COLUMN);
    }

    public String getISBN()
    {
        return (String) get(ISBN_COLUMN);
    }

    public Integer getOwnerId()
    {
        return (Integer) get(OWNER_ID_COLUMN);
    }

    public String getOwnerName()
    {
        return (String) get(OWNER_NAME_COLUMN);
    }

    public Integer getHolderId()
    {
        return (Integer) get(HOLDER_ID_COLUMN);
    }

    public String getHolderName()
    {
        return (String) get(HOLDER_NAME_COLUMN);
    }

    public Integer getPublisherId()
    {
        return (Integer) get(PUBLISHER_ID_COLUMN);
    }

    public String getPublisherName()
    {
        return (String) get(PUBLISHER_NAME_COLUMN);
    }

    public String getAuthor()
    {
        return (String) get(AUTHOR_COLUMN);
    }

    public Timestamp getDateAdded()
    {
        return (Timestamp) get(DATE_ADDED_COLUMN);
    }

    public String toString()
    {
        StringBuffer buffer;

        buffer = new StringBuffer("Book[");
        buffer.append(get(ID_COLUMN));
        buffer.append(' ');
        buffer.append(get(TITLE_COLUMN));
        buffer.append(']');

        return buffer.toString();
    }

    /**
     *  Returns true if the book is borrowed; that is, if its holder doesn't
     *  match its owner.
     *
     **/

    public boolean isBorrowed()
    {
        return !get(HOLDER_ID_COLUMN).equals(get(OWNER_ID_COLUMN));
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
        Boolean b = (Boolean) get(column);

        return b.booleanValue();
    }
}