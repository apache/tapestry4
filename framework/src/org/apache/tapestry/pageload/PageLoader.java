/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.pageload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Location;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.asset.ContextAsset;
import org.apache.tapestry.asset.ExternalAsset;
import org.apache.tapestry.asset.PrivateAsset;
import org.apache.tapestry.binding.ExpressionBinding;
import org.apache.tapestry.binding.FieldBinding;
import org.apache.tapestry.binding.ListenerBinding;
import org.apache.tapestry.binding.StaticBinding;
import org.apache.tapestry.binding.StringBinding;
import org.apache.tapestry.engine.IComponentClassEnhancer;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.engine.IPageSource;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.engine.ITemplateSource;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.resolver.ComponentSpecificationResolver;
import org.apache.tapestry.resource.ClasspathResourceLocation;
import org.apache.tapestry.resource.ContextResourceLocation;
import org.apache.tapestry.spec.AssetSpecification;
import org.apache.tapestry.spec.AssetType;
import org.apache.tapestry.spec.BindingSpecification;
import org.apache.tapestry.spec.BindingType;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.ContainedComponent;
import org.apache.tapestry.spec.ListenerBindingSpecification;
import org.apache.tapestry.spec.ParameterSpecification;
import org.apache.tapestry.spec.PropertySpecification;
import org.apache.tapestry.util.prop.OgnlUtils;

