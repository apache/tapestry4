package net.sf.tapestry.junit.utils;

import junit.framework.TestCase;
import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.util.prop.PropertyFinder;
import net.sf.tapestry.util.prop.PropertyInfo;

/**
 *  Tests the {@link net.sf.tapestry.util.prop.PropertyFinder}
 *  class.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public class TestPropertyFinder extends TestCase
{

    public TestPropertyFinder(String name)
    {
        super(name);
    }

    public void testReadOnlyProperty()
    {
        PropertyInfo i = PropertyFinder.getPropertyInfo(PublicBean.class, "syntheticProperty");

        assertEquals("syntheticProperty", i.getName());
        assertEquals(double.class, i.getType());
        assertEquals(true, i.isRead());
        assertEquals(false, i.isReadWrite());
        assertEquals(false, i.isWrite());
    }

    public void testReadWriteProperty()
    {
        PropertyInfo i = PropertyFinder.getPropertyInfo(AbstractComponent.class, "id");

        assertEquals("id", i.getName());
        assertEquals(String.class, i.getType());
        assertEquals(true, i.isRead());
        assertEquals(true, i.isReadWrite());
        assertEquals(true, i.isWrite());
    }

    public void testInheritedProperty()
    {
        PropertyInfo i = PropertyFinder.getPropertyInfo(BasePage.class, "name");

        assertEquals("name", i.getName());
        assertEquals(String.class, i.getType());
        assertEquals(true, i.isRead());
        assertEquals(true, i.isReadWrite());
        assertEquals(true, i.isWrite());
    }

    public void testUnknownProperty()
    {
        PropertyInfo i = PropertyFinder.getPropertyInfo(PublicBean.class, "fred");

        assertNull(i);
    }

}
