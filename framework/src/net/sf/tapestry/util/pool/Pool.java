/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2002 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package net.sf.tapestry.util.pool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Category;


import net.sf.tapestry.*;
import net.sf.tapestry.IRenderDescription;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.util.*;
import net.sf.tapestry.util.ICleanable;
import net.sf.tapestry.util.JanitorThread;

/**
 *  A Pool is used to pool instances of a useful class.  It uses
 *  keys, much like a {@link Map}, to identify a list of pooled objects.
 *  Retrieving an object from the Pool atomically removes it from the
 *  pool.  It can then be stored again later.
 *
 *  <p>The implementation of Pool is threadsafe.
 *
 *  <p>Pool implements {@link ICleanable}, with a goal of
 *  only keeping pooled objects that have been needed within
 *  a recent time frame.  A generational system is used, where each
 *  pooled object is assigned a generation count.  {@link #executeCleanup}
 *  culls objects whose generation count is too old (outside of a
 *  {@link #getWindow() window}).
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class Pool implements ICleanable, IRenderDescription
{
	private static final Category CAT = Category.getInstance(Pool.class);

	/**
	 *  The generation, used to cull unused pooled items.
	 *
	 * @since 1.0.5
	 */

	private int generation;

	/**
	 *  The generation window, used to identify which
	 *  items should be culled.
	 *
	 * @since 1.0.5
	 */

	private int window = 10;

	/**
	 *  The number of objects pooled.
	 *
	 */

	private int pooledCount;

	/**
	 *  A map of PoolLists, keyed on an arbitrary object.
	 *
	 */

	private Map map;

	/**
	 *  Creates a new Pool using the default map size.  Creation of the map is deferred.
	 *
	 *
	 */

	public Pool()
	{
		this(true);
	}

	/**
	 *  Creates a new Pool using the specified map size.  The map is created immediately.
	 *
	 */

	public Pool(int mapSize)
	{
		this(mapSize, true);
	}

	/**
	 *  @param useSharedJanitor if true, then the Pool is added to
	 *  the {@link JanitorThread#getSharedJanitorThread() shared janitor}.
	 *
	 *  @since 1.0.5
	 *
	 */

	public Pool(boolean useSharedJanitor)
	{
		if (useSharedJanitor)
			JanitorThread.getSharedJanitorThread().add(this);
	}

	/**
	 *  Standard constructor.
	 *
	 *  @param mapSize initial size of the map.
	 *  @param useSharedJanitor if true, then the Pool is added to
	 *  the {@link JanitorThread#getSharedJanitorThread() shared janitor}.
	 *
	 *  @since 1.0.5
	 */

	public Pool(int mapSize, boolean useSharedJanitor)
	{
		this(useSharedJanitor);

		map = new HashMap(mapSize);
	}

	/**
	 *  Returns the window used to cull pooled objects during a cleanup.
	 *  The default is 10, which works out to about five minutes with
	 *  a standard janitor (on a 30 second cycle).
	 *
	 *  @since 1.0.5
	 *
	 */

	public int getWindow()
	{
		return window;
	}

	/**
	 *  Sets the window, or number of generations that an object may stay
	 *  in the pool before being culled.
	 *
	 *  @throws IllegalArgumentException if value is less than 1.
	 *
	 *  @since 1.0.5
	 */

	public void setWindow(int value)
	{
		if (value < 1)
			throw new IllegalArgumentException("Pool window may not be less than 1.");

		window = value;
	}

	/**
	 *  Returns a previously pooled object with the given key, or null if no
	 *  such object exists.  Getting an object from a Pool removes it from the Pool,
	 *  but it can later be re-added with {@link #store(Object,Object)}.
	 *
	 */

	public synchronized Object retrieve(Object key)
	{
		PoolList list;
		Object result = null;

        if (map == null)
            map = new HashMap();

		list = (PoolList) map.get(key);

		if (list != null)
			result = list.retrieve();

		if (result != null)
			pooledCount--;

		if (CAT.isDebugEnabled())
			CAT.debug("Retrieved " + result + " from " + key);

		return result;
	}

	/**
	 *  Stores an object in the pool for later retrieval.  Invokes
	 *  {@link IPoolable#resetForPool()}, if the object
	 *  implements the {@link IPoolable} interface.
	 *
	 */

	public synchronized void store(Object key, Object object)
	{
		PoolList list;
		int count;

		if (object instanceof IPoolable)
		{
			((IPoolable) object).resetForPool();
		}

		if (map == null)
			map = new HashMap();

		list = (PoolList) map.get(key);

		if (list == null)
		{
			list = new PoolList();
			map.put(key, list);
		}

		count = list.store(generation, object);

		pooledCount++;

		if (CAT.isDebugEnabled())
			CAT.debug("Stored " + object + " into " + key + " (" + count + " pooled)");
	}

	/**
	 *  Removes all previously pooled objects from this Pool.
	 *
	 */

	public synchronized void clear()
	{
		if (map != null)
			map.clear();

		pooledCount = 0;

		if (CAT.isDebugEnabled())
			CAT.debug("Cleared");
	}

	/**
	 *  Returns the number of object pooled, the sum of the number
	 *  of objects in pooled under each key.  This number should be treated
	 *  as approximate, since there are a few minor windows where, under load,
	 *  it may not be properly synchronized.
	 *
	 *  @since 1.0.2
	 */

	public int getPooledCount()
	{
		return pooledCount;
	}

	/**
	 *  Returns the number of keys within the pool.
	 *
	 *  @since 1.0.2
	 */

	public synchronized int getKeyCount()
	{
		if (map == null)
			return 0;

		return map.size();
	}

	/**
	 *  Peforms culling of unneeded pooled objects.
	 *
	 *  @since 1.0.5
	 *
	 */

	public synchronized void executeCleanup()
	{
		if (map == null)
			return;

		if (CAT.isDebugEnabled())
			CAT.debug("Executing cleanup of " + this);

		generation++;

		int oldestGeneration = generation - window;

		if (oldestGeneration < 0)
			return;

		int oldCount = pooledCount;
		int culledKeys = 0;

		// During the cleanup, we keep the entire instance synchronized
		// (meaning other threads will block when trying to store
		// or retrieved pooled objects).  Fortunately, this
		// should be pretty darn quick!

		int newCount = 0;

		Iterator i = map.entrySet().iterator();
		while (i.hasNext())
		{
			Map.Entry e = (Map.Entry) i.next();

			PoolList list = (PoolList) e.getValue();

			int count = list.cleanup(oldestGeneration);

			if (count == 0)
			{
				i.remove();
				culledKeys++;
			}
			else
				newCount += count;
		}

		pooledCount = newCount;

		if (CAT.isDebugEnabled())
			CAT.debug("Culled " + (oldCount - pooledCount) + " pooled objects and " + culledKeys + " keys.");
	}

	public String toString()
	{
		if (map == null)
			return super.toString();

		StringBuffer buffer = new StringBuffer();

		buffer.append("Pool@");
		buffer.append(Integer.toHexString(hashCode()));

		buffer.append("[Generation ");
		buffer.append(generation);

		if (pooledCount > 0)
		{
			buffer.append(", ");
			buffer.append(pooledCount);
			buffer.append(" pooled");
		}

		synchronized (this)
		{
			Iterator i = map.entrySet().iterator();
			while (i.hasNext())
			{
				Map.Entry entry = (Map.Entry) i.next();
				PoolList list = (PoolList) entry.getValue();

				buffer.append(", ");
				buffer.append(entry.getKey());
				buffer.append('=');
				buffer.append(list.getPooledCount());
			}
		}

			buffer.append(']');

			return buffer.toString();
	}

	/** @since 1.0.6 **/

	public void renderDescription(IMarkupWriter writer)
	{
		writer.print("Pool[Generation = ");
		writer.print(generation);
		writer.print(" Pooled = ");
		writer.print(pooledCount);
		writer.print("]");

		if (map == null)
			return;

		boolean first = true;

		synchronized (this)
		{
			Iterator i = map.entrySet().iterator();

			while (i.hasNext())
			{
				Map.Entry entry = (Map.Entry) i.next();
				PoolList list = (PoolList) entry.getValue();

				if (first)
				{
					writer.begin("ul");
					first = false;
				}

				writer.begin("li");
				writer.print(entry.getKey().toString());
				writer.print(" = ");
				writer.print(list.getPooledCount());
				writer.println();
				writer.end();
			}
		}

			if (!first)
				writer.end(); // <ul>		
	}
}