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

package org.apache.tapestry.junit;

import org.apache.tapestry.ILocatable;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.Location;
import org.apache.tapestry.Tapestry;

/**
 *  Tests the method {@link org.apache.tapestry.Tapestry#findLocation(Object[])}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class TestTapestryFindLocation extends TapestryTestCase
{
    private static class TestLocatable implements ILocatable
    {
        private ILocation _l;

        TestLocatable(ILocation l)
        {
            _l = l;
        }

        public ILocation getLocation()
        {
            return _l;
        }
    }

    public void testEmpty()
    {
        assertNull(Tapestry.findLocation(new Object[] {
        }));
    }

    public void testAllNull()
    {
        assertNull(Tapestry.findLocation(new Object[] { null, null, null }));
    }

    public void testOrdering()
    {
        ILocation l1 = new Location(null);
        ILocation l2 = new Location(null);

        assertSame(l1, Tapestry.findLocation(new Object[] { l1, l2 }));
    }

    public void testLocatable()
    {
        ILocation l1 = new Location(null);
        ILocatable l2 = new TestLocatable(l1);

        assertSame(l1, Tapestry.findLocation(new Object[] { l2 }));
    }

    public void testNullLocatable()
    {
        ILocation l1 = new Location(null);
        ILocatable l2 = new TestLocatable(null);
        ILocatable l3 = new TestLocatable(l1);

        assertSame(l1, Tapestry.findLocation(new Object[] { l2, l3 }));
    }

    public void testSkipOther()
    {
        ILocation l1 = new Location(null);

        assertSame(l1, Tapestry.findLocation(new Object[] { this, "Hello", l1, "Goodbye" }));
    }

}
