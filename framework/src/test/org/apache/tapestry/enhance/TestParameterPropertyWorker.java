// Copyright 2004 The Apache Software Foundation
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

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.spec.Direction;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.apache.tapestry.spec.ParameterSpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.enhance.ParameterPropertyWorker}.
 * 
 * @author Howard M. Lewis Ship
 */
public class TestParameterPropertyWorker extends HiveMindTestCase
{
    private ParameterSpecification buildParameterSpec(String propertyName, String type,
            Direction direction)
    {
        return buildParameterSpec(propertyName, type, direction, null);
    }

    private ParameterSpecification buildParameterSpec(String propertyName, String type,
            Direction direction, Location location)
    {
        ParameterSpecification ps = new ParameterSpecification();

        ps.setPropertyName(propertyName);
        ps.setType(type);
        ps.setDirection(direction);
        ps.setLocation(location);

        return ps;
    }

    private IComponentSpecification buildComponentSpecification(String parameterName,
            IParameterSpecification ps)
    {
        MockControl c = newControl(IComponentSpecification.class);
        IComponentSpecification result = (IComponentSpecification) c.getMock();

        result.getParameterNames();

        c.setReturnValue(Collections.singletonList(parameterName));

        result.getParameter(parameterName);
        c.setReturnValue(ps);

        return result;
    }

    /**
     * Test 3.0 custom parameter behavior, which is to do nothing.
     */

    public void testCustomParameter()
    {
        IComponentSpecification spec = buildComponentSpecification("fred", buildParameterSpec(
                "fred",
                "boolean",
                Direction.CUSTOM));

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getSpecification();
        opc.setReturnValue(spec);

        replayControls();

        ParameterPropertyWorker w = new ParameterPropertyWorker();

        w.performEnhancement(op);

        verifyControls();
    }

    /**
     * Test for a Tapestry 3.0 in or form parameter.
     */

    public void testNormalParameter()
    {
        // fred is the parameter name, barney is the property name

        IComponentSpecification spec = buildComponentSpecification("fred", buildParameterSpec(
                "barney",
                "boolean",
                Direction.IN));

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getSpecification();
        opc.setReturnValue(spec);

        op.convertTypeName("boolean");
        opc.setReturnValue(boolean.class);

        op.validateProperty("barney", boolean.class);

        op.claimProperty("barney");

        op.addField("_$barney", boolean.class);

        op.getAccessorMethodName("barney");
        opc.setReturnValue("isBarney");

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(boolean.class, "isBarney", null, null),
                "return _$barney;");

        op.addMethod(Modifier.PUBLIC, new MethodSignature(void.class, "setBarney", new Class[]
        { boolean.class }, null), "_$barney = $1;");

        replayControls();

        ParameterPropertyWorker w = new ParameterPropertyWorker();

        w.performEnhancement(op);

        verifyControls();
    }

    public void testAutoParameter()
    {
        // fred is the parameter name, barney is the property name

        IComponentSpecification spec = buildComponentSpecification("fred", buildParameterSpec(
                "barney",
                "long",
                Direction.AUTO));

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getSpecification();
        opc.setReturnValue(spec);

        op.convertTypeName("long");
        opc.setReturnValue(long.class);

        op.validateProperty("barney", long.class);

        op.claimProperty("barney");

        op.getAccessorMethodName("barney");
        opc.setReturnValue("getBarney");

        op.getClassReference(long.class);
        opc.setReturnValue("_$class_long");

        BodyBuilder b = new BodyBuilder();
        b.begin();
        b.addln("org.apache.tapestry.IBinding binding = getBinding(\"fred\");");
        b.addln("return ($r)binding.getObject(\"fred\", _$class_long);");
        b.end();

        op.addMethod(Modifier.PUBLIC, new MethodSignature(long.class, "getBarney", null, null), b
                .toString());

        b = new BodyBuilder();
        b.begin();
        b.addln("org.apache.tapestry.IBinding binding = getBinding(\"fred\");");
        b.addln("binding.setObject(($w) $1);");
        b.end();

        op.addMethod(Modifier.PUBLIC, new MethodSignature(void.class, "setBarney", new Class[]
        { long.class }, null), b.toString());

        replayControls();

        ParameterPropertyWorker w = new ParameterPropertyWorker();

        w.performEnhancement(op);

        verifyControls();
    }

    public void testFailure() throws Exception
    {
        Location l = fabricateLocation(207);

        IComponentSpecification spec = buildComponentSpecification("fred", buildParameterSpec(
                "wilma",
                "String",
                Direction.IN,
                l));

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getSpecification();
        opc.setReturnValue(spec);

        op.convertTypeName("String");
        Throwable ex = new ApplicationRuntimeException("Simulated error.");
        opc.setThrowable(ex);

        op.getBaseClass();
        opc.setReturnValue(BaseComponent.class);

        Log log = (Log) newMock(Log.class);
        ErrorHandler errorHandler = (ErrorHandler) newMock(ErrorHandler.class);

        errorHandler
                .error(
                        log,
                        "Error adding property 'wilma' to class org.apache.tapestry.BaseComponent: Simulated error.",
                        l,
                        ex);

        replayControls();

        ParameterPropertyWorker w = new ParameterPropertyWorker();
        w.setLog(log);
        w.setErrorHandler(errorHandler);

        w.performEnhancement(op);

        verifyControls();
    }

}