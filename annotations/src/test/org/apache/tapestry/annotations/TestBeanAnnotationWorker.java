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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.bean.LightweightBeanInitializer;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.spec.BeanLifecycle;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IBeanSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.annotations.BeanAnnotationWorker}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */

public class TestBeanAnnotationWorker extends BaseAnnotationTestCase
{
    public void testBeanClassSpecified()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();

        MockControl beanc = newControl(Bean.class);
        Bean bean = (Bean) beanc.getMock();

        Method m = findMethod(Target.class, "getMapBean");

        trainBeanClass(beanc, bean, HashMap.class);
        trainLifecycle(beanc, bean, Lifecycle.RENDER);
        trainInitializer(beanc, bean, "");

        replayControls();

        new BeanAnnotationWorker().performEnhancement(op, spec, bean, m);

        verifyControls();

        IBeanSpecification bs = spec.getBeanSpecification("mapBean");

        assertEquals("mapBean", bs.getPropertyName());
        assertEquals(HashMap.class.getName(), bs.getClassName());
        assertEquals(BeanLifecycle.RENDER, bs.getLifecycle());
        assertNull(bs.getLocation());
        assertNull(bs.getInitializers());
    }

    public void testBeanClassNotSpecified()
    {
        MockControl opc = newControl(EnhancementOperation.class);
        EnhancementOperation op = (EnhancementOperation) opc.getMock();

        IComponentSpecification spec = new ComponentSpecification();

        MockControl beanc = newControl(Bean.class);
        Bean bean = (Bean) beanc.getMock();

        Method m = findMethod(Target.class, "getArrayListBean");

        trainBeanClass(beanc, bean, Object.class);

        op.getPropertyType("arrayListBean");
        opc.setReturnValue(ArrayList.class);

        trainLifecycle(beanc, bean, Lifecycle.RENDER);
        trainInitializer(beanc, bean, "");

        replayControls();

        new BeanAnnotationWorker().performEnhancement(op, spec, bean, m);

        verifyControls();

        IBeanSpecification bs = spec.getBeanSpecification("arrayListBean");

        assertEquals("arrayListBean", bs.getPropertyName());
        assertEquals(ArrayList.class.getName(), bs.getClassName());
        assertEquals(BeanLifecycle.RENDER, bs.getLifecycle());
        assertNull(bs.getLocation());
        assertNull(bs.getInitializers());
    }

    public void testInitializer()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();

        MockControl beanc = newControl(Bean.class);
        Bean bean = (Bean) beanc.getMock();

        Method m = findMethod(Target.class, "getMapBean");

        trainBeanClass(beanc, bean, HashMap.class);
        trainLifecycle(beanc, bean, Lifecycle.PAGE);
        trainInitializer(beanc, bean, "foo,bar=baz");

        replayControls();

        new BeanAnnotationWorker().performEnhancement(op, spec, bean, m);

        verifyControls();

        IBeanSpecification bs = spec.getBeanSpecification("mapBean");

        assertEquals("mapBean", bs.getPropertyName());
        assertEquals(BeanLifecycle.PAGE, bs.getLifecycle());
        assertNull(bs.getLocation());

        List l = bs.getInitializers();
        LightweightBeanInitializer lbi = (LightweightBeanInitializer) l.get(0);

        assertEquals("foo,bar=baz", lbi.getPropertyName());
    }

    private void trainInitializer(MockControl control, Bean bean, String initializer)
    {
        bean.initializer();
        control.setReturnValue(initializer);
    }

    private void trainLifecycle(MockControl control, Bean bean, Lifecycle lifecycle)
    {
        bean.lifecycle();
        control.setReturnValue(lifecycle);
    }

    private void trainBeanClass(MockControl beanc, Bean bean, Class beanClass)
    {
        bean.value();
        beanc.setReturnValue(beanClass);
    }
}
