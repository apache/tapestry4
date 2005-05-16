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
import java.util.Map;

import org.apache.hivemind.Location;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.spec.InjectSpecification;
import org.apache.tapestry.spec.InjectSpecificationImpl;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.enhance.InjectStateWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestInjectStateWorker extends HiveMindTestCase
{
    private ApplicationStateManager newASM()
    {
        return (ApplicationStateManager) newMock(ApplicationStateManager.class);
    }

    private InjectSpecification newSpec(String propertyName, String objectName, Location l)
    {
        InjectSpecification spec = new InjectSpecificationImpl();

        spec.setProperty(propertyName);
        spec.setObject(objectName);
        spec.setLocation(l);

        return spec;
    }

    public void testSuccess()
    {
        InjectSpecification spec = newSpec("fred", "barney", null);
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        ApplicationStateManager asm = newASM();

        op.getPropertyType("fred");
        opc.setReturnValue(Map.class);

        op.claimProperty("fred");
        op.addField("_$fred", Map.class);

        op.addInjectedField("_$applicationStateManager", asm);
        opc.setReturnValue("_$applicationStateManager");

        op.getAccessorMethodName("fred");
        opc.setReturnValue("getFred");

        BodyBuilder builder = new BodyBuilder();

        // Accessor

        builder.begin();
        builder.addln("if (_$fred == null)");
        builder.addln("  _$fred = (java.util.Map) _$applicationStateManager.get(\"barney\");");
        builder.addln("return _$fred;");
        builder.end();

        MethodSignature sig = new MethodSignature(Map.class, "getFred", null, null);

        op.addMethod(Modifier.PUBLIC, sig, builder.toString());

        builder.clear();
        builder.begin();
        builder.addln("_$applicationStateManager.store(\"barney\", $1);");
        builder.addln("_$fred = $1;");
        builder.end();

        sig = new MethodSignature(void.class, "setFred", new Class[]
        { Map.class }, null);

        op.addMethod(Modifier.PUBLIC, sig, builder.toString());
        op.extendMethodImplementation(
                PageDetachListener.class,
                EnhanceUtils.PAGE_DETACHED_SIGNATURE,
                "_$fred = null;");

        replayControls();

        InjectStateWorker w = new InjectStateWorker();
        w.setApplicationStateManager(asm);

        w.performEnhancement(op, spec);

        verifyControls();
    }
}