// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.pageload;

import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests {@link org.apache.tapestry.pageload.PropertyReinitializer}.
 * 
 * @author Howard M. Lewis Ship
 */
public class TestPropertyReinitializer extends HiveMindTestCase
{
    public void testSuccess()
    {
        ComponentFixture c = new ComponentFixture();

        c.setStringProperty("initial-value");

        PropertyReinitializer i = new PropertyReinitializer(c, "stringProperty");

        i.pageDetached(null);

        assertEquals("initial-value", c.getStringProperty());

        c.setStringProperty("updated-value");

        i.pageDetached(null);

        assertEquals("initial-value", c.getStringProperty());
    }
}