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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.HiveMind;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.service.ThreadLocale;
import org.apache.hivemind.util.ContextResource;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.ITemplateComponent;
import org.apache.tapestry.TapestryConstants;
import org.apache.tapestry.asset.AssetSource;
import org.apache.tapestry.binding.BindingSource;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.resolver.ComponentSpecificationResolver;
import org.apache.tapestry.services.ComponentConstructor;
import org.apache.tapestry.services.ComponentConstructorFactory;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.services.ComponentTemplateLoader;
import org.apache.tapestry.spec.BindingType;
import org.apache.tapestry.spec.ContainedComponent;
import org.apache.tapestry.spec.IAssetSpecification;
import org.apache.tapestry.spec.IBindingSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;
import org.apache.tapestry.spec.IParameterSpecification;
import org.apache.tapestry.web.WebContextResource;

/**
 * Implementation of tapestry.page.PageLoader. Runs the process of building the
 * component hierarchy for an entire page.
 * <p>
 * This implementation is not threadsafe, therefore the pooled service model
 * must be used.
 * 
 * @author Howard Lewis Ship
 */

public class PageLoader implements IPageLoader
{

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

    /** @since 4.0 */

    private ComponentConstructorFactory _componentConstructorFactory;

    /** @since 4.0 */

    private ValueConverter _valueConverter;

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

    public void initializeService()
    {

        // Create the mechanisms for walking the component tree when it is
        // complete
        IComponentVisitor verifyRequiredParametersVisitor = new VerifyRequiredParametersVisitor();

        _verifyRequiredParametersWalker = new ComponentTreeWalker(
                new IComponentVisitor[] { verifyRequiredParametersVisitor });

        _establishDefaultParameterValuesWalker = new ComponentTreeWalker(
                new IComponentVisitor[] { _establishDefaultParameterValuesVisitor });
    }

    /**
     * Binds properties of the component as defined by the container's
     * specification.
     * <p>
     * This implementation is very simple, we will need a lot more sanity
     * checking and eror checking in the final version.
     * 
     * @param container
     *            The containing component. For a dynamic binding ({@link ExpressionBinding})
     *            the property name is evaluated with the container as the root.
     * @param component
     *            The contained component being bound.
     * @param spec
     *            The specification of the contained component.
     * @param contained
     *            The contained component specification (from the container's
     *            {@link IComponentSpecification}).
     */

