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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.apache.tapestry.spec.ParameterSpecification;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

import java.lang.reflect.Modifier;
import java.util.Collections;

/**
 * Tests for {@link org.apache.tapestry.enhance.ParameterPropertyWorker}.
 *
 * @author Howard M. Lewis Ship
 */
@Test
public class TestParameterPropertyWorker extends BaseComponentTestCase
{

    private ParameterSpecification buildParameterSpec(String parameterName, String type,
                                                      Location location)
    {
        return buildParameterSpec(parameterName, parameterName, type, location);
    }

    private ParameterSpecification buildParameterSpec(String parameterName, String propertyName,
                                                      String type, Location location)
    {
        ParameterSpecification ps = new ParameterSpecification();

        ps.setParameterName(parameterName);
        ps.setPropertyName(propertyName);
        ps.setType(type);
        ps.setLocation(location);

        return ps;
    }

    private IComponentSpecification buildComponentSpecification(String parameterName,
                                                                IParameterSpecification ps)
    {
        IComponentSpecification result = newSpec();

        expect(result.getParameterNames()).andReturn(Collections.singletonList(parameterName));

        expect(result.getParameter(parameterName)).andReturn(ps);

        return result;
    }

    public void test_Failure() throws Exception
    {
        Location l = newLocation();

        IComponentSpecification spec = buildComponentSpecification("wilma", buildParameterSpec(
          "wilma",
          "String",
          l));

        EnhancementOperation op = newMock(EnhancementOperation.class);

        Throwable ex = new ApplicationRuntimeException("Simulated error.");
        expect(op.convertTypeName("String")).andThrow(ex);

        expect(op.getBaseClass()).andReturn(BaseComponent.class);

        ErrorLog log = newMock(ErrorLog.class);

        log
          .error(
            "Error adding property wilma to class org.apache.tapestry.BaseComponent: Simulated error.",
            l,
            ex);

        replay();

        ParameterPropertyWorker w = new ParameterPropertyWorker();
        w.setErrorLog(log);

        w.performEnhancement(op, spec);

        verify();
    }

    public void test_Skip_Parameter_Alias()
    {
        IComponentSpecification spec = buildComponentSpecification("barney", buildParameterSpec(
          "fred",
          null,
          null));

        EnhancementOperation op = newMock(EnhancementOperation.class);

        replay();

        ParameterPropertyWorker w = new ParameterPropertyWorker();

        w.performEnhancement(op, spec);

        verify();
    }

    /**
     * Tests for ordinary, object type of binding (as opposed to primitive types, which have a
     * slightly different control flow.
     */

