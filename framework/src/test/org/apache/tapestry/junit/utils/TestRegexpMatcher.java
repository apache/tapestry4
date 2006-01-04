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

package org.apache.tapestry.junit.utils;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.util.RegexpMatch;
import org.apache.tapestry.util.RegexpMatcher;

/**
 * Simple test case for {@link org.apache.tapestry.util.RegexpMatcher}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */
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

    /** @since 4.0 */

    public void testGetMatches()
    {
        RegexpMatcher m = new RegexpMatcher();

        String[] matches = m.getMatches("\\d+", "57,232 89 147", 0);

        assertListsEqual(new String[]
        { "57", "232", "89", "147" }, matches);
    }

    /** @since 4.0 */

    public void testGetMatchesAsObjects()
    {
        RegexpMatcher m = new RegexpMatcher();

        RegexpMatch[] matches = m.getMatches("\\w+(=(\\w+))?", "fred,barney=rubble;wilma=flintstone");

        assertEquals(3, matches.length);
        
        assertEquals("fred", matches[0].getInput());
        assertEquals("fred", matches[0].getGroup(0));
        
        assertEquals("barney=rubble", matches[1].getInput());
        assertEquals("rubble", matches[1].getGroup(2));
        
        assertEquals("wilma=flintstone", matches[2].getInput());
        assertEquals("flintstone", matches[2].getGroup(2));        
    }

    /** @since 4.0 */

    public void testGetMatchesNoMatch()
    {
        RegexpMatcher m = new RegexpMatcher();

        String[] matches = m.getMatches("A(B|C)", "aBCAaBA", 0);

        assertEquals(0, matches.length);
    }

    /** @since 4.0 */

    public void testGetMatchesSubgroup()
    {
        RegexpMatcher m = new RegexpMatcher();

        String matches[] = m.getMatches("A(B|C|fred)", "AA AC AB Afred AA AC", 1);

        assertListsEqual(new String[]
        { "C", "B", "fred", "C" }, matches);
    }

}