    void bind(IComponent container, IComponent component,
            IContainedComponent contained, String defaultBindingPrefix)
    {
        IComponentSpecification spec = component.getSpecification();
        boolean formalOnly = !spec.getAllowInformalParameters();

        if (contained.getInheritInformalParameters())
        {
            if (formalOnly)
                throw new ApplicationRuntimeException(PageloadMessages
                        .inheritInformalInvalidComponentFormalOnly(component),
                        component, contained.getLocation(), null);

            IComponentSpecification containerSpec = container
                    .getSpecification();

            if (!containerSpec.getAllowInformalParameters())
                throw new ApplicationRuntimeException(PageloadMessages
                        .inheritInformalInvalidContainerFormalOnly(container,
                                component), component, contained.getLocation(),
                        null);

            IQueuedInheritedBinding queued = new QueuedInheritInformalBindings(
                    component);
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
                throw new ApplicationRuntimeException(PageloadMessages
                        .formalParametersOnly(component, name), component,
                        bspec.getLocation(), null);

            // If an informal parameter that conflicts with a reserved name,
            // then skip it.

            if (!isFormal && spec.isReservedParameterName(name)) continue;

            if (isFormal)
            {
                if (!name.equals(parameterName))
                {
                    _log.warn(PageloadMessages.usedParameterAlias(contained,
                            name, parameterName, bspec.getLocation()));
                }
                else if (pspec.isDeprecated())
                    _log.warn(PageloadMessages.deprecatedParameter(name, bspec
                            .getLocation(), contained.getType()));
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
                QueuedInheritedBinding queued = new QueuedInheritedBinding(
                        component, bspec.getValue(), parameterName);
                _inheritedBindingQueue.add(queued);
                continue;
            }

            String description = PageloadMessages.parameterName(name);

            IBinding binding = convert(container, description,
                    defaultBindingPrefix, bspec);

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

    static void addBindingToComponent(IComponent component,
            String parameterName, IBinding binding)
    {
        IBinding existing = component.getBinding(parameterName);

        if (existing != null)
            throw new ApplicationRuntimeException(PageloadMessages
                    .duplicateParameter(parameterName, existing), component,
                    binding.getLocation(), null);

        component.setBinding(parameterName, binding);
    }

    private IBinding convert(IComponent container, String description,
            String defaultBindingType, IBindingSpecification spec)
    {
        Location location = spec.getLocation();
        String bindingReference = spec.getValue();

        return _bindingSource.createBinding(container, description,
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
     * @param the
     *            namespace of the container
     */

    private void constructComponent(IRequestCycle cycle, IPage page,
            IComponent container, IComponentSpecification containerSpec,
            INamespace namespace)
    {
        _depth++;
        if (_depth > _maxDepth) _maxDepth = _depth;

        String defaultBindingPrefix = _componentPropertySource
                .getComponentProperty(container,
                        TapestryConstants.DEFAULT_BINDING_PREFIX_NAME);

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

                IComponentSpecification componentSpecification = _componentResolver
                        .getSpecification();
                INamespace componentNamespace = _componentResolver
                        .getNamespace();

                // Instantiate the contained component.

                IComponent component = instantiateComponent(page, container,
                        id, componentSpecification, _componentResolver
                                .getType(), componentNamespace, contained);

                // Add it, by name, to the container.

                container.addComponent(component);

                // Set up any bindings in the IContainedComponent specification

                bind(container, component, contained, defaultBindingPrefix);

                // Now construct the component recusively; it gets its chance
                // to create its subcomponents and set their bindings.

                constructComponent(cycle, page, component,
                        componentSpecification, componentNamespace);
            }

            addAssets(container, containerSpec);

            // Finish the load of the component; most components (which
            // subclass BaseComponent) load their templates here.
            // Properties with initial values will be set here (or the
            // initial value will be recorded for later use in pageDetach().
            // That may cause yet more components to be created, and more
            // bindings to be set, so we defer some checking until
            // later.

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
            throw new ApplicationRuntimeException(PageloadMessages
                    .unableToInstantiateComponent(container, ex), container,
                    null, ex);
        }

        _depth--;
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

        _componentResolver.resolve(cycle, container.getNamespace(),
                componentType, location);

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
     * @see AbstractComponent
     */

    private IComponent instantiateComponent(IPage page, IComponent container,
            String id, IComponentSpecification spec, String type,
            INamespace namespace, IContainedComponent containedComponent)
    {
        ComponentClassProviderContext context = new ComponentClassProviderContext(
                type, spec, namespace);
        String className = _componentClassProvider
                .provideComponentClassName(context);

        // String className = spec.getComponentClassName();

        if (HiveMind.isBlank(className))
            className = BaseComponent.class.getName();
        else
        {
            Class componentClass = _classResolver.findClass(className);

            if (!IComponent.class.isAssignableFrom(componentClass))
                throw new ApplicationRuntimeException(PageloadMessages
                        .classNotComponent(componentClass), container, spec
                        .getLocation(), null);

            if (IPage.class.isAssignableFrom(componentClass))
                throw new ApplicationRuntimeException(PageloadMessages
                        .pageNotAllowed(id), container, spec.getLocation(),
                        null);
        }

        ComponentConstructor cc = _componentConstructorFactory
                .getComponentConstructor(spec, className);

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
     * @see IEngine
     * @see ChangeObserver
     */

    private IPage instantiatePage(String name, INamespace namespace,
            IComponentSpecification spec)
    {
        Location location = spec.getLocation();
        ComponentClassProviderContext context = new ComponentClassProviderContext(
                name, spec, namespace);
        String className = _pageClassProvider
                .provideComponentClassName(context);

        Class pageClass = _classResolver.findClass(className);

        if (!IPage.class.isAssignableFrom(pageClass))
            throw new ApplicationRuntimeException(PageloadMessages
                    .classNotPage(pageClass), location, null);

        String pageName = namespace.constructQualifiedName(name);

        ComponentConstructor cc = _componentConstructorFactory
                .getComponentConstructor(spec, className);

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

            // Now that the page has been properly constructed, the page
            // or any components on the page will have been registered as
            // page attach listeners.

            page.firePageAttached();
        }
        finally
        {
            _locale = null;
            _inheritedBindingQueue.clear();
        }

        if (_log.isDebugEnabled())
            _log.debug("Loaded page " + page + " with " + _count
                    + " components (maximum depth " + _maxDepth + ")");

        return page;
    }

    /** @since 4.0 */

    public void loadTemplateForComponent(IRequestCycle cycle,
            ITemplateComponent component)
    {
        _componentTemplateLoader.loadTemplate(cycle, component);
    }

    private void establishInheritedBindings()
    {
        _log.debug("Establishing inherited bindings");

        int count = _inheritedBindingQueue.size();

        for(int i = 0; i < count; i++)
        {
            IQueuedInheritedBinding queued = (IQueuedInheritedBinding) _inheritedBindingQueue
                    .get(i);

            queued.connect();
        }
    }

    private void addAssets(IComponent component,
            IComponentSpecification specification)
    {
        List names = specification.getAssetNames();

        if (names.isEmpty()) return;

        Iterator i = names.iterator();

        while(i.hasNext())
        {
            String name = (String) i.next();

            IAssetSpecification assetSpec = specification.getAsset(name);

            IAsset asset = convertAsset(assetSpec);

            component.addAsset(name, asset);
        }
    }

    /**
     * Builds an instance of {@link IAsset} from the specification.
     */

    private IAsset convertAsset(IAssetSpecification spec)
    {
        // AssetType type = spec.getType();
        String path = spec.getPath();
        Location location = spec.getLocation();

        Resource specResource = location.getResource();

        // And ugly, ugly kludge. For page and component specifications in the
        // context (typically, somewhere under WEB-INF), we evaluate them
        // relative the web application root.

        if (isContextResource(specResource))
            specResource = specResource.getRelativeResource("/");

        return _assetSource.findAsset(specResource, path, _locale, location);
    }

    private boolean isContextResource(Resource resource)
    {
        return (resource instanceof WebContextResource)
                || (resource instanceof ContextResource);
    }

    /** @since 4.0 */

    public void setLog(Log log)
    {
        _log = log;
    }

    /** @since 4.0 */

    public void setComponentResolver(ComponentSpecificationResolver resolver)
    {
        _componentResolver = resolver;
    }

    /** @since 4.0 */

    public void setBindingSource(BindingSource bindingSource)
    {
        _bindingSource = bindingSource;
    }

    /**
     * @since 4.0
     */
    public void setComponentTemplateLoader(
            ComponentTemplateLoader componentTemplateLoader)
    {
        _componentTemplateLoader = componentTemplateLoader;
    }

    /** @since 4.0 */
    public void setEstablishDefaultParameterValuesVisitor(
            IComponentVisitor establishDefaultParameterValuesVisitor)
    {
        _establishDefaultParameterValuesVisitor = establishDefaultParameterValuesVisitor;
    }

    /** @since 4.0 */
    public void setComponentConstructorFactory(
            ComponentConstructorFactory componentConstructorFactory)
    {
        _componentConstructorFactory = componentConstructorFactory;
    }

    /** @since 4.0 */
    public void setValueConverter(ValueConverter valueConverter)
    {
        _valueConverter = valueConverter;
    }

    /** @since 4.0 */
    public void setAssetSource(AssetSource assetSource)
    {
        _assetSource = assetSource;
    }

    /** @since 4.0 */
    public void setPageClassProvider(ComponentClassProvider pageClassProvider)
    {
        _pageClassProvider = pageClassProvider;
    }

    /** @since 4.0 */
    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }

    /**
     * @since 4.0
     */
    public void setComponentClassProvider(
            ComponentClassProvider componentClassProvider)
    {
        _componentClassProvider = componentClassProvider;
    }

    /** @since 4.0 */
    public void setThreadLocale(ThreadLocale threadLocale)
    {
        _threadLocale = threadLocale;
    }

    /** @since 4.0 */
    public void setComponentPropertySource(
            ComponentPropertySource componentPropertySource)
    {
        _componentPropertySource = componentPropertySource;
    }
}
