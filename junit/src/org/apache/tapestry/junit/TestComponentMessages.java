/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.junit;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.tapestry.IPage;

/**
 *  Tests the class {@link org.apache.tapestry.engine.DefaultStringsSource}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public class TestComponentMessages extends TapestryTestCase
{

    public TestComponentMessages(String name)
    {
        super(name);
    }

    private void check(IPage page, String key, String expected)
    {
        String actual = page.getMessage(key);

        assertEquals("Key " + key, expected, actual);
    }

    private static final String MOCK1 = "/org/apache/tapestry/junit/MockPage1.jwc";

    public void testOnlyInBase()
    {
        IPage page = createPage(MOCK1, new Locale("en", "US"));

        check(page, "only-in-base", "BASE1");
    }

    public void testMissingKey()
    {
        IPage page = createPage(MOCK1, new Locale("en", "GB"));

        check(page, "non-existant-key", "[NON-EXISTANT-KEY]");
    }

    public void testOverwrittenInLanguage()
    {
        IPage page = createPage(MOCK1, new Locale("en", "US"));

        check(page, "overwritten-in-language", "LANGUAGE1_en");

        page = createPage(MOCK1, new Locale("fr", ""));

        check(page, "overwritten-in-language", "LANGUAGE1_fr");
    }

    public void testOverwrittenInCountry()
    {
        IPage page = createPage(MOCK1, new Locale("en", "US"));

        check(page, "overwritten-in-country", "COUNTRY1_en_US");

        page = createPage(MOCK1, new Locale("fr", "CD"));

        check(page, "overwritten-in-country", "COUNTRY1_fr_CD");
    }

    public void testOverwrittenInVariant()
    {
        IPage page = createPage(MOCK1, new Locale("en", "US", "Tapestry"));

        check(page, "overwritten-in-variant", "VARIANT1_en_US_Tapestry");

        page = createPage(MOCK1, new Locale("fr", "CD", "Foo"));

        check(page, "overwritten-in-variant", "VARIANT1_fr_CD_Foo");
    }

    private static final String MOCK2 = "/org/apache/tapestry/junit/MockPage2.jwc";

    /**
     *  Tests that the code that locates properties files can deal
     *  with the base path (i.e., Foo.properties) doesn't exist.
     * 
     **/

    public void testMissingBase()
    {
        IPage page = createPage(MOCK2, new Locale("en", "US"));

        check(page, "language-key", "LANGUAGE1");
    }

    /**
     *  Tests that naming and search works correctly for locales
     *  that specify language and variant, but no country.
     * 
     **/

    public void testMissingCountry()
    {
        IPage page = createPage(MOCK2, new Locale("en", "", "Tapestry"));

        check(page, "overwritten-in-variant", "VARIANT1_en__Tapestry");
    }

    public void testDateFormatting()
    {
        IPage page = createPage(MOCK1, new Locale("en"));

        Calendar c = new GregorianCalendar(1966, Calendar.DECEMBER, 24);

        Date d = c.getTime();

        assertEquals(
            "A formatted date: 12/24/66",
            page.getMessages().format("using-date-format", d));
    }

    public void testDateFormatLocalization()
    {
        IPage page = createPage(MOCK1, new Locale("fr"));

        Calendar c = new GregorianCalendar(1966, Calendar.DECEMBER, 24);

        Date d = c.getTime();

        // French formatting puts the day before the month.

        assertEquals(
            "A formatted date: 24/12/66",
            page.getMessages().format("using-date-format", d));
    }
}