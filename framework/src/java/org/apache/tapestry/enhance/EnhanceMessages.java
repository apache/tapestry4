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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.hivemind.impl.MessageFormatter;
import org.apache.hivemind.service.ClassFabUtils;

/**
 * Messages for this package.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
class EnhanceMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(EnhanceMessages.class,
            "EnhanceStrings");

    public static String noImplForAbstractMethod(Method method, Class declareClass,
            String className, Class enhancedClass)
    {
        return _formatter.format("no-impl-for-abstract-method", new Object[]
        { method, declareClass.getName(), className, enhancedClass.getName() });
    }

    public static String unabelToIntrospectClass(Class targetClass, Throwable cause)
    {
        return _formatter.format("unable-to-introspect-class", targetClass.getName(), cause);
    }

    public static String propertyTypeMismatch(Class componentClass, String propertyName,
            Class actualPropertyType, Class expectedPropertyType)
    {
        return _formatter.format("property-type-mismatch", new Object[]
        { componentClass.getName(), propertyName,
                ClassFabUtils.getJavaClassName(actualPropertyType),
                ClassFabUtils.getJavaClassName(expectedPropertyType) });
    }

    public static String autoMustBeRequired(String propertyName)
    {
        return _formatter.format("auto-must-be-required", propertyName);
    }

    public static String errorAddingProperty(String propertyName, Class componentClass,
            Throwable cause)
    {
        return _formatter.format(
                "error-adding-property",
                propertyName,
                componentClass.getName(),
                cause);
    }

    public static String claimedProperty(String propertyName)
    {
        return _formatter.format("claimed-property", propertyName);
    }

    public static String instantiationFailure(Constructor c, Throwable cause)
    {
        return _formatter.format("instantiation-failure", c.getDeclaringClass().getName(), cause);
    }

    public static String missingConstructor(Class baseClass)
    {
        return _formatter.format("missing-constructor", baseClass.getName());
    }

    public static String locatedValueIsNull(String objectReference)
    {
        return _formatter.format("located-value-is-null", objectReference);
    }

    public static String incompatibleInjectType(String locator, Object value, Class propertyType)
    {
        return _formatter.format("incompatible-inject-type", locator, value, ClassFabUtils
                .getJavaClassName(propertyType));
    }
}