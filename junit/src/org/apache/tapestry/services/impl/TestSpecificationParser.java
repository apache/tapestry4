// Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.services.impl;

import org.apache.hivemind.Registry;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.services.SpecificationParser;
import org.apache.tapestry.spec.IApplicationSpecification;

/**
 * Tests for {@link org.apache.tapestry.services.impl.SpecificationParserImpl}
 * service implementation.
 *
 * @author Howard Lewis Ship
 */
public class TestSpecificationParser extends TapestryTestCase
{
    public void testParseApp()
    {
        Resource r = getResource("ParseApp.application");

        SpecificationParserImpl p = new SpecificationParserImpl();
        p.setClassResolver(new DefaultClassResolver());

        p.initializeService();

        IApplicationSpecification s = p.parseApplicationSpecification(r);

        assertEquals("ParseApp", s.getName());
    }

	/**
	 * Validate that the service is defined properly inside the module
	 * deployment descriptor.
	 */
    public void testIntegration()
    {
        Registry registry = RegistryBuilder.constructDefaultRegistry();

        SpecificationParser p =
            (SpecificationParser) registry.getService(
                "tapestry.SpecificationParser",
                SpecificationParser.class);

        Resource r = getResource("ParseApp.application");


		// May need a beefier spec to validate that the class resolver
		// is being set and used properly.
		
        IApplicationSpecification s = p.parseApplicationSpecification(r);

        assertEquals("ParseApp", s.getName());
    }
}
