//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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