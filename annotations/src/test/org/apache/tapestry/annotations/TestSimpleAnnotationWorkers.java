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

import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.InjectSpecification;

/**
 * Test for the "simple" annotation workers, that collect basic information and update the component
 * specification. {@link org.apache.tapestry.annotations.InjectPageAnnotationWorker}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestSimpleAnnotationWorkers extends BaseAnnotationTestCase
{
    public void testInjectPage()
    {
        IComponentSpecification spec = execute(new InjectPageAnnotationWorker(), "getMyPage");

        InjectSpecification is = (InjectSpecification) spec.getInjectSpecifications().get(0);

        assertEquals("myPage", is.getProperty());
        assertEquals("page", is.getType());
        assertEquals("SomePageName", is.getObject());
        assertNull(is.getLocation());
    }

    public void testInjectMeta()
    {
        IComponentSpecification spec = execute(new InjectMetaAnnotationWorker(), "getMetaFred");

        InjectSpecification is = (InjectSpecification) spec.getInjectSpecifications().get(0);

        assertEquals("metaFred", is.getProperty());
        assertEquals("meta", is.getType());
        assertEquals("fred", is.getObject());
        assertNull(is.getLocation());

    }

    public void testInjectScript()
    {
        IComponentSpecification spec = execute(new InjectScriptAnnotationWorker(), "getScript");

        InjectSpecification is = (InjectSpecification) spec.getInjectSpecifications().get(0);

        assertEquals("script", is.getProperty());
        assertEquals("script", is.getType());
        assertEquals("foo.script", is.getObject());
        assertNull(is.getLocation());

    }

    private IComponentSpecification execute(MethodAnnotationEnhancementWorker worker,
            String methodName)
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();

        Method method = findMethod(AnnotatedPage.class, methodName);

        replayControls();

        worker.performEnhancement(op, spec, method);

        verifyControls();

        return spec;
    }
}
