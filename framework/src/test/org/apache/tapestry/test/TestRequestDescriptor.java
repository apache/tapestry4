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

package org.apache.tapestry.test;

import java.util.Arrays;

import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.test.RequestDescriptor}.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestRequestDescriptor extends HiveMindTestCase
{
    private static class Assertion implements ResponseAssertion
    {
        private boolean _executed;

        public void execute(ScriptedTestSession session)
        {
            _executed = true;
        }

        boolean getExecuted()
        {
            return _executed;
        }
    }

    public void testAdd()
    {
        RequestDescriptor d = new RequestDescriptor();

        d.addParameter("frank", "burns");

        assertListsEqual(new String[]
        { "burns" }, d.getParameterValues("frank"));
    }

    public void testGetNames()
    {
        RequestDescriptor d = new RequestDescriptor();

        assertListsEqual(new Object[0], d.getParameterNames());

        d.addParameter("flintstone", "fred");
        d.addParameter("rubble", "barney");

        String[] names = d.getParameterNames();

        // Don't know what order they come back in, so sort them.

        Arrays.sort(names);

        assertListsEqual(new String[]
        { "flintstone", "rubble" }, names);
    }

    public void testAddMultiple()
    {
        RequestDescriptor d = new RequestDescriptor();

        d.addParameter("flintstone", "fred");
        d.addParameter("flintstone", "wilma");
        d.addParameter("flintstone", "dino");

        assertListsEqual(new String[]
        { "fred", "wilma", "dino" }, d.getParameterValues("flintstone"));
    }

    public void testGetUnknown()
    {
        RequestDescriptor d = new RequestDescriptor();

        assertNull(d.getParameterValues("unknown"));
    }

    public void testServletName()
    {
        RequestDescriptor d = new RequestDescriptor();

        assertNull(d.getServletName());

        d.setServletName("servlet");

        assertEquals("servlet", d.getServletName());
    }

    public void testExecuteAssertions()
    {
        RequestDescriptor d = new RequestDescriptor();

        Assertion a1 = new Assertion();
        Assertion a2 = new Assertion();

        d.addAssertion(a1);
        d.addAssertion(a2);

        d.executeAssertions(null);

        assertEquals(true, a1.getExecuted());
        assertEquals(true, a2.getExecuted());
    }

}