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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javassist.CtClass;
import javassist.CtMethod;

import org.apache.hivemind.Location;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Messages for this package.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
final class EnhanceMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(EnhanceMessages.class);

    /* defeat instantiation */
    private EnhanceMessages() { }
    
    static String noImplForAbstractMethod(Method method, Class declareClass, Class baseClass,
            Class enhancedClass)
    {
        return _formatter.format("no-impl-for-abstract-method", new Object[]
        { method, declareClass.getName(), baseClass.getName(), enhancedClass.getName() });
    }

    static String unimplementedInterfaceMethod(Method method, Class baseClass, Class enhancedClass)
    {
        return _formatter.format(
                "unimplemented-interface-method",
                method,
                baseClass.getName(),
                enhancedClass.getName());
    }

    static String unabelToIntrospectClass(Class targetClass, Throwable cause)
    {
        return _formatter.format("unable-to-introspect-class", targetClass.getName(), cause);
    }

    static String propertyTypeMismatch(Class componentClass, String propertyName,
            Class actualPropertyType, Class expectedPropertyType)
    {
        return _formatter.format("property-type-mismatch", new Object[]
        { componentClass.getName(), propertyName,
                ClassFabUtils.getJavaClassName(actualPropertyType),
                ClassFabUtils.getJavaClassName(expectedPropertyType) });
    }

    static String errorAddingProperty(String propertyName, Class componentClass, Throwable cause)
    {
        return _formatter.format(
                "error-adding-property",
                propertyName,
                componentClass.getName(),
                cause);
    }

    static String claimedProperty(String propertyName)
    {
        return _formatter.format("claimed-property", propertyName);
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

        return _formatter.format("instantiation-failure", new Object[]
        { c.getDeclaringClass().getName(), c, buffer.toString(), classFab, cause });
    }

    static String locatedValueIsNull(String objectReference)
    {
        return _formatter.format("located-value-is-null", objectReference);
    }

    static String incompatibleInjectType(String locator, Object value, Class propertyType)
    {
        return _formatter.format("incompatible-inject-type", locator, value, ClassFabUtils
                .getJavaClassName(propertyType));
    }

    static String initialValueForProperty(String propertyName)
    {
        return _formatter.format("initial-value-for-property", propertyName);
    }

    static String unknownInjectType(String propertyName, String injectType)
    {
        return _formatter.format("unknown-inject-type", propertyName, injectType);
    }

    static String wrongTypeForProperty(String propertyName, Class propertyType, Class requiredType)
    {
        return _formatter.format("wrong-type-for-property", propertyName, ClassFabUtils
                .getJavaClassName(propertyType), ClassFabUtils.getJavaClassName(requiredType));
    }

    static String wrongTypeForPageInjection(String propertyName, Class propertyType)
    {
        return _formatter.format("wrong-type-for-page-injection", propertyName, ClassFabUtils
                .getJavaClassName(propertyType));
    }

    static String incompatiblePropertyType(String propertyName, Class propertyType,
            Class expectedType)
    {
        return _formatter.format("incompatible-property-type", propertyName, ClassFabUtils
                .getJavaClassName(propertyType), ClassFabUtils.getJavaClassName(expectedType));
    }

    static String classEnhancementFailure(Class baseClass, Throwable cause)
    {
        return _formatter.format("class-enhancement-failure", ClassFabUtils
                .getJavaClassName(baseClass), cause);
    }

    static String methodConflict(MethodSignature sig, Location existing)
    {
        return _formatter.format("method-conflict", sig, existing);
    }

    static String readonlyProperty(String propertyName, Method writeMethod)
    {
        return _formatter.format("readonly-property", propertyName, writeMethod);
    }

    static String mustBeBoolean(String propertyName)
    {
        return _formatter.format("must-be-boolean", propertyName);
    }
    
    static String autowiring( String propertyName, IComponentSpecification spec, Object proxy )
    {
        return _formatter.format( "autowiring", propertyName, spec.getDescription(), proxy );
    }
    
    static String unableToCreateClass(String name, Class superClass, Throwable cause)
    {
        return _formatter.format("unable-to-create-class", name, superClass.getName(), cause);
    }
    
    static String unableToCreateInterface(String name, Exception cause)
    {
        return _formatter.format("unable-to-create-interface", name, cause);
    }
    
    static String unableToAddField(String fieldName, CtClass ctClass, Throwable cause)
    {
        return _formatter.format("unable-to-add-field", fieldName, ctClass.getName(), cause);
    }
    
    static String unableToLookupClass(String name, Throwable cause)
    {
        return _formatter.format("unable-to-lookup", name, cause);
    }
    
    static String unableToWriteClass(CtClass ctClass, Throwable cause)
    {
        return _formatter.format("unable-to-write-class", ctClass.getName(), cause);
    }
    
    static String duplicateMethodInClass(MethodSignature ms, ClassFabImpl cf)
    {
        return _formatter.format("duplicate-method-in-class", ms, cf.getName());
    }
    
    static String unableToAddMethod(MethodSignature methodSignature, CtClass ctClass,
            Throwable cause)
    {
        return _formatter.format("unable-to-add-method", methodSignature, ctClass.getName(), cause);
    }
    
    static String unableToAddConstructor(CtClass ctClass, Throwable cause)
    {
        return _formatter.format("unable-to-add-constructor", ctClass.getName(), cause);
    }
    
    static String unableToAddCatch(Class exceptionClass, CtMethod method, Throwable cause)
    {
        return _formatter.format("unable-to-add-catch", exceptionClass.getName(), method
                .getDeclaringClass().getName(), cause);
    }
    
    static String unableToExtendMethod(MethodSignature ms, String className, Throwable cause)
    {
        return _formatter.format("unable-to-extend-method", ms, className, cause);
    }
}
