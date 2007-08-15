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

import org.apache.hivemind.Location;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.spec.InjectSpecification;
import org.apache.tapestry.spec.InjectSpecificationImpl;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

import java.lang.reflect.Modifier;

/**
 * Tests for {@link org.apache.tapestry.enhance.InjectMetaWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestInjectMetaWorker extends BaseComponentTestCase
{
    private InjectSpecification newSpec(String propertyName, String object, Location location)
    {
        InjectSpecificationImpl result = new InjectSpecificationImpl();

        result.setProperty(propertyName);
        result.setObject(object);
        result.setLocation(location);

        return result;
    }

    private ComponentPropertySource newSource()
    {
        return newMock(ComponentPropertySource.class);
    }
    
    public void test_Primitive()
    {
        Location l = newLocation();
        InjectSpecification spec = newSpec("fooBar", "foo.bar", l);

        ComponentPropertySource source = newSource();
        
        EnhancementOperation op = newMock(EnhancementOperation.class);

        expect(op.getPropertyType("fooBar")).andReturn(int.class);

        op.claimReadonlyProperty("fooBar");

        MethodSignature sig = new MethodSignature(int.class, "getFooBar", null, null);

        expect(op.addInjectedField(InjectMetaWorker.SOURCE_NAME, ComponentPropertySource.class, source))
        .andReturn("_source");

        expect(op.getAccessorMethodName("fooBar")).andReturn("getFooBar");

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("java.lang.String meta = _source.getComponentProperty(this, \"foo.bar\");");
        builder.addln("return java.lang.Integer.parseInt(meta);");
        builder.end();

        op.addMethod(Modifier.PUBLIC, sig, builder.toString(), l);

        replay();

        InjectMetaWorker worker = new InjectMetaWorker();

        worker.setSource(source);

        worker.performEnhancement(op, spec);

        verify();
    }
    
    public void test_Boolean()
    {
        Location l = newLocation();
        InjectSpecification spec = newSpec("fooBar", "foo.bar", l);
        
        ComponentPropertySource source = newSource();
        
        EnhancementOperation op = newMock(EnhancementOperation.class);

        expect(op.getPropertyType("fooBar")).andReturn(boolean.class);

        op.claimReadonlyProperty("fooBar");

        MethodSignature sig = new MethodSignature(boolean.class, "getFooBar", null, null);

        expect(op.addInjectedField(InjectMetaWorker.SOURCE_NAME, ComponentPropertySource.class, source)).andReturn("_source");
        
        expect(op.getAccessorMethodName("fooBar")).andReturn("getFooBar");

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("java.lang.String meta = _source.getComponentProperty(this, \"foo.bar\");");
        builder.addln("return java.lang.Boolean.valueOf(meta).booleanValue();");
        builder.end();

        op.addMethod(Modifier.PUBLIC, sig, builder.toString(), l);

        replay();

        InjectMetaWorker worker = new InjectMetaWorker();

        worker.setSource(source);

        worker.performEnhancement(op, spec);

        verify();
    }
    
    public void test_Character()
    {
        Location l = newLocation();
        InjectSpecification spec = newSpec("fooBar", "foo.bar", l);

        ComponentPropertySource source = newSource();

        EnhancementOperation op = newMock(EnhancementOperation.class);

        expect(op.getPropertyType("fooBar")).andReturn(char.class);

        op.claimReadonlyProperty("fooBar");

        MethodSignature sig = new MethodSignature(char.class, "getFooBar", null, null);

        expect(op.addInjectedField(InjectMetaWorker.SOURCE_NAME, ComponentPropertySource.class, source))
        .andReturn("_source");

        expect(op.getAccessorMethodName("fooBar")).andReturn("getFooBar");

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("java.lang.String meta = _source.getComponentProperty(this, \"foo.bar\");");
        builder.addln("return meta.charAt(0);");
        builder.end();

        op.addMethod(Modifier.PUBLIC, sig, builder.toString(), l);

        replay();

        InjectMetaWorker worker = new InjectMetaWorker();

        worker.setSource(source);

        worker.performEnhancement(op, spec);

        verify();
    }
    
    public void test_Object()
    {
        Location l = newLocation();
        InjectSpecification spec = newSpec("fooBar", "foo.bar", l);

        ComponentPropertySource source = newSource();
        ValueConverter converter = newMock(ValueConverter.class);

        EnhancementOperation op = newMock(EnhancementOperation.class);

        expect(op.getPropertyType("fooBar")).andReturn(Object.class);

        op.claimReadonlyProperty("fooBar");

        MethodSignature sig = new MethodSignature(Object.class, "getFooBar", null, null);

        expect(op.addInjectedField(InjectMetaWorker.SOURCE_NAME, ComponentPropertySource.class, source))
        .andReturn("_source");

        expect(op.getAccessorMethodName("fooBar")).andReturn("getFooBar");

        expect(op.addInjectedField("_$valueConverter", ValueConverter.class, converter))
        .andReturn("vc");

        expect(op.getClassReference(Object.class)).andReturn("_$Object");

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("java.lang.String meta = _source.getComponentProperty(this, \"foo.bar\");");
        builder.addln("return (java.lang.Object) vc.coerceValue(meta, _$Object);");
        builder.end();

        op.addMethod(Modifier.PUBLIC, sig, builder.toString(), l);

        replay();

        InjectMetaWorker worker = new InjectMetaWorker();

        worker.setSource(source);
        worker.setValueConverter(converter);

        worker.performEnhancement(op, spec);

        verify();
    }
    
    public void test_Unimplemented_Property()
    {
        Location l = newLocation();
        InjectSpecification spec = newSpec("fooBar", "foo.bar", l);
        
        ComponentPropertySource source = newSource();
        ValueConverter converter = newMock(ValueConverter.class);
        
        EnhancementOperation op = newMock(EnhancementOperation.class);

        expect(op.getPropertyType("fooBar")).andReturn(null);
        
        op.claimReadonlyProperty("fooBar");

        MethodSignature sig = new MethodSignature(Object.class, "getFooBar", null, null);
        
        expect(op.addInjectedField(InjectMetaWorker.SOURCE_NAME, ComponentPropertySource.class, source)).andReturn("_source");
        
        expect(op.getAccessorMethodName("fooBar")).andReturn("getFooBar");
        
        expect(op.addInjectedField("_$valueConverter", ValueConverter.class, converter)).andReturn("vc");
        
        expect(op.getClassReference(Object.class)).andReturn("_$Object");
        
        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("java.lang.String meta = _source.getComponentProperty(this, \"foo.bar\");");
        builder.addln("return (java.lang.Object) vc.coerceValue(meta, _$Object);");
        builder.end();
        
        op.addMethod(Modifier.PUBLIC, sig, builder.toString(), l);

        replay();

        InjectMetaWorker worker = new InjectMetaWorker();
        worker.setSource(source);
        worker.setValueConverter(converter);

        worker.performEnhancement(op, spec);

        verify();
    }
    
}
