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

import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for the {@link org.apache.tapestry.test.ParameterList}class.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestParameterList extends HiveMindTestCase
{
    public void testEmpty()
    {
        ParameterList l = new ParameterList();

        assertListsEqual(new String[0], l.getValues());
    }

    public void testAdd()
    {
        ParameterList l = new ParameterList();

        l.add("alpha");
        l.add("beta");
        l.add("gamma");

        assertListsEqual(new String[]
        { "alpha", "beta", "gamma" }, l.getValues());
    }

    public void testAddDuplicates()
    {
        ParameterList l = new ParameterList();

        l.add("alpha");
        l.add("beta");
        l.add("gamma");
        l.add("gamma");
        l.add("alpha");

        assertListsEqual(new String[]
        { "alpha", "beta", "gamma", "gamma", "alpha" }, l.getValues());

    }
}