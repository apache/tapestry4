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

package org.apache.tapestry.record;

import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.record.ChangeKey}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestChangeKey extends HiveMindTestCase
{
    public void testSetters()
    {
        ChangeKey key = new ChangeKey("path", "name");

        assertEquals("path", key.getComponentPath());
        assertEquals("name", key.getPropertyName());

        key = new ChangeKey(null, "foo");

        assertNull(key.getComponentPath());
        assertEquals("foo", key.getPropertyName());
    }

    public void testHashCode()
    {
        ChangeKey key1 = new ChangeKey("path", "name1");
        ChangeKey key2 = new ChangeKey("path", "name1");
        ChangeKey key3 = new ChangeKey(null, "name2");

        assertEquals(key1.hashCode(), key2.hashCode());
        assertTrue(key1.hashCode() != key3.hashCode());
    }

    public void testEquals()
    {
        ChangeKey key1 = new ChangeKey("path", "name1");
        ChangeKey key2 = new ChangeKey("path", "name1");
        ChangeKey key3 = new ChangeKey(null, "name2");

        assertTrue(key1.equals(key2));
        assertFalse(key1.equals(key3));

        assertFalse(key1.equals(null));
        assertTrue(key1.equals(key1));
        assertFalse(key1.equals("foo"));
    }
}