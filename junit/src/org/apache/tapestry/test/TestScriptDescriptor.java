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

package org.apache.tapestry.test;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.test.ScriptDescriptor;
import org.apache.tapestry.test.ServletDescriptor;

/**
 * Additional tests for {@link TestScriptDescriptor}.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestScriptDescriptor extends HiveMindTestCase
{
	public void testDefaultServletDescriptor()
	{
		ScriptDescriptor sd = new ScriptDescriptor();
		
		assertNull(sd.getDefaultServletDescriptor());
		
		ServletDescriptor s1 = new ServletDescriptor();
		s1.setName("default");
		
		sd.addServletDescriptor(s1);
		
		ServletDescriptor s2 = new ServletDescriptor();
		s2.setName("not-default");
		
		sd.addServletDescriptor(s2);
		
		assertSame(s1, sd.getDefaultServletDescriptor());
	}
}
