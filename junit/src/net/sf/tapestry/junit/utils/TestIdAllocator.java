package net.sf.tapestry.junit.utils;

import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.util.IdAllocator;

/**
 *  Tests the {@link net.sf.tapestry.util.IdAllocator} class.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class TestIdAllocator extends TapestryTestCase
{

    public TestIdAllocator(String name)
    {
        super(name);
    }

    public void testSimple()
    {
        IdAllocator a = new IdAllocator();

        assertEquals("name", a.allocateId("name"));

        for (int i = 0; i < 10; i++)
            assertEquals("name_" + i, a.allocateId("name"));
    }

    public void testDegenerate()
    {
        IdAllocator a = new IdAllocator();

        assertEquals("d_1", a.allocateId("d_1"));

        assertEquals("d", a.allocateId("d"));
        assertEquals("d_0", a.allocateId("d"));
        assertEquals("d_2", a.allocateId("d"));

        assertEquals("d_3", a.allocateId("d"));
        assertEquals("d_1_0", a.allocateId("d_1"));
    }

    public void testClear()
    {
        IdAllocator a = new IdAllocator();

        assertEquals("foo", a.allocateId("foo"));
        assertEquals("foo_0", a.allocateId("foo_0"));

        a.clear();

        assertEquals("foo", a.allocateId("foo"));
        assertEquals("foo_0", a.allocateId("foo_0"));
    }
}
