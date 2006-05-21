// Copyright May 21, 2006 The Apache Software Foundation
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
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.services.impl.ComponentEventInvoker;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;


/**
 * Test functionality of {@link InjectEventInvokerWorker} enhancer.
 * 
 * @author jkuhnert
 */
public class TestInjectEventInvokerWorker extends BaseEnhancementTestCase
{

    public void testSuccess() throws Exception
    {
        Location l = newLocation();

        MockControl control = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) control.getMock();
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        
        IComponentSpecification spec = newSpec(l);
        
        op.claimReadonlyProperty("eventInvoker");
        
        op.addInjectedField("_$eventInvoker", ComponentEventInvoker.class, invoker);
        control.setReturnValue("_$eventInvoker");
        
        op.getAccessorMethodName("eventInvoker");
        control.setReturnValue("getEventInvoker");
        
        op.addMethod(Modifier.PUBLIC, new MethodSignature(ComponentEventInvoker.class,
                "getEventInvoker", null, null), "return _$eventInvoker;", l);
        
        replayControls();
        
        InjectEventInvokerWorker worker = new InjectEventInvokerWorker();
        worker.setComponentEventInvoker(invoker);
        worker.performEnhancement(op, spec);
        
        verifyControls();
    }
    
    public void testFailure()
    {
        Location l = newLocation();

        MockControl control = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) control.getMock();
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        
        ErrorLog log = (ErrorLog) newMock(ErrorLog.class);
        
        Throwable ex = new ApplicationRuntimeException(EnhanceMessages
                .claimedProperty("eventInvoker"));
        
        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        op.claimReadonlyProperty("eventInvoker");
        control.setThrowable(ex);

        op.getBaseClass();
        control.setReturnValue(BaseComponent.class);
        
        spec.getLocation();
        specc.setReturnValue(l);

        log.error(
                EnhanceMessages.errorAddingProperty("eventInvoker", BaseComponent.class, ex),
                l,
                ex);

        replayControls();

        InjectEventInvokerWorker worker = new InjectEventInvokerWorker();
        worker.setComponentEventInvoker(invoker);
        worker.setErrorLog(log);
        
        worker.performEnhancement(op, spec);

        verifyControls();
    }
    
}
