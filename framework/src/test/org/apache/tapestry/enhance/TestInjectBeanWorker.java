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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.spec.BeanSpecification;
import org.apache.tapestry.spec.IBeanSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.enhance.InjectBeanWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestInjectBeanWorker extends BaseComponentTestCase
{
    private IComponentSpecification newSpec(String beanName, String propertyName, Location location)
    {
        IBeanSpecification bs = new BeanSpecification();
        bs.setPropertyName(propertyName);
        bs.setLocation(location);
        
        IComponentSpecification spec = newSpec();

        expect(spec.getBeanNames()).andReturn(Collections.singletonList(beanName));

        expect(spec.getBeanSpecification(beanName)).andReturn(bs);

        return spec;
    }

    private EnhancementOperation newOp()
    {
        return newMock(EnhancementOperation.class);
    }
    
    public void testNoWork()
    {
        IComponentSpecification spec = newSpec("fred", null, null);
        EnhancementOperation op = newMock(EnhancementOperation.class);

        replay();

        new InjectBeanWorker().performEnhancement(op, spec);

        verify();
    }

    public void testSuccess()
    {
        Location l = newLocation();
        IComponentSpecification spec = newSpec("fred", "barney", l);
        
        EnhancementOperation op = newOp();

        op.claimReadonlyProperty("barney");

        expect(op.getPropertyType("barney")).andReturn(ArrayList.class);

        expect(op.getAccessorMethodName("barney")).andReturn("getBarney");

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(ArrayList.class, "getBarney", null, null),
                "return (java.util.ArrayList) getBeans().getBean(\"fred\");",
                l);

        replay();

        new InjectBeanWorker().performEnhancement(op, spec);

        verify();
    }

    public void testFailure()
    {
        Location l = fabricateLocation(99);
        Throwable ex = new ApplicationRuntimeException(EnhanceMessages.claimedProperty("barney"));

        EnhancementOperation op = newOp();

        IComponentSpecification spec = newSpec("fred", "barney", l);

        ErrorLog log = newMock(ErrorLog.class);

        op.claimReadonlyProperty("barney");
        expectLastCall().andThrow(ex);

        expect(op.getBaseClass()).andReturn(BaseComponent.class);

        log.error(EnhanceMessages.errorAddingProperty("barney", BaseComponent.class, ex), l, ex);

        replay();

        InjectBeanWorker w = new InjectBeanWorker();

        w.setErrorLog(log);

        w.performEnhancement(op, spec);

        verify();
    }

}