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
 *  A light-weight version of the {@link IPerson} bean.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Person implements Serializable
{
    private static final long serialVersionUID = 37148852625594728L;

    private Object[] columns;

    public static final int ID_COLUMN = 0;
    public static final int FIRST_NAME_COLUMN = 1;
    public static final int LAST_NAME_COLUMN = 2;
    public static final int EMAIL_COLUMN = 3;
    public static final int LOCKED_OUT_COLUMN = 4;
    public static final int ADMIN_COLUMN = 5;
    public static final int LAST_ACCESS_COLUMN = 6;

    public static final int N_COLUMNS = 7;

    public Person(Object[] columns)
    {
        if (columns == null)
            throw new IllegalArgumentException("Must provide a non-null columns.");

        if (columns.length != N_COLUMNS)
            throw new IllegalArgumentException("Wrong number of columns for a Person.");

        this.columns = new Object[N_COLUMNS];
        System.arraycopy(columns, 0, this.columns, 0, N_COLUMNS);
    }

    public Integer getId()
    {
        return (Integer) columns[ID_COLUMN];
    }

    public String getFirstName()
    {
        return (String) columns[FIRST_NAME_COLUMN];
    }

    public String getLastName()
    {
        return (String) columns[LAST_NAME_COLUMN];
    }

    public String getEmail()
    {
        return (String) columns[EMAIL_COLUMN];
    }

    public String getNaturalName()
    {
        if (columns[FIRST_NAME_COLUMN] == null)
            return (String) columns[LAST_NAME_COLUMN];

        return (String) columns[FIRST_NAME_COLUMN] + " " + (String) columns[LAST_NAME_COLUMN];
    }

    public Timestamp getLastAccess()
    {
        return (Timestamp) columns[LAST_ACCESS_COLUMN];
    }

    public String toString()
    {
        StringBuffer buffer;

        buffer = new StringBuffer("Person[");

        if (columns[FIRST_NAME_COLUMN] != null)
        {
            buffer.append((String) columns[FIRST_NAME_COLUMN]);
            buffer.append(' ');
        }

        buffer.append((String) columns[LAST_NAME_COLUMN]);
        buffer.append(']');

        return buffer.toString();
    }

    public boolean isAdmin()
    {
        return getBit(ADMIN_COLUMN);
    }

    public void setAdmin(boolean value)
    {
        setBit(ADMIN_COLUMN, value);
    }

    public boolean isLockedOut()
    {
        return getBit(LOCKED_OUT_COLUMN);
    }

    public void setLockedOut(boolean value)
    {
        setBit(LOCKED_OUT_COLUMN, value);
    }

    private void setBit(int column, boolean value)
    {
        columns[column] = value ? Boolean.TRUE : Boolean.FALSE;
    }

    private boolean getBit(int column)
    {
        Boolean b = (Boolean) columns[column];

        return b.booleanValue();
    }

}