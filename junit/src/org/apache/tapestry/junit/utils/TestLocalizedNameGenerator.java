/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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

    public TestLocalizedNameGenerator(String name)
    {
        super(name);
    }

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
