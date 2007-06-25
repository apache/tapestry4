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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import java.lang.reflect.Modifier;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.internal.event.IComponentEventInvoker;
import org.apache.tapestry.internal.event.impl.ComponentEventInvoker;
import org.apache.tapestry.spec.IComponentSpecification;
import org.testng.annotations.Test;


/**
 * Test functionality of {@link InjectEventInvokerWorker} enhancer.
 * 
 * @author jkuhnert
 */
@Test
public class TestInjectEventInvokerWorker extends BaseEnhancementTestCase
{

    public void testSuccess() throws Exception
    {
        EnhancementOperation op = newOp();
        IComponentEventInvoker invoker = new ComponentEventInvoker();
        
        Location l = newLocation();
        IComponentSpecification spec = newSpec(l);
        
        op.claimReadonlyProperty("eventInvoker");
        
        expect(op.addInjectedField("_$eventInvoker", IComponentEventInvoker.class, invoker))
        .andReturn("_$eventInvoker");
        
        expect(op.getAccessorMethodName("eventInvoker")).andReturn("getEventInvoker");
        
        op.addMethod(Modifier.PUBLIC, new MethodSignature(IComponentEventInvoker.class,
                "getEventInvoker", null, null), "return _$eventInvoker;", l);
        
        replay();
        
        InjectEventInvokerWorker worker = new InjectEventInvokerWorker();
        worker.setComponentEventInvoker(invoker);
        worker.performEnhancement(op, spec);
        
        verify();
    }
    
    public void testFailure()
    {
        Location l = newLocation();
        
        EnhancementOperation op = newOp();
        IComponentEventInvoker invoker = new ComponentEventInvoker();
        
        ErrorLog log = newMock(ErrorLog.class);
        
        Throwable ex = new ApplicationRuntimeException(EnhanceMessages
                .claimedProperty("eventInvoker"));
        
        IComponentSpecification spec = newSpec();

        op.claimReadonlyProperty("eventInvoker");
        expectLastCall().andThrow(ex);

        expect(op.getBaseClass()).andReturn(BaseComponent.class);
        
        expect(spec.getLocation()).andReturn(l);

        log.error(
                EnhanceMessages.errorAddingProperty("eventInvoker", BaseComponent.class, ex),
                l,
                ex);

        replay();

        InjectEventInvokerWorker worker = new InjectEventInvokerWorker();
        worker.setComponentEventInvoker(invoker);
        worker.setErrorLog(log);
        
        worker.performEnhancement(op, spec);

        verify();
    }
    
}
