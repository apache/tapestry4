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
 * Tests for {@link org.apache.tapestry.record.PropertyChangeImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPropertyChange extends HiveMindTestCase
{
    public void testAccessors()
    {
        Object newValue = new Object();

        PropertyChangeImpl pc = new PropertyChangeImpl("componentPath", "property", newValue);

        assertEquals("componentPath", pc.getComponentPath());
        assertEquals("property", pc.getPropertyName());
        assertSame(newValue, pc.getNewValue());
    }

    public void testEquals()
    {
        PropertyChangeImpl pc1 = new PropertyChangeImpl("foo", "bar", "baz");
        PropertyChangeImpl pc2 = new PropertyChangeImpl("foo", "bar", "baz");
        PropertyChangeImpl pc3 = new PropertyChangeImpl(null, "bar", "baz");

        assertTrue(pc1.equals(pc2));
        assertTrue(pc1.equals(pc1));
        assertFalse(pc1.equals(pc3));
        assertFalse(pc1.equals(null));
    }
}