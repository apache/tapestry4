//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IBeanProvider;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.spec.BeanLifecycle;
import net.sf.tapestry.spec.BeanSpecification;
import net.sf.tapestry.spec.ComponentSpecification;

/**
 *  Basic implementation of the {@link IBeanProvider} interface.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.4
 **/

public class BeanProvider implements IBeanProvider, PageDetachListener
{
    private static final Category CAT = Category.getInstance(BeanProvider.class);

    /**
     *  Indicates whether this instance has been registered with its
     *  page as a PageDetachListener.  Registration only occurs
     *  the first time a bean with lifecycle REQUEST is instantiated.
     *
     **/

    private boolean registered = false;

    /**
     *  The component for which beans are being created and tracked.
     *
     **/

    private IComponent component;

    /**
     *  Used for instantiating classes.
     *
     **/

    private IResourceResolver resolver;

    /**
     *  Map of beans, keyed on name.
     *
     **/

    private Map beans;

    public BeanProvider(IComponent component)
    {
        this.component = component;
        IEngine engine = component.getPage().getEngine();
        resolver = engine.getResourceResolver();

        if (CAT.isDebugEnabled())
            CAT.debug("Created BeanProvider for " + component);

    }

    /** @since 1.0.6 **/

    public Collection getBeanNames()
    {
        return component.getSpecification().getBeanNames();
    }

    /**
     *  @since 1.0.5
     *
     **/

    public IComponent getComponent()
    {
        return component;
    }

    public Object getBean(String name)
    {
        Object bean = null;

        if (beans != null)
            bean = beans.get(name);

        if (bean != null)
            return bean;

        BeanSpecification spec = component.getSpecification().getBeanSpecification(name);

        if (spec == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("BeanProvider.bean-not-defined", component.getExtendedId(), name));

        bean = instantiateBean(spec);

        BeanLifecycle lifecycle = spec.getLifecycle();

        if (lifecycle == BeanLifecycle.NONE)
            return bean;

        if (beans == null)
            beans = new HashMap();

        beans.put(name, bean);

        // The first time in a request that a REQUEST lifecycle bean is created,
        // register with the page to be notified at the end of the
        // request cycle.

        if (lifecycle == BeanLifecycle.REQUEST && !registered)
        {
            component.getPage().addPageDetachListener(this);
            registered = true;
        }

        // No need to register if a PAGE lifecycle bean; those can stick around
        // forever.

        return bean;
    }

    private Object instantiateBean(BeanSpecification spec)
    {
        String className = spec.getClassName();
        Object bean = null;

        if (CAT.isDebugEnabled())
            CAT.debug("Instantiating instance of " + className);

        // Do it the hard way!

        Class beanClass = resolver.findClass(className);

        try
        {
            bean = beanClass.newInstance();
        }
        catch (IllegalAccessException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
        catch (InstantiationException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }

        // OK, have the bean, have to initialize it.

        List initializers = spec.getInitializers();

        if (initializers == null)
            return bean;

        Iterator i = initializers.iterator();
        while (i.hasNext())
        {
            IBeanInitializer iz = (IBeanInitializer) i.next();

            if (CAT.isDebugEnabled())
                CAT.debug("Initializing property " + iz.getPropertyName());

            iz.setBeanProperty(this, bean);
        }

        return bean;
    }

    /**
     *  Removes all beans with the REQUEST lifecycle.  Beans with
     *  the PAGE lifecycle stick around, and beans with no lifecycle
     *  were never stored in the first place.
     *
     **/

    public void pageDetached(PageEvent event)
    {
        ComponentSpecification spec = null;

        if (beans == null)
            return;

        Iterator i = beans.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();
            String name = (String) e.getKey();

            if (spec == null)
                spec = component.getSpecification();

            BeanSpecification s = spec.getBeanSpecification(name);

            // If the bean has the REQUEST lifecycle, then remove
            // the key and bean from the map of beans.  Beans with
            // other lifecycles (i.e., page)
            // will stick around.

            if (s.getLifecycle() == BeanLifecycle.REQUEST)
            {
                Object bean = e.getValue();

                if (CAT.isDebugEnabled())
                    CAT.debug("Removing REQUEST bean " + name + ": " + bean);

                i.remove();
            }
        }
    }

    /** @since 1.0.8 **/

    public IResourceResolver getResourceResolver()
    {
        return resolver;
    }
}