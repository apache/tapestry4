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

package org.apache.tapestry.enhance;

import java.lang.reflect.Modifier;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.engine.HomeService;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.services.InjectedValueProvider;
import org.apache.tapestry.spec.InjectSpecification;
import org.apache.tapestry.spec.InjectSpecificationImpl;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.enhance.InjectObjectWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestInjectObjectWorker extends HiveMindTestCase
{
    private InjectSpecification newSpec(String name, String locator, Location location)
    {
        InjectSpecification is = new InjectSpecificationImpl();

        is.setProperty(name);
        is.setObject(locator);
        is.setLocation(location);

        return is;
    }

    public void testNoExistingProperty()
    {
        Location l = newLocation();
        Object injectedValue = new Object();

        InjectSpecification spec = newSpec("fred", "service:barney", l);

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        MockControl pc = newControl(InjectedValueProvider.class);
        InjectedValueProvider p = (InjectedValueProvider) pc.getMock();

        op.getPropertyType("fred");
        opc.setReturnValue(null);

        op.claimProperty("fred");

        p.obtainValue("service:barney", l);

        pc.setReturnValue(injectedValue);

        op.addInjectedField("_$fred", injectedValue);
        opc.setReturnValue("_$fred");

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(Object.class, "getFred", null, null),
                "return _$fred;");

        replayControls();

        InjectObjectWorker w = new InjectObjectWorker();
        w.setProvider(p);

        w.performEnhancement(op, spec);

        verifyControls();
    }

    public void testWithExistingProperty()
    {
        Location l = newLocation();
        Object injectedValue = new HomeService();

        InjectSpecification spec = newSpec("wilma", "service:betty", l);

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        MockControl pc = newControl(InjectedValueProvider.class);
        InjectedValueProvider p = (InjectedValueProvider) pc.getMock();

        op.getPropertyType("wilma");
        opc.setReturnValue(IEngineService.class);

        op.claimProperty("wilma");

        p.obtainValue("service:betty", l);
        pc.setReturnValue(injectedValue);

        op.addInjectedField("_$wilma", injectedValue);
        opc.setReturnValue("_$wilma");

        op.addMethod(Modifier.PUBLIC, new MethodSignature(IEngineService.class, "getWilma", null,
                null), "return _$wilma;");

        replayControls();

        InjectObjectWorker w = new InjectObjectWorker();
        w.setProvider(p);

        w.performEnhancement(op, spec);

        verifyControls();
    }

    /**
     * Test what happens when the value to be injected is null (usually indicating an error in the
     * locator and/or a configuration error inside the HiveMind descriptors).
     */

    public void testInjectNull()
    {
        Location l = newLocation();

        InjectSpecification spec = newSpec("fred", "service:barney", l);

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        MockControl pc = newControl(InjectedValueProvider.class);
        InjectedValueProvider p = (InjectedValueProvider) pc.getMock();

        op.getPropertyType("fred");
        opc.setReturnValue(null);

        op.claimProperty("fred");

        p.obtainValue("service:barney", l);
        pc.setReturnValue(null);

        replayControls();

        InjectObjectWorker w = new InjectObjectWorker();
        w.setProvider(p);

        try
        {
            w.performEnhancement(op, spec);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Value obtained using locator 'service:barney' is null.", ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verifyControls();
    }

    public void testInjectedValueWrongType()
    {
        Location l = newLocation();

        InjectSpecification spec = newSpec("fred", "service:barney", l);

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        MockControl pc = newControl(InjectedValueProvider.class);
        InjectedValueProvider p = (InjectedValueProvider) pc.getMock();

        op.getPropertyType("fred");
        opc.setReturnValue(IEngineService.class);

        op.claimProperty("fred");

        p.obtainValue("service:barney", l);
        pc.setReturnValue("INJECTED-VALUE");

        replayControls();

        InjectObjectWorker w = new InjectObjectWorker();
        w.setProvider(p);

        try
        {
            w.performEnhancement(op, spec);
            unreachable();

        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "The value obtained using locator 'service:barney' (INJECTED-VALUE) is not compatible with the existing property (of type org.apache.tapestry.engine.IEngineService).",
                    ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verifyControls();
    }
}