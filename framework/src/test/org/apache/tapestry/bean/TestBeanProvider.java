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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

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
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.bean.BeanProvider} (mostly new features added in release
 * 4.0).
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
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
        return newMock(IBeanSpecification.class);
    }

    protected void trainGetClassName(IBeanSpecification spec, String className)
    {
        expect(spec.getClassName()).andReturn(className);
    }

    public void testResolveClassFailure()
    {
        ClassResolver resolver = newResolver();
        IPage page = newPage();
        IComponent component = newComponent();
        ClassFinder finder = newClassFinder();

        trainForConstructor(page, component, resolver, finder);

        replay();

        BeanProvider bp = new BeanProvider(component);

        verify();

        IBeanSpecification bs = newBeanSpec();

        trainGetClassName(bs, "org.foo.Bar");

        trainFindClass(finder, "org.foo.Bar", null);

        trainGetExtendedId(component, "Fred/barney");

        Location l = newLocation();

        trainGetLocation(bs, l);

        replay();

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

        verify();
    }

    public void testInstantiateBeanFailure()
    {
        ClassResolver resolver = newResolver();
        IPage page = newPage();
        IComponent component = newComponent();
        ClassFinder finder = newClassFinder();

        trainForConstructor(page, component, resolver, finder);

        replay();

        BeanProvider bp = new BeanProvider(component);

        verify();

        IBeanSpecification bs = newBeanSpec();

        trainGetClassName(bs, "org.foo.Bar");

        trainFindClass(finder, "org.foo.Bar", InstantiateFailureBean.class);

        trainGetExtendedId(component, "Fred/barney");

        Location l = newLocation();

        trainGetLocation(bs, l);

        replay();

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

        verify();
    }

    private void trainForConstructor(IPage page, IComponent component, ClassResolver resolver,
            ClassFinder classFinder)
    {
        IRequestCycle cycle = newCycle();
        Infrastructure infrastructure = newMock(Infrastructure.class);
        INamespace namespace = newMock(INamespace.class);

        trainGetPage(component, page);

        expect(page.getRequestCycle()).andReturn(cycle);

        expect(cycle.getInfrastructure()).andReturn(infrastructure);

        expect(infrastructure.getClassResolver()).andReturn(resolver);

        expect(component.getNamespace()).andReturn(namespace);

        expect(namespace.getPropertyValue("org.apache.tapestry.bean-class-packages"))
        .andReturn("mypackage");

        expect(infrastructure.getClassFinder()).andReturn(classFinder);
    }

    protected ClassFinder newClassFinder()
    {
        return newMock(ClassFinder.class);
    }

    private ClassResolver newResolver()
    {
        return newMock(ClassResolver.class);
    }

    public void testInitializeFailure()
    {
        ClassResolver resolver = new DefaultClassResolver();
        IPage page = newPage();
        IComponent component = newComponent();
        ClassFinder finder = newClassFinder();

        trainForConstructor(page, component, resolver, finder);

        replay();

        BeanProvider bp = new BeanProvider(component);

        verify();

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

        replay();

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
        expect(finder.findClass("mypackage", className)).andReturn(clazz);
    }
}
