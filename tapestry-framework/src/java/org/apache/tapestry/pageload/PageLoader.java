// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.HiveMind;
import org.apache.hivemind.Location;
import org.apache.hivemind.service.ThreadLocale;
import org.apache.tapestry.*;
import org.apache.tapestry.asset.AssetSource;
import org.apache.tapestry.binding.BindingSource;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.resolver.ComponentSpecificationResolver;
import org.apache.tapestry.services.ComponentConstructor;
import org.apache.tapestry.services.ComponentConstructorFactory;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.services.ComponentTemplateLoader;
import org.apache.tapestry.spec.*;

import java.util.*;

/**
 * Implementation of tapestry.page.PageLoader. Runs the process of building the
 * component hierarchy for an entire page.
 * <p>
 * This implementation is not threadsafe, therefore the pooled service model
 * must be used.
 * </p>
 *
 * @author Howard Lewis Ship
 */

public class PageLoader implements IPageLoader {

    private Log _log;

    /** @since 4.0 */

    private ComponentSpecificationResolver _componentResolver;

    /** @since 4.0 */

    private BindingSource _bindingSource;

    /** @since 4.0 */

    private ComponentTemplateLoader _componentTemplateLoader;

    private List _inheritedBindingQueue = new ArrayList();

    /** @since 4.0 */
    private IComponentVisitor _establishDefaultParameterValuesVisitor;

    private ComponentTreeWalker _establishDefaultParameterValuesWalker;

    private ComponentTreeWalker _verifyRequiredParametersWalker;

    private IComponentVisitor _eventConnectionVisitor;

    private ComponentTreeWalker _eventConnectionWalker;

    private IComponentVisitor _componentTypeVisitor;

    /** @since 4.0 */

    private ComponentConstructorFactory _componentConstructorFactory;

    /** @since 4.0 */

    private AssetSource _assetSource;

    /**
     * Used to find the correct Java component class for a page.
     *
     * @since 4.0
     */

    private ComponentClassProvider _pageClassProvider;

    /**
     * Used to find the correct Java component class for a component (a similar
     * process to resolving a page, but with slightly differen steps and
     * defaults).
     *
     * @since 4.0
     */

    private ComponentClassProvider _componentClassProvider;

    /**
     * Used to resolve meta-data properties related to a component.
     *
     * @since 4.0
     */

    private ComponentPropertySource _componentPropertySource;

    /**
     * Tracks the current locale into which pages are loaded.
     *
     * @since 4.0
     */

    private ThreadLocale _threadLocale;

    /**
     * The locale of the application, which is also the locale of the page being
     * loaded.
     */

    private Locale _locale;

    /**
     * Number of components instantiated, excluding the page itself.
     */

    private int _count;

    /**
     * The recursion depth. A page with no components is zero. A component on a
     * page is one.
     */

    private int _depth;

    /**
     * The maximum depth reached while building the page.
     */

    private int _maxDepth;

    /** @since 4.0 */

    private ClassResolver _classResolver;

    /**
     * As each component is constructed it is placed on to the component stack,  when construction is finished it is pushed
     * back off the stack.  This helps in detecting component nesting and properly reporting errors.
     */
    private Stack _componentStack = new Stack();

    public void initializeService()
    {

        // Create the mechanisms for walking the component tree when it is
        // complete
        IComponentVisitor verifyRequiredParametersVisitor = new VerifyRequiredParametersVisitor();

        _verifyRequiredParametersWalker =
          new ComponentTreeWalker( new IComponentVisitor[] { verifyRequiredParametersVisitor });

        _establishDefaultParameterValuesWalker =
          new ComponentTreeWalker( new IComponentVisitor[] { _establishDefaultParameterValuesVisitor });

        _eventConnectionWalker =
          new ComponentTreeWalker( new IComponentVisitor[] { _eventConnectionVisitor, _componentTypeVisitor });
    }

    /**
     * Binds properties of the component as defined by the container's
     * specification.
     * <p>
     * This implementation is very simple, we will need a lot more sanity
     * checking and eror checking in the final version.
     *
     * @param container
     *            The containing component. For a dynamic binding ({@link org.apache.tapestry.binding.ExpressionBinding})
     *            the property name is evaluated with the container as the root.
     * @param component
     *            The contained component being bound.
     * @param contained
     *            The contained component specification (from the container's
     *            {@link IComponentSpecification}).
     * @param defaultBindingPrefix
     *            The default binding prefix to be used with the component.
     */

