/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
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
 *  A Pool is used to pool instances of a useful class.  It uses
 *  keys, much like a {@link Map}, to identify a list of pooled objects.
 *  Getting an object from the Pool atomically removes it from the
 *  pool.  It can then be re-added later.
 *
 *  <p>The implementation of Pool is threadsafe.
 *
 *  <p>TBD:  More info about objects in the pool, culling objects
 *  from the pool, etc.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

package com.primix.tapestry.util.pool;

import java.util.*; 
import org.apache.log4j.*;

public class Pool
{
	private static final Category CAT = 
		Category.getInstance(Pool.class);

	private static final int MAP_SIZE = 23;

	/**
	*  Creates a new Pool using the default map size.  Creation of the map is deferred.
	*
	*/

	public Pool()
	{
	}

	/**
	*  Creates a new Pool using the specified map size.  The map is created immediately.
	*
	*/

	public Pool(int mapSize)
	{
		map = new HashMap(mapSize);
	}

	/**
	*  A map of PoolLists, keyed on an arbitrary object.
	*
	*/

	private Map map;

	/**
	*  Returns a previously pooled object with the given key, or null if no
	*  such object exists.  Getting an object from a Pool removes it from the Pool,
	*  but it can later be re-added with {@link #store(Object,Object)}.
	*
	*/

	public Object retrieve(Object key)
	{
		PoolList list;
		Object result = null;

		if (map != null)
		{
			synchronized (map)
			{
				list = (PoolList)map.get(key);
			}

			if (list != null)
				result = list.retrieve();
		}

		if (CAT.isDebugEnabled())
			CAT.debug("Retrieved " + result + " from " + key);

		return result;	
	}

	/**
	*  Stores an object in the pool for later retrieval.
	*
	*/

	public void store(Object key, Object object)
	{
		PoolList list;
		int count;

		if (map == null)
		{
			synchronized(this)
			{
				if (map == null)
					map = new HashMap(MAP_SIZE);
			}
		}

		synchronized(map)
		{
			list = (PoolList)map.get(key);

			if (list == null)
			{
				list = new PoolList();
				map.put(key, list);
			}

			count = list.store(object);
		}
		
		if (CAT.isDebugEnabled())
			CAT.debug("Stored " + object + " into " + key + " (" + count + " pooled)");
	}

	/**
	*  Removes all previously pooled objects from this Pool.
	*
	*/

	public void clear()
	{
		if (map != null)
		{
			synchronized(map)
			{
				map.clear();
			}
		}
		
		if (CAT.isDebugEnabled())
			CAT.debug("Cleared");
	}

	public String toString()
	{
		if (map == null)
			return super.toString();

		StringBuffer buffer = new StringBuffer();

		buffer.append("Pool[");

		synchronized(map)
		{
			Iterator i = map.entrySet().iterator();
			while (i.hasNext())
			{
				Map.Entry entry = (Map.Entry)i.next();
				
				buffer.append(' ');
				buffer.append(entry.getKey());
				buffer.append('=');
				buffer.append(entry.getValue());
			}
		}

		buffer.append(']');

		return buffer.toString();
	}
}
