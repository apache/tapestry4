//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.junit.utils;

import junit.framework.TestCase;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.util.prop.PropertyFinder;
import org.apache.tapestry.util.prop.PropertyInfo;

/**
 *  Tests the {@link org.apache.tapestry.util.prop.PropertyFinder}
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
        PropertyInfo i = PropertyFinder.getPropertyInfo(BasePage.class, "pageName");

        assertEquals("pageName", i.getName());
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
