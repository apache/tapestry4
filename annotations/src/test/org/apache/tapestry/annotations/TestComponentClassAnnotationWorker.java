// Copyright 2005, 2006 The Apache Software Foundation
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

import org.apache.hivemind.Location;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Tests for {@link org.apache.tapestry.annotations.ComponentClassAnnotationWorker}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestComponentClassAnnotationWorker extends BaseAnnotationTestCase
{
    private IComponentSpecification attempt(Class baseClass, Location location)
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();

        replayControls();

        new ComponentClassAnnotationWorker().performEnhancement(op, spec, baseClass, location);

        verifyControls();

        return spec;
    }

    public void testBasic()
    {
        Location l = newLocation();
        IComponentSpecification spec = attempt(BasicComponent.class, l);

        assertEquals(true, spec.getAllowBody());
        assertEquals(true, spec.getAllowInformalParameters());
        assertEquals(false, spec.isReservedParameterName("foo"));
        assertEquals(false, spec.isReservedParameterName("bar"));
        assertEquals(false, spec.isDeprecated());
        assertSame(l, spec.getLocation());
    }

    public void testSubclass()
    {
        Location l = newLocation();
        IComponentSpecification spec = attempt(BasicComponentSubclass.class, l);

        assertEquals(true, spec.getAllowBody());
        assertEquals(true, spec.getAllowInformalParameters());
        assertEquals(false, spec.isReservedParameterName("foo"));
        assertEquals(false, spec.isReservedParameterName("bar"));
        assertEquals(false, spec.isDeprecated());
        assertSame(l, spec.getLocation());
    }

    public void testFormalOnly()
    {
        IComponentSpecification spec = attempt(FormalOnlyComponent.class, null);

        assertEquals(false, spec.getAllowBody());
        assertEquals(false, spec.getAllowInformalParameters());
        assertEquals(false, spec.isDeprecated());
    }

    public void testDeprecated()
    {
        IComponentSpecification spec = attempt(DeprecatedComponent.class, null);

        assertEquals(true, spec.isDeprecated());
    }

    public void testReservedParameters()
    {
        IComponentSpecification spec = attempt(ReservedParametersComponent.class, null);

        assertEquals(true, spec.isReservedParameterName("foo"));
        assertEquals(true, spec.isReservedParameterName("bar"));
    }
}
