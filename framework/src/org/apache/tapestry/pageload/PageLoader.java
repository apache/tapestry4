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

package org.apache.tapestry.pageload;

import java.util.ArrayList;
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
import org.apache.tapestry.ILocation;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IResourceResolver;
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
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.engine.ITemplateSource;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.resolver.ComponentSpecificationResolver;
import org.apache.tapestry.resource.ClasspathResourceLocation;
import org.apache.tapestry.resource.ContextResourceLocation;
import org.apache.tapestry.spec.AssetType;
import org.apache.tapestry.spec.BindingType;
import org.apache.tapestry.spec.IAssetSpecification;
import org.apache.tapestry.spec.IBindingSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;
import org.apache.tapestry.spec.IListenerBindingSpecification;
import org.apache.tapestry.spec.IPropertySpecification;

/**
 *  Runs the process of building the component hierarchy for an entire page.
 * 
 *  <p>
 *  This class is not threadsafe; however, {@link org.apache.tapestry.pageload.PageSource}
 *  creates a new instance of it for each page to be loaded, which bypasses
 *  multithreading issues.
 *
 *  @author Howard Lewis Ship
 * 
 **/

public class PageLoader implements IPageLoader
{
    private static final Log LOG = LogFactory.getLog(PageLoader.class);

    private IEngine _engine;
    private IResourceResolver _resolver;
    private IComponentClassEnhancer _enhancer;
    private ISpecificationSource _specificationSource;
    private ComponentSpecificationResolver _componentResolver;
    private List _inheritedBindingQueue = new ArrayList();
    private List _propertyInitializers = new ArrayList();
    private ComponentTreeWalker _establishDefaultParameterValuesWalker;
    private ComponentTreeWalker _verifyRequiredParametersWalker;

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

    private static interface IQueuedInheritedBinding
    {
        void connect();
    }

    private static class QueuedInheritedBinding implements IQueuedInheritedBinding
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

