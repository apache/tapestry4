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
import java.util.Collections;
import java.util.Map;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.util.DescribedLocation;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.annotations.AnnotationEnhancementWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */

public class TestAnnotationEnhancementWorker extends BaseAnnotationTestCase
{
    protected EnhancementOperation newOp(Class baseClass)
    {
        MockControl control = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) control.getMock();

        op.getBaseClass();
        control.setReturnValue(baseClass);

        return op;
    }

    protected Map newMap(Class annotationClass, Object worker)
    {
        return Collections.singletonMap(annotationClass, worker);
    }

    /**
     * No method annotations registered.
     */
    public void testNoAnnotations()
    {
        EnhancementOperation op = newOp(AnnotatedPage.class);
        IComponentSpecification spec = newSpec();

        replayControls();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setMethodWorkers(Collections.EMPTY_MAP);

        worker.performEnhancement(op, spec);

        verifyControls();
    }

    public void testAnnotationMatch()
    {
        ClassResolver resolver = new DefaultClassResolver();

        EnhancementOperation op = newOp(AnnotatedPage.class);
        IComponentSpecification spec = newSpec();

        MethodAnnotationEnhancementWorker methodWorker = (MethodAnnotationEnhancementWorker) newMock(MethodAnnotationEnhancementWorker.class);

        Method m = findMethod(AnnotatedPage.class, "getInjectedObject");

        DescribedLocation location = newMethodLocation(
                resolver,
                AnnotatedPage.class,
                m,
                InjectObject.class);

        methodWorker.performEnhancement(op, spec, m, location);

        replayControls();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setMethodWorkers(newMap(InjectObject.class, methodWorker));
        worker.setClassResolver(resolver);

        worker.performEnhancement(op, spec);

        verifyControls();
    }

    protected DescribedLocation newMethodLocation(ClassResolver resolver, Class baseClass,
            Method m, Class annotationClass)
    {
        Resource classResource = newResource(resolver, baseClass);

        return new DescribedLocation(classResource, AnnotationMessages.methodAnnotation(m
                .getAnnotation(annotationClass), m));
    }

    private DescribedLocation newClassLocation(ClassResolver resolver, Class baseClass,
            Class annotationClass)
    {
        Resource classResource = newResource(resolver, baseClass);
        Annotation annotation = baseClass.getAnnotation(annotationClass);

        return new DescribedLocation(classResource, AnnotationMessages.classAnnotation(
                annotation,
                baseClass));
    }

    public void testAnnotationWithSubclass()
    {
        ClassResolver resolver = new DefaultClassResolver();

        EnhancementOperation op = newOp(AnnotatedPageSubclass.class);
        IComponentSpecification spec = newSpec();

        MethodAnnotationEnhancementWorker methodWorker = (MethodAnnotationEnhancementWorker) newMock(MethodAnnotationEnhancementWorker.class);

        Method m = findMethod(AnnotatedPageSubclass.class, "getInjectedObject");

        DescribedLocation location = newMethodLocation(
                resolver,
                AnnotatedPageSubclass.class,
                m,
                InjectObject.class);

        methodWorker.performEnhancement(op, spec, m, location);

        replayControls();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setMethodWorkers(newMap(InjectObject.class, methodWorker));
        worker.setClassResolver(resolver);

        worker.performEnhancement(op, spec);

        verifyControls();
    }

    public void testAnnotationFailure()
    {
        ClassResolver resolver = new DefaultClassResolver();

        ErrorLog log = newLog();
        Throwable t = new RuntimeException("Woops!");

        EnhancementOperation op = newOp(AnnotatedPage.class);
        IComponentSpecification spec = newSpec();

        MockControl methodWorkerc = newControl(MethodAnnotationEnhancementWorker.class);
        MethodAnnotationEnhancementWorker methodWorker = (MethodAnnotationEnhancementWorker) methodWorkerc
                .getMock();

        Method m = findMethod(AnnotatedPage.class, "getInjectedObject");

        DescribedLocation location = newMethodLocation(
                resolver,
                AnnotatedPage.class,
                m,
                InjectObject.class);

        methodWorker.performEnhancement(op, spec, m, location);
        methodWorkerc.setThrowable(t);

        log
                .error(
                        "An error occured processing annotation "
                                + "@org.apache.tapestry.annotations.InjectObject(value=barney) of "
                                + "public abstract java.lang.Object org.apache.tapestry.annotations.AnnotatedPage.getInjectedObject(): Woops!",
                        null,
                        t);

        replayControls();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setMethodWorkers(newMap(InjectObject.class, methodWorker));
        worker.setErrorLog(log);
        worker.setClassResolver(resolver);

        worker.performEnhancement(op, spec);

        verifyControls();
    }

    private ErrorLog newLog()
    {
        return (ErrorLog) newMock(ErrorLog.class);
    }

    public void testClassAnnotation()
    {
        ClassResolver resolver = new DefaultClassResolver();

        EnhancementOperation op = newOp(DeprecatedBean.class);
        IComponentSpecification spec = newSpec();

        ClassAnnotationEnhancementWorker classWorker = (ClassAnnotationEnhancementWorker) newMock(ClassAnnotationEnhancementWorker.class);

        DescribedLocation location = newClassLocation(
                resolver,
                DeprecatedBean.class,
                Deprecated.class);

        classWorker.performEnhancement(op, spec, DeprecatedBean.class, location);

        replayControls();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setClassWorkers(newMap(Deprecated.class, classWorker));
        worker.setClassResolver(resolver);

        worker.performEnhancement(op, spec);

        verifyControls();
    }

    public void testClassAnnotationFailure()
    {
        ClassResolver resolver = new DefaultClassResolver();

        ErrorLog log = newLog();
        EnhancementOperation op = newOp(DeprecatedBean.class);
        IComponentSpecification spec = newSpec();

        MockControl classWorkerc = newControl(ClassAnnotationEnhancementWorker.class);
        ClassAnnotationEnhancementWorker classWorker = (ClassAnnotationEnhancementWorker) classWorkerc
                .getMock();

        Throwable t = new RuntimeException("Simulated failure.");

        DescribedLocation location = newClassLocation(
                resolver,
                DeprecatedBean.class,
                Deprecated.class);

        classWorker.performEnhancement(op, spec, DeprecatedBean.class, location);
        classWorkerc.setThrowable(t);

        log
                .error(
                        "An error occured processing annotation @java.lang.Deprecated() of "
                                + "class org.apache.tapestry.annotations.DeprecatedBean: Simulated failure.",
                        null,
                        t);

        replayControls();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setClassWorkers(newMap(Deprecated.class, classWorker));
        worker.setErrorLog(log);
        worker.setClassResolver(resolver);

        worker.performEnhancement(op, spec);

        verifyControls();
    }

    public void testClassAnnotationNoMatch()
    {
        EnhancementOperation op = newOp(DeprecatedBean.class);
        IComponentSpecification spec = newSpec();

        replayControls();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setClassWorkers(Collections.EMPTY_MAP);

        worker.performEnhancement(op, spec);

        verifyControls();
    }
}
