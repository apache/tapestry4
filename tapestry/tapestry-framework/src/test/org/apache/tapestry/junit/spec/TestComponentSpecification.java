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

package org.apache.tapestry.junit.spec;

import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.spec.*;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Test cases for page and component specifications.
 *
 * @author Howard Lewis Ship
 * @since 2.2
 */
@Test
public class TestComponentSpecification extends TapestryTestCase
{

    public void test_Bean_Property() throws Exception
    {
        IComponentSpecification s = parseComponent("BeanProperty.jwc");
        IBeanSpecification fred = s.getBeanSpecification("fred");

        checkList("propertyNames",
                  new String[]{ "bruce", "nicole", "zeta" },
                  fred.getPropertyNames());

        checkProperty(fred, "bruce", "wayne");
        checkProperty(fred, "nicole", "kidman");
        checkProperty(fred, "zeta", "jones");

    }

    public void test_Component_Property() throws Exception
    {
        IComponentSpecification s = parseComponent("ComponentProperty.jwc");
        IContainedComponent c = s.getComponent("barney");

        checkList("propertyNames", new String[]
          { "apple", "chocolate", "frozen" }, c.getPropertyNames());

        checkProperty(c, "apple", "pie");
        checkProperty(c, "chocolate", "cake");
        checkProperty(c, "frozen", "yogurt");

    }

    public void test_Asset_Property() throws Exception
    {
        IComponentSpecification s = parseComponent("AssetProperty.jwc");

        checkAsset(s, "private", "hugh", "grant");
        checkAsset(s, "external", "joan", "rivers");
        checkAsset(s, "context", "john", "cusak");
    }

    private void checkAsset(IComponentSpecification s, String assetName, String propertyName,
                            String expectedValue)
    {
        IAssetSpecification a = s.getAsset(assetName);

        assertEquals(expectedValue, a.getProperty(propertyName));
    }

    /** @since 4.0 */

    public void testGetReservedParameterNames()
    {
        IComponentSpecification s = new ComponentSpecification();

        assertEquals(Collections.EMPTY_SET, s.getReservedParameterNames());

        s.addReservedParameterName("Fred");

        Set expected = new HashSet();

        expected.add("fred");

        assertEquals(expected, s.getReservedParameterNames());

        IParameterSpecification ps = new ParameterSpecification();

        ps.setAliases("wilma,barney");
        ps.setParameterName("bambam");

        s.addParameter(ps);

        expected.add("wilma");
        expected.add("barney");
        expected.add("bambam");

        assertEquals(expected, s.getReservedParameterNames());

        try
        {
            s.getReservedParameterNames().clear();
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // expected
        }

    }
}
