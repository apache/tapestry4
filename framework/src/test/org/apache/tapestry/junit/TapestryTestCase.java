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

import java.util.Arrays;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.binding.LiteralBinding;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.parse.SpecificationParser;
import org.apache.tapestry.services.BindingSource;
import org.apache.tapestry.services.ExpressionCache;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.apache.tapestry.services.impl.ExpressionCacheImpl;
import org.apache.tapestry.services.impl.ExpressionEvaluatorImpl;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;
import org.apache.tapestry.util.IPropertyHolder;

/**
 * Base class for Tapestry test cases.
 * 
 * @author Howard Lewis Ship
 * @since 2.2
 */

public class TapestryTestCase extends HiveMindTestCase
{
    protected static final boolean IS_JDK13 = System.getProperty("java.specification.version")
            .equals("1.3");

    private ClassResolver _resolver = new DefaultClassResolver();

    /** @since 3.1 */
    private ValueConverter _valueConverter = new ValueConverter()
    {
        public Object coerceValue(Object value, Class desiredType)
        {
            return value;
        }
    };

    /** @since 3.1 */
    private class BindingSourceFixture implements BindingSource
    {

        public IBinding createBinding(IComponent component, String description, String locator,
                Location location)
        {
            return new LiteralBinding(description, locator, _valueConverter, location);
        }
    }

    protected IComponentSpecification parseComponent(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser(_resolver);

        Resource location = getSpecificationResourceLocation(simpleName);

        return parser.parseComponentSpecification(location);
    }

    protected IComponentSpecification parsePage(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser(_resolver);

        parser.setBindingSource(new BindingSourceFixture());

        Resource location = getSpecificationResourceLocation(simpleName);

        return parser.parsePageSpecification(location);
    }

    protected IApplicationSpecification parseApp(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser(_resolver);

        parser.setExpressionEvaluator(createExpressionEvaluator());

        Resource location = getSpecificationResourceLocation(simpleName);

        return parser.parseApplicationSpecification(location);
    }

    protected Resource getSpecificationResourceLocation(String simpleName)
    {
        String adjustedClassName = "/" + getClass().getName().replace('.', '/') + ".class";

        Resource classResource = new ClasspathResource(_resolver, adjustedClassName);

        return classResource.getRelativeResource(simpleName);
    }

    protected ILibrarySpecification parseLib(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser(_resolver);

        parser.setExpressionEvaluator(createExpressionEvaluator());

        Resource location = getSpecificationResourceLocation(simpleName);

        return parser.parseLibrarySpecification(location);
    }

    public static void checkList(String propertyName, Object[] expected, Object[] actual)
    {
        checkList(propertyName, expected, Arrays.asList(actual));
    }

    public static void checkList(String propertyName, Object[] expected, List actual)
    {
        int count = Tapestry.size(actual);

        assertEquals(propertyName + " element count", expected.length, count);

        for (int i = 0; i < count; i++)
        {
            assertEquals("propertyName[" + i + "]", expected[i], actual.get(i));
        }
    }

    public static void checkProperty(IPropertyHolder h, String propertyName, String expectedValue)
    {
        assertEquals("Property " + propertyName + ".", expectedValue, h.getProperty(propertyName));
    }

    public static void checkException(Throwable ex, String string)
    {
        if (ex.getMessage().indexOf(string) >= 0)
            return;

        throw new AssertionFailedError("Exception " + ex + " does not contain sub-string '"
                + string + "'.");
    }

    protected static ExpressionEvaluator createExpressionEvaluator()
    {
        ExpressionCache cache = new ExpressionCacheImpl();
        ExpressionEvaluatorImpl result = new ExpressionEvaluatorImpl();
        result.setExpressionCache(cache);

        return result;
    }

}