    void bind(IComponent container, IComponent component,
              IContainedComponent contained, String defaultBindingPrefix)
    {
        IComponentSpecification spec = component.getSpecification();
        boolean formalOnly = !spec.getAllowInformalParameters();

        if (contained.getInheritInformalParameters())
        {
            if (formalOnly)
                throw new ApplicationRuntimeException(PageloadMessages.inheritInformalInvalidComponentFormalOnly(component),
                                                      component, contained.getLocation(), null);

            IComponentSpecification containerSpec = container.getSpecification();

            if (!containerSpec.getAllowInformalParameters())
                throw new ApplicationRuntimeException(PageloadMessages.inheritInformalInvalidContainerFormalOnly(container, component),
                                                      component, contained.getLocation(), null);

            IQueuedInheritedBinding queued = new QueuedInheritInformalBindings(component);
            _inheritedBindingQueue.add(queued);
        }

        Iterator i = contained.getBindingNames().iterator();

        while(i.hasNext())
        {
            String name = (String) i.next();

            IParameterSpecification pspec = spec.getParameter(name);

            boolean isFormal = pspec != null;

            String parameterName = isFormal ? pspec.getParameterName() : name;

            IBindingSpecification bspec = contained.getBinding(name);

            // If not allowing informal parameters, check that each binding
            // matches
            // a formal parameter.

            if (formalOnly && !isFormal)
                throw new ApplicationRuntimeException(PageloadMessages.formalParametersOnly(component, name),
                                                      component, bspec.getLocation(), null);

            // If an informal parameter that conflicts with a reserved name,
            // then skip it.

            if (!isFormal && spec.isReservedParameterName(name))
                continue;

            if (isFormal)
            {
                if (!name.equals(parameterName))
                {
                    _log.warn(PageloadMessages.usedParameterAlias(contained, name, parameterName, bspec.getLocation()));
                }
                else if (pspec.isDeprecated())
                    _log.warn(PageloadMessages.deprecatedParameter(name, bspec.getLocation(), contained.getType()));
            }

            // The type determines how to interpret the value:
            // As a simple static String
            // As a nested property name (relative to the component)
            // As the name of a binding inherited from the containing component.
            // As the name of a public field
            // As a script for a listener

            BindingType type = bspec.getType();

            // For inherited bindings, defer until later. This gives components
            // a chance to setup bindings from static values and expressions in
            // the template. The order of operations is tricky, template
            // bindings
            // come later. Note that this is a hold over from the Tapestry 3.0
            // DTD
            // and will some day no longer be supported.

            if (type == BindingType.INHERITED)
            {
                QueuedInheritedBinding queued = new QueuedInheritedBinding(component, bspec.getValue(), parameterName);
                _inheritedBindingQueue.add(queued);
                continue;
            }

            String description = PageloadMessages.parameterName(name);

            IBinding binding = convert(container, description, pspec, defaultBindingPrefix, bspec);

            addBindingToComponent(component, parameterName, binding);
        }
    }

    /**
     * Adds a binding to the component, checking to see if there's a name
     * conflict (an existing binding for the same parameter ... possibly because
     * parameter names can be aliased).
     *
     * @param component
     *            to which the binding should be added
     * @param parameterName
     *            the name of the parameter to bind, which should be a true
     *            name, not an alias
     * @param binding
     *            the binding to add
     * @throws ApplicationRuntimeException
     *             if a binding already exists
     * @since 4.0
     */

    static void addBindingToComponent(IComponent component, String parameterName, IBinding binding)
    {
        IBinding existing = component.getBinding(parameterName);

        if (existing != null)
            throw new ApplicationRuntimeException(PageloadMessages.duplicateParameter(parameterName, existing),
                                                  component, binding.getLocation(), null);

        component.setBinding(parameterName, binding);
    }

