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

import org.apache.hivemind.Messages;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.service.ClassFactory;
import org.apache.hivemind.service.impl.ClassFactoryImpl;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IPage;
import org.apache.tapestry.engine.Namespace;
import org.apache.tapestry.enhance.EnhancementOperationImpl;
import org.apache.tapestry.enhance.InjectMessagesWorker;
import org.apache.tapestry.enhance.InjectSpecificationWorker;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.services.impl.ComponentMessagesSourceImpl;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;
import org.apache.tapestry.spec.LibrarySpecification;

/**
 * Tests the class {@link org.apache.tapestry.services.impl.ComponentMessagesSourceImpl}.
 * <p>
 * TODO: Add tests realted to messages encoding (which can be specified as meta-data on the
 * component specification or, ultimately, in the namespace (library specification).
 * 
 * @author Howard Lewis Ship
 * @since 2.0.4
 */

public class TestComponentMessages extends TapestryTestCase
{
    private static class NullComponentPropertySource implements ComponentPropertySource
    {

        public String getComponentProperty(IComponent component, String propertyName)
        {
            return null;
        }

        public String getLocalizedComponentProperty(IComponent component, Locale locale,
                String propertyName)
        {
            return null;
        }

        public String getNamespaceProperty(INamespace namespace, String propertyName)
        {
            return null;
        }

        public String getLocalizedNamespaceProperty(INamespace namespace, Locale locale,
                String propertyName)
        {
            return null;
        }
    }

    private void check(Messages messages, String key, String expected)
    {
        String actual = messages.getMessage(key);

        assertEquals("Key " + key, expected, actual);

    }

    private static final String MOCK1 = "/org/apache/tapestry/junit/MockPage1.page";

    private IComponentSpecification newSpec(String path)
    {
        Resource resource = new ClasspathResource(new DefaultClassResolver(), path);

        IComponentSpecification spec = new ComponentSpecification();
        spec.setSpecificationLocation(resource);

        return spec;
    }

    private ILibrarySpecification newLibrarySpec()
    {
        Resource resource = new ClasspathResource(new DefaultClassResolver(),
                "/org/apache/tapestry/junit/Library.library");

        ILibrarySpecification spec = new LibrarySpecification();
        spec.setSpecificationLocation(resource);

        return spec;
    }

    /**
     * Mocking up the page is too hard ... the relationship between the component messagess source
     * and the page is too varied and complex. Instead, we use the tools to create the page itself,
     * much as the full framework would do at runtime.
     */

    private IPage newPage(IComponentSpecification specification, Locale locale)
    {
        ClassFactory classFactory = new ClassFactoryImpl();

        EnhancementOperationImpl op = new EnhancementOperationImpl(new DefaultClassResolver(),
                specification, BasePage.class, classFactory);

        new InjectMessagesWorker().performEnhancement(op, specification);
        new InjectSpecificationWorker().performEnhancement(op, specification);
            
        IPage result = (IPage) op.getConstructor().newInstance();

        result.setLocale(locale);
        result.setPage(result);

        return result;
    }

    private Messages createMessages(String location, Locale locale)
    {
        ComponentMessagesSourceImpl source = new ComponentMessagesSourceImpl();
        source.setComponentPropertySource(new NullComponentPropertySource());

        IComponentSpecification spec = newSpec(location);

        IPage page = newPage(spec, locale);

        INamespace namespace = new Namespace(null, null, newLibrarySpec(), null, null);

        page.setNamespace(namespace);

        return source.getMessages(page);
    }

    public void testOnlyInBase()
    {
        Messages messages = createMessages(MOCK1, new Locale("en", "US"));

        check(messages, "only-in-base", "BASE1");
    }

    /** @since 3.1 */
    public void testOnlyInNamespace()
    {
        Messages messages = createMessages(MOCK1, new Locale("en", "US"));

        check(messages, "only-in-namespace", "LIBRARY_BASE.only-in-namespace");
    }

    /** @since 3.1 */
    public void testLocalizedInNamespace()
    {
        Messages messages = createMessages(MOCK1, new Locale("fr"));

        check(messages, "localized-in-namespace", "LIBRARY_FR.localized-in-namespace");
    }

    /** @since 3.1 */
    public void testComponentOverridesNamespace()
    {
        Messages messages = createMessages(MOCK1, new Locale("en", "US"));

        check(
                messages,
                "component-overrides-namespace",
                "MOCKPAGE1_BASE.component-overrides-namespace");
    }

    /** @since 3.1 */
    public void testLocalizedComponentOverridesLocalizedNamespace()
    {
        Messages messages = createMessages(MOCK1, new Locale("fr"));

        check(
                messages,
                "localized-component-overrides-namespace",
                "MOCKPAGE1_FR.localized-component-overrides-namespace");
    }

    public void testMissingKey()
    {
        Messages messages = createMessages(MOCK1, new Locale("en", "GB"));

        check(messages, "non-existant-key", "[NON-EXISTANT-KEY]");
    }

    public void testOverwrittenInLanguage()
    {
        Messages messages = createMessages(MOCK1, new Locale("en", "US"));

        check(messages, "overwritten-in-language", "LANGUAGE1_en");

        messages = createMessages(MOCK1, new Locale("fr", ""));

        check(messages, "overwritten-in-language", "LANGUAGE1_fr");
    }

    public void testOverwrittenInCountry()
    {
        Messages messages = createMessages(MOCK1, new Locale("en", "US"));

        check(messages, "overwritten-in-country", "COUNTRY1_en_US");

        messages = createMessages(MOCK1, new Locale("fr", "CD"));

        check(messages, "overwritten-in-country", "COUNTRY1_fr_CD");
    }

    public void testOverwrittenInVariant()
    {
        Messages messages = createMessages(MOCK1, new Locale("en", "US", "Tapestry"));

        check(messages, "overwritten-in-variant", "VARIANT1_en_US_Tapestry");

        messages = createMessages(MOCK1, new Locale("fr", "CD", "Foo"));

        check(messages, "overwritten-in-variant", "VARIANT1_fr_CD_Foo");
    }

    private static final String MOCK2 = "/org/apache/tapestry/junit/MockPage2.page";

    /**
     * Tests that the code that locates properties files can deal with the base path (i.e.,
     * Foo.properties) doesn't exist.
     */

    public void testMissingBase()
    {
        Messages messages = createMessages(MOCK2, new Locale("en", "US"));

        check(messages, "language-key", "LANGUAGE1");
    }

    /**
     * Tests that naming and search works correctly for locales that specify language and variant,
     * but no country.
     */

    public void testMissingCountry()
    {
        Messages messages = createMessages(MOCK2, new Locale("en", "", "Tapestry"));

        check(messages, "overwritten-in-variant", "VARIANT1_en__Tapestry");
    }

    public void testDateFormatting()
    {
        Messages messages = createMessages(MOCK1, Locale.ENGLISH);

        Calendar c = new GregorianCalendar(1966, Calendar.DECEMBER, 24);

        Date d = c.getTime();

        assertEquals("A formatted date: 12/24/66", messages.format("using-date-format", d));
    }

    public void testDateFormatLocalization()
    {
        Messages messages = createMessages(MOCK1, Locale.FRENCH);

        Calendar c = new GregorianCalendar(1966, Calendar.DECEMBER, 24);

        Date d = c.getTime();

        // French formatting puts the day before the month.

        assertEquals("A formatted date: 24/12/66", messages.format("using-date-format", d));

    }
}