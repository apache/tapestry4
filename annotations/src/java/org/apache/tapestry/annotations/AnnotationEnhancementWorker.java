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

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.enhance.EnhancementWorker;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.util.DescribedLocation;

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
    private ClassResolver _classResolver;

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

        Resource classResource = newClassResource(clazz);

        for (Annotation a : clazz.getAnnotations())
        {
            performClassEnhancement(op, spec, clazz, a, classResource);
        }

        for (Method m : clazz.getMethods())
        {
            performMethodEnhancement(op, spec, m, classResource);
        }
    }

    private ClasspathResource newClassResource(Class clazz)
    {
        return new ClasspathResource(_classResolver, clazz.getName().replace('.', '/'));
    }

    void performClassEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Class clazz, Annotation annotation, Resource classResource)
    {
        ClassAnnotationEnhancementWorker worker = (ClassAnnotationEnhancementWorker) _classWorkers
                .get(annotation.annotationType());

        if (worker == null)
            return;

        try
        {
            Location location = new DescribedLocation(classResource, AnnotationMessages
                    .classAnnotation(annotation, clazz));

            worker.performEnhancement(op, spec, clazz, location);
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
            Method method, Resource classResource)
    {
        for (Annotation a : method.getAnnotations())
        {
            performMethodEnhancement(op, spec, method, a, classResource);
        }
    }

    void performMethodEnhancement(EnhancementOperation op, IComponentSpecification spec,
            Method method, Annotation annotation, Resource classResource)
    {
        MethodAnnotationEnhancementWorker worker = (MethodAnnotationEnhancementWorker) _methodWorkers
                .get(annotation.annotationType());

        if (worker == null)
            return;

        try
        {
            Location location = new DescribedLocation(classResource, AnnotationMessages
                    .methodAnnotation(annotation, method));
            worker.performEnhancement(op, spec, method, location);
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

    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }
}
