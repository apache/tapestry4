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
import java.util.HashMap;
import java.util.List;

import org.apache.hivemind.Location;
import org.apache.tapestry.bean.LightweightBeanInitializer;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.BeanLifecycle;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IBeanSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.annotations.BeanAnnotationWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestBeanAnnotationWorker extends BaseAnnotationTestCase
{
    public void testBeanClassSpecified()
    {
        Location l = newLocation();
        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();

        Method m = findMethod(AnnotatedPage.class, "getMapBean");

        replay();

        new BeanAnnotationWorker().performEnhancement(op, spec, m, l);

        verify();

        IBeanSpecification bs = spec.getBeanSpecification("mapBean");

        assertEquals("mapBean", bs.getPropertyName());
        assertEquals(HashMap.class.getName(), bs.getClassName());
        assertEquals(BeanLifecycle.REQUEST, bs.getLifecycle());
        assertSame(l, bs.getLocation());
        assertNull(bs.getInitializers());
    }

    private EnhancementOperation newOp(String propertyName, Class propertyType)
    {
        EnhancementOperation op = newMock(EnhancementOperation.class);
        
        expect(op.getPropertyType(propertyName)).andReturn(propertyType);

        return op;
    }

    public void testBeanClassNotSpecified()
    {
        Location l = newLocation();
        EnhancementOperation op = newOp("hashMapBean", HashMap.class);
        IComponentSpecification spec = new ComponentSpecification();

        Method m = findMethod(AnnotatedPage.class, "getHashMapBean");

        replay();

        new BeanAnnotationWorker().performEnhancement(op, spec, m, l);

        verify();

        IBeanSpecification bs = spec.getBeanSpecification("hashMapBean");

        assertEquals("hashMapBean", bs.getPropertyName());
        assertEquals(HashMap.class.getName(), bs.getClassName());
        assertEquals(BeanLifecycle.REQUEST, bs.getLifecycle());
        assertSame(l, bs.getLocation());
        assertNull(bs.getInitializers());
    }

    public void testInitializer()
    {
        EnhancementOperation op = newOp("beanWithInitializer", TargetValues.class);
        IComponentSpecification spec = new ComponentSpecification();

        Method m = findMethod(AnnotatedPage.class, "getBeanWithInitializer");

        replay();

        new BeanAnnotationWorker().performEnhancement(op, spec, m, null);

        verify();

        IBeanSpecification bs = spec.getBeanSpecification("beanWithInitializer");

        List l = bs.getInitializers();
        LightweightBeanInitializer lbi = (LightweightBeanInitializer) l.get(0);

        assertEquals("intValue=10", lbi.getPropertyName());
    }

    public void testLifecycle()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();

        Method m = findMethod(AnnotatedPage.class, "getRenderLifecycleBean");

        replay();

        new BeanAnnotationWorker().performEnhancement(op, spec, m, null);

        verify();

        IBeanSpecification bs = spec.getBeanSpecification("renderLifecycleBean");

        assertEquals(BeanLifecycle.RENDER, bs.getLifecycle());
    }
}
