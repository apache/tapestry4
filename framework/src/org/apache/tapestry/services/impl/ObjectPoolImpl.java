package org.apache.tapestry.services.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.services.ObjectPool;

/**
 * Implementation of the {@link org.apache.tapestry.services.ObjectPool} interface.
 * 
 * <p>
 * This ia a minimal implementation, one that has no concept of automatically removing
 * unused pooled objects. Eventually, it will also register for notifications about
 * general cache cleaning.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class ObjectPoolImpl implements ObjectPool
{
    /**
     * Pool of Lists (of pooled objects), keyed on arbitrary key.
     */
    private Map _pool = new HashMap();

    public synchronized Object get(Object key)
    {
        List pooled = (List) _pool.get(key);

        if (pooled == null || pooled.isEmpty())
            return null;

        return pooled.remove(0);
    }

    public synchronized void store(Object key, Object value)
    {
        List pooled = (List) _pool.get(key);

        if (pooled == null)
        {
            pooled = new LinkedList();
            _pool.put(key, pooled);
        }

        pooled.add(value);
    }

}
