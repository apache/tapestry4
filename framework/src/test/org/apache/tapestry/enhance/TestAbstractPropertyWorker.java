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

import java.lang.reflect.Modifier;
import java.util.Collections;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.spec.IComponentSpecification;
import org.testng.annotations.Test;

/**
 * Tests {@link org.apache.tapestry.enhance.AbstractPropertyWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestAbstractPropertyWorker extends BaseEnhancementTestCase
{

    public void testSuccess()
    {
        Location l = newLocation();

        IComponentSpecification spec = newSpec(l);
        EnhancementOperation op = newOp();

        expect(op.findUnclaimedAbstractProperties()).andReturn(Collections.singletonList("fred"));

        expect(op.getPropertyType("fred")).andReturn(String.class);

        op.addField("_$fred", String.class);
        op.addField("_$fred$defaultValue", String.class);

        expect(op.getAccessorMethodName("fred")).andReturn("getFred");

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(String.class, "getFred", null, null),
                "return _$fred;",
                l);

        op.addMethod(Modifier.PUBLIC, new MethodSignature(void.class, "setFred", new Class[]
        { String.class }, null), "_$fred = $1;", l);

        op.extendMethodImplementation(
                IComponent.class,
                EnhanceUtils.FINISH_LOAD_SIGNATURE,
                "_$fred$defaultValue = _$fred;");

        op.extendMethodImplementation(
                PageDetachListener.class,
                EnhanceUtils.PAGE_DETACHED_SIGNATURE,
                "_$fred = _$fred$defaultValue;");

        op.claimProperty("fred");

        replay();

        new AbstractPropertyWorker().performEnhancement(op, spec);

        verify();
    }

    public void testFailure()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();

        Location l = fabricateLocation(21);

        ErrorLog log = newMock(ErrorLog.class);

        expect(op.findUnclaimedAbstractProperties()).andReturn(Collections.singletonList("fred"));
        Throwable ex = new ApplicationRuntimeException("Arbitrary error.");

        expect(op.getPropertyType("fred")).andThrow(ex);

        expect(op.getBaseClass()).andReturn(BaseComponent.class);

        expect(spec.getLocation()).andReturn(l);

        log.error(EnhanceMessages.errorAddingProperty("fred", BaseComponent.class, ex), l, ex);

        replay();

        AbstractPropertyWorker w = new AbstractPropertyWorker();
        w.setErrorLog(log);

        w.performEnhancement(op, spec);

        verify();
    }
}
