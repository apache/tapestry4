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

import static org.easymock.EasyMock.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.util.DescribedLocation;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.annotations.AnnotationEnhancementWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class AnnotationEnhancementWorkerTest extends BaseAnnotationTestCase
{
    protected EnhancementOperation newOp(Class baseClass)
    {
        EnhancementOperation op = newMock(EnhancementOperation.class);

        expect(op.getBaseClass()).andReturn(baseClass);

        return op;
    }

    protected Map newMap(Class annotationClass, Object worker)
    {
        return Collections.singletonMap(annotationClass, worker);
    }

    private class NoOp implements SecondaryAnnotationWorker
    {
        public boolean canEnhance(Method method)
        {
            return false;
        }

        public void peformEnhancement(EnhancementOperation op, IComponentSpecification spec,
                Method method, Resource classResource)
        {
        }
    }

    /**
     * No method annotations registered.
     */
    public void testNoAnnotations()
    {
        EnhancementOperation op = newOp(AnnotatedPage.class);
        IComponentSpecification spec = newSpec();

        replay();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setMethodWorkers(Collections.EMPTY_MAP);
        worker.setSecondaryAnnotationWorker(new NoOp());

        worker.performEnhancement(op, spec);

        verify();
    }

    public void testAnnotationMatch()
    {
        ClassResolver resolver = new DefaultClassResolver();

        EnhancementOperation op = newOp(AnnotatedPage.class);
        IComponentSpecification spec = newSpec();

        MethodAnnotationEnhancementWorker methodWorker = newMethodAnnotationEnhancementWorker();

        Method m = findMethod(AnnotatedPage.class, "getInjectedObject");

        Location location = newMethodLocation(AnnotatedPage.class, m, InjectObject.class);

        methodWorker.performEnhancement(op, spec, m, location);

        replay();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setMethodWorkers(newMap(InjectObject.class, methodWorker));
        worker.setClassResolver(resolver);
        worker.setSecondaryAnnotationWorker(new NoOp());

        worker.performEnhancement(op, spec);

        verify();
    }

    protected MethodAnnotationEnhancementWorker newMethodAnnotationEnhancementWorker()
    {
        return newMock(MethodAnnotationEnhancementWorker.class);
    }

    private DescribedLocation newClassLocation(Class baseClass, Class annotationClass)
    {
        Resource classResource = newResource(baseClass);
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

        MethodAnnotationEnhancementWorker methodWorker = newMethodAnnotationEnhancementWorker();

        Method m = findMethod(AnnotatedPageSubclass.class, "getInjectedObject");

        Location location = newMethodLocation(AnnotatedPageSubclass.class, m, InjectObject.class);

        methodWorker.performEnhancement(op, spec, m, location);

        replay();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setMethodWorkers(newMap(InjectObject.class, methodWorker));
        worker.setClassResolver(resolver);
        worker.setSecondaryAnnotationWorker(new NoOp());

        worker.performEnhancement(op, spec);

        verify();
    }

    public void testAnnotationFailure()
    {
        ClassResolver resolver = new DefaultClassResolver();

        ErrorLog log = newErrorLog();
        Throwable t = new RuntimeException("Woops!");

        EnhancementOperation op = newOp(AnnotatedPage.class);
        IComponentSpecification spec = newSpec();

        MethodAnnotationEnhancementWorker methodWorker = newMethodAnnotationEnhancementWorker();

        Method m = findMethod(AnnotatedPage.class, "getInjectedObject");

        Location location = newMethodLocation(AnnotatedPage.class, m, InjectObject.class);

        methodWorker.performEnhancement(op, spec, m, location);
        expectLastCall().andThrow(t);

        log.error("An error occured processing annotation "
                + "@org.apache.tapestry.annotations.InjectObject(value=barney) of "
                + "public abstract java.lang.Object org.apache.tapestry.annotations.AnnotatedPage.getInjectedObject(): Woops!",
                null,
                t);

        replay();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setMethodWorkers(newMap(InjectObject.class, methodWorker));
        worker.setErrorLog(log);
        worker.setClassResolver(resolver);
        worker.setSecondaryAnnotationWorker(new NoOp());

        worker.performEnhancement(op, spec);

        verify();
    }

    public void testClassAnnotation()
    {
        ClassResolver resolver = new DefaultClassResolver();

        EnhancementOperation op = newOp(DeprecatedBean.class);
        IComponentSpecification spec = newSpec();

        ClassAnnotationEnhancementWorker classWorker = newMock(ClassAnnotationEnhancementWorker.class);

        DescribedLocation location = newClassLocation(DeprecatedBean.class, Deprecated.class);

        classWorker.performEnhancement(op, spec, DeprecatedBean.class, location);

        replay();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setClassWorkers(newMap(Deprecated.class, classWorker));
        worker.setClassResolver(resolver);
        worker.setSecondaryAnnotationWorker(new NoOp());

        worker.performEnhancement(op, spec);

        verify();
    }

    public void testClassAnnotationFailure()
    {
        ClassResolver resolver = new DefaultClassResolver();

        ErrorLog log = newErrorLog();
        EnhancementOperation op = newOp(DeprecatedBean.class);
        IComponentSpecification spec = newSpec();

        ClassAnnotationEnhancementWorker classWorker = newMock(ClassAnnotationEnhancementWorker.class);

        Throwable t = new RuntimeException("Simulated failure.");

        DescribedLocation location = newClassLocation(DeprecatedBean.class, Deprecated.class);

        classWorker.performEnhancement(op, spec, DeprecatedBean.class, location);
        expectLastCall().andThrow(t);

        log.error("An error occured processing annotation @java.lang.Deprecated() of "
                + "class org.apache.tapestry.annotations.DeprecatedBean: Simulated failure.",
                null,
                t);

        replay();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setClassWorkers(newMap(Deprecated.class, classWorker));
        worker.setErrorLog(log);
        worker.setClassResolver(resolver);
        worker.setSecondaryAnnotationWorker(new NoOp());

        worker.performEnhancement(op, spec);

        verify();
    }

    public void testClassAnnotationNoMatch()
    {
        EnhancementOperation op = newOp(DeprecatedBean.class);
        IComponentSpecification spec = newSpec();

        replay();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setClassWorkers(Collections.EMPTY_MAP);
        worker.setSecondaryAnnotationWorker(new NoOp());

        worker.performEnhancement(op, spec);

        verify();
    }

    public void testSecondaryEnhancementWorker()
    {
        SecondaryAnnotationWorker secondary = newSecondaryAnnotationWorker();

        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        Method method = findMethod(AnnotatedPage.class, "getPropertyWithInitialValue");

        Resource classResource = newResource(AnnotatedPage.class);

        expect(secondary.canEnhance(method)).andReturn(true);

        secondary.peformEnhancement(op, spec, method, classResource);

        replay();

        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setSecondaryAnnotationWorker(secondary);
        worker.setMethodWorkers(Collections.EMPTY_MAP);

        worker.performMethodEnhancement(op, spec, method, classResource);

        verify();
    }

    public void testSecondaryEnhancementWorkerFailure()
    {
        SecondaryAnnotationWorker secondary = newSecondaryAnnotationWorker();

        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        ErrorLog log = newErrorLog();

        Method method = findMethod(AnnotatedPage.class, "getPropertyWithInitialValue");

        Resource classResource = newResource(AnnotatedPage.class);

        RuntimeException cause = new RuntimeException("Forced.");

        expect(secondary.canEnhance(method)).andThrow(cause);
        
        log.error(AnnotationMessages.failureEnhancingMethod(method, cause), null, cause);
        
        replay();
        
        AnnotationEnhancementWorker worker = new AnnotationEnhancementWorker();
        worker.setSecondaryAnnotationWorker(secondary);
        worker.setMethodWorkers(Collections.EMPTY_MAP);
        worker.setErrorLog(log);

        worker.performMethodEnhancement(op, spec, method, classResource);

        verify();
    }

    protected SecondaryAnnotationWorker newSecondaryAnnotationWorker()
    {
        return newMock(SecondaryAnnotationWorker.class);
    }
}
