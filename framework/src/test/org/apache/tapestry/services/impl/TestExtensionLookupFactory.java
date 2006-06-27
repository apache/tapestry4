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

package org.apache.tapestry.services.impl;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertSame;

import java.util.Collections;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.ServiceImplementationFactoryParameters;
import org.apache.hivemind.lib.DefaultImplementationBuilder;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.spec.IApplicationSpecification;

/**
 * Tests {@link org.apache.tapestry.services.impl.ExtensionLookupFactory}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestExtensionLookupFactory extends BaseComponentTestCase
{
    private List createParameters(String extensionName)
    {
        return createParameters(extensionName, null);
    }

    private List createParameters(String extensionName, Object defaultValue)
    {
        ExtensionLookupParameter p = new ExtensionLookupParameter();

        p.setExtensionName(extensionName);
        p.setDefault(defaultValue);

        return Collections.singletonList(p);
    }

    public void testInSpecification()
    {
        IApplicationSpecification spec = newMock(IApplicationSpecification.class);

        Runnable r = newMock(Runnable.class);
        
        ServiceImplementationFactoryParameters fp = newMock(ServiceImplementationFactoryParameters.class);

        // Training

        expect(fp.getParameters()).andReturn(createParameters("foo.bar"));

        expect(fp.getServiceInterface()).andReturn(Runnable.class);

        expect(spec.checkExtension("foo.bar")).andReturn(true);

        expect(spec.getExtension("foo.bar", Runnable.class)).andReturn(r);

        replay();

        ExtensionLookupFactory f = new ExtensionLookupFactory();
        f.setSpecification(spec);

        Object actual = f.createCoreServiceImplementation(fp);

        assertSame(r, actual);

        verify();
    }

    public void testSyntheticDefault()
    {
        IApplicationSpecification spec = newMock(IApplicationSpecification.class);
        
        DefaultImplementationBuilder dib = newMock(DefaultImplementationBuilder.class);

        Runnable r = newMock(Runnable.class);

        ServiceImplementationFactoryParameters fp = newMock(ServiceImplementationFactoryParameters.class);

        // Training

        expect(fp.getParameters()).andReturn(createParameters("foo.bar"));

        expect(fp.getServiceInterface()).andReturn(Runnable.class);
        
        expect(spec.checkExtension("foo.bar")).andReturn(false);

        expect(dib.buildDefaultImplementation(Runnable.class)).andReturn(r);

        replay();

        ExtensionLookupFactory f = new ExtensionLookupFactory();
        f.setSpecification(spec);
        f.setDefaultBuilder(dib);

        Object actual = f.createCoreServiceImplementation(fp);

        assertSame(r, actual);

        verify();
    }

    public void testConfigurationDefault()
    {
        IApplicationSpecification spec = newMock(IApplicationSpecification.class);

        Runnable r = newMock(Runnable.class);

        ServiceImplementationFactoryParameters fp = newMock(ServiceImplementationFactoryParameters.class);

        // Training

        expect(fp.getParameters()).andReturn(createParameters("foo.bar", r));

        expect(fp.getServiceInterface()).andReturn(Runnable.class);

        expect(spec.checkExtension("foo.bar")).andReturn(false);

        replay();

        ExtensionLookupFactory f = new ExtensionLookupFactory();
        f.setSpecification(spec);

        Object actual = f.createCoreServiceImplementation(fp);

        assertSame(r, actual);

        verify();
    }

    public void testFailure()
    {
        Location l = fabricateLocation(264);
        ExtensionLookupParameter p = new ExtensionLookupParameter();

        p.setLocation(l);
        p.setExtensionName("gnip.gnop");

        ServiceImplementationFactoryParameters fp = newMock(ServiceImplementationFactoryParameters.class);

        expect(fp.getParameters()).andReturn(Collections.singletonList(p));

        expect(fp.getServiceInterface()).andReturn(null);

        ExtensionLookupFactory f = new ExtensionLookupFactory();

        replay();

        try
        {
            f.createCoreServiceImplementation(fp);

            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertSame(l, ex.getLocation());
        }

        verify();
    }
}