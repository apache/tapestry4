// Copyright 2004 The Apache Software Foundation
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

import java.util.ArrayList;
import java.util.List;

import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.test.Creator}.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestCreator extends HiveMindTestCase
{

    public void testNonAbstract() throws Exception
    {
        interceptLogging("org.apache.tapestry.test");

        Creator i = new Creator();

        Object result = i.getInstance(ArrayList.class);

        assertEquals(ArrayList.class, result.getClass());

        assertLoggedMessage("Class java.util.ArrayList is not an abstract class.");

    }

    public void testInterface() throws Exception
    {

        try
        {
            Creator i = new Creator();

            i.getInstance(List.class);
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertEquals(
                ex.getMessage(),
                "Can not create instance of java.util.List. Interfaces, arrays and primitive types may not be enhanced.");
        }

    }

    public void testObjectType()
    {
        Creator i = new Creator();

        StringSubject s = (StringSubject) i.getInstance(StringSubject.class);

        s.setTitle("title");

        assertEquals("title", s.getTitle());
    }

    public void testPrimitiveType()
    {
        Creator i = new Creator();

        IntSubject s = (IntSubject) i.getInstance(IntSubject.class);

        s.setPriority(-1);

        assertEquals(-1, s.getPriority());
    }

    public void testArrayType()
    {
        Creator i = new Creator();

        ArraySubject s = (ArraySubject) i.getInstance(ArraySubject.class);

        int[] counts = new int[] { 3, 7, 9 };

        s.setCounts(counts);

        assertSame(counts, s.getCounts());
    }

    public void testInherited()
    {
        Creator i = new Creator();

        InheritedSubject s = (InheritedSubject) i.getInstance(InheritedSubject.class);

        s.setFlag(true);
        s.setPriority(5);

        assertEquals(true, s.getFlag());
        assertEquals(5, s.getPriority());
    }

    public void testMethodNameNotOverriden()
    {
        Creator i = new Creator();

        BooleanSubject s = (BooleanSubject) i.getInstance(BooleanSubject.class);

        s.setKnown(true);

        assertEquals(true, s.isKnown());
    }

    public void testUniqueInstances()
    {
        Creator i = new Creator();

        StringSubject s1 = (StringSubject) i.getInstance(StringSubject.class);
        StringSubject s2 = (StringSubject) i.getInstance(StringSubject.class);

        assertNotSame(s1, s2);
    }
}
