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

package org.apache.tapestry.enhance;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.spec.BeanSpecification;
import org.apache.tapestry.spec.IBeanSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.enhance.InjectBeanWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestInjectBeanWorker extends HiveMindTestCase
{
    private IComponentSpecification newSpec(String beanName, String propertyName, Location location)
    {
        IBeanSpecification bs = new BeanSpecification();
        bs.setPropertyName(propertyName);
        bs.setLocation(location);

        MockControl control = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) control.getMock();

        spec.getBeanNames();
        control.setReturnValue(Collections.singletonList(beanName));

        spec.getBeanSpecification(beanName);
        control.setReturnValue(bs);

        return spec;
    }

    public void testNoWork()
    {
        IComponentSpecification spec = newSpec("fred", null, null);
        EnhancementOperation op = (EnhancementOperation) newMock(EnhancementOperation.class);

        replayControls();

        new InjectBeanWorker().performEnhancement(op, spec);

        verifyControls();
    }

    public void testSuccess()
    {
        Location l = newLocation();
        IComponentSpecification spec = newSpec("fred", "barney", l);
        MockControl control = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) control.getMock();

        op.claimReadonlyProperty("barney");

        op.getPropertyType("barney");
        control.setReturnValue(ArrayList.class);

        op.getAccessorMethodName("barney");
        control.setReturnValue("getBarney");

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(ArrayList.class, "getBarney", null, null),
                "return (java.util.ArrayList) getBeans().getBean(\"fred\");",
                l);

        replayControls();

        new InjectBeanWorker().performEnhancement(op, spec);

        verifyControls();
    }

    public void testFailure()
    {
        Location l = fabricateLocation(99);
        Throwable ex = new ApplicationRuntimeException(EnhanceMessages.claimedProperty("barney"));

        MockControl control = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) control.getMock();

        IComponentSpecification spec = newSpec("fred", "barney", l);

        ErrorLog log = (ErrorLog) newMock(ErrorLog.class);

        op.claimReadonlyProperty("barney");
        control.setThrowable(ex);

        op.getBaseClass();
        control.setReturnValue(BaseComponent.class);

        log.error(EnhanceMessages.errorAddingProperty("barney", BaseComponent.class, ex), l, ex);

        replayControls();

        InjectBeanWorker w = new InjectBeanWorker();

        w.setErrorLog(log);

        w.performEnhancement(op, spec);

        verifyControls();
    }

}