        public void connect()
        {
            IBinding binding = _component.getContainer().getBinding(_containerParameterName);

            if (binding == null)
                return;

            _component.setBinding(_parameterName, binding);
        }
    }

    private static class QueuedInheritInformalBindings implements IQueuedInheritedBinding
    {
        private IComponent _component;

        private QueuedInheritInformalBindings(IComponent component)
        {
            _component = component;
        }

        public void connect()
        {

            IComponent container = _component.getContainer();

            for (Iterator it = container.getBindingNames().iterator(); it.hasNext();)
            {
                String bindingName = (String) it.next();
                connectInformalBinding(container, _component, bindingName);
            }
        }

        private void connectInformalBinding(
            IComponent container,
            IComponent component,
            String bindingName)
        {
            IComponentSpecification componentSpec = component.getSpecification();
            IComponentSpecification containerSpec = container.getSpecification();

            // check if binding already exists in the component
            if (component.getBinding(bindingName) != null)
                return;

            // check if parameter is informal for the component
            if (componentSpec.getParameter(bindingName) != null
                || componentSpec.isReservedParameterName(bindingName))
                return;

            // check if parameter is informal for the container
            if (containerSpec.getParameter(bindingName) != null
                || containerSpec.isReservedParameterName(bindingName))
                return;

            // if everything passes, establish binding
            IBinding binding = container.getBinding(bindingName);
            component.setBinding(bindingName, binding);
        }
    }

    /**
     *  Constructor.
     *
     **/

    public PageLoader(IRequestCycle cycle)
    {
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

        // Create the mechanisms for walking the component tree when it is complete
        IComponentVisitor verifyRequiredParametersVisitor = new VerifyRequiredParametersVisitor();
        _verifyRequiredParametersWalker = new ComponentTreeWalker(new IComponentVisitor[] { verifyRequiredParametersVisitor });

        IComponentVisitor establishDefaultParameterValuesVisitor =
            new EstablishDefaultParameterValuesVisitor(_resolver);
        _establishDefaultParameterValuesWalker = new ComponentTreeWalker(new IComponentVisitor[] { establishDefaultParameterValuesVisitor });
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
     *  {@link IComponentSpecification}).
     *
     **/

    private void bind(IComponent container, IComponent component, IContainedComponent contained)
    {
        IComponentSpecification spec = component.getSpecification();
        boolean formalOnly = !spec.getAllowInformalParameters();

        IComponentSpecification containerSpec = container.getSpecification();
        boolean containerFormalOnly = !containerSpec.getAllowInformalParameters();

        if (contained.getInheritInformalParameters())
        {
            if (formalOnly)
                throw new ApplicationRuntimeException(
                    Tapestry.format(
                        "PageLoader.inherit-informal-invalid-component-formal-only",
                        component.getExtendedId()),
                    component,
                    contained.getLocation(),
                    null);

            if (containerFormalOnly)
                throw new ApplicationRuntimeException(
                    Tapestry.format(
                        "PageLoader.inherit-informal-invalid-container-formal-only",
                        container.getExtendedId(),
                        component.getExtendedId()),
                    component,
                    contained.getLocation(),
                    null);

            IQueuedInheritedBinding queued = new QueuedInheritInformalBindings(component);
            _inheritedBindingQueue.add(queued);
        }

        Iterator i = contained.getBindingNames().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();

            boolean isFormal = spec.getParameter(name) != null;

            IBindingSpecification bspec = contained.getBinding(name);

            // If not allowing informal parameters, check that each binding matches
            // a formal parameter.

            if (formalOnly && !isFormal)
                throw new ApplicationRuntimeException(
                    Tapestry.format(
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
                constructListenerBinding(component, name, (IListenerBindingSpecification) bspec);
                continue;
            }

            IBinding binding = convert(container, bspec);

            if (binding != null)
                component.setBinding(name, binding);
        }
    }

    private IBinding convert(IComponent container, IBindingSpecification spec)
    {
        BindingType type = spec.getType();
        ILocation location = spec.getLocation();
        String value = spec.getValue();

        // The most common type. 
        // TODO These bindings should be created somehow using the SpecFactory in SpecificationParser
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
     *  @since 3.0
     * 
     **/

    private void constructListenerBinding(
        IComponent component,
        String bindingName,
        IListenerBindingSpecification spec)
    {
        String language = spec.getLanguage();

        // If not provided in the page or component specification, then
        // search for a default (factory default is "jython").

        if (Tapestry.isBlank(language))
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
     * <li>Invoking {@link IComponent#finishLoad(IRequestCycle, IPageLoader, IComponentSpecification)}
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
        IComponentSpecification containerSpec,
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

                IContainedComponent contained = containerSpec.getComponent(id);

                String type = contained.getType();
                ILocation location = contained.getLocation();

                _componentResolver.resolve(cycle, namespace, type, location);

                IComponentSpecification componentSpecification =
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

                // Set up any bindings in the IContainedComponent specification

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
                Tapestry.format(
                    "PageLoader.unable-to-instantiate-component",
                    container.getExtendedId(),
                    ex.getMessage()),
                container,
                null,
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
     *  @since 3.0
     * 
     **/

    public IComponent createImplicitComponent(
        IRequestCycle cycle,
        IComponent container,
        String componentId,
        String componentType,
        ILocation location)
    {
        IPage page = container.getPage();

        _componentResolver.resolve(cycle, container.getNamespace(), componentType, location);

        INamespace componentNamespace = _componentResolver.getNamespace();
        IComponentSpecification spec = _componentResolver.getSpecification();

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
        IComponentSpecification spec,
        INamespace namespace,
        ILocation location)
    {
        IComponent result = null;
        String className = spec.getComponentClassName();

        if (Tapestry.isBlank(className))
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
                Tapestry.format("PageLoader.class-not-component", enhancedClassName),
                container,
                spec.getLocation(),
                ex);
        }
        catch (Throwable ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("PageLoader.unable-to-instantiate", enhancedClassName),
                container,
                spec.getLocation(),
                ex);
        }

        if (result instanceof IPage)
            throw new ApplicationRuntimeException(
                Tapestry.format("PageLoader.page-not-allowed", result.getExtendedId()),
                result,
                null,
                null);

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

    private IPage instantiatePage(String name, INamespace namespace, IComponentSpecification spec)
    {
        IPage result = null;

        String pageName = namespace.constructQualifiedName(name);
        String className = spec.getComponentClassName();
        ILocation location = spec.getLocation();

        if (Tapestry.isBlank(className))
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
                Tapestry.format("PageLoader.class-not-page", enhancedClassName),
                location,
                ex);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("PageLoader.unable-to-instantiate", enhancedClassName),
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
        IComponentSpecification specification)
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
            
            // As of 3.0.1, this is done now, rather than after constructing the page and its 
            // components.
            
            page.setRequestCycle(cycle);

            constructComponent(cycle, page, page, specification, namespace);

            // Walk through the complete component tree to set up the default parameter values.
            _establishDefaultParameterValuesWalker.walkComponentTree(page);

            establishInheritedBindings();

            // Walk through the complete component tree to ensure that required parameters are bound 
            _verifyRequiredParametersWalker.walkComponentTree(page);
            
            establishDefaultPropertyValues();
        }
        finally
        {
            _locale = null;
            _engine = null;
            _inheritedBindingQueue.clear();
            _propertyInitializers.clear();
        }

        if (LOG.isDebugEnabled())
            LOG.debug(
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
            IQueuedInheritedBinding queued =
                (IQueuedInheritedBinding) _inheritedBindingQueue.get(i);

            queued.connect();
        }
    }
    
    private void establishDefaultPropertyValues()
    {
        LOG.debug("Setting default property values");

        int count = _propertyInitializers.size();

        for (int i = 0; i < count; i++)
        {
            PageDetachListener initializer =
                (PageDetachListener) _propertyInitializers.get(i);

            initializer.pageDetached(null);
        }
    }

    private void addAssets(IComponent component, IComponentSpecification specification)
    {
        List names = specification.getAssetNames();

        if (names.isEmpty())
            return;

        IResourceLocation specLocation = specification.getSpecificationLocation();

        Iterator i = names.iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();
            IAssetSpecification assetSpec = specification.getAsset(name);
            IAsset asset = convert(name, component, assetSpec, specLocation);

            component.addAsset(name, asset);
        }
    }

    /**
     *  Invoked from 
     *  {@link #constructComponent(IRequestCycle, IPage, IComponent, IComponentSpecification, INamespace)}
     *  after {@link IComponent#finishLoad(IRequestCycle, IPageLoader, IComponentSpecification)}
     *  is invoked.  This iterates over any
     *  {@link org.apache.tapestry.spec.IPropertySpecification}s for the component,
     *  create an initializer for each.
     * 
     **/

    private void createPropertyInitializers(
        IPage page,
        IComponent component,
        IComponentSpecification spec)
    {
        List names = spec.getPropertySpecificationNames();
        int count = names.size();

        for (int i = 0; i < count; i++)
        {
            String name = (String) names.get(i);
            IPropertySpecification ps = spec.getPropertySpecification(name);
            String expression = ps.getInitialValue();

            PageDetachListener initializer =
                new PropertyInitializer(_resolver, component, name, expression, ps.getLocation());

            _propertyInitializers.add(initializer);
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
        IAssetSpecification spec,
        IResourceLocation specificationLocation)
    {
        AssetType type = spec.getType();
        String path = spec.getPath();
        ILocation location = spec.getLocation();

        if (type == AssetType.EXTERNAL)
            return new ExternalAsset(path, location);

        if (type == AssetType.PRIVATE)
        {
            IResourceLocation baseLocation = specificationLocation;

            // Fudge a special case for private assets with complete paths.  The specificationLocation
            // can't be used because it is often a ContextResourceLocation,
            // not a ClasspathResourceLocation.

            if (path.startsWith("/"))
            {
                baseLocation = new ClasspathResourceLocation(_resolver, "/");
                path = path.substring(1);
            }

            return new PrivateAsset(
                (ClasspathResourceLocation) findAsset(assetName,
                    component,
                    baseLocation,
                    path,
                    location),
                location);
        }

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
        ILocation location)
    {
        IResourceLocation assetLocation = baseLocation.getRelativeLocation(path);
        IResourceLocation localizedLocation = assetLocation.getLocalization(_locale);

        if (localizedLocation == null)
            throw new ApplicationRuntimeException(
                Tapestry.format(
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