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
 * Tests for {@link org.apache.tapestry.pageload.NamespaceClassSearchPageClassProvider}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestNamespaceClassSearchPageClassProvider extends HiveMindTestCase
{
    private IComponentSpecification newSpec()
    {
        return (IComponentSpecification) newMock(IComponentSpecification.class);
    }

    private INamespace newNamespace(String prefixes)
    {
        MockControl control = newControl(INamespace.class);
        INamespace namespace = (INamespace) control.getMock();

        namespace.getPropertyValue(NamespaceClassSearchPageClassProvider.PACKAGES_NAME);
        control.setReturnValue(prefixes);

        return namespace;
    }

    public void testDefaultSearchList()
    {
        INamespace namespace = newNamespace(null);

        replayControls();

        List prefixes = new NamespaceClassSearchPageClassProvider()
                .buildPackageSearchList(namespace);

        assertListsEqual(new String[]
        { "" }, prefixes);

        verifyControls();
    }

    public void testSearchListWithPrefixes()
    {
        INamespace namespace = newNamespace("org.foo,org.bar.baz");

        replayControls();

        List prefixes = new NamespaceClassSearchPageClassProvider()
                .buildPackageSearchList(namespace);

        assertListsEqual(new String[]
        { "org.foo.", "org.bar.baz.", "" }, prefixes);

        verifyControls();
    }

    public void testFound()
    {
        INamespace namespace = newNamespace("org.foo");

        MockControl crc = newControl(ClassResolver.class);
        ClassResolver cr = (ClassResolver) crc.getMock();

        IComponentSpecification spec = newSpec();

        cr.checkForClass("org.foo.bar.Baz");
        crc.setReturnValue(getClass());

        replayControls();

        PageClassProviderContext context = new PageClassProviderContext("bar/Baz", spec, namespace);

        NamespaceClassSearchPageClassProvider provider = new NamespaceClassSearchPageClassProvider();
        provider.setClassResolver(cr);

        assertEquals("org.foo.bar.Baz", provider.providePageClassName(context));

        verifyControls();
    }

    public void testNotFound()
    {
        INamespace namespace = newNamespace("org.foo");

        MockControl crc = newControl(ClassResolver.class);
        ClassResolver cr = (ClassResolver) crc.getMock();

        IComponentSpecification spec = newSpec();

        cr.checkForClass("org.foo.bar.Baz");
        crc.setReturnValue(null);

        cr.checkForClass("bar.Baz");
        crc.setReturnValue(null);

        replayControls();

        PageClassProviderContext context = new PageClassProviderContext("bar/Baz", spec, namespace);

        NamespaceClassSearchPageClassProvider provider = new NamespaceClassSearchPageClassProvider();
        provider.setClassResolver(cr);

        assertNull(provider.providePageClassName(context));

        verifyControls();
    }
}