    private IBinding convert(IComponent container, String description, IParameterSpecification param,
                             String defaultBindingType, IBindingSpecification spec)
    {
        Location location = spec.getLocation();
        String bindingReference = spec.getValue();

        return _bindingSource.createBinding(container, param, description,
                                            bindingReference, defaultBindingType, location);
    }

    /**
     * Sets up a component. This involves:
     * <ul>
     * <li>Instantiating any contained components.
     * <li>Add the contained components to the container.
     * <li>Setting up bindings between container and containees.
     * <li>Construct the containees recursively.
     * <li>Invoking
     * {@link IComponent#finishLoad(IRequestCycle, IPageLoader, IComponentSpecification)}
     * </ul>
     *
     * @param cycle
     *            the request cycle for which the page is being (initially)
     *            constructed
     * @param page
     *            The page on which the container exists.
     * @param container
     *            The component to be set up.
     * @param containerSpec
     *            The specification for the container.
     * @param namespace
     *            The namespace of the container
     */

    private void constructComponent(IRequestCycle cycle, IPage page,
                                    IComponent container, IComponentSpecification containerSpec,
                                    INamespace namespace)
    {
        _depth++;
        if (_depth > _maxDepth)
            _maxDepth = _depth;

        beginConstructComponent(container, containerSpec);

        String defaultBindingPrefix = _componentPropertySource.getComponentProperty(container, TapestryConstants.DEFAULT_BINDING_PREFIX_NAME);

        List ids = new ArrayList(containerSpec.getComponentIds());
        int count = ids.size();

        try
        {
            for(int i = 0; i < count; i++)
            {
                String id = (String) ids.get(i);

                // Get the sub-component specification from the
                // container's specification.

                IContainedComponent contained = containerSpec.getComponent(id);

                String type = contained.getType();
                Location location = contained.getLocation();

                _componentResolver.resolve(cycle, namespace, type, location);

                IComponentSpecification componentSpecification = _componentResolver.getSpecification();
                INamespace componentNamespace = _componentResolver.getNamespace();

                // Instantiate the contained component.

                IComponent component = instantiateComponent(page, container,
                                                            id, componentSpecification, _componentResolver.getType(), componentNamespace, contained);

                // Add it, by name, to the container.

                container.addComponent(component);

                // Set up any bindings in the IContainedComponent specification

                bind(container, component, contained, defaultBindingPrefix);

                // Now construct the component recusively; it gets its chance
                // to create its subcomponents and set their bindings.

                constructComponent(cycle, page, component, componentSpecification, componentNamespace);
            }

            addAssets(container, containerSpec);

            // Finish the load of the component; most components (which
            // subclass BaseComponent) load their templates here.
            // Properties with initial values will be set here (or the
            // initial value will be recorded for later use in pageDetach().
            // That may cause yet more components to be created, and more
            // bindings to be set, so we defer some checking until later.

            container.finishLoad(cycle, this, containerSpec);

            // Have the component switch over to its active state.

            container.enterActiveState();
        }
        catch (ApplicationRuntimeException ex)
        {
            throw ex;
        }
        catch (RuntimeException ex)
        {
            throw new ApplicationRuntimeException(PageloadMessages.unableToInstantiateComponent(container, ex),
                                                  container, null, ex);
        } finally {

            endConstructComponent(container);
        }

        _depth--;
    }

    /**
     * Checks the component stack to ensure that the specified component hasn't been improperly nested
     * and referenced recursively within itself.
     *
     * @param component
     *          The component to add to the current component stack and check for recursion.
     * @param specification
     *          The specification of the specified component.
     */
    void beginConstructComponent(IComponent component, IComponentSpecification specification)
    {
        // check recursion

        int position = _componentStack.search(component);
        if (position > -1)
        {
            Location location = specification.getLocation();

            // try to get the more precise container position location that was referenced
            // in the template to properly report the precise position of the recursive reference

            IContainedComponent container = component.getContainedComponent();
            if (container != null)
                location = container.getLocation();

            throw new ApplicationRuntimeException(PageloadMessages.recursiveComponent(component), location, null);
        }

        _componentStack.push(component);
    }

    /**
     * Pops the current component off the stack.
     *
     * @param component
     *          The component that has just been constructed.
     */
    void endConstructComponent(IComponent component)
    {
        _componentStack.pop();
    }

