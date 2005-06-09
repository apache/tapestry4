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

package org.apache.tapestry.annotations;

import java.beans.Introspector;
import java.lang.reflect.Method;

import org.apache.hivemind.ApplicationRuntimeException;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */

public class AnnotationUtils
{
    public static String getPropertyName(Method method)
    {
        String name = method.getName();

        if (name.startsWith("is"))
        {
            checkGetter(method);
            return Introspector.decapitalize(name.substring(2));
        }

        if (name.startsWith("get"))
        {
            checkGetter(method);
            return Introspector.decapitalize(name.substring(3));
        }

        if (name.startsWith("set"))
        {
            checkSetter(method);
            return Introspector.decapitalize(name.substring(3));
        }

        throw new ApplicationRuntimeException(AnnotationMessages.notAccessor(method));
    }

    private static void checkGetter(Method method)
    {
        if (method.getParameterTypes().length > 0)
            throw new ApplicationRuntimeException(AnnotationMessages.noParametersExpected(method));

        if (method.getReturnType().equals(void.class))
            throw new ApplicationRuntimeException(AnnotationMessages.voidAccessor(method));

    }

    private static void checkSetter(Method method)
    {
        if (!method.getReturnType().equals(void.class))
            throw new ApplicationRuntimeException(AnnotationMessages.nonVoidMutator(method));

        if (method.getParameterTypes().length != 1)
            throw new ApplicationRuntimeException(AnnotationMessages.wrongParameterCount(method));
    }
}
