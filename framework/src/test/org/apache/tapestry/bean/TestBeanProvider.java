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

package org.apache.tapestry.bean;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Location;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBeanProvider;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.spec.BeanSpecification;
import org.apache.tapestry.spec.IBeanSpecification;

/**
 * Tests for {@link org.apache.tapestry.bean.BeanProvider} (mostly new features added in release
 * 4.0).
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestBeanProvider extends BaseComponentTestCase
{
    public static class BeanInitializerFixture extends AbstractBeanInitializer
    {
        private final RuntimeException _exception;

        public BeanInitializerFixture(String propertyName, RuntimeException exception)
        {
            setPropertyName(propertyName);
            _exception = exception;
        }

        public void setBeanProperty(IBeanProvider provider, Object bean)
        {
            throw _exception;
        }

    }

    protected IBeanSpecification newBeanSpec()
    {
        return (IBeanSpecification) newMock(IBeanSpecification.class);
    }

    protected void trainGetClassName(IBeanSpecification spec, String className)
    {
        spec.getClassName();
        getControl(spec).setReturnValue(className);
    }

    public void testInstantiateFailure()
    {
        ClassResolver resolver = newResolver();
        IEngine engine = newEngine(resolver);
        IPage page = newPage();
        IComponent component = newComponent();

        trainGetPage(component, page);
        trainGetEngine(page, engine);

        replayControls();

        BeanProvider bp = new BeanProvider(component);

        verifyControls();

        IBeanSpecification bs = newBeanSpec();

        trainGetClassName(bs, "org.foo.Bar");

        Throwable t = new RuntimeException("Poof!");

        resolver.findClass("org.foo.Bar");
        getControl(resolver).setThrowable(t);

        trainGetExtendedId(component, "Fred/barney");

        Location l = newLocation();

        trainGetLocation(bs, l);

        replayControls();

        try
        {
            bp.instantiateBean("wilma", bs);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Unable to instantiate bean 'wilma' (for component Fred/barney) as class org.foo.Bar: Poof!",
                    ex.getMessage());
            assertSame(component, ex.getComponent());
            assertSame(l, ex.getLocation());
            assertSame(t, ex.getRootCause());
        }

        verifyControls();
    }

    private ClassResolver newResolver()
    {
        return (ClassResolver) newMock(ClassResolver.class);
    }

    public void testInitializeFailure()
    {
        ClassResolver resolver = new DefaultClassResolver();
        IEngine engine = newEngine(resolver);
        IPage page = newPage();
        IComponent component = newComponent();

        trainGetPage(component, page);
        trainGetEngine(page, engine);

        replayControls();

        BeanProvider bp = new BeanProvider(component);

        verifyControls();

        IBeanSpecification spec = new BeanSpecification();
        spec.setClassName(TargetBean.class.getName());

        RuntimeException t = new RuntimeException("Blat!");

        Location l = newLocation();

        IBeanInitializer bi = new BeanInitializerFixture("foo", t);
        bi.setLocation(l);

        spec.addInitializer(bi);

        trainGetExtendedId(component, "Fred/barney");

        replayControls();

        try
        {
            bp.instantiateBean("wilma", spec);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Error initializing property foo of bean 'wilma' (of component Fred/barney): Blat!",
                    ex.getMessage());
            assertSame(TargetBean.class, ex.getComponent().getClass());
            assertSame(l, ex.getLocation());
            assertSame(t, ex.getRootCause());
        }

    }
}
