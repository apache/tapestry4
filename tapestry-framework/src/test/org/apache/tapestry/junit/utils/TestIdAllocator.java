// Copyright 2004, 2005 The Apache Software Foundation
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

import static org.testng.AssertJUnit.assertEquals;

import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.util.IdAllocator;
import org.testng.annotations.Test;

/**
 * Tests the {@link org.apache.tapestry.util.IdAllocator}class.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */
@Test
public class TestIdAllocator extends TapestryTestCase
{
    public void testSimple()
    {
        IdAllocator a = new IdAllocator();

        assertEquals("name", a.allocateId("name"));

        for (int i = 0; i < 10; i++)
            assertEquals("name_" + i, a.allocateId("name"));
    }

    public void testSimpleNamespace()
    {
        IdAllocator a = new IdAllocator("_NS");

        assertEquals("name_NS", a.allocateId("name"));

        for (int i = 0; i < 10; i++)
            assertEquals("name_NS_" + i, a.allocateId("name"));

        // This is current behavior, but is probably something
        // that could be improved.

        assertEquals("foo_NS_NS", a.allocateId("foo_NS"));
        assertEquals("foo_NS_NS_0", a.allocateId("foo_NS"));
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

    public void testDegenerateNamespace()
    {
        IdAllocator a = new IdAllocator("_NS");

        assertEquals("d_1_NS", a.allocateId("d_1"));

        assertEquals("d_NS", a.allocateId("d"));
        assertEquals("d_NS_0", a.allocateId("d"));
        assertEquals("d_NS_1", a.allocateId("d"));
        assertEquals("d_NS_2", a.allocateId("d"));
        assertEquals("d_NS_3", a.allocateId("d"));

        assertEquals("d_1_NS_0", a.allocateId("d_1"));

        // This is very degenerate, and maybe something that needs fixing.

        assertEquals("d_1_NS_NS", a.allocateId("d_1_NS"));
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