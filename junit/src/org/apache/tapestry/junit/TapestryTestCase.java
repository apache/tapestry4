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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.DefaultComponentMessagesSource;
import org.apache.tapestry.engine.IComponentMessagesSource;
import org.apache.tapestry.parse.SpecificationParser;
import org.apache.tapestry.resource.ClasspathResourceLocation;

import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;
import org.apache.tapestry.util.DefaultResourceResolver;
import org.apache.tapestry.util.IPropertyHolder;

/**
 *  Base class for Tapestry test cases.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class TapestryTestCase extends TestCase
{
    protected static final boolean IS_JDK13 =
        System.getProperty("java.specification.version").equals("1.3");

    private IResourceResolver _resolver = new DefaultResourceResolver();

    protected IPage createPage(String specificationPath, Locale locale)
    {
        IResourceLocation location = new ClasspathResourceLocation(_resolver, specificationPath);

        IComponentMessagesSource source = new DefaultComponentMessagesSource();
        MockEngine engine = new MockEngine();
        engine.setComponentStringsSource(source);

        MockPage result = new MockPage();
        result.setEngine(engine);
        result.setLocale(locale);

  		// TODO the SpecFactory in SpecificationParser should be used in some way to create an IComponentSpecification!
        IComponentSpecification spec = new ComponentSpecification(); 
        spec.setSpecificationLocation(location);
        result.setSpecification(spec);

        return result;
    }

    protected IComponentSpecification parseComponent(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser(_resolver);

        IResourceLocation location = getSpecificationResourceLocation(simpleName);

        return parser.parseComponentSpecification(location);
    }

    protected IComponentSpecification parsePage(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser(_resolver);

        IResourceLocation location = getSpecificationResourceLocation(simpleName);

        return parser.parsePageSpecification(location);
    }

    protected IApplicationSpecification parseApp(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser(_resolver);

        IResourceLocation location = getSpecificationResourceLocation(simpleName);

        return parser.parseApplicationSpecification(location);
    }

    protected IResourceLocation getSpecificationResourceLocation(String simpleName)
    {
        String adjustedClassName = "/" + getClass().getName().replace('.', '/') + ".class";

        IResourceLocation classResourceLocation =
            new ClasspathResourceLocation(_resolver, adjustedClassName);

        IResourceLocation appSpecLocation = classResourceLocation.getRelativeLocation(simpleName);
        return appSpecLocation;
    }

    protected ILibrarySpecification parseLib(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser(_resolver);

        IResourceLocation location = getSpecificationResourceLocation(simpleName);

        return parser.parseLibrarySpecification(location);
    }

    protected void checkList(String propertyName, Object[] expected, Object[] actual)
    {
        checkList(propertyName, expected, Arrays.asList(actual));
    }

    protected void checkList(String propertyName, Object[] expected, List actual)
    {
        int count = Tapestry.size(actual);

        assertEquals(propertyName + " element count", expected.length, count);

        for (int i = 0; i < count; i++)
        {
            assertEquals("propertyName[" + i + "]", expected[i], actual.get(i));
        }
    }

    protected void checkProperty(IPropertyHolder h, String propertyName, String expectedValue)
    {
        assertEquals("Property " + propertyName + ".", expectedValue, h.getProperty(propertyName));
    }

    protected void checkException(Throwable ex, String string)
    {
        if (ex.getMessage().indexOf(string) >= 0)
            return;

        throw new AssertionFailedError(
            "Exception " + ex + " does not contain sub-string '" + string + "'.");
    }

    protected void unreachable()
    {
        throw new AssertionFailedError("This code should be unreachable.");
    }
}
