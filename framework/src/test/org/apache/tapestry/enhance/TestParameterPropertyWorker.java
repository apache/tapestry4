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
import java.util.Collections;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IComponent;
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
            Location location)
    {
        ParameterSpecification ps = new ParameterSpecification();

        ps.setPropertyName(propertyName);
        ps.setType(type);
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

    public void testFailure() throws Exception
    {
        Location l = fabricateLocation(207);

        IComponentSpecification spec = buildComponentSpecification("fred", buildParameterSpec(
                "wilma",
                "String",
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

        ErrorLog log = (ErrorLog) newMock(ErrorLog.class);

        log
                .error(
                        "Error adding property 'wilma' to class org.apache.tapestry.BaseComponent: Simulated error.",
                        l,
                        ex);

        replayControls();

        ParameterPropertyWorker w = new ParameterPropertyWorker();
        w.setErrorLog(log);

        w.performEnhancement(op);

        verifyControls();
    }

    /**
     * Tests for ordinary, object type of binding (as opposed to primitive types, which have a
     * slightly different control flow.
     */

    public void testStandard()
    {
        IComponentSpecification spec = buildComponentSpecification("fred", buildParameterSpec(
                "fred",
                null,
                null));

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getSpecification();
        opc.setReturnValue(spec);

        op.getPropertyType("fred");
        opc.setReturnValue(String.class);

        op.claimProperty("fred");

        op.addField("_$fred", String.class);
        op.addField("_$fred$Default", String.class);
        op.addField("_$fred$Cached", boolean.class);

        op.getClassReference(String.class);
        opc.setReturnValue("_class$String");

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("if (_$fred$Cached) return _$fred;");
        builder.addln("org.apache.tapestry.IBinding binding = getBinding(\"fred\");");
        builder.addln("if (binding == null) return _$fred$Default;");
        builder.add("java.lang.String result = ");
        builder.addln("(java.lang.String) binding.getObject(_class$String);");
        builder.addln("if (isRendering() || binding.isInvariant())");
        builder.begin();
        builder.addln("_$fred = result;");
        builder.addln("_$fred$Cached = true;");
        builder.end();
        builder.addln("return result;");
        builder.end();

        op.getAccessorMethodName("fred");
        opc.setReturnValue("getFred");

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(String.class, "getFred", null, null),
                builder.toString());

        builder.clear();

        builder.begin();
        builder.addln("if (! isInActiveState())");
        builder.begin();
        builder.addln("_$fred$Default = $1;");
        builder.addln("return;");
        builder.end();

        builder.addln("org.apache.tapestry.IBinding binding = getBinding(\"fred\");");

        builder.addln("if (binding == null)");
        builder
                .addln("  throw new org.apache.hivemind.ApplicationRuntimeException(\"Parameter 'fred' is not bound and can not be updated.\");");

        builder.addln("binding.setObject(($w) $1);");

        builder.addln("if (isRendering())");
        builder.begin();
        builder.addln("_$fred = $1;");
        builder.addln("_$fred$Cached = true;");
        builder.end();
        builder.end();

        op.addMethod(Modifier.PUBLIC, new MethodSignature(void.class, "setFred", new Class[]
        { String.class }, null), builder.toString());

        BodyBuilder expectedCleanup = new BodyBuilder();

        expectedCleanup.addln("org.apache.tapestry.IBinding fredBinding = getBinding(\"fred\");");
        expectedCleanup.addln("if (_$fred$Cached && ! fredBinding.isInvariant())");
        expectedCleanup.begin();
        expectedCleanup.addln("_$fred$Cached = false;");
        expectedCleanup.addln("_$fred = _$fred$Default;");
        expectedCleanup.end();

        op.extendMethodImplementation(
                IComponent.class,
                EnhanceUtils.CLEANUP_AFTER_RENDER_SIGNATURE,
                expectedCleanup.toString());

        replayControls();

        ParameterPropertyWorker w = new ParameterPropertyWorker();

        w.performEnhancement(op);

        verifyControls();
    }

    /**
     * Test where the parameter name does not equal the property name. The parameter is "barney",
     * but the binding is "fred".
     */

    public void testDifferentPropertyName()
    {
        IComponentSpecification spec = buildComponentSpecification("barney", buildParameterSpec(
                "fred",
                null,
                null));

        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getSpecification();
        opc.setReturnValue(spec);

        op.getPropertyType("fred");
        opc.setReturnValue(String.class);

        op.claimProperty("fred");

        op.addField("_$fred", String.class);
        op.addField("_$fred$Default", String.class);
        op.addField("_$fred$Cached", boolean.class);

        op.getClassReference(String.class);
        opc.setReturnValue("_class$String");

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("if (_$fred$Cached) return _$fred;");
        builder.addln("org.apache.tapestry.IBinding binding = getBinding(\"barney\");");
        builder.addln("if (binding == null) return _$fred$Default;");
        builder.add("java.lang.String result = ");
        builder.addln("(java.lang.String) binding.getObject(_class$String);");
        builder.addln("if (isRendering() || binding.isInvariant())");
        builder.begin();
        builder.addln("_$fred = result;");
        builder.addln("_$fred$Cached = true;");
        builder.end();
        builder.addln("return result;");
        builder.end();

        op.getAccessorMethodName("fred");
        opc.setReturnValue("getFred");

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(String.class, "getFred", null, null),
                builder.toString());

        builder.clear();

        builder.begin();
        builder.addln("if (! isInActiveState())");
        builder.begin();
        builder.addln("_$fred$Default = $1;");
        builder.addln("return;");
        builder.end();

        builder.addln("org.apache.tapestry.IBinding binding = getBinding(\"barney\");");

        builder.addln("if (binding == null)");
        builder
                .addln("  throw new org.apache.hivemind.ApplicationRuntimeException(\"Parameter 'barney' is not bound and can not be updated.\");");

        builder.addln("binding.setObject(($w) $1);");

        builder.addln("if (isRendering())");
        builder.begin();
        builder.addln("_$fred = $1;");
        builder.addln("_$fred$Cached = true;");
        builder.end();
        builder.end();

        op.addMethod(Modifier.PUBLIC, new MethodSignature(void.class, "setFred", new Class[]
        { String.class }, null), builder.toString());

        BodyBuilder expectedCleanup = new BodyBuilder();

        expectedCleanup.addln("org.apache.tapestry.IBinding fredBinding = getBinding(\"barney\");");
        expectedCleanup.addln("if (_$fred$Cached && ! fredBinding.isInvariant())");
        expectedCleanup.begin();
        expectedCleanup.addln("_$fred$Cached = false;");
        expectedCleanup.addln("_$fred = _$fred$Default;");
        expectedCleanup.end();

        op.extendMethodImplementation(
                IComponent.class,
                EnhanceUtils.CLEANUP_AFTER_RENDER_SIGNATURE,
                expectedCleanup.toString());

        replayControls();

        ParameterPropertyWorker w = new ParameterPropertyWorker();

        w.performEnhancement(op);

        verifyControls();

    }

    public void testPrimitiveType()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        op.getClassReference(boolean.class);
        opc.setReturnValue("_class$boolean");

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("if (_$fred$Cached) return _$fred;");
        builder.addln("org.apache.tapestry.IBinding binding = getBinding(\"barney\");");
        builder.addln("if (binding == null) return _$fred$Default;");
        builder.add("boolean result = ");
        builder.addln("((Boolean) binding.getObject(_class$boolean)).booleanValue();");
        builder.addln("if (isRendering() || binding.isInvariant())");
        builder.begin();
        builder.addln("_$fred = result;");
        builder.addln("_$fred$Cached = true;");
        builder.end();
        builder.addln("return result;");
        builder.end();

        op.getAccessorMethodName("fred");
        opc.setReturnValue("isFred");

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(boolean.class, "isFred", null, null),
                builder.toString());

        replayControls();

        new ParameterPropertyWorker().buildAccessor(
                op,
                "barney",
                "fred",
                boolean.class,
                "_$fred",
                "_$fred$Default",
                "_$fred$Cached");

        verifyControls();
    }
}