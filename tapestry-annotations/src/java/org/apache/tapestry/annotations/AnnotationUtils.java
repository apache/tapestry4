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
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.util.DescribedLocation;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */

public final class AnnotationUtils
{
    /* defeat instantiation */
    private AnnotationUtils() { }
    
    /**
     * Determines the property name for a method, by stripping off the is/get/set prefix and
     * decapitalizing the first name.
     * 
     * @param method
     *            accessor method (get/set/is)
     * @return the property name for the method
     * @throws ApplicationRuntimeException
     *             if the method is not an accessor or mutator method
     */
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
    
    /**
     * Converts a method name to a property key. The steps performed are:
     * <p>The prefix "get" is stripped off (if present)
     * <p>The letter following "get" is converted to lower case
     * <p>Other capitalized letters are converted to lower case and preceded with a dash ("-")
     * 
     * @param methodName the method to convert
     * @return the converted key
     * 
     * @since 4.1.1 
     */
    public static String convertMethodNameToKeyName(String methodName)
    {
        StringBuffer buffer = new StringBuffer();
    
        int cursorx = methodName.startsWith("get") ? 3 : 0;
        int length = methodName.length();
        boolean atStart = true;
    
        while (cursorx < length)
        {
            char ch = methodName.charAt(cursorx);
    
            if (Character.isUpperCase(ch))
            {
                if (!atStart)
                    buffer.append('-');
                buffer.append(Character.toLowerCase(ch));
            }
            else
                buffer.append(ch);
    
            atStart = false;
    
            cursorx++;
        }
    
        return buffer.toString();
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

    public static Location buildLocationForAnnotation(Method method, Annotation annotation,
            Resource classResource)
    {
        return new DescribedLocation(classResource, AnnotationMessages.methodAnnotation(
                annotation,
                method));
    }
}
