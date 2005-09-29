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
import org.apache.hivemind.Location;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.apache.tapestry.spec.InjectSpecification;
import org.apache.tapestry.spec.InjectSpecificationImpl;

/**
 * Tests for {@link org.apache.tapestry.enhance.InjectStateFlagWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InjectStateFlagWorkerTest extends BaseEnhancementTestCase
{
    public void testNoExistingProperty()
    {
        Location l = newLocation();
        ApplicationStateManager asm = newApplicationStateManager();

        InjectSpecification is = new InjectSpecificationImpl();
        is.setProperty("fred");
        is.setObject("fredASO");
        is.setLocation(l);

        EnhancementOperation op = newOp();

        trainGetPropertyType(op, "fred", null);

        op.claimReadonlyProperty("fred");

        trainAddInjectedField(
                op,
                "_$applicationStateManager",
                ApplicationStateManager.class,
                asm,
                "_$asm");

        trainGetAccessorMethodName(op, "fred", "isFred");

        MethodSignature sig = new MethodSignature(boolean.class, "isFred", null, null);

        String code = "{\n  return _$asm.exists(\"fredASO\");\n}\n";

        op.addMethod(Modifier.PUBLIC, sig, code, l);

        replayControls();

        InjectStateFlagWorker worker = new InjectStateFlagWorker();

        worker.setApplicationStateManager(asm);

        worker.performEnhancement(op, is);

        verifyControls();
    }

    public void testWithExistingProperty()
    {
        Location l = newLocation();
        ApplicationStateManager asm = newApplicationStateManager();

        InjectSpecification is = new InjectSpecificationImpl();
        is.setProperty("fred");
        is.setObject("fredASO");
        is.setLocation(l);

        EnhancementOperation op = newOp();

        trainGetPropertyType(op, "fred", boolean.class);

        op.claimReadonlyProperty("fred");

        trainAddInjectedField(
                op,
                "_$applicationStateManager",
                ApplicationStateManager.class,
                asm,
                "_$asm");

        trainGetAccessorMethodName(op, "fred", "getFred");

        MethodSignature sig = new MethodSignature(boolean.class, "getFred", null, null);

        String code = "{\n  return _$asm.exists(\"fredASO\");\n}\n";

        op.addMethod(Modifier.PUBLIC, sig, code, l);

        replayControls();

        InjectStateFlagWorker worker = new InjectStateFlagWorker();

        worker.setApplicationStateManager(asm);

        worker.performEnhancement(op, is);

        verifyControls();
    }

    public void testWithExistingPropertyWrongType()
    {
        Location l = newLocation();
        ApplicationStateManager asm = newApplicationStateManager();

        InjectSpecification is = new InjectSpecificationImpl();
        is.setProperty("fred");
        is.setObject("fredASO");
        is.setLocation(l);

        EnhancementOperation op = newOp();

        trainGetPropertyType(op, "fred", int.class);

        replayControls();

        InjectStateFlagWorker worker = new InjectStateFlagWorker();

        worker.setApplicationStateManager(asm);

        try
        {
            worker.performEnhancement(op, is);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Property fred must be type boolean for this type of injection.", ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verifyControls();
    }
}
