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

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.testng.annotations.Test;

/**
 * Tests for {@link TestInjectListenerRegistrationWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestInjectListenerRegistrationWorker extends BaseComponentTestCase
{

    public void testNonMatch()
    {
        EnhancementOperation op = newMock(EnhancementOperation.class);

        IComponentSpecification spec = newSpec();

        expect(op.implementsInterface(Runnable.class)).andReturn(false);

        replay();

        InjectListenerRegistrationWorker w = new InjectListenerRegistrationWorker();
        w.setListenerInterface(Runnable.class);

        w.performEnhancement(op, spec);

        verify();
    }

    public void testMatch()
    {
        EnhancementOperation op = newMock(EnhancementOperation.class);

        IComponentSpecification spec = newSpec();

        expect(op.implementsInterface(IComponent.class)).andReturn(false);

        op.extendMethodImplementation(
                IComponent.class,
                EnhanceUtils.FINISH_LOAD_SIGNATURE,
                "getPage().addThisComponentAsListener(this);");

        replay();

        InjectListenerRegistrationWorker w = new InjectListenerRegistrationWorker();
        w.setListenerInterface(IComponent.class);
        w.setRegisterMethodName("addThisComponentAsListener");

        w.performEnhancement(op, spec);

        verify();
    }

}