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

package org.apache.tapestry.junit.parse;

import org.apache.tapestry.ILocation;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Location;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.resource.ClasspathResourceLocation;
import org.apache.tapestry.resource.ContextResourceLocation;
import org.apache.tapestry.util.DefaultResourceResolver;

/**
 *  Test the {@link org.apache.tapestry.parse.LocationTag} class.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class TestLocation extends TapestryTestCase
{
    private static final IResourceResolver _resolver = new DefaultResourceResolver();
    private static final IResourceLocation _resource1 =
        new ClasspathResourceLocation(_resolver, "/somepackage/somefile");
    private static final IResourceLocation _resource2 =
        new ClasspathResourceLocation(_resolver, "/someotherpackage/someotherfile");

    private IResourceLocation _location = new ContextResourceLocation(null, "/WEB-INF/foo.bar");

    public void testNoNumbers()
    {
        ILocation l = new Location(_location);

        assertSame(_location, l.getResourceLocation());
        assertTrue(l.getLineNumber() <= 0);
        assertTrue(l.getColumnNumber() <= 0);
    }

    public void testToStringShort()
    {
        ILocation l = new Location(_location);

        assertEquals("context:/WEB-INF/foo.bar", l.toString());
    }

    public void testWithLine()
    {
        ILocation l = new Location(_location, 22);

        assertEquals(22, l.getLineNumber());
        assertEquals("context:/WEB-INF/foo.bar, line 22", l.toString());
    }

    public void testWithNumbers()
    {
        ILocation l = new Location(_location, 100, 15);

        assertEquals(100, l.getLineNumber());
        assertEquals(15, l.getColumnNumber());
    }

    public void testToStringLong()
    {
        ILocation l = new Location(_location, 97, 3);

        assertEquals("context:/WEB-INF/foo.bar, line 97, column 3", l.toString());
    }
    public void testEqualsBare()
    {
        ILocation l1 = new Location(_resource1);

        assertEquals(true, l1.equals(new Location(_resource1)));

        assertEquals(false, l1.equals(new Location(_resource2)));
        assertEquals(false, l1.equals(new Location(_resource1, 10)));
    }

    public void testEqualsLineNo()
    {
        ILocation l1 = new Location(_resource1, 10);

        assertEquals(true, l1.equals(new Location(_resource1, 10)));
        assertEquals(false, l1.equals(new Location(_resource1, 11)));
        assertEquals(false, l1.equals(new Location(_resource2, 10)));
        assertEquals(false, l1.equals(new Location(_resource1, 10, 1)));
    }

    public void testEqualsFull()
    {
        ILocation l1 = new Location(_resource1, 10, 5);

        assertEquals(true, l1.equals(new Location(_resource1, 10, 5)));
        assertEquals(false, l1.equals(new Location(_resource1, 10, 6)));
        assertEquals(false, l1.equals(new Location(_resource1, 11, 5)));
        assertEquals(false, l1.equals(new Location(_resource2, 10, 5)));
    }

    public void testHashCodeBare()
    {
        ILocation l1 = new Location(_resource1);
        ILocation l2 = new Location(_resource1);

        assertEquals(l1.hashCode(), l2.hashCode());
    }

    public void testHashCodeLineNo()
    {
        ILocation l1 = new Location(_resource1, 15);
        ILocation l2 = new Location(_resource1, 15);

        assertEquals(l1.hashCode(), l2.hashCode());
    }

    public void testHashCodeFull()
    {
        ILocation l1 = new Location(_resource1, 15, 20);
        ILocation l2 = new Location(_resource1, 15, 20);

        assertEquals(l1.hashCode(), l2.hashCode());
    }

}
