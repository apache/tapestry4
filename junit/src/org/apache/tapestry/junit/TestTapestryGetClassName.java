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

package org.apache.tapestry.junit;

import org.apache.tapestry.Tapestry;

/**
 *  Tests for {@link org.apache.tapestry.Tapestry#getClassName(Class)}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/
public class TestTapestryGetClassName extends TapestryTestCase
{

    private void attempt(String expected, Class subject)
    {
        String actual = Tapestry.getClassName(subject);

        assertEquals(expected, actual);
    }

    public void testObject()
    {
        attempt("java.lang.Object", Object.class);
    }

    public void testObjectArray()
    {
        attempt("java.lang.Object[]", Object[].class);
    }

    public void testStringArray()
    {
        attempt("java.lang.String[]", String[].class);
    }

    public void testMultiArray()
    {
        attempt("java.lang.String[][]", String[][].class);
    }

    public void testScalar()
    {
        attempt("boolean", boolean.class);
    }

    public void testScalarArray()
    {
        attempt("int[]", int[].class);
    }
}
