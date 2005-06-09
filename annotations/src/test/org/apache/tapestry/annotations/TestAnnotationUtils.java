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

import java.lang.reflect.Method;

import org.apache.hivemind.ApplicationRuntimeException;

/**
 * Tests for {@link org.apache.tapestry.annotations.AnnotationUtils}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestAnnotationUtils extends BaseAnnotationTestCase
{
    private String attemptGetPropertyName(Class clazz, String name)
    {
        Method m = findMethod(clazz, name);

        return AnnotationUtils.getPropertyName(m);
    }

    public void testGetPropertyName()
    {
        assertEquals("stringValue", attemptGetPropertyName(Target.class, "getStringValue"));
        assertEquals("intValue", attemptGetPropertyName(Target.class, "setIntValue"));
        assertEquals("booleanValue", attemptGetPropertyName(Target.class, "isBooleanValue"));
    }

    public void testGetPropertyNameSetterNoParameters()
    {
        try
        {
            attemptGetPropertyName(Target.class, "setNoParameters");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Annotated method public abstract void org.apache.tapestry.annotations.Target.setNoParameters() is named like a mutator method,"
                            + " but takes an incorrect number of parameters (it should have exactly one parameter).",
                    ex.getMessage());
        }
    }

    public void testNonVoidSetter()
    {
        try
        {
            attemptGetPropertyName(Target.class, "setNonVoidMethod");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Annotated method public abstract java.lang.String org.apache.tapestry.annotations.Target.setNonVoidMethod(java.lang.String) "
                            + "is named like a mutator method, but does not return void.",
                    ex.getMessage());
        }
    }

    public void testGetterWithParameters()
    {
        try
        {
            attemptGetPropertyName(Target.class, "getHasParameters");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Annotated method public abstract java.lang.String org.apache.tapestry.annotations.Target.getHasParameters(java.lang.String) "
                            + "is expected to be an accessor, and should have no parameters.",
                    ex.getMessage());
        }
    }

    public void testGetterReturnsVoid()
    {
        try
        {
            attemptGetPropertyName(Target.class, "isVoidGetter");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Annotated method public abstract void org.apache.tapestry.annotations.Target.isVoidGetter() "
                            + "is named like an accessor method, but returns void.",
                    ex.getMessage());
        }
    }

}
