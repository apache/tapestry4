package org.apache.tapestry.junit.form;

import org.apache.tapestry.form.ListEditMap;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.spec.Direction;

/**
 *  Suite of tests for {@link org.apache.tapestry.form.ListEditMap}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class TestListEditMap extends TapestryTestCase
{

    public TestListEditMap(String name)
    {
        super(name);
    }

    private ListEditMap create()
    {
        ListEditMap m = new ListEditMap();

        m.add("in", Direction.IN);
        m.add("auto", Direction.AUTO);
        m.add("custom", Direction.CUSTOM);

        return m;
    }

    public void testAdd()
    {
        ListEditMap m = create();

		assertEquals("custom", m.getKey());

        checkList("keys", new Object[] { "in", "auto", "custom" }, m.getKeys());
        checkList(
            "all values",
            new Object[] { Direction.IN, Direction.AUTO, Direction.CUSTOM },
            m.getAllValues());
        checkList(
            "all values",
            new Object[] { Direction.IN, Direction.AUTO, Direction.CUSTOM },
            m.getValues());

        assertNull(m.getDeletedKeys());
    }

    public void testGet()
    {
        ListEditMap m = create();

        m.setKey("auto");

        assertEquals("auto", m.getKey());
        assertSame(Direction.AUTO, m.getValue());
    }

    public void testGetUnknown()
    {
        ListEditMap m = create();

        m.setKey("unknown");

        assertNull(m.getValue());
    }

    public void testMarkDeleted()
    {
        ListEditMap m = create();

        m.setKey("auto");

        assertEquals(false, m.isDeleted());

        m.setDeleted(true);

        assertEquals(true, m.isDeleted());

        checkList(
            "all values",
            new Object[] { Direction.IN, Direction.AUTO, Direction.CUSTOM },
            m.getAllValues());

        checkList(
            "undeleted values",
            new Object[] { Direction.IN, Direction.CUSTOM },
            m.getValues());

        checkList("deleted keys", new Object[] { "auto" }, m.getDeletedKeys());
    }

    public void testMarkAlreadyDeleted()
    {

        ListEditMap m = create();

        m.setKey("auto");

        assertEquals(false, m.isDeleted());

        m.setDeleted(false);

        assertEquals(false, m.isDeleted());
    }

    public void testMarkMultipleDeleted()
    {
        ListEditMap m = create();

        m.setKey("auto");
        m.setDeleted(true);

        m.setKey("custom");
        assertEquals(false, m.isDeleted());
        m.setDeleted(true);

        assertEquals(true, m.isDeleted());

        checkList("undeleted values", new Object[] { Direction.IN }, m.getValues());
    }

    public void testDeleteUndelete()
    {
        ListEditMap m = create();

        m.setKey("auto");
        m.setDeleted(true);
        m.setDeleted(false);

        m.setKey("custom");
        m.setDeleted(true);

        checkList("undeleted values", new Object[] { Direction.IN, Direction.AUTO }, m.getValues());
    }

}