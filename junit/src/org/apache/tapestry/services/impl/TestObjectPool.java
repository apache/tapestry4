package org.apache.tapestry.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.services.ObjectPool;

/**
 * Tests for {@link org.apache.tapestry.services.impl.ObjectPoolImpl}.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestObjectPool extends HiveMindTestCase
{
    public void testStoreAndGet()
    {
        String key = "POOLED-KEY";
        String pooled = "POOLED";
        ObjectPool p = new ObjectPoolImpl();

        assertNull(p.get(key));

        p.store(key, pooled);

        assertSame(pooled, p.get(key));

        assertNull(p.get(key));
    }

    public void testStoreMany()
    {
        ObjectPool p = new ObjectPoolImpl();

        Object pooled1 = new Object();
        Object pooled2 = new Object();

        String key = "POOLED-KEY";

        p.store(key, pooled1);
        p.store(key, pooled2);

		// No guarantee that we'll get them out in the order they were put in.
		
		List l = new ArrayList();
		l.add(pooled1);
		l.add(pooled2);
		
        for (int i = 0; i < 2; i++)
        {
            Object pooled = p.get(key);

            assertTrue(l.remove(pooled));
        }

        assertNull(p.get(key));
    }
}
