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

import java.util.List;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.pageload.NamespaceClassSearchComponentClassProvider}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestNamespaceClassSearchComponentClassProvider extends HiveMindTestCase
{
    private IComponentSpecification newSpec()
    {
        return (IComponentSpecification) newMock(IComponentSpecification.class);
    }

    private INamespace newNamespace(String key, String prefixes)
    {
        MockControl control = newControl(INamespace.class);
        INamespace namespace = (INamespace) control.getMock();

        namespace.getPropertyValue(key);
        control.setReturnValue(prefixes);

        return namespace;
    }

    public void testDefaultSearchList()
    {
        INamespace namespace = newNamespace("foo", null);

        replayControls();

        NamespaceClassSearchComponentClassProvider p = new NamespaceClassSearchComponentClassProvider();
        p.setPackagesName("foo");

        List prefixes = p.buildPackageSearchList(namespace);

        assertListsEqual(new String[]
        { "" }, prefixes);

        verifyControls();
    }

    public void testSearchListWithPrefixes()
    {
        INamespace namespace = newNamespace("foo", "org.foo,org.bar.baz");

        replayControls();

        NamespaceClassSearchComponentClassProvider p = new NamespaceClassSearchComponentClassProvider();
        p.setPackagesName("foo");

        List prefixes = p.buildPackageSearchList(namespace);

        assertListsEqual(new String[]
        { "org.foo.", "org.bar.baz.", "" }, prefixes);

        verifyControls();
    }

    public void testFound()
    {
        INamespace namespace = newNamespace("zip", "org.foo");

        MockControl crc = newControl(ClassResolver.class);
        ClassResolver cr = (ClassResolver) crc.getMock();

        IComponentSpecification spec = newSpec();

        cr.checkForClass("org.foo.bar.Baz");
        crc.setReturnValue(getClass());

        replayControls();

        ComponentClassProviderContext context = new ComponentClassProviderContext("bar/Baz", spec,
                namespace);

        NamespaceClassSearchComponentClassProvider provider = new NamespaceClassSearchComponentClassProvider();
        provider.setClassResolver(cr);
        provider.setPackagesName("zip");

        assertEquals("org.foo.bar.Baz", provider.provideComponentClassName(context));

        verifyControls();
    }

    public void testNotFound()
    {
        INamespace namespace = newNamespace("zap", "org.foo");

        MockControl crc = newControl(ClassResolver.class);
        ClassResolver cr = (ClassResolver) crc.getMock();

        IComponentSpecification spec = newSpec();

        cr.checkForClass("org.foo.bar.Baz");
        crc.setReturnValue(null);

        cr.checkForClass("bar.Baz");
        crc.setReturnValue(null);

        replayControls();

        ComponentClassProviderContext context = new ComponentClassProviderContext("bar/Baz", spec,
                namespace);

        NamespaceClassSearchComponentClassProvider provider = new NamespaceClassSearchComponentClassProvider();
        provider.setClassResolver(cr);
        provider.setPackagesName("zap");

        assertNull(provider.provideComponentClassName(context));

        verifyControls();
    }
}