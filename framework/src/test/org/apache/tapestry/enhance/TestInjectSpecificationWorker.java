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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.enhance.InjectSpecificationWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestInjectSpecificationWorker extends HiveMindTestCase
{
    private IComponentSpecification newSpec()
    {
        return (IComponentSpecification) newMock(IComponentSpecification.class);
    }

    public void testSuccess() throws Exception
    {
        MockControl control = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) control.getMock();

        IComponentSpecification spec = newSpec();

        op.claimProperty("specification");

        op.addFinalField("_$specification",  spec);
        control.setReturnValue("_$specification");

        op.getAccessorMethodName("specification");
        control.setReturnValue("getSpecification");

        op.addMethod(Modifier.PUBLIC, new MethodSignature(IComponentSpecification.class,
                "getSpecification", null, null), "return _$specification;");

        replayControls();

        new InjectSpecificationWorker().performEnhancement(op, spec);

        verifyControls();
    }

    public void testFailure()
    {
        Location l = fabricateLocation(11);

        MockControl control = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) control.getMock();

        ErrorLog log = (ErrorLog) newMock(ErrorLog.class);

        Throwable ex = new ApplicationRuntimeException(EnhanceMessages
                .claimedProperty("specification"));

        MockControl specc = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) specc.getMock();

        op.claimProperty("specification");
        control.setThrowable(ex);

        op.getBaseClass();
        control.setReturnValue(BaseComponent.class);

        spec.getLocation();
        specc.setReturnValue(l);

        log.error(
                EnhanceMessages.errorAddingProperty("specification", BaseComponent.class, ex),
                l,
                ex);

        replayControls();

        InjectSpecificationWorker w = new InjectSpecificationWorker();
        w.setErrorLog(log);

        w.performEnhancement(op, spec);

        verifyControls();
    }
}