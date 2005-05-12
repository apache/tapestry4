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

import java.util.Collections;
import java.util.Map;

import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.InjectSpecification;
import org.apache.tapestry.spec.InjectSpecificationImpl;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.enhance.DispatchToInjectWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestDispatchToInjectWorker extends HiveMindTestCase
{
    private InjectSpecification newInjectSpecification(String propertyName, String type,
            String object)
    {
        return newInjectSpecification(propertyName, type, object, null);
    }

    private InjectSpecification newInjectSpecification(String propertyName, String type,
            String object, Location location)
    {
        InjectSpecification result = new InjectSpecificationImpl();
        result.setProperty(propertyName);
        result.setType(type);
        result.setObject(object);
        result.setLocation(location);

        return result;
    }

    private IComponentSpecification newSpec(InjectSpecification injectSpec)
    {
        MockControl control = newControl(IComponentSpecification.class);
        IComponentSpecification spec = (IComponentSpecification) control.getMock();

        spec.getInjectSpecifications();
        control.setReturnValue(Collections.singletonList(injectSpec));

        return spec;
    }

    private Map newMap(String key, Object value)
    {
        return Collections.singletonMap(key, value);
    }

    public void testSuccess()
    {
        EnhancementOperation op = newOp();
        InjectSpecification is = newInjectSpecification("property", "object", "service:Foo");
        InjectEnhancementWorker worker = newWorker();
        Map map = newMap("object", worker);
        IComponentSpecification spec = newSpec(is);

        worker.performEnhancement(op, is);

        replayControls();

        DispatchToInjectWorker d = new DispatchToInjectWorker();
        d.setInjectWorkers(map);

        d.performEnhancement(op, spec);

        verifyControls();
    }

    public void testUnknownType()
    {
        Location l = newLocation();
        EnhancementOperation op = newOp();
        InjectSpecification is = newInjectSpecification(
                "injectedProperty",
                "object",
                "service:Foo",
                l);
        IComponentSpecification spec = newSpec(is);
        ErrorLog log = newLog();

        log.error(EnhanceMessages.unknownInjectType("injectedProperty", "object"), l, null);

        replayControls();

        DispatchToInjectWorker d = new DispatchToInjectWorker();
        d.setInjectWorkers(Collections.EMPTY_MAP);
        d.setErrorLog(log);

        d.performEnhancement(op, spec);

        verifyControls();
    }

    public void testFailure()
    {
        Location l = newLocation();
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();
        InjectSpecification is = newInjectSpecification("myProperty", "object", "service:Foo", l);
        MockControl workerc = newControl(InjectEnhancementWorker.class);
        InjectEnhancementWorker worker = (InjectEnhancementWorker) workerc.getMock();
        Map map = newMap("object", worker);
        IComponentSpecification spec = newSpec(is);
        Throwable t = new RuntimeException("Simulated failure.");
        ErrorLog log = newLog();

        worker.performEnhancement(op, is);
        workerc.setThrowable(t);

        op.getBaseClass();
        opc.setReturnValue(BasePage.class);

        log
                .error(
                        "Error adding property 'myProperty' to class org.apache.tapestry.html.BasePage: Simulated failure.",
                        l,
                        t);

        replayControls();

        DispatchToInjectWorker d = new DispatchToInjectWorker();
        d.setInjectWorkers(map);
        d.setErrorLog(log);

        d.performEnhancement(op, spec);

        verifyControls();
    }

    private InjectEnhancementWorker newWorker()
    {
        return (InjectEnhancementWorker) newMock(InjectEnhancementWorker.class);
    }

    private ErrorLog newLog()
    {
        return (ErrorLog) newMock(ErrorLog.class);
    }

    private EnhancementOperation newOp()
    {
        return (EnhancementOperation) newMock(EnhancementOperation.class);
    }
}
