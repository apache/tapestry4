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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;

import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRender;
import org.testng.annotations.Test;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestEnhanceUtils extends BaseComponentTestCase
{
    protected EnhancementOperation newOp()
    {
        return newMock(EnhancementOperation.class);
    }
    
    public void testTypeUnspecifiedWithNoExistingProperty()
    {
        EnhancementOperation op = newOp();
        
        expect(op.getPropertyType("wilma")).andReturn(null);

        replay();

        Class result = EnhanceUtils.extractPropertyType(op, "wilma", null);

        assertEquals(Object.class, result);

        verify();
    }

    public void testTypeUnspecifiedButExistingProperty()
    {
        EnhancementOperation op = newOp();

        expect(op.getPropertyType("fred")).andReturn(Map.class);

        replay();

        Class result = EnhanceUtils.extractPropertyType(op, "fred", null);

        assertEquals(Map.class, result);

        verify();
    }

    public void testTypeSpecified()
    {
        EnhancementOperation op = newOp();

        expect(op.convertTypeName("int[]")).andReturn(int[].class);

        op.validateProperty("betty", int[].class);

        replay();

        Class result = EnhanceUtils.extractPropertyType(op, "betty", "int[]");

        assertEquals(int[].class, result);

        verify();
    }

    public void testCreateUnwrapForPrimitive()
    {
        EnhancementOperation op = newOp();

        replay();

        String result = EnhanceUtils.createUnwrapExpression(op, "mybinding", int.class);

        assertEquals("org.apache.tapestry.enhance.EnhanceUtils.toInt(mybinding)", result);

        verify();
    }

    public void testCreateUnwrapForObjectType()
    {
        EnhancementOperation op = newOp();

        expect(op.getClassReference(String.class)).andReturn("_$class$String");

        replay();

        String result = EnhanceUtils.createUnwrapExpression(op, "thebinding", String.class);

        assertEquals("(java.lang.String) thebinding.getObject(_$class$String)", result);

        verify();
    }

    public void testVerifyPropertyTypeNoProperty()
    {
        EnhancementOperation op = newOp();

        expect(op.getPropertyType("foo")).andReturn(null);

        replay();

        assertEquals(Object.class, EnhanceUtils.verifyPropertyType(op, "foo", String.class));

        verify();
    }

    public void testVerifyPropertyTypeSuccess()
    {
        EnhancementOperation op = newOp();

        expect(op.getPropertyType("foo")).andReturn(Object.class);

        replay();

        assertEquals(Object.class, EnhanceUtils.verifyPropertyType(op, "foo", String.class));

        verify();
    }

    public void testVerifyPropertyTypeWithDeclaredPropertyType()
    {
        EnhancementOperation op = newOp();

        expect(op.getPropertyType("foo")).andReturn(IRender.class);

        replay();

        assertEquals(IRender.class, EnhanceUtils.verifyPropertyType(op, "foo", IComponent.class));

        verify();

    }

    public void testVerifyPropertyTypeFailure()
    {
        EnhancementOperation op = newOp();

        expect(op.getPropertyType("foo")).andReturn(String.class);

        replay();

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

        verify();
    }

    protected IBinding newBinding(Class expectedType, Object value)
    {
        IBinding binding = newMock(IBinding.class);

        expect(binding.getObject(expectedType)).andReturn(value);

        return binding;
    }

    public void testToBooleanNull()
    {
        IBinding binding = newBinding(Boolean.class, null);

        replay();

        assertEquals(false, EnhanceUtils.toBoolean(binding));

        verify();
    }

    public void testToBoolean()
    {
        IBinding binding = newBinding(Boolean.class, Boolean.TRUE);

        replay();

        assertEquals(true, EnhanceUtils.toBoolean(binding));

        verify();
    }

    public void testToByteNull()
    {
        IBinding binding = newBinding(Byte.class, null);

        replay();

        assertEquals(0, EnhanceUtils.toByte(binding));

        verify();
    }

    public void testToByte()
    {
        IBinding binding = newBinding(Byte.class, new Byte((byte) 37));

        replay();

        assertEquals(37, EnhanceUtils.toByte(binding));

        verify();
    }

    public void testToCharNull()
    {
        IBinding binding = newBinding(Character.class, null);

        replay();

        assertEquals(0, EnhanceUtils.toChar(binding));

        verify();
    }

    public void testToChar()
    {
        IBinding binding = newBinding(Character.class, new Character('q'));

        replay();

        assertEquals('q', EnhanceUtils.toChar(binding));

        verify();
    }

    public void testToShortNull()
    {
        IBinding binding = newBinding(Short.class, null);

        replay();

        assertEquals(0, EnhanceUtils.toShort(binding));

        verify();
    }

    public void testToShort()
    {
        IBinding binding = newBinding(Short.class, new Short((short) 99));

        replay();

        assertEquals(99, EnhanceUtils.toShort(binding));

        verify();
    }

    public void testToIntNull()
    {
        IBinding binding = newBinding(Integer.class, null);

        replay();

        assertEquals(0, EnhanceUtils.toInt(binding));

        verify();
    }

    public void testToInt()
    {
        IBinding binding = newBinding(Integer.class, new Integer(107));

        replay();

        assertEquals(107, EnhanceUtils.toInt(binding));

        verify();
    }

    public void testToLongNull()
    {
        IBinding binding = newBinding(Long.class, null);

        replay();

        assertEquals(0, EnhanceUtils.toLong(binding));

        verify();
    }

    public void testToLong()
    {
        IBinding binding = newBinding(Long.class, new Long(90000));

        replay();

        assertEquals(90000, EnhanceUtils.toLong(binding));

        verify();
    }

    public void testToFloatNull()
    {
        IBinding binding = newBinding(Float.class, null);

        replay();

        assertEquals(0.0f, EnhanceUtils.toFloat(binding), 0.0f);

        verify();
    }

    public void testToFloat()
    {
        IBinding binding = newBinding(Float.class, new Float(2.5f));

        replay();

        assertEquals(2.5f, EnhanceUtils.toFloat(binding), 0.0f);

        verify();
    }

    public void testToDoubleNull()
    {
        IBinding binding = newBinding(Double.class, null);

        replay();

        assertEquals(0.0d, EnhanceUtils.toDouble(binding), 0.0d);

        verify();
    }

    public void testToDouble()
    {
        IBinding binding = newBinding(Double.class, new Double(2.5d));

        replay();

        assertEquals(2.5d, EnhanceUtils.toDouble(binding), 0.0d);

        verify();
    }

}