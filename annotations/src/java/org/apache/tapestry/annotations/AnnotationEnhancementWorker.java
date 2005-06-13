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
import java.util.Map;

import org.apache.hivemind.ErrorLog;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.enhance.EnhancementWorker;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Implementation of {@link org.apache.tapestry.enhance.EnhancementWorker} that finds class and
 * method annotations and delegates out to specific
 * {@link org.apache.tapestry.annotations.ClassAnnotationEnhancementWorker} and
 * {@link org.apache.tapestry.annotations.MethodAnnotationEnhancementWorker} instances.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class AnnotationEnhancementWorker implements EnhancementWorker
{
    private ErrorLog _errorLog;

    private Map _methodWorkers;

    private Map _classWorkers;

    public void setClassWorkers(Map classWorkers)
    {
        _classWorkers = classWorkers;
    }

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec)
    {
        Class clazz = op.getBaseClass();

        for (Annotation a : clazz.getAnnotations())
        {
            performClassEnhancement(op, spec, clazz, a);
        }

        for (Method m : clazz.getMethods())
        {
            performMethodEnhancement(op, spec, m);
        }
    }

    void performClassEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Class clazz, Annotation annotation)
    {
        ClassAnnotationEnhancementWorker worker = (ClassAnnotationEnhancementWorker) _classWorkers
                .get(annotation.annotationType());

        if (worker == null)
            return;

        try
        {
            worker.performEnhancement(op, spec, annotation);
        }
        catch (Exception ex)
        {
            _errorLog.error(AnnotationMessages.failureProcessingClassAnnotation(
                    annotation,
                    clazz,
                    ex), null, ex);
        }

    }

    void performMethodEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method)
    {
        for (Annotation a : method.getAnnotations())
        {
            performMethodEnhancement(op, spec, method, a);
        }
    }

    void performMethodEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method, Annotation annotation)
    {
        MethodAnnotationEnhancementWorker worker = (MethodAnnotationEnhancementWorker) _methodWorkers
                .get(annotation.annotationType());

        if (worker == null)
            return;

        try
        {
            worker.performEnhancement(op, spec, annotation, method);
        }
        catch (Exception ex)
        {
            _errorLog.error(
                    AnnotationMessages.failureProcessingAnnotation(annotation, method, ex),
                    null,
                    ex);
        }

    }

    public void setMethodWorkers(Map methodWorkers)
    {
        _methodWorkers = methodWorkers;
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
}
