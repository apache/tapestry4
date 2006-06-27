// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.pageload;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.services.ClassFinder;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Tests for {@link org.apache.tapestry.pageload.NamespaceClassSearchComponentClassProvider}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestNamespaceClassSearchComponentClassProvider extends BaseComponentTestCase
{
    private INamespace newNamespace(String key, String prefixes)
    {
        INamespace namespace = newMock(INamespace.class);

        expect(namespace.getPropertyValue(key)).andReturn(prefixes);

        return namespace;
    }

    private ClassFinder newClassFinder(String packageList, String className, Class resultClass)
    {
        ClassFinder finder = newMock(ClassFinder.class);

        expect(finder.findClass(packageList, className)).andReturn(resultClass);

        return finder;
    }

    public void testFound()
    {
        INamespace namespace = newNamespace("zip", "org.apache.tapestry.pageload");
        ClassFinder finder = newClassFinder(
                "org.apache.tapestry.pageload",
                "bar.Baz",
                PageLoaderTest.class);

        IComponentSpecification spec = newSpec();

        replay();

        ComponentClassProviderContext context = new ComponentClassProviderContext("bar/Baz", spec,
                namespace);

        NamespaceClassSearchComponentClassProvider provider = new NamespaceClassSearchComponentClassProvider();
        provider.setClassFinder(finder);
        provider.setPackagesName("zip");

        assertEquals(PageLoaderTest.class.getName(), provider.provideComponentClassName(context));

        verify();
    }

    public void testNotFound()
    {
        INamespace namespace = newNamespace("zap", "org.foo");
        ClassFinder finder = newClassFinder("org.foo", "bar.Baz", null);

        IComponentSpecification spec = newSpec();

        replay();

        ComponentClassProviderContext context = new ComponentClassProviderContext("bar/Baz", spec,
                namespace);

        NamespaceClassSearchComponentClassProvider provider = new NamespaceClassSearchComponentClassProvider();
        provider.setClassFinder(finder);
        provider.setPackagesName("zap");

        assertNull(provider.provideComponentClassName(context));

        verify();
    }
}