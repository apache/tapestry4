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
import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.spec.InjectSpecification;

/**
 * Injects meta data obtained via {@link org.apache.tapestry.services.ComponentPropertySource}
 * (meaning that meta-data is searched for in the component's specification, then it's namespace
 * (library or application specification), then the global application properties.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InjectMetaWorker implements InjectEnhancementWorker
{
    static final String SOURCE_NAME = "_$componentPropertySource";

    private ComponentPropertySource _source;

    private ValueConverter _valueConverter;

    private Map _primitiveParser = new HashMap();

    {
        _primitiveParser.put(boolean.class, "java.lang.Boolean.valueOf");
        _primitiveParser.put(short.class, "java.lang.Short.parseShort");
        _primitiveParser.put(int.class, "java.lang.Integer.parseInt");
        _primitiveParser.put(long.class, "java.lang.Long.parseLong");
        _primitiveParser.put(double.class, "java.lang.Double.parseDouble");
        _primitiveParser.put(float.class, "java.lang.Float.parseFloat");
    }

    public void performEnhancement(EnhancementOperation op, InjectSpecification spec)
    {
        String propertyName = spec.getProperty();
        Class propertyType = op.getPropertyType(propertyName);

        op.claimProperty(propertyName);

        String sourceName = op.addFinalField(SOURCE_NAME, _source);

        MethodSignature sig = new MethodSignature(propertyType, op
                .getAccessorMethodName(propertyName), null, null);

        String parser = (String) _primitiveParser.get(propertyType);

        if (parser != null)
        {
            addPrimitive(op, spec, propertyName, sig, sourceName, parser);
            return;
        }

        if (propertyType == char.class)
        {
            addCharacterPrimitive(op, spec, propertyName, sig, sourceName);
            return;
        }

        addObject(op, spec, propertyName, propertyType, sig, sourceName);

    }

    private void addPrimitive(EnhancementOperation op, InjectSpecification spec,
            String propertyName, MethodSignature sig, String sourceName, String parser)
    {
        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln(
                "java.lang.String meta = {0}.getComponentProperty(this, \"{1}\");",
                sourceName,
                spec.getObject());
        builder.addln("return {0}(meta);", parser);
        builder.end();

        op.addMethod(Modifier.PUBLIC, sig, builder.toString());
    }

    private void addCharacterPrimitive(EnhancementOperation op, InjectSpecification spec,
            String propertyName, MethodSignature sig, String sourceName)
    {
        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln(
                "java.lang.String meta = {0}.getComponentProperty(this, \"{1}\");",
                sourceName,
                spec.getObject());
        builder.addln("return meta.charAt(0);");
        builder.end();

        op.addMethod(Modifier.PUBLIC, sig, builder.toString());
    }

    private void addObject(EnhancementOperation op, InjectSpecification spec, String propertyName,
            Class propertyType, MethodSignature sig, String sourceName)
    {
        String valueConverterName = op.addFinalField("_$valueConverter", _valueConverter);
        String classRef = op.getClassReference(propertyType);

        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln(
                "java.lang.String meta = {0}.getComponentProperty(this, \"{1}\");",
                sourceName,
                spec.getObject());
        builder.addln("return ({0}) {1}.coerceValue(meta, {2});", ClassFabUtils
                .getJavaClassName(propertyType), valueConverterName, classRef);
        builder.end();

        op.addMethod(Modifier.PUBLIC, sig, builder.toString());
    }

    public void setSource(ComponentPropertySource source)
    {
        _source = source;
    }

    public void setValueConverter(ValueConverter valueConverter)
    {
        _valueConverter = valueConverter;
    }
}
