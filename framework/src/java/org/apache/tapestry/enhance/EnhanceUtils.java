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

import org.apache.hivemind.Defense;
import org.apache.hivemind.service.MethodSignature;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Convienience methods needed by various parts of the enhancement subsystem.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
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
            String propertyName, Class propertyType)
    {
        String methodName = op.getAccessorMethodName(propertyName);

        op.addMethod(
                Modifier.PUBLIC,
                new MethodSignature(propertyType, methodName, null, null),
                "return " + fieldName + ";");
    }

    public static void createSimpleMutator(EnhancementOperation op, String fieldName,
            String propertyName, Class propertyType)
    {
        String methodName = createMutatorMethodName(propertyName);

        op.addMethod(Modifier.PUBLIC, new MethodSignature(void.class, methodName, new Class[]
        { propertyType }, null), fieldName + " = $1;");
    }

    public static void createSimpleProperty(EnhancementOperation op, String propertyName,
            Class propertyType)
    {
        String fieldName = "_$" + propertyName;

        op.addField(fieldName, propertyType);

        createSimpleAccessor(op, fieldName, propertyName, propertyType);
        createSimpleMutator(op, fieldName, propertyName, propertyType);
    }

    /**
     * Returns the corect class for a property to be enhanced into a class. If a type name is
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

}