    public void test_Standard()
    {
        IComponentSpecification spec = buildComponentSpecification("fred", buildParameterSpec(
          "fred",
          null,
          null));

        EnhancementOperation op = newMock(EnhancementOperation.class);

        expect(op.getPropertyType("fred")).andReturn(String.class);

        op.claimProperty("fred");

        String bindingFieldName = "_$fred$Binding";

        op.addField("_$fred", String.class);
        op.addField("_$fred$Default", String.class);
        op.addField("_$fred$Cached", boolean.class);
        op.addField("_$fred$Binding", IBinding.class);

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("if ({0} == null)", bindingFieldName);
        builder.begin();
        builder.addln("{0} = getBinding(\"{1}\");", bindingFieldName, "fred");
        builder.end();
        builder.addln("return {0};", bindingFieldName);
        builder.end();

        String methodName = EnhanceUtils.createAccessorMethodName(bindingFieldName);
        op.addMethod(Modifier.PUBLIC,
                     new MethodSignature(IBinding.class, methodName, new Class[0], null),
                     builder.toString(), null);

        expect(op.getClassReference(String.class)).andReturn("_class$String");

        builder.clear();
        builder.begin();
        builder.addln("if (_$fred$Cached) return _$fred;");
        builder.addln("if (get_$fred$Binding() == null) return _$fred$Default;");
        builder.add("java.lang.String result = ");
        builder.addln("(java.lang.String) get_$fred$Binding().getObject(_class$String);");
        builder.addln("if (isRendering() || get_$fred$Binding().isInvariant())");
        builder.begin();
        builder.addln("_$fred = result;");
        builder.addln("_$fred$Cached = true;");
        builder.end();
        builder.addln("return result;");
        builder.end();

        expect(op.getAccessorMethodName("fred")).andReturn("getFred");

        op.addMethod(Modifier.PUBLIC, new MethodSignature(String.class, "getFred", null, null),
                     builder.toString(),
                     null);

        builder.clear();

        builder.begin();
        builder.addln("if (! isInActiveState())");
        builder.begin();
        builder.addln("_$fred$Default = $1;");
        builder.addln("return;");
        builder.end();

        builder.addln("if (get_$fred$Binding() == null)");
        builder
          .addln("  throw new org.apache.hivemind.ApplicationRuntimeException(\"Parameter 'fred' is not bound and can not be updated.\");");

        builder.addln("get_$fred$Binding().setObject(($w) $1);");

        builder.addln("if (isRendering())");
        builder.begin();
        builder.addln("_$fred = $1;");
        builder.addln("_$fred$Cached = true;");
        builder.end();
        builder.end();

        op.addMethod(Modifier.PUBLIC, new MethodSignature(void.class, "setFred", new Class[]
          { String.class }, null), builder.toString(), null);

        BodyBuilder expectedCleanup = new BodyBuilder();

        expectedCleanup.addln("if (_$fred$Cached && ! get_$fred$Binding().isInvariant())");
        expectedCleanup.begin();
        expectedCleanup.addln("_$fred$Cached = false;");
        expectedCleanup.addln("_$fred = _$fred$Default;");
        expectedCleanup.end();

        op.extendMethodImplementation(
          IComponent.class,
          EnhanceUtils.CLEANUP_AFTER_RENDER_SIGNATURE,
          expectedCleanup.toString());

        replay();

        ParameterPropertyWorker w = new ParameterPropertyWorker();

        w.performEnhancement(op, spec);

        verify();
    }

    /**
     * Test where the parameter name does not equal the property name. The parameter is "barney",
     * but the binding is "fred".
     */

    public void test_Different_Property_Name()
    {
        Location l = newLocation();
        IComponentSpecification spec = buildComponentSpecification("myparam", buildParameterSpec("myparam", "fred", null, l));

        EnhancementOperation op = newMock(EnhancementOperation.class);

        expect(op.getPropertyType("fred")).andReturn(String.class);

        op.claimProperty("fred");

        String bindingFieldName = "_$fred$Binding";

        op.addField("_$fred", String.class);
        op.addField("_$fred$Default", String.class);
        op.addField("_$fred$Cached", boolean.class);
        op.addField("_$fred$Binding", IBinding.class);

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("if ({0} == null)", bindingFieldName);
        builder.begin();
        builder.addln("{0} = getBinding(\"{1}\");", bindingFieldName, "myparam");
        builder.end();
        builder.addln("return {0};", bindingFieldName);
        builder.end();

        String methodName = EnhanceUtils.createAccessorMethodName(bindingFieldName);
        op.addMethod(Modifier.PUBLIC,
                     new MethodSignature(IBinding.class, methodName, new Class[0], null),
                     builder.toString(), l);
        
        expect(op.getClassReference(String.class)).andReturn("_class$String");

        builder.clear();
        builder.begin();
        builder.addln("if (_$fred$Cached) return _$fred;");
        builder.addln("if (get_$fred$Binding() == null) return _$fred$Default;");
        builder.add("java.lang.String result = ");
        builder.addln("(java.lang.String) get_$fred$Binding().getObject(_class$String);");
        builder.addln("if (isRendering() || get_$fred$Binding().isInvariant())");
        builder.begin();
        builder.addln("_$fred = result;");
        builder.addln("_$fred$Cached = true;");
        builder.end();
        builder.addln("return result;");
        builder.end();

        expect(op.getAccessorMethodName("fred")).andReturn("getFred");

        op.addMethod(
          Modifier.PUBLIC,
          new MethodSignature(String.class, "getFred", null, null),
          builder.toString(),
          l);

        builder.clear();

        builder.begin();
        builder.addln("if (! isInActiveState())");
        builder.begin();
        builder.addln("_$fred$Default = $1;");
        builder.addln("return;");
        builder.end();

        builder.addln("if (get_$fred$Binding() == null)");
        builder
          .addln("  throw new org.apache.hivemind.ApplicationRuntimeException(\"Parameter 'myparam' is not bound and can not be updated.\");");

        builder.addln("get_$fred$Binding().setObject(($w) $1);");

        builder.addln("if (isRendering())");
        builder.begin();
        builder.addln("_$fred = $1;");
        builder.addln("_$fred$Cached = true;");
        builder.end();
        builder.end();

        op.addMethod(Modifier.PUBLIC, new MethodSignature(void.class, "setFred", new Class[]
          { String.class }, null), builder.toString(), l);

        BodyBuilder expectedCleanup = new BodyBuilder();

        expectedCleanup.addln("if (_$fred$Cached && ! get_$fred$Binding().isInvariant())");
        expectedCleanup.begin();
        expectedCleanup.addln("_$fred$Cached = false;");
        expectedCleanup.addln("_$fred = _$fred$Default;");
        expectedCleanup.end();

        op.extendMethodImplementation(
          IComponent.class,
          EnhanceUtils.CLEANUP_AFTER_RENDER_SIGNATURE,
          expectedCleanup.toString());

        replay();

        ParameterPropertyWorker w = new ParameterPropertyWorker();

        w.performEnhancement(op, spec);

        verify();
    }

