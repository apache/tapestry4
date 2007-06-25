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
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IPropertySpecification;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.annotations.PersistAnnotationWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class PersistAnnotationWorkerTest extends AnnotationEnhancementWorkerTest
{
    public void testDefaultStrategy()
    {
        Location l = newLocation();

        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();

        replay();

        Method m = findMethod(AnnotatedPage.class, "getPersistentProperty");

        new PersistAnnotationWorker().performEnhancement(op, spec, m, l);

        verify();

        IPropertySpecification ps = spec.getPropertySpecification("persistentProperty");

        assertEquals("session", ps.getPersistence());
        assertEquals("persistentProperty", ps.getName());
        assertSame(l, ps.getLocation());
        assertNull(ps.getInitialValue());
    }

    public void testStrategySpecified()
    {
        Location l = newLocation();

        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();

        replay();

        Method m = findMethod(AnnotatedPage.class, "getClientPersistentProperty");

        new PersistAnnotationWorker().performEnhancement(op, spec, m, l);

        verify();

        IPropertySpecification ps = spec.getPropertySpecification("clientPersistentProperty");

        assertEquals("client", ps.getPersistence());
        assertEquals("clientPersistentProperty", ps.getName());
        assertSame(l, ps.getLocation());
        assertNull(ps.getInitialValue());
    }

    public void testWithInitialValue()
    {
        Location l = newLocation();

        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();

        replay();

        Method m = findMethod(AnnotatedPage.class, "getPersistentPropertyWithInitialValue");

        new PersistAnnotationWorker().performEnhancement(op, spec, m, l);

        verify();

        IPropertySpecification ps = spec
                .getPropertySpecification("persistentPropertyWithInitialValue");

        assertEquals("session", ps.getPersistence());
        assertEquals("persistentPropertyWithInitialValue", ps.getName());
        assertSame(l, ps.getLocation());
        assertEquals("user.naturalName", ps.getInitialValue());
    }
}
