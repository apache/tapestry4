package com.primix.foundation.pool;

import java.util.*; 

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
 *  A wrapper around a list of objects for a given key in a {@link Pool}.
 *  The current implementation of this provides is FIFO.  This class is closely
 *  tied to {@link Pool}, which controls synchronization for it.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

class PoolList
{
    private Object[] array;
    private int count;

    private static final int ARRAY_SIZE = 3;

    /**
     *  Returns an object previously stored into the list, or null if the list
     *  is empty.  The returned object is removed from the list.
     *
     */

    public Object get()
    {
        if (count == 0)
            return null;

        return array[--count];
    }

    /**
     *  Adds the object to this PoolList.  An arbitrary number of objects can be
     *  stored.  The objects can later be retrieved using {@link #get()}.
     *
     */

    public void add(Object object)
    {
        if (array == null)
        {
            array = new Object[ARRAY_SIZE];
            array[0] = object;
            count = 1;
            return;
        }

        // If the array is full then expand it.

        if (count == array.length)
        {
            int newSize = array.length * 2 + 1;
            Object[] newArray = new Object[newSize];

            System.arraycopy(array, 0, newArray, 0, count);
            array = newArray;
        }

        array[count++] = object;            
    }
}
