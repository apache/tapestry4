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

package org.apache.tapestry.annotations;

import org.apache.hivemind.ApplicationRuntimeException;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * Tests for {@link org.apache.tapestry.annotations.AnnotationUtils}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestAnnotationUtils extends BaseAnnotationTestCase
{
    private String attemptGetPropertyName(Class clazz, String name)
    {
        Method m = findMethod(clazz, name);

        return AnnotationUtils.getPropertyName(m);
    }

    public void testGetPropertyName()
    {
        assertEquals("stringValue", attemptGetPropertyName(TargetValues.class, "getStringValue"));
        assertEquals("intValue", attemptGetPropertyName(TargetValues.class, "setIntValue"));
        assertEquals("booleanValue", attemptGetPropertyName(TargetValues.class, "isBooleanValue"));
    }

    public void testGetPropertyNameNotAGetter()
    {
        try
        {
            attemptGetPropertyName(TargetValues.class, "notAGetter");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Annotated method public abstract java.lang.String org.apache.tapestry.annotations.TargetValues.notAGetter() "
                            + "should be an accessor (no parameters), or a mutator (single parameter, returns void).",
                    ex.getMessage());
        }
    }

    public void testGetPropertyNameSetterNoParameters()
    {
        try
        {
            attemptGetPropertyName(TargetValues.class, "setNoParameters");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Annotated method public abstract void org.apache.tapestry.annotations.TargetValues.setNoParameters() is named like a mutator method,"
                            + " but takes an incorrect number of parameters (it should have exactly one parameter).",
                    ex.getMessage());
        }
    }

    public void testNonVoidSetter()
    {
        try
        {
            attemptGetPropertyName(TargetValues.class, "setNonVoidMethod");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Annotated method public abstract java.lang.String org.apache.tapestry.annotations.TargetValues.setNonVoidMethod(java.lang.String) "
                            + "is named like a mutator method, but does not return void.",
                    ex.getMessage());
        }
    }

    public void testGetterWithParameters()
    {
        try
        {
            attemptGetPropertyName(TargetValues.class, "getHasParameters");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Annotated method public abstract java.lang.String org.apache.tapestry.annotations.TargetValues.getHasParameters(java.lang.String) "
                            + "is expected to be an accessor, and should have no parameters.",
                    ex.getMessage());
        }
    }

    public void testGetterReturnsVoid()
    {
        try
        {
            attemptGetPropertyName(TargetValues.class, "isVoidGetter");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Annotated method public abstract void org.apache.tapestry.annotations.TargetValues.isVoidGetter() "
                            + "is named like an accessor method, but returns void.",
                    ex.getMessage());
        }
    }
    
    public void testConvertMethodNameToKeyName()
    {
        assertEquals("foo-bar", AnnotationUtils.convertMethodNameToKeyName("fooBar"));
        assertEquals("foo-bar", AnnotationUtils.convertMethodNameToKeyName("FooBar"));
        assertEquals("foo-bar", AnnotationUtils.convertMethodNameToKeyName("getFooBar"));
        assertEquals("foo", AnnotationUtils.convertMethodNameToKeyName("foo"));
    }    

}
