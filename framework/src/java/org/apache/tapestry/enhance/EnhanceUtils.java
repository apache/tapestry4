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
    static MethodSignature FINISH_LOAD_SIGNATURE = new MethodSignature(void.class, "finishLoad",
            new Class[]
            { IRequestCycle.class, IPageLoader.class, IComponentSpecification.class }, null);

    static MethodSignature PAGE_DETACHED_SIGNATURE = new MethodSignature(void.class,
            "pageDetached", new Class[]
            { PageEvent.class }, null);

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
}