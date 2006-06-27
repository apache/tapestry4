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

package org.apache.tapestry.junit;

import static org.testng.AssertJUnit.assertEquals;

import org.apache.tapestry.multipart.ValuePart;
import org.testng.annotations.Test;

/**
 * A few tests to fill in the code coverage of {@link org.apache.tapestry.multipart.ValuePart}and
 * {@link org.apache.tapestry.multipart.UploadPart}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */
@Test
public class TestMultipart extends TapestryTestCase
{

    public void testSingle()
    {
        ValuePart p = new ValuePart("first");

        assertEquals(1, p.getCount());
        assertEquals("first", p.getValue());

        checkList("values", new String[]
        { "first" }, p.getValues());
    }

    public void testTwo()
    {
        ValuePart p = new ValuePart("alpha");

        p.add("beta");

        assertEquals(2, p.getCount());
        assertEquals("alpha", p.getValue());
        checkList("values", new String[]
        { "alpha", "beta" }, p.getValues());
    }

    public void testThree()
    {
        ValuePart p = new ValuePart("moe");
        p.add("larry");
        p.add("curly");

        checkList("values", new String[]
        { "moe", "larry", "curly" }, p.getValues());
    }
}