    /**
     * Invoked to create an implicit component (one which is defined in the
     * containing component's template, rather that in the containing
     * component's specification).
     *
     * @see org.apache.tapestry.services.impl.ComponentTemplateLoaderImpl
     * @since 3.0
     */

    public IComponent createImplicitComponent(IRequestCycle cycle,
                                              IComponent container, String componentId, String componentType,
                                              Location location)
    {
        IPage page = container.getPage();

        _componentResolver.resolve(cycle, container.getNamespace(), componentType, location);

        INamespace componentNamespace = _componentResolver.getNamespace();
        IComponentSpecification spec = _componentResolver.getSpecification();

        IContainedComponent contained = new ContainedComponent();
        contained.setLocation(location);
        contained.setType(componentType);

        IComponent result = instantiateComponent(page, container, componentId,
                                                 spec, _componentResolver.getType(), componentNamespace,
                                                 contained);

        container.addComponent(result);

        // Recusively build the component.

        constructComponent(cycle, page, result, spec, componentNamespace);

        return result;
    }

    /**
     * Instantiates a component from its specification. We instantiate the
     * component object, then set its specification, page, container and id.
     *
     * @param page
     *          The page component is to be attached to.
     * @param container
     *          The containing component.
     * @param id
     *          The components unique id
     * @param spec
     *          The specification for the component
     * @param type
     *          The type (ie Any / For / DirectLink)
     * @param namespace
     *          Which namespace / library
     * @param containedComponent
     *          Possible contained component.
     *
     * @return The instantiated component instance.
     *
     * @see org.apache.tapestry.AbstractComponent
     */

    private IComponent instantiateComponent(IPage page, IComponent container,
                                            String id, IComponentSpecification spec, String type,
                                            INamespace namespace, IContainedComponent containedComponent)
    {
        ComponentClassProviderContext context = new ComponentClassProviderContext(type, spec, namespace);

        String className = _componentClassProvider.provideComponentClassName(context);

        if (HiveMind.isBlank(className))
            className = BaseComponent.class.getName();
        else
        {
            Class componentClass = _classResolver.findClass(className);

            if (!IComponent.class.isAssignableFrom(componentClass))
                throw new ApplicationRuntimeException(PageloadMessages.classNotComponent(componentClass),
                                                      container, spec.getLocation(), null);

            if (IPage.class.isAssignableFrom(componentClass))
                throw new ApplicationRuntimeException(PageloadMessages.pageNotAllowed(id),
                                                      container, spec.getLocation(), null);
        }

        ComponentConstructor cc = _componentConstructorFactory.getComponentConstructor(spec, className);

        IComponent result = (IComponent) cc.newInstance();

        result.setNamespace(namespace);
        result.setPage(page);
        result.setContainer(container);
        result.setId(id);
        result.setContainedComponent(containedComponent);
        result.setLocation(containedComponent.getLocation());

        _count++;

        return result;
    }

    /**
     * Instantitates a page from its specification.
     *
     * @param name
     *            the unqualified, simple, name for the page
     * @param namespace
     *            the namespace containing the page's specification
     * @param spec
     *            the page's specification We instantiate the page object, then
     *            set its specification, names and locale.
     *
     * @return The instantiated page instance.
     *
     * @see org.apache.tapestry.IEngine
     * @see org.apache.tapestry.event.ChangeObserver
     */

    private IPage instantiatePage(String name, INamespace namespace, IComponentSpecification spec)
    {
        Location location = spec.getLocation();
        ComponentClassProviderContext context = new ComponentClassProviderContext(name, spec, namespace);

        String className = _pageClassProvider.provideComponentClassName(context);

        Class pageClass = _classResolver.findClass(className);

        if (!IPage.class.isAssignableFrom(pageClass))
            throw new ApplicationRuntimeException(PageloadMessages.classNotPage(pageClass), location, null);

        String pageName = namespace.constructQualifiedName(name);

        ComponentConstructor cc = _componentConstructorFactory.getComponentConstructor(spec, className);

        IPage result = (IPage) cc.newInstance();

        result.setNamespace(namespace);
        result.setPageName(pageName);
        result.setPage(result);
        result.setLocale(_locale);
        result.setLocation(location);

        return result;
    }

