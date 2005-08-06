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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;

/**
 * Tests {@link org.apache.tapestry.enhance.AbstractPropertyWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestAbstractPropertyWorker extends BaseEnhancementTestCase
{

    public void testSuccess()
    {
        Location l = newLocation();

        IComponentSpecification spec = newSpec(l);

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.findUnclaimedAbstractProperties();
        opc.setReturnValue(Collections.singletonList("fred"));

        op.getPropertyType("fred");
        opc.setReturnValue(String.class);

        op.addField("_$fred", String.class);
        op.addField("_$fred$defaultValue", String.class);

        op.getAccessorMethodName("fred");
        opc.setReturnValue("getFred");

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

        replayControls();

        new AbstractPropertyWorker().performEnhancement(op, spec);

        verifyControls();
    }

    public void testFailure()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        Location l = fabricateLocation(21);

        ErrorLog log = (ErrorLog) newMock(ErrorLog.class);

        op.findUnclaimedAbstractProperties();
        opc.setReturnValue(Collections.singletonList("fred"));

        Throwable ex = new ApplicationRuntimeException("Arbitrary error.");

        op.getPropertyType("fred");
        opc.setThrowable(ex);

        op.getBaseClass();
        opc.setReturnValue(BaseComponent.class);

        spec.getLocation();
        specc.setReturnValue(l);

        log.error(EnhanceMessages.errorAddingProperty("fred", BaseComponent.class, ex), l, ex);

        replayControls();

        AbstractPropertyWorker w = new AbstractPropertyWorker();
        w.setErrorLog(log);

        w.performEnhancement(op, spec);

        verifyControls();
    }
}