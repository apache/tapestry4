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
