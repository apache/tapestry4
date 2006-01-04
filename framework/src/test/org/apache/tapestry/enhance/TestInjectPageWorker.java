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

package org.apache.tapestry.enhance;

import java.lang.reflect.Modifier;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.spec.InjectSpecification;
import org.apache.tapestry.spec.InjectSpecificationImpl;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.enhance.InjectPageWorker}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestInjectPageWorker extends HiveMindTestCase
{
    public void testPrimitivePropertyType()
    {
        Location l = newLocation();

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getPropertyType("somePage");
        opc.setReturnValue(int.class);

        replayControls();

        InjectSpecification is = newSpec(l);

        try
        {
            new InjectPageWorker().performEnhancement(op, is);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Property somePage is type int, which is not compatible with injection. The property type should be Object, IPage, or a specific page class.",
                    ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verifyControls();
    }

    private InjectSpecification newSpec(Location l)
    {
        InjectSpecification is = new InjectSpecificationImpl();
        is.setProperty("somePage");
        is.setObject("SomePage");
        is.setLocation(l);

        return is;
    }

    /**
     * Test for when there's no existing property.
     */

    public void testNoPropertyType()
    {
        Location l = newLocation();

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getPropertyType("somePage");
        opc.setReturnValue(null);

        op.claimReadonlyProperty("somePage");

        op.getAccessorMethodName("somePage");
        opc.setReturnValue("getSomePage");

        MethodSignature sig = new MethodSignature(Object.class, "getSomePage", null, null);

        op.addMethod(
                Modifier.PUBLIC,
                sig,
                "return getPage().getRequestCycle().getPage(\"SomePage\");",
                l);

        replayControls();

        InjectSpecification is = newSpec(l);

        new InjectPageWorker().performEnhancement(op, is);

        verifyControls();
    }

    public void testExistingPropertyType()
    {
        Location l = newLocation();

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getPropertyType("somePage");
        opc.setReturnValue(BasePage.class);

        op.claimReadonlyProperty("somePage");

        op.getAccessorMethodName("somePage");
        opc.setReturnValue("getSomePage");

        MethodSignature sig = new MethodSignature(BasePage.class, "getSomePage", null, null);

        // BasePage is too specific (the getPage() method returns IPage),
        // so we see a cast.

        op
                .addMethod(
                        Modifier.PUBLIC,
                        sig,
                        "return (org.apache.tapestry.html.BasePage)getPage().getRequestCycle().getPage(\"SomePage\");",
                        l);

        replayControls();

        InjectSpecification is = newSpec(l);

        new InjectPageWorker().performEnhancement(op, is);

        verifyControls();
    }
}
