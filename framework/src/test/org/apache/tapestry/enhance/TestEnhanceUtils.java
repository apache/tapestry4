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

package org.apache.tapestry.enhance;

import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRender;
import org.easymock.MockControl;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestEnhanceUtils extends HiveMindTestCase
{
    public void testTypeUnspecifiedWithNoExistingProperty()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getPropertyType("wilma");
        opc.setReturnValue(null);

        replayControls();

        Class result = EnhanceUtils.extractPropertyType(op, "wilma", null);

        assertEquals(Object.class, result);

        verifyControls();
    }

    public void testTypeUnspecifiedButExistingProperty()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getPropertyType("fred");
        opc.setReturnValue(Map.class);

        replayControls();

        Class result = EnhanceUtils.extractPropertyType(op, "fred", null);

        assertEquals(Map.class, result);

        verifyControls();
    }

    public void testTypeSpecified()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.convertTypeName("int[]");
        opc.setReturnValue(int[].class);

        op.validateProperty("betty", int[].class);

        replayControls();

        Class result = EnhanceUtils.extractPropertyType(op, "betty", "int[]");

        assertEquals(int[].class, result);

        verifyControls();
    }

    public void testCreateUnwrapForPrimitive()
    {
        EnhancementOperation op = (EnhancementOperation) newMock(EnhancementOperation.class);

        replayControls();

        String result = EnhanceUtils.createUnwrapExpression(op, "mybinding", int.class);

        assertEquals("org.apache.tapestry.enhance.EnhanceUtils.toInt(mybinding)", result);

        verifyControls();
    }

    public void testCreateUnwrapForObjectType()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getClassReference(String.class);
        opc.setReturnValue("_$class$String");

        replayControls();

        String result = EnhanceUtils.createUnwrapExpression(op, "thebinding", String.class);

        assertEquals("(java.lang.String) thebinding.getObject(_$class$String)", result);

        verifyControls();
    }

    public void testVerifyPropertyTypeNoProperty()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getPropertyType("foo");
        opc.setReturnValue(null);

        replayControls();

        assertEquals(Object.class, EnhanceUtils.verifyPropertyType(op, "foo", String.class));

        verifyControls();
    }

    public void testVerifyPropertyTypeSuccess()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getPropertyType("foo");
        opc.setReturnValue(Object.class);

        replayControls();

        assertEquals(Object.class, EnhanceUtils.verifyPropertyType(op, "foo", String.class));

        verifyControls();
    }

    public void testVerifyPropertyTypeWithDeclaredPropertyType()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getPropertyType("foo");
        opc.setReturnValue(IRender.class);

        replayControls();

        assertEquals(IRender.class, EnhanceUtils.verifyPropertyType(op, "foo", IComponent.class));

        verifyControls();

    }

    public void testVerifyPropertyTypeFailure()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getPropertyType("foo");
        opc.setReturnValue(String.class);

        replayControls();

        try
        {
            EnhanceUtils.verifyPropertyType(op, "foo", IComponent.class);
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Property foo is type java.lang.String, which is not compatible with org.apache.tapestry.IComponent.",
                    ex.getMessage());
        }

        verifyControls();

    }

}