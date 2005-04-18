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
import java.util.Collections;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.test.AggregateArgumentsMatcher;
import org.apache.hivemind.test.ArgumentMatcher;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.hivemind.test.TypeMatcher;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.engine.HomeService;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.services.InjectedValueProvider;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.InjectSpecification;
import org.apache.tapestry.spec.InjectSpecificationImpl;
import org.easymock.MockControl;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestInjectWorker extends HiveMindTestCase
{
    private List createInjectSpec(String name, String locator, Location location)
    {
        InjectSpecification is = new InjectSpecificationImpl();

        is.setProperty(name);
        is.setObjectReference(locator);
        is.setLocation(location);

        return Collections.singletonList(is);
    }

    private IComponentSpecification createSpec(String name, String locator, Location location)
    {
        return createSpec(createInjectSpec(name, locator, location));
    }

    private IComponentSpecification createSpec(List injectSpecs)
    {
        MockControl control = newControl(IComponentSpecification.class);
        IComponentSpecification result = (IComponentSpecification) control.getMock();

        result.getInjectSpecifications();
        control.setReturnValue(injectSpecs);

        return result;
    }

    public void testNoExistingProperty()
    {
        Location l = fabricateLocation(22);
        Object injectedValue = new Object();

        IComponentSpecification spec = createSpec("fred", "service:barney", l);

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        MockControl pc = newControl(InjectedValueProvider.class);
        InjectedValueProvider p = (InjectedValueProvider) pc.getMock();

        op.getPropertyType("fred");
        opc.setReturnValue(null);

        op.claimProperty("fred");

        p.obtainValue("service:barney", l);

        pc.setReturnValue(injectedValue);

        op.addField("_$fred", Object.class, injectedValue);

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(Object.class, "getFred", null, null),
                "return _$fred;");

        replayControls();

        InjectWorker w = new InjectWorker();
        w.setProvider(p);

        w.performEnhancement(op, spec);

        verifyControls();
    }

    public void testWithExistingProperty()
    {
        Location l = fabricateLocation(11);
        Object injectedValue = new HomeService();

        IComponentSpecification spec = createSpec("wilma", "service:betty", l);

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        MockControl pc = newControl(InjectedValueProvider.class);
        InjectedValueProvider p = (InjectedValueProvider) pc.getMock();

        op.getPropertyType("wilma");
        opc.setReturnValue(IEngineService.class);

        op.claimProperty("wilma");

        p.obtainValue("service:betty", l);

        pc.setReturnValue(injectedValue);

        op.addField("_$wilma", IEngineService.class, injectedValue);

        op.addMethod(Modifier.PUBLIC, new MethodSignature(IEngineService.class, "getWilma", null,
                null), "return _$wilma;");

        replayControls();

        InjectWorker w = new InjectWorker();
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
        Location l = fabricateLocation(22);

        IComponentSpecification spec = createSpec("fred", "service:barney", l);

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        MockControl pc = newControl(InjectedValueProvider.class);
        InjectedValueProvider p = (InjectedValueProvider) pc.getMock();

        MockControl logc = newControl(ErrorLog.class);
        ErrorLog log = (ErrorLog) logc.getMock();

        op.getPropertyType("fred");
        opc.setReturnValue(null);

        op.claimProperty("fred");

        p.obtainValue("service:barney", l);
        pc.setReturnValue(null);

        op.getBaseClass();
        opc.setReturnValue(BaseComponent.class);

        trainForException(
                logc,
                log,
                "fred",
                EnhanceMessages.locatedValueIsNull("service:barney"),
                l);

        replayControls();

        InjectWorker w = new InjectWorker();
        w.setProvider(p);
        w.setErrorLog(log);

        w.performEnhancement(op, spec);

        verifyControls();
    }

    public void testInjectedValueWrongType()
    {
        Location l = fabricateLocation(22);

        IComponentSpecification spec = createSpec("fred", "service:barney", l);

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        MockControl pc = newControl(InjectedValueProvider.class);
        InjectedValueProvider p = (InjectedValueProvider) pc.getMock();

        MockControl logc = newControl(ErrorLog.class);
        ErrorLog log = (ErrorLog) logc.getMock();

        op.getPropertyType("fred");
        opc.setReturnValue(IEngineService.class);

        op.claimProperty("fred");

        p.obtainValue("service:barney", l);
        pc.setReturnValue("INJECTED-VALUE");

        op.getBaseClass();
        opc.setReturnValue(BaseComponent.class);

        trainForException(logc, log, "fred", EnhanceMessages.incompatibleInjectType(
                "service:barney",
                "INJECTED-VALUE",
                IEngineService.class), l);

        replayControls();

        InjectWorker w = new InjectWorker();
        w.setProvider(p);
        w.setErrorLog(log);

        w.performEnhancement(op, spec);

        verifyControls();
    }

    private void trainForException(MockControl control, ErrorLog log, String propertyName,
            String innerMessage, Location l)
    {
        ApplicationRuntimeException inner = new ApplicationRuntimeException(innerMessage);
        String outerMessage = EnhanceMessages.errorAddingProperty(
                propertyName,
                BaseComponent.class,
                inner);

        log.error(outerMessage, l, inner);
        control.setMatcher(new AggregateArgumentsMatcher(new ArgumentMatcher[]
        { null, null, new TypeMatcher() }));
    }
}