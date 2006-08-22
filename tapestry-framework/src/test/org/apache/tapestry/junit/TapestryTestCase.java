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

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Location;
import org.apache.hivemind.Registry;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.binding.BindingSource;
import org.apache.tapestry.binding.LiteralBinding;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.parse.SpecificationParser;
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
public abstract class TapestryTestCase extends BaseComponentTestCase
{
    protected static final boolean IS_JDK13 = System.getProperty("java.specification.version")
            .equals("1.3");

    private ClassResolver _resolver = new DefaultClassResolver();

    /** @since 4.0 */
    private ValueConverter _valueConverter = new ValueConverter()
    {
        public Object coerceValue(Object value, Class desiredType)
        {
            return value;
        }
    };

    /** @since 4.0 */
    private class BindingSourceFixture implements BindingSource
    {

        public IBinding createBinding(IComponent component, String description, String reference,
                String defaultBindingType, Location location)
        {
            return new LiteralBinding(description, _valueConverter, location, reference);
        }
    }

    /** @since 4.0 */
    protected BindingSourceFixture newBindingSource()
    {
        return new BindingSourceFixture();
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

        parser.setBindingSource(newBindingSource());

        Resource location = getSpecificationResourceLocation(simpleName);

        return parser.parsePageSpecification(location);
    }

    protected IApplicationSpecification parseApp(String simpleName) throws Exception
    {
        SpecificationParser parser = new SpecificationParser(_resolver);

        parser.setValueConverter(createValueConverter());

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

        parser.setValueConverter(createValueConverter());

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

        assertEquals(expected.length, count);

        for (int i = 0; i < count; i++)
        {
            assertEquals(expected[i], actual.get(i));
        }
    }

    public static void checkProperty(IPropertyHolder h, String propertyName, String expectedValue)
    {
        assertEquals(expectedValue, h.getProperty(propertyName));
    }

    public static void checkException(Throwable ex, String string)
    {
        if (ex.getMessage().indexOf(string) >= 0)
            return;

        throw new AssertionError("Exception " + ex + " does not contain sub-string '"
                + string + "'.");
    }

    private static ValueConverter _sharedValueConverter;

    protected ValueConverter createValueConverter()
    {
        // Only build the Registry the first time this is called. The same Registry
        // can then be used for any remaining calls.

        if (_sharedValueConverter == null)
        {

            Registry r = RegistryBuilder.constructDefaultRegistry();

            _sharedValueConverter = (ValueConverter) r.getService(
                    "tapestry.coerce.ValueConverter",
                    ValueConverter.class);
        }

        return _sharedValueConverter;
    }

    protected IComponent newComponent()
    {
        return newMock(IComponent.class);
    }
}