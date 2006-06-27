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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import java.lang.reflect.Modifier;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.engine.HomeService;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.services.InjectedValueProvider;
import org.apache.tapestry.spec.InjectSpecification;
import org.apache.tapestry.spec.InjectSpecificationImpl;

/**
 * Tests for {@link org.apache.tapestry.enhance.InjectObjectWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestInjectObjectWorker extends BaseComponentTestCase
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

        EnhancementOperation op = newMock(EnhancementOperation.class);

        InjectedValueProvider p = newMock(InjectedValueProvider.class);

        expect(op.getPropertyType("fred")).andReturn(null);

        op.claimReadonlyProperty("fred");

        expect(p.obtainValue("service:barney", l)).andReturn(injectedValue);

        // When the same bean is injected multiple times, the field name
        // returned won't match the field name suggested; make sure the code
        // generation used the correct field name.

        expect(op.addInjectedField("_$fred", Object.class, injectedValue)).andReturn("_$gnarly");

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(Object.class, "getFred", null, null),
                "return _$gnarly;",
                l);

        replay();

        InjectObjectWorker w = new InjectObjectWorker();
        w.setProvider(p);

        w.performEnhancement(op, spec);

        verify();
    }

    public void testWithExistingProperty()
    {
        Location l = newLocation();
        Object injectedValue = new HomeService();

        InjectSpecification spec = newSpec("wilma", "service:betty", l);

        EnhancementOperation op = newMock(EnhancementOperation.class);

        InjectedValueProvider p = newMock(InjectedValueProvider.class);

        expect(op.getPropertyType("wilma")).andReturn(IEngineService.class);

        op.claimReadonlyProperty("wilma");

        expect(p.obtainValue("service:betty", l)).andReturn(injectedValue);

        expect(op.addInjectedField("_$wilma", IEngineService.class, injectedValue)).andReturn("_$wilma");

        op.addMethod(Modifier.PUBLIC, new MethodSignature(IEngineService.class, "getWilma", null,
                null), "return _$wilma;", l);

        replay();

        InjectObjectWorker w = new InjectObjectWorker();
        w.setProvider(p);

        w.performEnhancement(op, spec);

        verify();
    }

    /**
     * Test what happens when the value to be injected is null (usually indicating an error in the
     * locator and/or a configuration error inside the HiveMind descriptors).
     */

    public void testInjectNull()
    {
        Location l = newLocation();

        InjectSpecification spec = newSpec("fred", "service:barney", l);

        EnhancementOperation op = newMock(EnhancementOperation.class);

        InjectedValueProvider p = newMock(InjectedValueProvider.class);

        expect(op.getPropertyType("fred")).andReturn(null);

        op.claimReadonlyProperty("fred");

        expect(p.obtainValue("service:barney", l)).andReturn(null);

        replay();

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

        verify();
    }

    public void testInjectedValueWrongType()
    {
        Location l = newLocation();

        InjectSpecification spec = newSpec("fred", "service:barney", l);

        EnhancementOperation op = newMock(EnhancementOperation.class);

        InjectedValueProvider p = newMock(InjectedValueProvider.class);

        expect(op.getPropertyType("fred")).andReturn(IEngineService.class);

        op.claimReadonlyProperty("fred");

        expect(p.obtainValue("service:barney", l)).andReturn("INJECTED-VALUE");

        replay();

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

        verify();
    }
}