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

package org.apache.tapestry.junit.utils;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.util.RegexpMatcher;

/**
 *  Simple test case for {@link org.apache.tapestry.util.RegexpMatcher}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/
public class TestRegexpMatcher extends TapestryTestCase
{

    public void testMatch()
    {
        RegexpMatcher m = new RegexpMatcher();

        assertTrue(m.matches("[a-z]+", "c"));
        assertTrue(m.matches("foo|foot", "foo"));
    }

    public void testNonmatch()
    {
        RegexpMatcher m = new RegexpMatcher();

        assertTrue(!m.matches("[0-9]+", "q"));
        assertTrue(!m.matches("foo|foot", "foot"));
    }

    public void testBadPattern()
    {
        RegexpMatcher m = new RegexpMatcher();

        try
        {
            m.matches("[[[", "x");

            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            checkException(ex, "Unmatched [] in expression");
        }
    }

    public void testClear()
    {
        RegexpMatcher m = new RegexpMatcher();

        m.clear();
    }

    public void testContains()
    {
        RegexpMatcher m = new RegexpMatcher();

        assertTrue(m.contains("[a-z]+", "c"));
        assertTrue(m.contains("^(\\d{5}(-\\d{4})?)$", "06514"));
        assertTrue(m.contains("^(\\d{5}(-\\d{4})?)$", "06514-3149"));
        assertTrue(m.contains("foo|foot", "12foot12"));
    }

    public void testNotContains()
    {
        RegexpMatcher m = new RegexpMatcher();

        assertTrue(!m.contains("[0-9]+", "q"));
        assertTrue(!m.contains("^(\\d{5}(-\\d{4})?)$", "0651"));
        assertTrue(!m.contains("^(\\d{5}(-\\d{4})?)$", "065147"));
        assertTrue(!m.contains("^(\\d{5}(-\\d{4})?)$", "06514-314"));
        assertTrue(!m.contains("^(\\d{5}(-\\d{4})?)$", "06514-31497"));
        assertTrue(!m.contains("^(foo|foot)$", "12foot12"));
    }

    public void testGetEscapedPatternStrings()
    {
        RegexpMatcher m = new RegexpMatcher();

        assertEquals(m.getEscapedPatternString("^\\d$"), "\\^\\\\d\\$");
    }
}
