//  Copyright 2004 The Apache Software Foundation
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IBeanProvider;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.spec.BeanLifecycle;
import org.apache.tapestry.spec.IBeanSpecification;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 *  Basic implementation of the {@link IBeanProvider} interface.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.4
 **/

public class BeanProvider implements IBeanProvider, PageDetachListener, PageRenderListener
{
    private static final Log LOG = LogFactory.getLog(BeanProvider.class);

    /**
     *  Indicates whether this instance has been registered with its
     *  page as a PageDetachListener.  Registration only occurs
     *  the first time a bean with lifecycle REQUEST is instantiated.
     *
     **/

    private boolean _registeredForDetach = false;

    /**
     *  Indicates whether this instance has been registered as a render
     *  listener with the page.
     * 
     **/

    private boolean _registeredForRender = false;

    /**
     *  The component for which beans are being created and tracked.
     *
     **/

    private IComponent _component;

    /**
     *  Used for instantiating classes.
     *
     **/

    private IResourceResolver _resolver;

    /**
     *  Map of beans, keyed on name.
     *
     **/

    private Map _beans;

    /**
     *  Set of bean names provided by this provider.
     * 
     *  @since 2.2
     * 
     **/

    private Set _beanNames;

    public BeanProvider(IComponent component)
    {
        this._component = component;
        IEngine engine = component.getPage().getEngine();
        _resolver = engine.getResourceResolver();

        if (LOG.isDebugEnabled())
            LOG.debug("Created BeanProvider for " + component);

    }

    /** @since 1.0.6 **/

    public Collection getBeanNames()
    {
        if (_beanNames == null)
        {
            Collection c = _component.getSpecification().getBeanNames();

            if (c == null || c.isEmpty())
                _beanNames = Collections.EMPTY_SET;
            else
                _beanNames = Collections.unmodifiableSet(new HashSet(c));
        }

        return _beanNames;
    }

    /**
     *  @since 1.0.5
     *
     **/

    public IComponent getComponent()
    {
        return _component;
    }

    public Object getBean(String name)
    {
        Object bean = null;

        if (_beans != null)
            bean = _beans.get(name);

        if (bean != null)
            return bean;

        IBeanSpecification spec = _component.getSpecification().getBeanSpecification(name);

        if (spec == null)
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "BeanProvider.bean-not-defined",
                    _component.getExtendedId(),
                    name));

        bean = instantiateBean(name, spec);

        BeanLifecycle lifecycle = spec.getLifecycle();

        if (lifecycle == BeanLifecycle.NONE)
            return bean;

        if (_beans == null)
            _beans = new HashMap();

        _beans.put(name, bean);

        // The first time in a request that a REQUEST lifecycle bean is created,
        // register with the page to be notified at the end of the
        // request cycle.

        if (lifecycle == BeanLifecycle.REQUEST && !_registeredForDetach)
        {
            _component.getPage().addPageDetachListener(this);
            _registeredForDetach = true;
        }

        if (lifecycle == BeanLifecycle.RENDER && !_registeredForRender)
        {
            _component.getPage().addPageRenderListener(this);
            _registeredForRender = true;
        }

        // No need to register if a PAGE lifecycle bean; those can stick around
        // forever.

        return bean;
    }

    private Object instantiateBean(String beanName, IBeanSpecification spec)
    {
        String className = spec.getClassName();
        Object bean = null;

        if (LOG.isDebugEnabled())
            LOG.debug("Instantiating instance of " + className);

        // Do it the hard way!

        try
        {
            Class beanClass = _resolver.findClass(className);

            bean = beanClass.newInstance();
        }
        catch (Exception ex)
        {

            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "BeanProvider.instantiation-error",
                    new Object[] {
                        beanName,
                        _component.getExtendedId(),
                        className,
                        ex.getMessage()}),
                spec.getLocation(),
                ex);
        }

        // OK, have the bean, have to initialize it.

        List initializers = spec.getInitializers();

        if (initializers == null)
            return bean;

        Iterator i = initializers.iterator();
        while (i.hasNext())
        {
            IBeanInitializer iz = (IBeanInitializer) i.next();

            if (LOG.isDebugEnabled())
                LOG.debug("Initializing property " + iz.getPropertyName());

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
        removeBeans(BeanLifecycle.REQUEST);
    }

    /**
     *  Removes any beans with the specified lifecycle.
     * 
     *  @since 2.2
     * 
     **/

    private void removeBeans(BeanLifecycle lifecycle)
    {
        if (_beans == null)
            return;

        IComponentSpecification spec = null;

        Iterator i = _beans.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();
            String name = (String) e.getKey();

            if (spec == null)
                spec = _component.getSpecification();

            IBeanSpecification s = spec.getBeanSpecification(name);

            if (s.getLifecycle() == lifecycle)
            {
                Object bean = e.getValue();

                if (LOG.isDebugEnabled())
                    LOG.debug("Removing " + lifecycle.getName() + " bean " + name + ": " + bean);

                i.remove();
            }
        }
    }

    /** @since 1.0.8 **/

    public IResourceResolver getResourceResolver()
    {
        return _resolver;
    }

    /** @since 2.2 **/

    public void pageBeginRender(PageEvent event)
    {
    }

    /** @since 2.2 **/

    public void pageEndRender(PageEvent event)
    {
        removeBeans(BeanLifecycle.RENDER);
    }

    /** @since 2.2 **/

    public boolean canProvideBean(String name)
    {
        return getBeanNames().contains(name);
    }

}