// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.hivemind.Location;
import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.Tapestry;

/**
 * Messages for this package.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
final class EnhanceMessages
{
    private final static Messages MESSAGES = new MessageFormatter(EnhanceMessages.class);

    /** @since 4.1 */
    private EnhanceMessages(){}
    static String noImplForAbstractMethod(Method method, Class declareClass, Class baseClass,
            Class enhancedClass)
    {
        return MESSAGES.format("no-impl-for-abstract-method", new Object[]
        { method, declareClass.getName(), baseClass.getName(), enhancedClass.getName() });
    }

    static String unimplementedInterfaceMethod(Method method, Class baseClass, Class enhancedClass)
    {
        return MESSAGES.format(
                "unimplemented-interface-method",
                method,
                baseClass.getName(),
                enhancedClass.getName());
    }

    static String unabelToIntrospectClass(Class targetClass, Throwable cause)
    {
        return MESSAGES.format("unable-to-introspect-class", targetClass.getName(), cause);
    }

    static String propertyTypeMismatch(Class componentClass, String propertyName,
            Class actualPropertyType, Class expectedPropertyType)
    {
        return MESSAGES.format("property-type-mismatch", new Object[]
        { componentClass.getName(), propertyName,
                ClassFabUtils.getJavaClassName(actualPropertyType),
                ClassFabUtils.getJavaClassName(expectedPropertyType) });
    }

    static String errorAddingProperty(String propertyName, Class componentClass, Throwable cause)
    {
        return MESSAGES.format(
                "error-adding-property",
                propertyName,
                componentClass.getName(),
                cause);
    }

    static String claimedProperty(String propertyName)
    {
        return MESSAGES.format("claimed-property", propertyName);
    }

    static String instantiationFailure(Constructor c, Object[] parameters, String classFab,
            Throwable cause)
    {
        int count = Tapestry.size(parameters);
        StringBuffer buffer = new StringBuffer("[");
        for (int i = 0; i < count; i++)
        {
            if (i > 0)
                buffer.append(", ");
            buffer.append(parameters[i]);
        }

        buffer.append("]");

        return MESSAGES.format("instantiation-failure", new Object[]
        { c.getDeclaringClass().getName(), c, buffer.toString(), classFab, cause });
    }

    static String locatedValueIsNull(String objectReference)
    {
        return MESSAGES.format("located-value-is-null", objectReference);
    }

    static String incompatibleInjectType(String locator, Object value, Class propertyType)
    {
        return MESSAGES.format("incompatible-inject-type", locator, value, ClassFabUtils
                .getJavaClassName(propertyType));
    }

    static String initialValueForProperty(String propertyName)
    {
        return MESSAGES.format("initial-value-for-property", propertyName);
    }

    static String unknownInjectType(String propertyName, String injectType)
    {
        return MESSAGES.format("unknown-inject-type", propertyName, injectType);
    }

    static String wrongTypeForProperty(String propertyName, Class propertyType, Class requiredType)
    {
        return MESSAGES.format("wrong-type-for-property", propertyName, ClassFabUtils
                .getJavaClassName(propertyType), ClassFabUtils.getJavaClassName(requiredType));
    }

    static String wrongTypeForPageInjection(String propertyName, Class propertyType)
    {
        return MESSAGES.format("wrong-type-for-page-injection", propertyName, ClassFabUtils
                .getJavaClassName(propertyType));
    }

    static String incompatiblePropertyType(String propertyName, Class propertyType,
            Class expectedType)
    {
        return MESSAGES.format("incompatible-property-type", propertyName, ClassFabUtils
                .getJavaClassName(propertyType), ClassFabUtils.getJavaClassName(expectedType));
    }

    static String classEnhancementFailure(Class baseClass, Throwable cause)
    {
        return MESSAGES.format("class-enhancement-failure", ClassFabUtils
                .getJavaClassName(baseClass), cause);
    }

    static String methodConflict(MethodSignature sig, Location existing)
    {
        return MESSAGES.format("method-conflict", sig, existing);
    }

    static String readonlyProperty(String propertyName, Method writeMethod)
    {
        return MESSAGES.format("readonly-property", propertyName, writeMethod);
    }

    static String mustBeBoolean(String propertyName)
    {
        return MESSAGES.format("must-be-boolean", propertyName);
    }
}