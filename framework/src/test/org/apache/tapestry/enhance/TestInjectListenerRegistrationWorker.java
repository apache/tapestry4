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

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link TestInjectListenerRegistrationWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestInjectListenerRegistrationWorker extends HiveMindTestCase
{
    private IComponentSpecification newSpec()
    {
        return (IComponentSpecification) newMock(IComponentSpecification.class);
    }

    public void testNonMatch()
    {
        MockControl control = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) control.getMock();

        IComponentSpecification spec = newSpec();

        op.implementsInterface(Runnable.class);
        control.setReturnValue(false);

        replayControls();

        InjectListenerRegistrationWorker w = new InjectListenerRegistrationWorker();
        w.setListenerInterface(Runnable.class);

        w.performEnhancement(op, spec);

        verifyControls();
    }

    public void testMatch()
    {
        MockControl control = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) control.getMock();

        IComponentSpecification spec = newSpec();

        op.implementsInterface(IComponent.class);
        control.setReturnValue(true);

        op.extendMethodImplementation(
                IComponent.class,
                EnhanceUtils.FINISH_LOAD_SIGNATURE,
                "getPage().addThisComponentAsListener(this);");

        replayControls();

        InjectListenerRegistrationWorker w = new InjectListenerRegistrationWorker();
        w.setListenerInterface(IComponent.class);
        w.setRegisterMethodName("addThisComponentAsListener");

        w.performEnhancement(op, spec);

        verifyControls();
    }

}