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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.InjectStateSpecification;
import org.apache.tapestry.spec.InjectStateSpecificationImpl;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.enhance.InjectStateWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestInjectStateWorker extends HiveMindTestCase
{
    private ApplicationStateManager newASM()
    {
        return (ApplicationStateManager) newMock(ApplicationStateManager.class);
    }

    private IComponentSpecification newSpecification(List injects)
    {
        MockControl control = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) control.getMock();

        spec.getInjectStateSpecifications();
        control.setReturnValue(injects);

        return spec;
    }

    private List newInjects(String propertyName, String objectName, Location l)
    {
        InjectStateSpecification spec = new InjectStateSpecificationImpl();

        spec.setProperty(propertyName);
        spec.setObjectName(objectName);
        spec.setLocation(l);

        return Collections.singletonList(spec);
    }

    public void testNoWorkToDo()
    {
        IComponentSpecification spec = newSpecification(Collections.EMPTY_LIST);
        EnhancementOperation op = (EnhancementOperation) newMock(EnhancementOperation.class);

        replayControls();

        new InjectStateWorker().performEnhancement(op, spec);

        verifyControls();
    }

    public void testSuccess()
    {
        IComponentSpecification spec = newSpecification(newInjects("fred", "barney", null));
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        ApplicationStateManager asm = newASM();

        op.addField("_$applicationStateManager", ApplicationStateManager.class, asm);

        op.getPropertyType("fred");
        opc.setReturnValue(Map.class);

        op.claimProperty("fred");
        op.addField("_$fred", Map.class);

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

    public void testFailure()
    {
        Throwable ex = new ApplicationRuntimeException(EnhanceMessages.claimedProperty("fred"));
        Location l = fabricateLocation(22);

        IComponentSpecification spec = newSpecification(newInjects("fred", "barney", l));
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        ErrorLog errorLog = (ErrorLog) newMock(ErrorLog.class);

        ApplicationStateManager asm = newASM();

        op.addField("_$applicationStateManager", ApplicationStateManager.class, asm);

        op.getPropertyType("fred");
        opc.setReturnValue(Map.class);

        op.claimProperty("fred");
        opc.setThrowable(ex);

        op.getBaseClass();
        opc.setReturnValue(BaseComponent.class);

        errorLog.error(EnhanceMessages.errorAddingProperty("fred", BaseComponent.class, ex), l, ex);

        replayControls();

        InjectStateWorker w = new InjectStateWorker();
        w.setApplicationStateManager(asm);
        w.setErrorLog(errorLog);

        w.performEnhancement(op, spec);

        verifyControls();
    }
}