//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.junit;

import java.util.Locale;

import net.sf.tapestry.IPage;

/**
 *  Tests the class {@link net.sf.tapestry.engine.DefaultStringsSource}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public class ComponentStringsTest extends TapestryTestCase
{

    public ComponentStringsTest(String name)
    {
        super(name);
    }

    private void check(IPage page, String key, String expected)
    {
        String actual = page.getString(key);

        assertEquals("Key " + key, expected, actual);
    }

    private static final String MOCK1 = "/net/sf/tapestry/junit/MockPage1.jwc";

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
    
    private static final String MOCK2 = "/net/sf/tapestry/junit/MockPage2.jwc";
    
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
}