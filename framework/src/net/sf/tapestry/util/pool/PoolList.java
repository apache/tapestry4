//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.util.pool;

/**
 *  A wrapper around a list of objects for a given key in a {@link Pool}.
 *  The current implementation of this is FIFO.  This class is closely
 *  tied to {@link Pool}, which controls synchronization for it.
 *
 *  <p>This class, and {@link Pool}, were heavily revised in 1.0.5
 *  to support generational cleaning.  The PoolList acts like a first-in
 *  first-out queue and each pooled object is tagged with a "generation
 *  count", provided by the {@link Pool}. The generation count is
 *  incremented periodically.  This allows us to track, roughly,
 *  how often a pooled object has been accessed; unused objects will
 *  be buried with relatively low generation counts.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class PoolList
{
    /**
     *  Linked list of pooled objects.
     *
     * @since 1.0.5
     **/

    private Entry first;

    /**
     *  Linked list of "spare" Entries, ready to be re-used.
     *
     * @since 1.0.5
     **/

    private Entry spare;

    /**
     *  Overall count of items pooled.
     *
     **/

    private int count;

    /**
     * A simple linked-list entry for items stored in the PoolList.
     *
     * @since 1.0.5
     **/

    private static class Entry
    {
        int generation;
        Object pooled;
        Entry next;
    }

    /**
     *  Returns the number of pooled objects currently stored.
     *
     *  @since 1.0.5
     **/

    public int getPooledCount()
    {
        return count;
    }

    /**
     *  Returns an object previously stored into the list, or null if the list
     *  is empty.  The returned object is removed from the list.
     *
     **/

    public Object retrieve()
    {
        if (count == 0)
            return null;

        count--;

        Entry e = first;
        Object result = e.pooled;

        // Okay, store e into the list of spare entries.

        first = e.next;

        e.next = spare;
        spare = e;
        e.generation = 0;
        e.pooled = null;

        return result;
    }

    /**
     *  Adds the object to this PoolList.  An arbitrary number of objects can be
     *  stored.  The objects can later be retrieved using {@link #get()}.
     *  The list requires that generation never decrease.  On each subsequent
     *  invocation, it should be the same as, or greater, than the previous value.
     *
     *  @return The number of objects stored in the list (after adding the new object).
     **/

    public int store(int generation, Object object)
    {
        Entry e;

        if (spare == null)
        {
            e = new Entry();
        }
        else
        {
            e = spare;
            spare = spare.next;
        }

        e.generation = generation;
        e.pooled = object;
        e.next = first;
        first = e;

        return ++count;
    }

    /**
     *  Invoked to cleanup the list, freeing unneeded objects.
     *
     * @param generation pooled objects stored in this generation or
     * earlier are released.
     *
     * @since 1.0.5
     **/

    public int cleanup(int generation)
    {
        spare = null;

        count = 0;

        Entry prev = null;

        // Walk through the list.  They'll be sorted by generation.

        Entry e = first;
        while (true)
        {
            if (e == null)
                break;

            // If found a too-old entry then we want to
            // delete it.

            if (e.generation <= generation)
            {
                // Set the next pointer of the previous node to null.
                // If the very first node inspected was too old,
                // set the first pointer to null.

                if (prev == null)
                    first = null;
                else
                    prev.next = null;

                break;
            }

            prev = e;
            e = e.next;
            count++;
        }

        return count;
    }
    public String toString()
    {
        return "PoolList[" + count + "]";
    }
}