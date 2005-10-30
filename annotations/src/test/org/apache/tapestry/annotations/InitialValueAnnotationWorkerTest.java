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

import java.lang.reflect.Method;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IPropertySpecification;

/**
 * Tests for {@link org.apache.tapestry.annotations.InitialValueAnnotationWorker}.
 * 
 * @author Howard M. Lewis Ship
 */
public class InitialValueAnnotationWorkerTest extends BaseAnnotationTestCase
{
    public void testCanEnhance()
    {
        InitialValueAnnotationWorker worker = new InitialValueAnnotationWorker();

        assertEquals(false, worker.canEnhance(findMethod(AnnotatedPage.class, "getMapBean")));
        assertEquals(true, worker.canEnhance(findMethod(
                AnnotatedPage.class,
                "getPropertyWithInitialValue")));
    }

    public void testEnhanceNonMatch()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        Method method = findMethod(AnnotatedPage.class, "getMapBean");
        Resource resource = newResource(AnnotatedPage.class);

        replayControls();

        InitialValueAnnotationWorker worker = new InitialValueAnnotationWorker();

        worker.peformEnhancement(op, spec, method, resource);

        verifyControls();
    }

    public void testEnhancePersistAnnotationPresent()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        Method method = findMethod(AnnotatedPage.class, "getPersistentPropertyWithInitialValue");
        Resource resource = newResource(AnnotatedPage.class);

        replayControls();

        InitialValueAnnotationWorker worker = new InitialValueAnnotationWorker();

        worker.peformEnhancement(op, spec, method, resource);

        verifyControls();
    }

    public void testJustInitialValue()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();
        Method method = findMethod(AnnotatedPage.class, "getPropertyWithInitialValue");
        Resource resource = newResource(AnnotatedPage.class);

        replayControls();

        InitialValueAnnotationWorker worker = new InitialValueAnnotationWorker();

        worker.peformEnhancement(op, spec, method, resource);

        verifyControls();

        IPropertySpecification ps = spec.getPropertySpecification("propertyWithInitialValue");

        assertEquals("propertyWithInitialValue", ps.getName());
        assertEquals(false, ps.isPersistent());
        assertEquals("fred", ps.getInitialValue());

        Location expectedLocation = newMethodLocation(
                AnnotatedPage.class,
                method,
                InitialValue.class);

        assertEquals(expectedLocation, ps.getLocation());
    }
}
