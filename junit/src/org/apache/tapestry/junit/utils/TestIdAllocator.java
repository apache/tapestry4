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

import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.util.IdAllocator;

/**
 *  Tests the {@link org.apache.tapestry.util.IdAllocator} class.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class TestIdAllocator extends TapestryTestCase
{

    public void testSimple()
    {
        IdAllocator a = new IdAllocator();

        assertEquals("name", a.allocateId("name"));

        for (int i = 0; i < 10; i++)
            assertEquals("name$" + i, a.allocateId("name"));
    }

    public void testDegenerate()
    {
        IdAllocator a = new IdAllocator();

        assertEquals("d$1", a.allocateId("d$1"));

        assertEquals("d", a.allocateId("d"));
        assertEquals("d$0", a.allocateId("d"));
        assertEquals("d$2", a.allocateId("d"));

        assertEquals("d$3", a.allocateId("d"));
        assertEquals("d$1$0", a.allocateId("d$1"));
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
