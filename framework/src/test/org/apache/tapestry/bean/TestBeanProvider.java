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
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.ClassFinder;
import org.apache.tapestry.services.Infrastructure;
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
        setReturnValue(spec, className);
    }

    public void testResolveClassFailure()
    {
        ClassResolver resolver = newResolver();
        IPage page = newPage();
        IComponent component = newComponent();
        ClassFinder finder = newClassFinder();

        trainForConstructor(page, component, resolver, finder);

        replayControls();

        BeanProvider bp = new BeanProvider(component);

        verifyControls();

        IBeanSpecification bs = newBeanSpec();

        trainGetClassName(bs, "org.foo.Bar");

        trainFindClass(finder, "org.foo.Bar", null);

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
                    "Unable to instantiate bean 'wilma' of component Fred/barney: Unable to find class org.foo.Bar within package list 'mypackage'.",
                    ex.getMessage());
            assertSame(component, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verifyControls();
    }

    public void testInstantiateBeanFailure()
    {
        ClassResolver resolver = newResolver();
        IPage page = newPage();
        IComponent component = newComponent();
        ClassFinder finder = newClassFinder();

        trainForConstructor(page, component, resolver, finder);

        replayControls();

        BeanProvider bp = new BeanProvider(component);

        verifyControls();

        IBeanSpecification bs = newBeanSpec();

        trainGetClassName(bs, "org.foo.Bar");

        trainFindClass(finder, "org.foo.Bar", InstantiateFailureBean.class);

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
                    "Unable to instantiate bean 'wilma' (for component Fred/barney) as class org.apache.tapestry.bean.InstantiateFailureBean: Boom!",
                    ex.getMessage());
            assertSame(component, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verifyControls();
    }

    private void trainForConstructor(IPage page, IComponent component, ClassResolver resolver,
            ClassFinder classFinder)
    {
        IRequestCycle cycle = newCycle();
        Infrastructure infrastructure = (Infrastructure) newMock(Infrastructure.class);
        INamespace namespace = (INamespace) newMock(INamespace.class);

        trainGetPage(component, page);

        page.getRequestCycle();
        setReturnValue(page, cycle);

        cycle.getInfrastructure();
        setReturnValue(cycle, infrastructure);

        infrastructure.getClassResolver();
        setReturnValue(infrastructure, resolver);

        component.getNamespace();
        setReturnValue(component, namespace);

        namespace.getPropertyValue("org.apache.tapestry.bean-class-packages");
        setReturnValue(namespace, "mypackage");

        infrastructure.getClassFinder();
        setReturnValue(infrastructure, classFinder);
    }

    protected ClassFinder newClassFinder()
    {
        return (ClassFinder) newMock(ClassFinder.class);
    }

    private ClassResolver newResolver()
    {
        return (ClassResolver) newMock(ClassResolver.class);
    }

    public void testInitializeFailure()
    {
        ClassResolver resolver = new DefaultClassResolver();
        IPage page = newPage();
        IComponent component = newComponent();
        ClassFinder finder = newClassFinder();

        trainForConstructor(page, component, resolver, finder);

        replayControls();

        BeanProvider bp = new BeanProvider(component);

        verifyControls();

        String className = TargetBean.class.getName();

        trainFindClass(finder, className, TargetBean.class);

        IBeanSpecification spec = new BeanSpecification();
        spec.setClassName(className);

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

    private void trainFindClass(ClassFinder finder, String className, Class clazz)
    {
        finder.findClass("mypackage", className);
        setReturnValue(finder, clazz);
    }
}
