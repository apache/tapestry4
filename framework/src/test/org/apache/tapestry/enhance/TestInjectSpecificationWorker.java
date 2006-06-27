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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Tests for {@link org.apache.tapestry.enhance.InjectSpecificationWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestInjectSpecificationWorker extends BaseEnhancementTestCase
{

    public void testSuccess() throws Exception
    {
        Location l = newLocation();
        
        EnhancementOperation op = newOp();

        IComponentSpecification spec = newSpec(l);

        op.claimReadonlyProperty("specification");

        expect(op.addInjectedField("_$specification", IComponentSpecification.class, spec))
        .andReturn("_$specification");

        expect(op.getAccessorMethodName("specification")).andReturn("getSpecification");

        op.addMethod(Modifier.PUBLIC, new MethodSignature(IComponentSpecification.class,
                "getSpecification", null, null), "return _$specification;", l);

        replay();

        new InjectSpecificationWorker().performEnhancement(op, spec);

        verify();
    }

    public void testFailure()
    {
        Location l = newLocation();
        EnhancementOperation op = newOp();

        ErrorLog log = (ErrorLog) newMock(ErrorLog.class);

        Throwable ex = new ApplicationRuntimeException(EnhanceMessages
                .claimedProperty("specification"));
        
        IComponentSpecification spec = newSpec();

        op.claimReadonlyProperty("specification");
        expectLastCall().andThrow(ex);

        expect(op.getBaseClass()).andReturn(BaseComponent.class);

        expect(spec.getLocation()).andReturn(l);

        log.error(
                EnhanceMessages.errorAddingProperty("specification", BaseComponent.class, ex),
                l,
                ex);

        replay();

        InjectSpecificationWorker w = new InjectSpecificationWorker();
        w.setErrorLog(log);

        w.performEnhancement(op, spec);

        verify();
    }
}