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

import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.annotations.ComponentAnnotationWorker}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestComponentAnnotationWorker extends BaseAnnotationTestCase
{
    private EnhancementOperation newOp(Class componentClass)
    {
        MockControl control = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) control.getMock();

        op.getBaseClass();
        control.setReturnValue(componentClass);

        return op;
    }

    private IComponentSpecification attempt(Class baseClass)
    {
        EnhancementOperation op = newOp(baseClass);
        IComponentSpecification spec = new ComponentSpecification();

        Annotation a = baseClass.getAnnotation(Component.class);

        replayControls();

        new ComponentAnnotationWorker().performEnhancement(op, spec, a);

        verifyControls();

        return spec;
    }

    public void testBasic()
    {
        IComponentSpecification spec = attempt(BasicComponent.class);

        assertEquals(true, spec.getAllowBody());
        assertEquals(true, spec.getAllowInformalParameters());
        assertEquals(false, spec.isReservedParameterName("foo"));
        assertEquals(false, spec.isReservedParameterName("bar"));
        assertEquals(false, spec.isDeprecated());
    }

    public void testFormalOnly()
    {
        IComponentSpecification spec = attempt(FormalOnlyComponent.class);

        assertEquals(false, spec.getAllowBody());
        assertEquals(false, spec.getAllowInformalParameters());
        assertEquals(false, spec.isDeprecated());
    }

    public void testDeprecated()
    {
        IComponentSpecification spec = attempt(DeprecatedComponent.class);

        assertEquals(true, spec.isDeprecated());
    }

    public void testReservedParameters()
    {
        IComponentSpecification spec = attempt(ReservedParametersComponent.class);

        assertEquals(true, spec.isReservedParameterName("foo"));
        assertEquals(true, spec.isReservedParameterName("bar"));
    }
}
