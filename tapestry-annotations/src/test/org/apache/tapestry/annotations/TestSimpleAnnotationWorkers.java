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
import org.apache.tapestry.spec.InjectSpecification;
import org.testng.annotations.Test;

/**
 * Test for the "simple" annotation workers, that collect basic information and update the component
 * specification.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestSimpleAnnotationWorkers extends BaseAnnotationTestCase
{
    public void test_Inject_Page()
    {
        Location l = newLocation();
        IComponentSpecification spec = execute(new InjectPageAnnotationWorker(), "getMyPage", l);

        InjectSpecification is = (InjectSpecification) spec.getInjectSpecifications().get(0);

        assertEquals("myPage", is.getProperty());
        assertEquals("page", is.getType());
        assertEquals("SomePageName", is.getObject());
        assertSame(l, is.getLocation());
    }

    public void test_Inject_Meta()
    {
        Location l = newLocation();
        IComponentSpecification spec = execute(new InjectMetaAnnotationWorker(), "getMetaFred", l);

        InjectSpecification is = (InjectSpecification) spec.getInjectSpecifications().get(0);

        assertEquals("metaFred", is.getProperty());
        assertEquals("meta", is.getType());
        assertEquals("fred", is.getObject());
        assertSame(l, is.getLocation());

    }
    
    public void test_Inject_Meta_NoValue()
    {
        Location l = newLocation();
        IComponentSpecification spec = execute(new InjectMetaAnnotationWorker(), "getPageTitle", l);

        InjectSpecification is = (InjectSpecification) spec.getInjectSpecifications().get(0);

        assertEquals("pageTitle", is.getProperty());
        assertEquals("meta", is.getType());
        assertEquals("page-title", is.getObject());
        assertSame(l, is.getLocation());

    }    

    public void test_Inject_Script()
    {
        Location l = newLocation();
        IComponentSpecification spec = execute(new InjectScriptAnnotationWorker(), "getScript", l);

        InjectSpecification is = (InjectSpecification) spec.getInjectSpecifications().get(0);

        assertEquals("script", is.getProperty());
        assertEquals("script", is.getType());
        assertEquals("foo.script", is.getObject());
        assertSame(l, is.getLocation());
    }

    public void test_Inject_State()
    {
        Location l = newLocation();
        IComponentSpecification spec = execute(new InjectStateAnnotationWorker(), "getBarney", l);

        InjectSpecification is = (InjectSpecification) spec.getInjectSpecifications().get(0);

        assertEquals("barney", is.getProperty());
        assertEquals("state", is.getType());
        assertEquals("barneyASO", is.getObject());
        assertSame(l, is.getLocation());
    }
    
    public void test_Inject_State_NoValue()
    {
        Location l = newLocation();
        IComponentSpecification spec = execute(new InjectStateAnnotationWorker(), "getMyVisit", l);

        InjectSpecification is = (InjectSpecification) spec.getInjectSpecifications().get(0);

        assertEquals("myVisit", is.getProperty());
        assertEquals("state", is.getType());
        assertEquals("my-visit", is.getObject());
        assertSame(l, is.getLocation());
    }    

    public void test_Inject_State_Flag()
    {
        Location l = newLocation();
        IComponentSpecification spec = execute(
                new InjectStateFlagAnnotationWorker(),
                "getBarneyExists",
                l);

        InjectSpecification is = (InjectSpecification) spec.getInjectSpecifications().get(0);

        assertEquals("barneyExists", is.getProperty());
        assertEquals("state-flag", is.getType());
        assertEquals("barneyASO", is.getObject());
        assertSame(l, is.getLocation());
    }
    
    public void test_Inject_State_Flag_NoValue()
    {
        Location l = newLocation();
        IComponentSpecification spec = execute(
                new InjectStateFlagAnnotationWorker(),
                "getMyVisitExists",
                l);

        InjectSpecification is = (InjectSpecification) spec.getInjectSpecifications().get(0);

        assertEquals("myVisitExists", is.getProperty());
        assertEquals("state-flag", is.getType());
        assertEquals("my-visit", is.getObject());
        assertSame(l, is.getLocation());
    }    

    private IComponentSpecification execute(MethodAnnotationEnhancementWorker worker,
            String methodName, Location location)
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();

        Method method = findMethod(AnnotatedPage.class, methodName);

        replay();

        worker.performEnhancement(op, spec, method, location);

        verify();

        return spec;
    }
}