    public IPage loadPage(String name, INamespace namespace,
                          IRequestCycle cycle, IComponentSpecification specification)
    {
        IPage page = null;

        _count = 0;
        _depth = 0;
        _maxDepth = 0;
        _componentStack.clear();

        _locale = _threadLocale.getLocale();

        try
        {
            page = instantiatePage(name, namespace, specification);

            // The page is now attached to the engine and request cycle; some
            // code
            // inside the page's finishLoad() method may require this.
            // TAPESTRY-763

            page.attach(cycle.getEngine(), cycle);

            constructComponent(cycle, page, page, specification, namespace);

            // Walk through the complete component tree to set up the default
            // parameter values.

            _establishDefaultParameterValuesWalker.walkComponentTree(page);

            establishInheritedBindings();

            // Walk through the complete component tree to ensure that required
            // parameters are bound

            _verifyRequiredParametersWalker.walkComponentTree(page);

            // connect @EventListener style client side events

            _eventConnectionWalker.walkComponentTree(page);
        }
        finally
        {
            _locale = null;
            _inheritedBindingQueue.clear();
        }

        if (_log.isDebugEnabled())
            _log.debug("Loaded page " + page + " with " + _count + " components (maximum depth " + _maxDepth + ")");

        return page;
    }

    /** @since 4.0 */

    public void loadTemplateForComponent(IRequestCycle cycle, ITemplateComponent component)
    {
        _componentTemplateLoader.loadTemplate(cycle, component);
    }

    private void establishInheritedBindings()
    {
        _log.debug("Establishing inherited bindings");

        int count = _inheritedBindingQueue.size();

        for(int i = 0; i < count; i++)
        {
            IQueuedInheritedBinding queued = (IQueuedInheritedBinding) _inheritedBindingQueue.get(i);

            queued.connect();
        }
    }

    private void addAssets(IComponent component, IComponentSpecification specification)
    {
        List names = specification.getAssetNames();

        if (names.isEmpty()) return;

        Iterator i = names.iterator();

        while(i.hasNext())
        {
            String name = (String) i.next();

            IAssetSpecification assetSpec = specification.getAsset(name);

            IAsset asset = _assetSource.findAsset(assetSpec.getLocation().getResource(), specification,
                                                  assetSpec.getPath(), _locale, assetSpec.getLocation());

            component.addAsset(name, asset);
        }
    }

    public void setLog(Log log)
    {
        _log = log;
    }

    public void setComponentResolver(ComponentSpecificationResolver resolver)
    {
        _componentResolver = resolver;
    }

    public void setBindingSource(BindingSource bindingSource)
    {
        _bindingSource = bindingSource;
    }

    public void setComponentTemplateLoader(ComponentTemplateLoader componentTemplateLoader)
    {
        _componentTemplateLoader = componentTemplateLoader;
    }

    public void setEstablishDefaultParameterValuesVisitor(IComponentVisitor establishDefaultParameterValuesVisitor)
    {
        _establishDefaultParameterValuesVisitor = establishDefaultParameterValuesVisitor;
    }

    public void setEventConnectionVisitor(IComponentVisitor eventConnectionVisitor)
    {
        _eventConnectionVisitor = eventConnectionVisitor;
    }

    public void setComponentTypeVisitor(IComponentVisitor visitor)
    {
        _componentTypeVisitor = visitor;
    }

    public void setComponentConstructorFactory(ComponentConstructorFactory componentConstructorFactory)
    {
        _componentConstructorFactory = componentConstructorFactory;
    }

    public void setAssetSource(AssetSource assetSource)
    {
        _assetSource = assetSource;
    }

    public void setPageClassProvider(ComponentClassProvider pageClassProvider)
    {
        _pageClassProvider = pageClassProvider;
    }

    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }

    public void setComponentClassProvider(ComponentClassProvider componentClassProvider)
    {
        _componentClassProvider = componentClassProvider;
    }

    public void setThreadLocale(ThreadLocale threadLocale)
    {
        _threadLocale = threadLocale;
    }

    public void setComponentPropertySource(ComponentPropertySource componentPropertySource)
    {
        _componentPropertySource = componentPropertySource;
    }
}
