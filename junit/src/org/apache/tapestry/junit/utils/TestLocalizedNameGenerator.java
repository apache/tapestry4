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

import java.util.Locale;
import java.util.NoSuchElementException;

import junit.framework.AssertionFailedError;

import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.util.LocalizedNameGenerator;

/**
 *  Suite of tests for {@link org.apache.tapestry.util.LocalizedNameGenerator}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class TestLocalizedNameGenerator extends TapestryTestCase
{

    public void testBasic()
    {
        LocalizedNameGenerator g = new LocalizedNameGenerator("basic", Locale.US, ".test");

        assertTrue(g.more());
        assertEquals("basic_en_US.test", g.next());

        assertTrue(g.more());
        assertEquals("basic_en.test", g.next());

        assertTrue(g.more());
        assertEquals("basic.test", g.next());

        assertTrue(!g.more());
    }

    public void testNoCountry()
    {
        LocalizedNameGenerator g = new LocalizedNameGenerator("noCountry", Locale.FRENCH, ".zap");

        assertTrue(g.more());
        assertEquals("noCountry_fr.zap", g.next());

        assertTrue(g.more());
        assertEquals("noCountry.zap", g.next());

        assertTrue(!g.more());
    }

    public void testVariantWithoutCountry()
    {
        LocalizedNameGenerator g =
            new LocalizedNameGenerator("fred", new Locale("en", "", "GEEK"), ".foo");

        assertTrue(g.more());

        // The double-underscore is correct, it's a kind
        // of placeholder for the null country.
        // JDK1.3 always converts the locale to upper case.  JDK 1.4
        // does not.  To keep this test happyt, we selected an all-uppercase
        // locale.

        assertEquals("fred_en__GEEK.foo", g.next());

        assertTrue(g.more());
        assertEquals("fred_en.foo", g.next());

        assertTrue(g.more());
        assertEquals("fred.foo", g.next());

        assertTrue(!g.more());
    }

    public void testNullLocale()
    {
        LocalizedNameGenerator g = new LocalizedNameGenerator("nullLocale", null, ".bar");

        assertTrue(g.more());
        assertEquals("nullLocale.bar", g.next());

        assertTrue(!g.more());
    }

    public void testNullSuffix()
    {
        LocalizedNameGenerator g = new LocalizedNameGenerator("nullSuffix", null, null);

        assertTrue(g.more());
        assertEquals("nullSuffix", g.next());

        assertTrue(!g.more());
    }

    public void testForException()
    {
        LocalizedNameGenerator g = new LocalizedNameGenerator("bob", null, ".foo");

        assertTrue(g.more());
        assertEquals("bob.foo", g.next());

        assertTrue(!g.more());

        try
        {
            g.next();

            throw new AssertionFailedError("Unreachable.");
        }
        catch (NoSuchElementException ex)
        {
        }
    }
}
