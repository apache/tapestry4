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

package org.apache.tapestry.junit.enhance;

import java.lang.reflect.Method;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.enhance.MethodSignature;
import org.apache.tapestry.junit.TapestryTestCase;

/**
 *  Tests for the {@link org.apache.tapestry.enhance.MethodSignature}
 *  class used by the {@link org.apache.tapestry.enhance.DefaultComponentClassEnhancer}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/
public class TestMethodSignature extends TapestryTestCase
{

    private MethodSignature build(Class subject, String methodName)
    {
        Method[] m = subject.getDeclaredMethods();

        for (int i = 0; i < m.length; i++)
        {
            if (m[i].getName().equals(methodName))
                return new MethodSignature(m[i]);
        }

        unreachable();

        return null;
    }

    public void testEquals()
    {
        // No parameters
        MethodSignature m1 = build(Object.class, "hashCode");
        MethodSignature m2 = build(Integer.class, "hashCode");

        // With parameters
        MethodSignature m3 = build(AbstractComponent.class, "renderComponent");
        MethodSignature m4 = build(BaseComponent.class, "renderComponent");

        assertEquals(true, m1.equals(m2));
        assertEquals(true, m3.equals(m4));
    }

    public void testUnequal()
    {
        MethodSignature m1 = build(Object.class, "hashCode");
        MethodSignature m2 = build(Integer.class, "toString");

        assertEquals(false, m1.equals(m2));

        assertEquals(false, m1.equals(null));

        assertEquals(false, m1.equals("hashCode"));
    }

    public void testHashCode()
    {
        MethodSignature m1 = build(Object.class, "toString");
        MethodSignature m2 = build(StringBuffer.class, "toString");
        MethodSignature m3 = build(AbstractComponent.class, "renderComponent");
        MethodSignature m4 = build(BaseComponent.class, "renderComponent");

        assertEquals(true, m1.hashCode() == m2.hashCode());
        assertEquals(true, m3.hashCode() == m4.hashCode());

        // Different signatures should have different hashcode

        assertEquals(true, m1.hashCode() != m3.hashCode());
    }

}
