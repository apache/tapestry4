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

    public void testInterface() throws Exception
    {

        try
        {
            Creator c = new Creator();

            c.newInstance(List.class);
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
        Creator c = new Creator();

        StringSubject s = (StringSubject) c.newInstance(StringSubject.class);

        s.setTitle("title");

        assertEquals("title", s.getTitle());
    }

    public void testPrimitiveType()
    {
        Creator c = new Creator();

        IntSubject s = (IntSubject) c.newInstance(IntSubject.class);

        s.setPriority(-1);

        assertEquals(-1, s.getPriority());
    }

    public void testArrayType()
    {
        Creator c = new Creator();

        ArraySubject s = (ArraySubject) c.newInstance(ArraySubject.class);

        int[] counts = new int[]
        { 3, 7, 9 };

        s.setCounts(counts);

        assertSame(counts, s.getCounts());
    }

    public void testInherited()
    {
        Creator c = new Creator();

        InheritedSubject s = (InheritedSubject) c.newInstance(InheritedSubject.class);

        s.setFlag(true);
        s.setPriority(5);

        assertEquals(true, s.getFlag());
        assertEquals(5, s.getPriority());
    }

    public void testMethodNameNotOverriden()
    {
        Creator c = new Creator();

        BooleanSubject s = (BooleanSubject) c.newInstance(BooleanSubject.class);

        s.setKnown(true);

        assertEquals(true, s.isKnown());
    }

    public void testUniqueInstances()
    {
        Creator c = new Creator();

        StringSubject s1 = (StringSubject) c.newInstance(StringSubject.class);
        StringSubject s2 = (StringSubject) c.newInstance(StringSubject.class);

        assertNotSame(s1, s2);
    }

    public void testInitializer()
    {
        Creator c = new Creator();

        StringSubject ss = (StringSubject) c.newInstance(StringSubject.class, new Object[]
        { "title", "Hitchhiker's Guide" });

        assertEquals("Hitchhiker's Guide", ss.getTitle());
    }
}