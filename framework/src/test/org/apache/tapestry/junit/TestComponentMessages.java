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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IPage;
import org.apache.tapestry.engine.Namespace;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.junit.mock.c21.NullPropertySource;
import org.apache.tapestry.services.ComponentMessagesSource;
import org.apache.tapestry.services.impl.ComponentMessagesSourceImpl;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.LibrarySpecification;
import org.easymock.MockControl;

/**
 * Tests the class {@link org.apache.tapestry.engine.DefaultStringsSource}.
 * <p>
 * TODO: Add tests realted to messages encoding (which can be specified as meta-data on the
 * component specification or, ultimately, in the namespace (library specification).
 * 
 * @author Howard Lewis Ship
 * @since 2.0.4
 */

public class TestComponentMessages extends TapestryTestCase
{
    private void check(IPage page, String key, String expected)
    {
        replayControls();

        String actual = page.getMessages().getMessage(key);

        assertEquals("Key " + key, expected, actual);

        verifyControls();
    }

    private static final String MOCK1 = "/org/apache/tapestry/junit/MockPage1.page";

    private IEngine newEngine(ComponentMessagesSource source)
    {
        MockControl control = newControl(IEngine.class);
        IEngine engine = (IEngine) control.getMock();

        engine.getComponentMessagesSource();
        control.setReturnValue(source);

        return engine;
    }

    private IComponentSpecification newSpec(String path)
    {
        Resource resource = new ClasspathResource(new DefaultClassResolver(), path);

        IComponentSpecification spec = new ComponentSpecification();
        spec.setSpecificationLocation(resource);

        return spec;
    }

    private IPage createPage(String location, Locale locale)
    {
        BasePage page = new BasePage();

        ComponentMessagesSourceImpl source = new ComponentMessagesSourceImpl();

        source.setApplicationPropertySource(new NullPropertySource());

        IEngine engine = newEngine(source);

        page.attach(engine);
        page.setPage(page);
        page.setLocale(locale);

        IComponentSpecification spec = newSpec(location);

        page.setSpecification(spec);

        INamespace namespace = new Namespace(null, null, new LibrarySpecification(), null);

        page.setNamespace(namespace);

        return page;
    }

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

    private static final String MOCK2 = "/org/apache/tapestry/junit/MockPage2.page";

    /**
     * Tests that the code that locates properties files can deal with the base path (i.e.,
     * Foo.properties) doesn't exist.
     */

    public void testMissingBase()
    {
        IPage page = createPage(MOCK2, new Locale("en", "US"));

        check(page, "language-key", "LANGUAGE1");
    }

    /**
     * Tests that naming and search works correctly for locales that specify language and variant,
     * but no country.
     */

    public void testMissingCountry()
    {
        IPage page = createPage(MOCK2, new Locale("en", "", "Tapestry"));

        check(page, "overwritten-in-variant", "VARIANT1_en__Tapestry");
    }

    public void testDateFormatting()
    {
        IPage page = createPage(MOCK1, Locale.ENGLISH);

        Calendar c = new GregorianCalendar(1966, Calendar.DECEMBER, 24);

        Date d = c.getTime();

        replayControls();

        assertEquals("A formatted date: 12/24/66", page.getMessages()
                .format("using-date-format", d));

        verifyControls();
    }

    public void testDateFormatLocalization()
    {
        IPage page = createPage(MOCK1, Locale.FRENCH);

        Calendar c = new GregorianCalendar(1966, Calendar.DECEMBER, 24);

        Date d = c.getTime();

        // French formatting puts the day before the month.

        replayControls();

        assertEquals("A formatted date: 24/12/66", page.getMessages()
                .format("using-date-format", d));

        verifyControls();
    }
}