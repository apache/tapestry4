// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.util;

import junit.framework.TestCase;

/**
 * Tests for {@link org.apache.tapestry.util.ObjectIdentityMap}
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestObjectIdentityMap extends TestCase
{
    public void testGetNotFound()
    {
        ObjectIdentityMap map = new ObjectIdentityMap();

        map.put("fee", "fie");

        assertNull(map.get("foe"));
    }

    public void testGetFound()
    {
        ObjectIdentityMap map = new ObjectIdentityMap();

        Object value = new Object();
        Object key = new Object();

        map.put(key, value);

        assertSame(value, map.get(key));
    }

    public void testGetWhenEmpty()
    {
        ObjectIdentityMap map = new ObjectIdentityMap();

        assertNull(map.get(null));
    }

    public void testStoreNull()
    {
        ObjectIdentityMap map = new ObjectIdentityMap();

        Object value = new Object();

        map.put(null, value);

        assertSame(value, map.get(null));
    }

    public void testStoreMany()
    {
        ObjectIdentityMap map = new ObjectIdentityMap();

        int count = 50;

        Object[] keys = new Object[count];
        Object[] values = new Object[count];

        for (int i = 0; i < count; i++)
        {
            keys[i] = new Object();
            values[i] = new Object();

            map.put(keys[i], values[i]);
        }

        for (int i = 0; i < count; i++)
        {
            assertSame(values[i], map.get(keys[i]));
        }
    }
}