    public void test_Primitive_Type()
    {
        Location l = newLocation();

        EnhancementOperation op = newMock(EnhancementOperation.class);

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("if (_$fred$Cached) return _$fred;");
        builder.addln("if (get_$fred$Binding() == null) return _$fred$Default;");
        builder.add("boolean result = ");
        builder.addln(EnhanceUtils.class.getName() + ".toBoolean(get_$fred$Binding());");
        builder.addln("if (isRendering() || get_$fred$Binding().isInvariant())");
        builder.begin();
        builder.addln("_$fred = result;");
        builder.addln("_$fred$Cached = true;");
        builder.end();
        builder.addln("return result;");
        builder.end();

        expect(op.getAccessorMethodName("fred")).andReturn("isFred");

        op.addMethod(
          Modifier.PUBLIC,
          new MethodSignature(boolean.class, "isFred", null, null),
          builder.toString(),
          l);

        replay();

        new ParameterPropertyWorker().buildAccessor(
          op,
          "fred",
          boolean.class,
          "_$fred",
          "_$fred$Default",
          "_$fred$Cached",
          "get_$fred$Binding()",
          true,
          l);

        verify();
    }

    public void test_Parameter_Cache_Disabled()
    {
        Location l = newLocation();

        EnhancementOperation op = newMock(EnhancementOperation.class);

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("if (_$fred$Cached) return _$fred;");
        builder.addln("if (get_$fred$Binding() == null) return _$fred$Default;");
        builder.add("boolean result = ");
        builder.addln(EnhanceUtils.class.getName() + ".toBoolean(get_$fred$Binding());");
        builder.addln("if (get_$fred$Binding().isInvariant())");
        builder.begin();
        builder.addln("_$fred = result;");
        builder.addln("_$fred$Cached = true;");
        builder.end();
        builder.addln("return result;");
        builder.end();

        expect(op.getAccessorMethodName("fred")).andReturn("isFred");

        op.addMethod(
          Modifier.PUBLIC,
          new MethodSignature(boolean.class, "isFred", null, null),
          builder.toString(),
          l);

        replay();

        new ParameterPropertyWorker().buildAccessor(
          op,
          "fred",
          boolean.class,
          "_$fred",
          "_$fred$Default",
          "_$fred$Cached",
          "get_$fred$Binding()",
          false,
          l);

        verify();
    }
}