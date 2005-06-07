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

import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.spec.InjectSpecification;
import org.apache.tapestry.spec.InjectSpecificationImpl;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.enhance.InjectMetaWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestInjectMetaWorker extends HiveMindTestCase
{
    private InjectSpecification newSpec(String propertyName, String object)
    {
        InjectSpecificationImpl result = new InjectSpecificationImpl();

        result.setProperty(propertyName);
        result.setObject(object);

        return result;
    }

    private ComponentPropertySource newSource()
    {
        return (ComponentPropertySource) newMock(ComponentPropertySource.class);
    }

    public void testPrimitive()
    {
        InjectSpecification spec = newSpec("fooBar", "foo.bar");

        ComponentPropertySource source = newSource();

        MockControl control = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) control.getMock();

        op.getPropertyType("fooBar");
        control.setReturnValue(int.class);

        op.claimProperty("fooBar");

        MethodSignature sig = new MethodSignature(int.class, "getFooBar", null, null);

        op.addInjectedField(InjectMetaWorker.SOURCE_NAME, ComponentPropertySource.class, source);
        control.setReturnValue("_source");

        op.getAccessorMethodName("fooBar");
        control.setReturnValue("getFooBar");

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("java.lang.String meta = _source.getComponentProperty(this, \"foo.bar\");");
        builder.addln("return java.lang.Integer.parseInt(meta);");
        builder.end();

        op.addMethod(Modifier.PUBLIC, sig, builder.toString());

        replayControls();

        InjectMetaWorker worker = new InjectMetaWorker();

        worker.setSource(source);

        worker.performEnhancement(op, spec);

        verifyControls();
    }

    public void testCharacter()
    {
        InjectSpecification spec = newSpec("fooBar", "foo.bar");

        ComponentPropertySource source = newSource();

        MockControl control = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) control.getMock();

        op.getPropertyType("fooBar");
        control.setReturnValue(char.class);

        op.claimProperty("fooBar");

        MethodSignature sig = new MethodSignature(char.class, "getFooBar", null, null);

        op.addInjectedField(InjectMetaWorker.SOURCE_NAME, ComponentPropertySource.class, source);
        control.setReturnValue("_source");

        op.getAccessorMethodName("fooBar");
        control.setReturnValue("getFooBar");

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("java.lang.String meta = _source.getComponentProperty(this, \"foo.bar\");");
        builder.addln("return meta.charAt(0);");
        builder.end();

        op.addMethod(Modifier.PUBLIC, sig, builder.toString());

        replayControls();

        InjectMetaWorker worker = new InjectMetaWorker();

        worker.setSource(source);

        worker.performEnhancement(op, spec);

        verifyControls();
    }

    public void testObject()
    {
        InjectSpecification spec = newSpec("fooBar", "foo.bar");

        ComponentPropertySource source = newSource();
        ValueConverter converter = (ValueConverter) newMock(ValueConverter.class);

        MockControl control = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) control.getMock();

        op.getPropertyType("fooBar");
        control.setReturnValue(Object.class);

        op.claimProperty("fooBar");

        MethodSignature sig = new MethodSignature(Object.class, "getFooBar", null, null);

        op.addInjectedField(InjectMetaWorker.SOURCE_NAME, ComponentPropertySource.class, source);
        control.setReturnValue("_source");

        op.getAccessorMethodName("fooBar");
        control.setReturnValue("getFooBar");

        op.addInjectedField("_$valueConverter", ValueConverter.class, converter);
        control.setReturnValue("vc");

        op.getClassReference(Object.class);
        control.setReturnValue("_$Object");

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("java.lang.String meta = _source.getComponentProperty(this, \"foo.bar\");");
        builder.addln("return (java.lang.Object) vc.coerceValue(meta, _$Object);");
        builder.end();

        op.addMethod(Modifier.PUBLIC, sig, builder.toString());

        replayControls();

        InjectMetaWorker worker = new InjectMetaWorker();

        worker.setSource(source);
        worker.setValueConverter(converter);

        worker.performEnhancement(op, spec);

        verifyControls();
    }

}
