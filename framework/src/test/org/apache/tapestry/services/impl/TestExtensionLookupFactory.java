// Copyright 2004 The Apache Software Foundation
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

import java.util.Collections;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.lib.DefaultImplementationBuilder;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.easymock.MockControl;

/**
 * Tests {@link org.apache.tapestry.services.impl.ExtensionLookupFactory}.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestExtensionLookupFactory extends HiveMindTestCase
{
    private List createParameters(String extensionName)
    {
        ExtensionLookupParameter p = new ExtensionLookupParameter();

        p.setExtensionName(extensionName);

        return Collections.singletonList(p);
    }

    public void testInSpecification()
    {
        MockControl specControl = newControl(IApplicationSpecification.class);
        IApplicationSpecification spec = (IApplicationSpecification) specControl.getMock();

        Runnable r = (Runnable) newMock(Runnable.class);

        // Training

        spec.checkExtension("foo.bar");
        specControl.setReturnValue(true);

        spec.getExtension("foo.bar", Runnable.class);
        specControl.setReturnValue(r);

        replayControls();

        ExtensionLookupFactory f = new ExtensionLookupFactory();
        f.setSpecification(spec);

        Object actual = f.createCoreServiceImplementation(
                "biz.bap.Boom",
                Runnable.class,
                null,
                null,
                createParameters("foo.bar"));

        assertSame(r, actual);

        verifyControls();
    }

    public void testDefault()
    {
        MockControl specControl = newControl(IApplicationSpecification.class);
        IApplicationSpecification spec = (IApplicationSpecification) specControl.getMock();

        MockControl dibControl = newControl(DefaultImplementationBuilder.class);
        DefaultImplementationBuilder dib = (DefaultImplementationBuilder) dibControl.getMock();

        Module module = (Module) newMock(Module.class);

        Runnable r = (Runnable) newMock(Runnable.class);

        // Training

        spec.checkExtension("foo.bar");
        specControl.setReturnValue(false);

        dib.buildDefaultImplementation(Runnable.class, module);
        dibControl.setReturnValue(r);

        replayControls();

        ExtensionLookupFactory f = new ExtensionLookupFactory();
        f.setSpecification(spec);
        f.setDefaultBuilder(dib);

        Object actual = f.createCoreServiceImplementation(
                "biz.bap.Boom",
                Runnable.class,
                null,
                module,
                createParameters("foo.bar"));

        assertSame(r, actual);

        verifyControls();
    }

    public void testFailure()
    {
        Location l = fabricateLocation(264);
        ExtensionLookupParameter p = new ExtensionLookupParameter();

        p.setLocation(l);
        p.setExtensionName("gnip.gnop");

        List parameters = Collections.singletonList(p);

        ExtensionLookupFactory f = new ExtensionLookupFactory();

        try
        {
            f.createCoreServiceImplementation(null, null, null, null, parameters);

            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertSame(l, ex.getLocation());
        }
    }
}