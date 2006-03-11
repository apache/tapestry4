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
import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.hivemind.service.MethodSignature;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Convienience methods needed by various parts of the enhancement subsystem.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class EnhanceUtils
{
    public static final MethodSignature FINISH_LOAD_SIGNATURE = new MethodSignature(void.class,
            "finishLoad", new Class[]
            { IRequestCycle.class, IPageLoader.class, IComponentSpecification.class }, null);

    public static final MethodSignature PAGE_DETACHED_SIGNATURE = new MethodSignature(void.class,
            "pageDetached", new Class[]
            { PageEvent.class }, null);

    public static final MethodSignature CLEANUP_AFTER_RENDER_SIGNATURE = new MethodSignature(
            void.class, "cleanupAfterRender", new Class[]
            { IRequestCycle.class }, null);

    public static String createMutatorMethodName(String propertyName)
    {
        return "set" + upcase(propertyName);
    }

    public static String createAccessorMethodName(String propertyName)
    {
        return "get" + upcase(propertyName);
    }

    private static String upcase(String name)
    {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static void createSimpleAccessor(EnhancementOperation op, String fieldName,
            String propertyName, Class propertyType, Location location)
    {
        String methodName = op.getAccessorMethodName(propertyName);

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(propertyType, methodName, null, null),
                "return " + fieldName + ";",
                location);
    }

    public static void createSimpleMutator(EnhancementOperation op, String fieldName,
            String propertyName, Class propertyType, Location location)
    {
        String methodName = createMutatorMethodName(propertyName);

        op.addMethod(Modifier.PUBLIC, new MethodSignature(void.class, methodName, new Class[]
        { propertyType }, null), fieldName + " = $1;", location);
    }

    /**
     * Returns the correct class for a property to be enhanced into a class. If a type name is
     * non-null, then it is converted to a Class. If the class being enhanced defines a property,
     * then the type must be an exact match (this is largely a holdover from Tapestry 3.0, where the
     * type had to be provided in the specification). If the type name is null, then the value
     * returned is the type of the existing property (if such a property exists), or
     * java.lang.Object is no property exists.
     * 
     * @param op
     *            the enhancement operation, which provides most of this logic
     * @param propertyName
     *            the name of the property (the property may or may not exist)
     * @param definedTypeName
     *            the type indicated for the property, may be null to make the return value match
     *            the type of an existing property.
     */

    public static Class extractPropertyType(EnhancementOperation op, String propertyName,
            String definedTypeName)
    {
        Defense.notNull(op, "op");
        Defense.notNull(propertyName, "propertyName");

        if (definedTypeName != null)
        {
            Class propertyType = op.convertTypeName(definedTypeName);

            op.validateProperty(propertyName, propertyType);

            return propertyType;
        }

        Class propertyType = op.getPropertyType(propertyName);

        return propertyType == null ? Object.class : propertyType;
    }

    // The following methods are actually invoked from fabricated methods in
    // enhanced classes.

    public static boolean toBoolean(IBinding binding)
    {
        Boolean wrapped = (Boolean) binding.getObject(Boolean.class);

        return wrapped == null ? false : wrapped.booleanValue();
    }

    public static byte toByte(IBinding binding)
    {
        Byte wrapped = (Byte) binding.getObject(Byte.class);

        return wrapped == null ? 0 : wrapped.byteValue();
    }

    public static char toChar(IBinding binding)
    {
        Character wrapped = (Character) binding.getObject(Character.class);

        return wrapped == null ? 0 : wrapped.charValue();
    }

    public static short toShort(IBinding binding)
    {
        Short wrapped = (Short) binding.getObject(Short.class);

        return wrapped == null ? 0 : wrapped.shortValue();
    }

    public static int toInt(IBinding binding)
    {
        Integer wrapped = (Integer) binding.getObject(Integer.class);

        return wrapped == null ? 0 : wrapped.intValue();
    }

    public static long toLong(IBinding binding)
    {
        Long wrapped = (Long) binding.getObject(Long.class);

        return wrapped == null ? 0 : wrapped.longValue();
    }

    public static float toFloat(IBinding binding)
    {
        Float wrapped = (Float) binding.getObject(Float.class);

        return wrapped == null ? 0.0f : wrapped.floatValue();
    }

    public static double toDouble(IBinding binding)
    {
        Double wrapped = (Double) binding.getObject(Double.class);

        return wrapped == null ? 0.0d : wrapped.doubleValue();
    }

    /**
     * Used to unwrap primitive types inside the accessor method. In each case, the binding is in a
     * variable named "binding", and {0} will be the actual type of the property. The Map is keyed
     * on the primtive type.
     */

    private static Map _unwrappers = new HashMap();

    static
    {
        _unwrappers.put(boolean.class, "toBoolean");
        _unwrappers.put(byte.class, "toByte");
        _unwrappers.put(char.class, "toChar");
        _unwrappers.put(short.class, "toShort");
        _unwrappers.put(int.class, "toInt");
        _unwrappers.put(long.class, "toLong");
        _unwrappers.put(float.class, "toFloat");
        _unwrappers.put(double.class, "toDouble");
    }

    /**
     * Returns the name of the static method, within EnhanceUtils, used to unwrap a binding to a
     * primitive type. Returns null if the type is not a primitve.
     */

    public static String getUnwrapperMethodName(Class type)
    {
        Defense.notNull(type, "type");

        return (String) _unwrappers.get(type);
    }

    /**
     * Builds a Javassist expression for unwrapping a binding's value to a type (either primitive or
     * a class type).
     * 
     * @param op
     *            the enhancement operation
     * @param bindingName
     *            the name of the field (or an expression) that will evaluate to the binding from
     *            which a value will be extracted.
     * @param valueType
     *            the type of value to be extracted from the binding.
     */

    public static String createUnwrapExpression(EnhancementOperation op, String bindingName,
            Class valueType)
    {
        Defense.notNull(op, "op");
        Defense.notNull(bindingName, "bindingName");
        Defense.notNull(valueType, "valueType");

        StringBuffer buffer = new StringBuffer();

        String unwrapper = getUnwrapperMethodName(valueType);

        if (unwrapper == null)
        {
            String propertyTypeRef = op.getClassReference(valueType);

            buffer.append("(");
            buffer.append(ClassFabUtils.getJavaClassName(valueType));
            buffer.append(") ");
            buffer.append(bindingName);
            buffer.append(".getObject(");
            buffer.append(propertyTypeRef);
            buffer.append(")");
        }
        else
        {
            buffer.append(EnhanceUtils.class.getName());
            buffer.append(".");
            buffer.append(unwrapper);
            buffer.append("(");
            buffer.append(bindingName);
            buffer.append(")");
        }

        return buffer.toString();
    }

    /**
     * Verifies that a property type can be assigned a particular type of value.
     * 
     * @param op
     *            the enhancement operation
     * @param propertyName
     *            the name of the property to check
     * @param requiredType
     *            the type of value that will be assigned to the property
     * @return the property type, or java.lang.Object if the class does not define the property
     */
    public static Class verifyPropertyType(EnhancementOperation op, String propertyName,
            Class requiredType)
    {
        Defense.notNull(op, "op");
        Defense.notNull(propertyName, "propertyName");
        Defense.notNull(requiredType, "requiredType");

        Class propertyType = op.getPropertyType(propertyName);

        // When the property type is not defined, it will end up being
        if (propertyType == null)
            return Object.class;

        // Make sure that an object of the required type is assignable
        // to the property type.

        if (!propertyType.isAssignableFrom(requiredType))
            throw new ApplicationRuntimeException(EnhanceMessages.wrongTypeForProperty(
                    propertyName,
                    propertyType,
                    requiredType));

        return propertyType;
    }
}