/**
 *  Runs the process of building the component hierarchy for an entire page.
 * 
 *  <p>
 *  This class is not threadsafe; however, {@link org.apache.tapestry.pageload.PageSource}
 *  creates a new instance of it for each page to be loaded, which bypasses
 *  multithreading issues.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class PageLoader implements IPageLoader
{
    private static final Log LOG = LogFactory.getLog(PageLoader.class);

    private IEngine _engine;
    private IResourceResolver _resolver;
    private IComponentClassEnhancer _enhancer;
    private ISpecificationSource _specificationSource;
    private IPageSource _pageSource;
    private ComponentSpecificationResolver _componentResolver;
    private List _inheritedBindingQueue = new ArrayList();

    /**
     * The locale of the application, which is also the locale
     * of the page being loaded.
     *
     **/

    private Locale _locale;

    /**
     *  Number of components instantiated, excluding the page itself.
     *
     **/

    private int _count;

    /**
     *  The recursion depth.  A page with no components is zero.  A component on
     *  a page is one.
     *
     **/

    private int _depth;

    /**
     *  The maximum depth reached while building the page.
     *
     **/

    private int _maxDepth;

    /**
     *  Used to figure relative paths for context assets.
     * 
     **/

    private IResourceLocation _servletLocation;

    private static class QueuedInheritedBinding
    {
        private IComponent _component;
        private String _containerParameterName;
        private String _parameterName;

        private QueuedInheritedBinding(
            IComponent component,
            String containerParameterName,
            String parameterName)
        {
            _component = component;
            _containerParameterName = containerParameterName;
            _parameterName = parameterName;
        }

        private void connect()
        {
            IBinding binding = _component.getContainer().getBinding(_containerParameterName);

            if (binding == null)
                return;

            _component.setBinding(_parameterName, binding);
        }
    }

    /**
     *  Constructor.
     *
     **/

    public PageLoader(IPageSource pageSource, IRequestCycle cycle)
    {
        _pageSource = pageSource;

        IEngine engine = cycle.getEngine();

        _specificationSource = engine.getSpecificationSource();
        _resolver = engine.getResourceResolver();
        _enhancer = engine.getComponentClassEnhancer();
        _componentResolver = new ComponentSpecificationResolver(cycle);

        RequestContext context = cycle.getRequestContext();

        // Need the location of the servlet within the context as the basis
        // for building relative context asset paths.

        HttpServletRequest request = context.getRequest();

        String servletPath = request.getServletPath();

        _servletLocation =
            new ContextResourceLocation(context.getServlet().getServletContext(), servletPath);
    }

    /**
     *  Binds properties of the component as defined by the container's specification.
     *
     * <p>This implementation is very simple, we will need a lot more
     *  sanity checking and eror checking in the final version.
     *
     *  @param container The containing component.  For a dynamic
     *  binding ({@link ExpressionBinding}) the property name
     *  is evaluated with the container as the root.
     *  @param component The contained component being bound.
     *  @param spec The specification of the contained component.
     *  @param contained The contained component specification (from the container's
     *  {@link ComponentSpecification}).
     *
     **/

    private void bind(IComponent container, IComponent component, ContainedComponent contained)
    {
        ComponentSpecification spec = component.getSpecification();
        boolean formalOnly = !spec.getAllowInformalParameters();

        Iterator i = contained.getBindingNames().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();

            boolean isFormal = spec.getParameter(name) != null;

            BindingSpecification bspec = contained.getBinding(name);

            // If not allowing informal parameters, check that each binding matches
            // a formal parameter.

            if (formalOnly && !isFormal)
                throw new ApplicationRuntimeException(
                    Tapestry.getString(
                        "PageLoader.formal-parameters-only",
                        component.getExtendedId(),
                        name),
                    component,
                    bspec.getLocation(),
                    null);

            // If an informal parameter that conflicts with a reserved name, then
            // skip it.

            if (!isFormal && spec.isReservedParameterName(name))
                continue;

            // The type determines how to interpret the value:
            // As a simple static String
            // As a nested property name (relative to the component)
            // As the name of a binding inherited from the containing component.
            // As the name of a public field
            // As a script for a listener

            BindingType type = bspec.getType();

            // For inherited bindings, defer until later.  This gives components
            // a chance to setup bindings from static values and expressions in the
            // template.  The order of operations is tricky, template bindings come
            // later.

            if (type == BindingType.INHERITED)
            {
                QueuedInheritedBinding queued =
                    new QueuedInheritedBinding(component, bspec.getValue(), name);
                _inheritedBindingQueue.add(queued);
                continue;
            }

            if (type == BindingType.LISTENER)
            {
                constructListenerBinding(component, name, (ListenerBindingSpecification) bspec);
                continue;
            }

            IBinding binding = convert(container, bspec);

            if (binding != null)
                component.setBinding(name, binding);
        }
    }

    /**
     *  Invoked from {@link #loadPage(String, INamespace, IRequestCycle, ComponentSpecification)}
     *  after the entire tree of components in the page has been constructed.  Recursively
     *  checks each component in the tree to ensure that
     *  all of its required parameters are bound.
     * 
     *  @since 2.4
     * 
     **/

    private void verifyRequiredParameters(IComponent component)
    {
        ComponentSpecification spec = component.getSpecification();

        Iterator i = spec.getParameterNames().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();
            ParameterSpecification parameterSpec = spec.getParameter(name);

            if (parameterSpec.isRequired() && component.getBinding(name) == null)
                throw new ApplicationRuntimeException(
                    Tapestry.getString(
                        "PageLoader.required-parameter-not-bound",
                        name,
                        component.getExtendedId()),
                    component,
                    component.getLocation(),
                    null);
        }

        Collection components = component.getComponents().values();

        if (Tapestry.size(components) == 0)
            return;

        i = components.iterator();

        while (i.hasNext())
        {
            IComponent embedded = (IComponent) i.next();

            verifyRequiredParameters(embedded);
        }
    }

    private IBinding convert(IComponent container, BindingSpecification spec)
    {
        BindingType type = spec.getType();
        Location location = spec.getLocation();
        String value = spec.getValue();

        // The most common type. 

        if (type == BindingType.DYNAMIC)
            return new ExpressionBinding(_resolver, container, value, location);

        // String bindings are new in 2.0.4.  For the momement,
        // we don't even try to cache and share them ... they
        // are most often unique within a page.

        if (type == BindingType.STRING)
            return new StringBinding(container, value, location);

        // static and field bindings are pooled.  This allows the
        // same instance to be used with many components.

        if (type == BindingType.STATIC)
            return new StaticBinding(value, location);

        // BindingType.FIELD is on the way out, it is in the
        // 1.3 DTD but not the 1.4 DTD.

        if (type == BindingType.FIELD)
            return new FieldBinding(_resolver, value, location);

        // This code is unreachable, at least until a new type
        // of binding is created.

        throw new ApplicationRuntimeException("Unexpected type: " + type + ".");
    }

    /**
     *  Construct a {@link ListenerBinding} for the component, and add it.
     * 
     *  @since 2.4
     * 
     **/

    private void constructListenerBinding(
        IComponent component,
        String bindingName,
        ListenerBindingSpecification spec)
    {
        String language = spec.getLanguage();

        // If not provided in the page or component specification, then
        // search for a default (factory default is "jython").

        if (Tapestry.isNull(language))
            language =
                _engine.getPropertySource().getPropertyValue(
                    "org.apache.tapestry.default-script-language");

        // Construct the binding.  The first parameter is the compononent
        // (not the DirectLink or Form, but the page or component containing the link or form).

        IBinding binding =
            new ListenerBinding(
                component.getContainer(),
                language,
                spec.getScript(),
                spec.getLocation());

        component.setBinding(bindingName, binding);
    }

    /**
     *  Sets up a component.  This involves:
     *  <ul>
     * <li>Instantiating any contained components.
     * <li>Add the contained components to the container.
     * <li>Setting up bindings between container and containees.
     * <li>Construct the containees recursively.
     * <li>Invoking {@link IComponent#finishLoad(IRequestCycle, IPageLoader, ComponentSpecification)}
     * </ul>
     *
     *  @param cycle the request cycle for which the page is being (initially) constructed
     *  @param page The page on which the container exists.
     *  @param container The component to be set up.
     *  @param containerSpec The specification for the container.
     *  @param the namespace of the container
     *
     **/

    private void constructComponent(
        IRequestCycle cycle,
        IPage page,
        IComponent container,
        ComponentSpecification containerSpec,
        INamespace namespace)
    {
        _depth++;
        if (_depth > _maxDepth)
            _maxDepth = _depth;

        List ids = new ArrayList(containerSpec.getComponentIds());
        int count = ids.size();

        try
        {
            for (int i = 0; i < count; i++)
            {
                String id = (String) ids.get(i);

                // Get the sub-component specification from the
                // container's specification.

                ContainedComponent contained = containerSpec.getComponent(id);

                String type = contained.getType();
                Location location = contained.getLocation();

                _componentResolver.resolve(cycle, namespace, type, location);

                ComponentSpecification componentSpecification =
                    _componentResolver.getSpecification();
                INamespace componentNamespace = _componentResolver.getNamespace();

                // Instantiate the contained component.

                IComponent component =
                    instantiateComponent(
                        page,
                        container,
                        id,
                        componentSpecification,
                        componentNamespace,
                        location);

                // Add it, by name, to the container.

                container.addComponent(component);

                // Set up any bindings in the ContainedComponent specification

                bind(container, component, contained);

                // Now construct the component recusively; it gets its chance
                // to create its subcomponents and set their bindings.

                constructComponent(
                    cycle,
                    page,
                    component,
                    componentSpecification,
                    componentNamespace);
            }

            addAssets(container, containerSpec);

            // Finish the load of the component; most components (which
            // subclass BaseComponent) load their templates here.
            // That may cause yet more components to be created, and more
            // bindings to be set, so we defer some checking until
            // later.

            container.finishLoad(cycle, this, containerSpec);

            // Finally, we create an initializer for each
            // specified property.

            createPropertyInitializers(page, container, containerSpec);
        }
        catch (ApplicationRuntimeException ex)
        {
            throw ex;
        }
        catch (RuntimeException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "PageLoader.unable-to-instantiate-component",
                    container.getExtendedId(),
                    ex.getMessage()),
                container,
                ex);
        }

        _depth--;
    }

    /**
     *  Invoked to create an implicit component (one which is defined in the
     *  containing component's template, rather that in the containing component's
     *  specification).
     * 
     *  @see org.apache.tapestry.BaseComponentTemplateLoader
     *  @since 2.4
     * 
     **/

    public IComponent createImplicitComponent(
        IRequestCycle cycle,
        IComponent container,
        String componentId,
        String componentType,
        Location location)
    {
        IPage page = container.getPage();

        _componentResolver.resolve(cycle, container.getNamespace(), componentType, location);

        INamespace componentNamespace = _componentResolver.getNamespace();
        ComponentSpecification spec = _componentResolver.getSpecification();

        IComponent result =
            instantiateComponent(page, container, componentId, spec, componentNamespace, location);

        container.addComponent(result);

        // Recusively build the component.

        constructComponent(cycle, page, result, spec, componentNamespace);

        return result;
    }

    /**
     *  Instantiates a component from its specification. We instantiate
     *  the component object, then set its specification, page, container and id.
     *
     *  @see AbstractComponent
     * 
     **/

    private IComponent instantiateComponent(
        IPage page,
        IComponent container,
        String id,
        ComponentSpecification spec,
        INamespace namespace,
        Location location)
    {
        IComponent result = null;
        String className = spec.getComponentClassName();

        if (Tapestry.isNull(className))
            className = BaseComponent.class.getName();

        Class componentClass = _enhancer.getEnhancedClass(spec, className);
        String enhancedClassName = componentClass.getName();

        try
        {
            result = (IComponent) componentClass.newInstance();

        }
        catch (ClassCastException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("PageLoader.class-not-component", enhancedClassName),
                container,
                spec.getLocation(),
                ex);
        }
        catch (Throwable ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("PageLoader.unable-to-instantiate", enhancedClassName),
                container,
                spec.getLocation(),
                ex);
        }

        if (result instanceof IPage)
            throw new ApplicationRuntimeException(
                Tapestry.getString("PageLoader.page-not-allowed", result.getExtendedId()),
                result);

        result.setNamespace(namespace);
        result.setSpecification(spec);
        result.setPage(page);
        result.setContainer(container);
        result.setId(id);
        result.setLocation(location);

        _count++;

        return result;
    }

    /**
     *  Instantitates a page from its specification.
     *
     *  @param name the unqualified, simple, name for the page
     *  @param namespace the namespace containing the page's specification
     *  @param spec the page's specification
     * 
     *  We instantiate the page object, then set its specification,
     *  names and locale.
     *
     *  @see IEngine
     *  @see ChangeObserver
     **/

    private IPage instantiatePage(String name, INamespace namespace, ComponentSpecification spec)
    {
        IPage result = null;

        String pageName = namespace.constructQualifiedName(name);
        String className = spec.getComponentClassName();
        Location location = spec.getLocation();

        if (Tapestry.isNull(className))
        {
            if (LOG.isDebugEnabled())
                LOG.debug(
                    "Page "
                        + namespace.constructQualifiedName(name)
                        + " does not specify a component class.");

            className =
                _engine.getPropertySource().getPropertyValue(
                    "org.apache.tapestry.default-page-class");

            if (className == null)
                className = BasePage.class.getName();

            if (LOG.isDebugEnabled())
                LOG.debug("Defaulting to class " + className);
        }

        Class pageClass = _enhancer.getEnhancedClass(spec, className);
        String enhancedClassName = pageClass.getName();

        try
        {
            result = (IPage) pageClass.newInstance();

            result.setNamespace(namespace);
            result.setSpecification(spec);
            result.setPageName(pageName);
            result.setPage(result);
            result.setLocale(_locale);
            result.setLocation(location);
        }
        catch (ClassCastException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("PageLoader.class-not-page", enhancedClassName),
                location,
                ex);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("PageLoader.unable-to-instantiate", enhancedClassName),
                location,
                ex);
        }

        return result;
    }

    /**
     *  Invoked by the {@link PageSource} to load a specific page.  This
     *  method is not reentrant ... the PageSource ensures that
     *  any given instance of PageLoader is loading only a single page at a time.
     *  The page is immediately attached to the {@link IEngine engine}.
     *
     *  @param name the simple (unqualified) name of the page to load
     *  @param namespace from which the page is to be loaded (used
     *  when resolving components embedded by the page)
     *  @param cycle the request cycle the page is 
     *  initially loaded for (this is used
     *  to define the locale of the new page, and provide access
     *  to the corect specification source, etc.).
     *  @param specification the specification for the page
     *
     **/

    public IPage loadPage(
        String name,
        INamespace namespace,
        IRequestCycle cycle,
        ComponentSpecification specification)
    {
        IPage page = null;

        _engine = cycle.getEngine();

        _locale = _engine.getLocale();

        _count = 0;
        _depth = 0;
        _maxDepth = 0;

        try
        {
            page = instantiatePage(name, namespace, specification);

            page.attach(_engine);

            constructComponent(cycle, page, page, specification, namespace);

            establishInheritedBindings();

            verifyRequiredParameters(page);
        }
        finally
        {
            _locale = null;
            _engine = null;
            _inheritedBindingQueue.clear();
        }

        if (LOG.isInfoEnabled())
            LOG.info(
                "Loaded page "
                    + page
                    + " with "
                    + _count
                    + " components (maximum depth "
                    + _maxDepth
                    + ")");

        return page;
    }

    private void establishInheritedBindings()
    {
        LOG.debug("Establishing inherited bindings");

        int count = _inheritedBindingQueue.size();

        for (int i = 0; i < count; i++)
        {
            QueuedInheritedBinding queued = (QueuedInheritedBinding) _inheritedBindingQueue.get(i);

            queued.connect();
        }
    }

    private void addAssets(IComponent component, ComponentSpecification specification)
    {
        List names = specification.getAssetNames();

        if (names.isEmpty())
            return;

        IResourceLocation specLocation = specification.getSpecificationLocation();

        Iterator i = names.iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();
            AssetSpecification assetSpec = specification.getAsset(name);
            IAsset asset = convert(name, component, assetSpec, specLocation);

            component.addAsset(name, asset);
        }
    }

    /**
     *  Invoked from 
     *  {@link #constructComponent(IRequestCycle, IPage, IComponent, ComponentSpecification, INamespace)}
     *  after {@link IComponent#finishLoad(IRequestCycle, IPageLoader, ComponentSpecification)}
     *  is invoked.  This iterates over any
     *  {@link org.apache.tapestry.spec.PropertySpecification}s for the component,
     *  create an initializer for each.
     * 
     **/

    private void createPropertyInitializers(
        IPage page,
        IComponent component,
        ComponentSpecification spec)
    {
        List names = spec.getPropertySpecificationNames();
        int count = names.size();

        for (int i = 0; i < count; i++)
        {
            String name = (String) names.get(i);
            PropertySpecification ps = spec.getPropertySpecification(name);

            String expression = ps.getInitialValue();
            Object initialValue = null;

            // If no initial value expression is provided, then read the current
            // property of the expression.  This may be null, or may be
            // a value set in finishLoad() (via an abstract accessor).

            try
            {
                if (Tapestry.isNull(expression))
                {
                    initialValue = OgnlUtils.get(name, _resolver, component);
                }
                else
                {
                    // Evaluate the expression and update the property.

                    initialValue = OgnlUtils.get(expression, _resolver, component);

                    OgnlUtils.set(name, _resolver, component, initialValue);
                }
            }
            catch (Exception ex)
            {
                throw new ApplicationRuntimeException(
                    Tapestry.getString(
                        "PageLoader.unable-to-initialize-property",
                        name,
                        component,
                        ex.getMessage()),
                    ps.getLocation(),
                    ex);
            }

            PageDetachListener initializer =
                new PropertyInitializer(_resolver, component, name, initialValue);

            page.addPageDetachListener(initializer);
        }

    }

    /**
     *  Builds an instance of {@link IAsset} from the specification.
     *
     **/

    private IAsset convert(
        String assetName,
        IComponent component,
        AssetSpecification spec,
        IResourceLocation specificationLocation)
    {
        AssetType type = spec.getType();
        String path = spec.getPath();
        Location location = spec.getLocation();

        if (type == AssetType.EXTERNAL)
            return new ExternalAsset(path, location);

        if (type == AssetType.PRIVATE)
            return new PrivateAsset(
                (ClasspathResourceLocation) findAsset(assetName,
                    component,
                    specificationLocation,
                    path,
                    location),
                location);

        return new ContextAsset(
            (ContextResourceLocation) findAsset(assetName,
                component,
                _servletLocation,
                path,
                location),
            location);
    }

    private IResourceLocation findAsset(
        String assetName,
        IComponent component,
        IResourceLocation baseLocation,
        String path,
        Location location)
    {
        IResourceLocation assetLocation = baseLocation.getRelativeLocation(path);
        IResourceLocation localizedLocation = assetLocation.getLocalization(_locale);

        if (localizedLocation == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "PageLoader.missing-asset",
                    assetName,
                    component.getExtendedId(),
                    assetLocation),
                component,
                location,
                null);

        return localizedLocation;
    }

    public IEngine getEngine()
    {
        return _engine;
    }

    public ITemplateSource getTemplateSource()
    {
        return _engine.getTemplateSource();
    }
}