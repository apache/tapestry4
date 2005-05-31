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

package org.apache.tapestry.spec;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests a number of <em>failure</em> cases in
 * {@link org.apache.tapestry.spec.ComponentSpecification}. Success cases are covered by
 * {@link org.apache.tapestry.junit.parse.TestSpecificationParser} and the (ugly and slow) mock
 * integration tests.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestComponentSpecification extends HiveMindTestCase
{
    public void testClaimPropertyOK()
    {
        InjectSpecificationImpl inject1 = new InjectSpecificationImpl();
        inject1.setProperty("fred");

        InjectSpecificationImpl inject2 = new InjectSpecificationImpl();
        inject2.setProperty("barney");

        ComponentSpecification cs = new ComponentSpecification();

        cs.addInjectSpecification(inject1);
        cs.addInjectSpecification(inject2);

        assertEquals(2, cs.getInjectSpecifications().size());
    }

    public void testClaimPropertyConflict()
    {
        Location l1 = fabricateLocation(13);
        Location l2 = fabricateLocation(97);

        InjectSpecificationImpl inject1 = new InjectSpecificationImpl();
        inject1.setProperty("fred");
        inject1.setLocation(l1);

        InjectSpecificationImpl inject2 = new InjectSpecificationImpl();
        inject2.setProperty("fred");
        inject2.setLocation(l2);

        ComponentSpecification cs = new ComponentSpecification();

        cs.addInjectSpecification(inject1);

        try
        {
            cs.addInjectSpecification(inject2);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(SpecMessages.claimedProperty("fred", inject1), ex.getMessage());
            assertSame(l2, ex.getLocation());
        }

        assertEquals(1, cs.getInjectSpecifications().size());
    }

    public void testAddAssetConflict()
    {
        Location l1 = fabricateLocation(13);
        Location l2 = fabricateLocation(97);

        AssetSpecification asset1 = new AssetSpecification();
        asset1.setLocation(l1);

        AssetSpecification asset2 = new AssetSpecification();
        asset2.setLocation(l2);

        ComponentSpecification cs = new ComponentSpecification();

        cs.addAsset("wilma", asset1);
        try
        {
            cs.addAsset("wilma", asset2);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(SpecMessages.duplicateAsset("wilma", asset1), ex.getMessage());
            assertSame(l2, ex.getLocation());
        }
    }

    public void testAddComponentConflict()
    {
        Location l1 = fabricateLocation(13);
        Location l2 = fabricateLocation(97);

        ContainedComponent cc1 = new ContainedComponent();
        cc1.setLocation(l1);

        ContainedComponent cc2 = new ContainedComponent();
        cc2.setLocation(l2);

        ComponentSpecification cs = new ComponentSpecification();

        cs.addComponent("fred", cc1);
        try
        {
            cs.addComponent("fred", cc2);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(SpecMessages.duplicateComponent("fred", cc1), ex.getMessage());
            assertSame(l2, ex.getLocation());
        }
    }

    public void testAddParameterConflict()
    {
        Location l1 = fabricateLocation(13);
        Location l2 = fabricateLocation(97);

        ParameterSpecification p1 = new ParameterSpecification();
        p1.setParameterName("dino");
        p1.setLocation(l1);

        ParameterSpecification p2 = new ParameterSpecification();
        p2.setParameterName("dino");
        p2.setLocation(l2);

        ComponentSpecification cs = new ComponentSpecification();

        cs.addParameter(p1);

        try
        {
            cs.addParameter(p2);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(SpecMessages.duplicateParameter("dino", p1), ex.getMessage());
            assertSame(l2, ex.getLocation());
        }
    }

    public void testAddBeanSpecificationConflict()
    {
        Location l1 = fabricateLocation(13);
        Location l2 = fabricateLocation(97);

        BeanSpecification b1 = new BeanSpecification();
        b1.setLocation(l1);

        BeanSpecification b2 = new BeanSpecification();
        b2.setLocation(l2);

        ComponentSpecification cs = new ComponentSpecification();

        cs.addBeanSpecification("wilma", b1);

        try
        {
            cs.addBeanSpecification("wilma", b2);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(SpecMessages.duplicateBean("wilma", b1), ex.getMessage());
            assertSame(l2, ex.getLocation());
        }

    }
}
