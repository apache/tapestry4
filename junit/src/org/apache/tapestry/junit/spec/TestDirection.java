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

package org.apache.tapestry.junit.spec;

import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.spec.Direction;

/**
 * Simple tests for the {@link org.apache.tapestry.spec.Direction} class.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 3.0
 */
public class TestDirection extends TapestryTestCase
{
	public void testAllowInvariant()
	{
		assertEquals(true, Direction.IN.getAllowInvariant());
		assertEquals(true, Direction.CUSTOM.getAllowInvariant());
		assertEquals(true, Direction.AUTO.getAllowInvariant());
		assertEquals(false, Direction.FORM.getAllowInvariant());	
	}
	
	public void testGetDisplayName()
	{
		assertEquals("in", Direction.IN.getDisplayName());
		assertEquals("form", Direction.FORM.getDisplayName());
		assertEquals("auto", Direction.AUTO.getDisplayName());
		assertEquals("custom", Direction.CUSTOM.getDisplayName());
	}
}
