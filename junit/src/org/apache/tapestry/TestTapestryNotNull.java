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

package org.apache.tapestry;

import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Test for {@link org.apache.tapestry.Defense#notNull(Object, String)}.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestTapestryNotNull extends HiveMindTestCase
{
    public void testSuccess()
    {
        Defense.notNull("foo", "bar");
    }

    public void testFailure()
    {
        try
        {
            Defense.notNull(null, "woops");
            unreachable();
        }
        catch (NullPointerException ex)
        {
            assertEquals(TapestryMessages.paramNotNull("woops"), ex.getMessage());
        }
    }
}
