// Copyright 2004, 2005 The Apache Software Foundation
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IPropertySpecification;
import org.apache.tapestry.spec.PropertySpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.enhance.SpecifiedPropertyWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestSpecifiedPropertyWorker extends HiveMindTestCase
{
    private List buildPropertySpecs(String name, String type, boolean persistent)
    {
        return buildPropertySpecs(name, type, persistent, null);
    }

    private List buildPropertySpecs(String name, String type, boolean persistent, Location location)
    {
        PropertySpecification ps = new PropertySpecification();
        ps.setName(name);
        ps.setType(type);
        ps.setPersistent(persistent);
        ps.setLocation(location);

        return Collections.singletonList(ps);
    }

    private IComponentSpecification buildComponentSpecification(List propertySpecs)
    {
        List names = new ArrayList();
        Iterator i = propertySpecs.iterator();
        while (i.hasNext())
        {
            IPropertySpecification ps = (IPropertySpecification) i.next();

            names.add(ps.getName());
        }

        MockControl c = newControl(IComponentSpecification.class);
        IComponentSpecification result = (IComponentSpecification) c.getMock();

        result.getPropertySpecificationNames();
        c.setReturnValue(names);

        i = propertySpecs.iterator();
        while (i.hasNext())
        {
            IPropertySpecification ps = (IPropertySpecification) i.next();

            result.getPropertySpecification(ps.getName());
            c.setReturnValue(ps);
        }

        return result;
    }

    private IComponentSpecification buildComponentSpecification(String name, String type,
            boolean persistent)
    {
        return buildComponentSpecification(buildPropertySpecs(name, type, persistent));
    }

    public void testAddNormal() throws Exception
    {
        IComponentSpecification spec = buildComponentSpecification("fred", "boolean", false);

        // Training

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getSpecification();
        opc.setReturnValue(spec);

        op.convertTypeName("boolean");
        opc.setReturnValue(boolean.class);

        op.validateProperty("fred", boolean.class);
        op.claimProperty("fred");
        op.addField("_$fred", boolean.class);

        op.getAccessorMethodName("fred");
        opc.setReturnValue("isFred");

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(boolean.class, "isFred", null, null),
                "return _$fred;");

        op.addMethod(Modifier.PUBLIC, new MethodSignature(void.class, "setFred", new Class[]
        { boolean.class }, null), "{\n  _$fred = $1;\n}\n");

        replayControls();

        SpecifiedPropertyWorker w = new SpecifiedPropertyWorker();

        w.performEnhancement(op);

        verifyControls();
    }

    public void testAddPersistent() throws Exception
    {
        IComponentSpecification spec = buildComponentSpecification(
                "barney",
                "java.lang.String",
                true);

        // Training

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getSpecification();
        opc.setReturnValue(spec);

        op.convertTypeName("java.lang.String");
        opc.setReturnValue(String.class);

        op.validateProperty("barney", String.class);
        op.claimProperty("barney");
        op.addField("_$barney", String.class);

        op.getAccessorMethodName("barney");
        opc.setReturnValue("getBarney");

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(String.class, "getBarney", null, null),
                "return _$barney;");

        BodyBuilder b = new BodyBuilder();
        b.begin();
        b.addln("org.apache.tapestry.Tapestry#fireObservedChange(this, \"barney\", ($w) $1);");
        b.addln("_$barney = $1;");
        b.end();

        op.addMethod(Modifier.PUBLIC, new MethodSignature(void.class, "setBarney", new Class[]
        { String.class }, null), b.toString());

        replayControls();

        SpecifiedPropertyWorker w = new SpecifiedPropertyWorker();

        w.performEnhancement(op);

        verifyControls();
    }

    public void testFailure() throws Exception
    {
        Location l = fabricateLocation(207);
        // Should be "java.lang.Long"
        List propertySpecs = buildPropertySpecs("wilma", "Long", false, l);
        IComponentSpecification spec = buildComponentSpecification(propertySpecs);

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getSpecification();
        opc.setReturnValue(spec);

        op.convertTypeName("Long");
        Throwable ex = new ApplicationRuntimeException("Simulated error.");
        opc.setThrowable(ex);

        op.getBaseClass();
        opc.setReturnValue(BaseComponent.class);

        ErrorLog log = (ErrorLog) newMock(ErrorLog.class);

        log
                .error(
                        "Error adding property 'wilma' to class org.apache.tapestry.BaseComponent: Simulated error.",
                        l,
                        ex);

        replayControls();

        SpecifiedPropertyWorker w = new SpecifiedPropertyWorker();
        w.setErrorLog(log);

        w.performEnhancement(op);

        verifyControls();
    }

}