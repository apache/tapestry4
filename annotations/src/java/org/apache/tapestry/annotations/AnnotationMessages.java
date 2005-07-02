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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.hivemind.impl.MessageFormatter;
import org.apache.hivemind.service.ClassFabUtils;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */

class AnnotationMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(
            AnnotationMessages.class, "AnnotationStrings");

    public static String noParametersExpected(Method m)
    {
        return _formatter.format("no-parameters-expected", m);
    }

    public static String notAccessor(Method method)
    {
        return _formatter.format("no-accessor", method);
    }

    public static String voidAccessor(Method method)
    {
        return _formatter.format("void-accessor", method);
    }

    public static String nonVoidMutator(Method method)
    {
        return _formatter.format("non-void-mutator", method);
    }

    public static String wrongParameterCount(Method method)
    {
        return _formatter.format("wrong-parameter-count", method);
    }

    public static String failureProcessingAnnotation(Annotation annotation, Method method,
            Throwable cause)
    {
        return _formatter.format("failure-processing-annotation", annotation, method, cause);
    }

    public static String failureProcessingClassAnnotation(Annotation annotation, Class clazz,
            Throwable cause)
    {
        return _formatter.format(
                "failure-processing-class-annotation",
                annotation,
                clazz.getName(),
                cause);
    }

    public static String returnStringOnly(Class returnType)
    {
        return _formatter.format("return-string-only", ClassFabUtils.getJavaClassName(returnType));
    }

    public static String bindingWrongFormat(String binding)
    {
        return _formatter.format("binding-wrong-format", binding);
    }

    public static String methodAnnotation(Annotation annotation, Method method)
    {
        return _formatter.format("method-annotation", annotation, method);
    }

    public static String classAnnotation(Annotation annotation, Class clazz)
    {
        return _formatter.format("class-annotation", annotation, clazz.getName());
    }

}
