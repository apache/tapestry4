/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.pageload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.IAsset;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IComponent;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IPageLoader;
import net.sf.tapestry.IPageSource;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.ISpecificationSource;
import net.sf.tapestry.ITemplateSource;
import net.sf.tapestry.PageLoaderException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.binding.ExpressionBinding;
import net.sf.tapestry.binding.StringBinding;
import net.sf.tapestry.spec.AssetSpecification;
import net.sf.tapestry.spec.AssetType;
import net.sf.tapestry.spec.BindingSpecification;
import net.sf.tapestry.spec.BindingType;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.ContainedComponent;
import net.sf.tapestry.spec.ParameterSpecification;

/**
 *  Runs the process of building the component hierarchy for an entire page.
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
    private ISpecificationSource _specificationSource;
    private IPageSource _pageSource;

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

    private class ComponentResolver
    {
        private INamespace _namespace;
        private ComponentSpecification _spec;

        private ComponentResolver(INamespace containerNamespace, String alias)
        {
            // For compatibility with the 1.1 and 1.2 specifications, which allow
            // the component type to be a complete specification path.
            
            if (alias.startsWith("/"))
            {
                _namespace = _specificationSource.getApplicationNamespace();
                _spec = _specificationSource.getComponentSpecification(alias);
                return;
            }

            int colonx = alias.indexOf(':');

            if (colonx > 0)
            {
                String id = alias.substring(0, colonx);
                _namespace = containerNamespace.getChildNamespace(id);

                String bareAlias = alias.substring(colonx + 1);
                _spec = _namespace.getComponentSpecification(bareAlias);
                return;
            }

            // A bare component type may be in the namespace of the container
            // (typically the application namespace, but possibly a 
            // library namespace).  Check there first and, if not found,
            // check the framework namespace.

            if (containerNamespace.containsAlias(alias))
                _namespace = containerNamespace;
            else
                _namespace = _specificationSource.getFrameworkNamespace();

            _spec = _namespace.getComponentSpecification(alias);
        }

        private INamespace getNamespace()
        {
            return _namespace;
        }

        private ComponentSpecification getSpecification()
        {
            return _spec;
        }
    }

    /**
     *  Constructor.
     *
     **/

    public PageLoader(IPageSource pageSource)
    {
        _pageSource = pageSource;
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
     *  @param propertyBindings a cache of {@link ExpressionBinding}s for the container
     *
     **/

    private void bind(IComponent container, IComponent component, ContainedComponent contained, Map propertyBindings)
        throws PageLoaderException
    {
        ComponentSpecification spec = component.getSpecification();
        boolean formalOnly = !spec.getAllowInformalParameters();

        Iterator i = contained.getBindingNames().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();

            boolean isFormal = spec.getParameter(name) != null;

            // If not allowing informal parameters, check that each binding matches
            // a formal parameter.

            if (formalOnly && !isFormal)
                throw new PageLoaderException(
                    Tapestry.getString("PageLoader.formal-parameters-only", component.getExtendedId(), name),
                    component,
                    null);

            // If an informal parameter that conflicts with a reserved name, then
            // skip it.

            if (!isFormal && spec.isReservedParameterName(name))
                continue;

            BindingSpecification bspec = contained.getBinding(name);

            // The type determines how to interpret the value:
            // As a simple static String
            // As a nested property name (relative to the component)
            // As the name of a binding inherited from the containing component.

            BindingType type = bspec.getType();
            String bindingValue = bspec.getValue();

            IBinding binding = convert(type, bindingValue, container, propertyBindings);

            if (binding != null)
                component.setBinding(name, binding);
        }

        i = spec.getParameterNames().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();
            ParameterSpecification parameterSpec = spec.getParameter(name);

            if (parameterSpec.isRequired() && component.getBinding(name) == null)
                throw new PageLoaderException(
                    Tapestry.getString("PageLoader.required-parameter-not-bound", name, component.getExtendedId()),
                    component,
                    null);
        }
    }

    private IBinding convert(BindingType type, String bindingValue, IComponent container, Map propertyBindings)
    {
        // The most common type.  propertyBindings is a cache of
        // property bindings for the container, we re-use
        // the bindings for the same property path.

        if (type == BindingType.DYNAMIC)
        {
            IBinding result = (IBinding) propertyBindings.get(bindingValue);

            if (result == null)
            {
                result = new ExpressionBinding(_resolver, container, bindingValue);
                propertyBindings.put(bindingValue, result);
            }

            return result;
        }

        // String bindings are new in 2.0.4.  For the momement,
        // we don't even try to cache and share them ... they
        // are most often unique within a page.

        if (type == BindingType.STRING)
            return new StringBinding(container, bindingValue);

        // static and field bindings are pooled.  This allows the
        // same instance to be used with many components.

        if (type == BindingType.STATIC)
            return _pageSource.getStaticBinding(bindingValue);

        if (type == BindingType.FIELD)
            return _pageSource.getFieldBinding(bindingValue);

        // Otherwise, its an inherited binding.  Dig it out of the container.
        // This may return null if the container doesn't have the named binding.

        return container.getBinding(bindingValue);

    }

    /**
     *  Sets up a component.  This involves:
     *  <ul>
     * <li>Instantiating any contained components.
     * <li>Add the contained components to the container.
     * <li>Setting up bindings between container and containees.
     * <li>Construct the containees recursively.
     * <li>Telling the component its 'ready' (so that it can load its HTML template)
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
        throws PageLoaderException
    {
        _depth++;
        if (_depth > _maxDepth)
            _maxDepth = _depth;

        List ids = new ArrayList(containerSpec.getComponentIds());
        int count = ids.size();

        for (int i = 0; i < count; i++)
        {
            String id = (String) ids.get(i);

            // Get the sub-component specification from the
            // container's specification.

            ContainedComponent contained = containerSpec.getComponent(id);

            String type = contained.getType();

            ComponentResolver resolver = new ComponentResolver(namespace, type);
            ComponentSpecification componentSpecification = resolver.getSpecification();
            INamespace componentNamespace = resolver.getNamespace();

            // Instantiate the contained component.

            IComponent component =
                instantiateComponent(page, container, id, componentSpecification, componentNamespace);

            // Add it, by name, to the container.

            container.addComponent(component);

            // Recursively construct the component

            constructComponent(cycle, page, component, componentSpecification, componentNamespace);
        }

        addAssets(container, containerSpec);

        container.finishLoad(cycle, this, containerSpec);

        _depth--;
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
        INamespace namespace)
        throws PageLoaderException
    {
        IComponent result = null;

        String className = spec.getComponentClassName();
        Class componentClass = _resolver.findClass(className);

        try
        {
            result = (IComponent) componentClass.newInstance();

            result.setNamespace(namespace);
            result.setSpecification(spec);
            result.setPage(page);
            result.setContainer(container);
            result.setId(id);

        }
        catch (ClassCastException ex)
        {
            throw new PageLoaderException(
                Tapestry.getString("PageLoader.class-not-component", className),
                container,
                ex);
        }
        catch (Exception ex)
        {
            throw new PageLoaderException(
                Tapestry.getString("PageLoader.unable-to-instantiate", className),
                container,
                ex);
        }

        if (result instanceof IPage)
            throw new PageLoaderException(
                Tapestry.getString("PageLoader.page-not-allowed", result.getExtendedId()),
                result);

        _count++;

        return result;
    }

    /**
     *  Instantitates a page from its specification.
     *
     *
     * We instantiate the page object, then set its specification,
     * name and locale.
     *
     * @see IEngine
     * @see ChangeObserver
     **/

    private IPage instantiatePage(String name, INamespace namespace, ComponentSpecification spec)
        throws PageLoaderException
    {
        String className;
        Class pageClass;
        IPage result = null;

        className = spec.getComponentClassName();

        pageClass = _resolver.findClass(className);

        try
        {
            result = (IPage) pageClass.newInstance();

            result.setNamespace(namespace);
            result.setSpecification(spec);
            result.setName(name);
            result.setLocale(_locale);
        }
        catch (ClassCastException ex)
        {
            throw new PageLoaderException(Tapestry.getString("PageLoader.class-not-page", className), name, ex);
        }
        catch (Exception ex)
        {
            throw new PageLoaderException(Tapestry.getString("PageLoader.unable-to-instantiate", className), name, ex);
        }

        return result;
    }

    /**
     *  Invoked by the {@link PageSource} to load a specific page.  This
     *  method is not reentrant ... the PageSource ensures that
     *  any given instance of PageLoader is loading only a single page at a time.
     *  The page is immediately attached to the {@link IEngine engine}.
     *
     *  @param name the name of the page to load
     *  @param namespace from which the page is to be loaded (used
     *  when resolving components embedded by the page)
     *  @param cycle the request cycle the page is 
     *  initially loaded for (this is used
     *  to define the locale of the new page, and provide access
     *  to the corect specification source, etc.).
     *  @param specification the specification for the page
     *
     **/

    public IPage loadPage(String name, INamespace namespace, IRequestCycle cycle, ComponentSpecification specification)
        throws PageLoaderException
    {
        IPage page = null;

        _engine = cycle.getEngine();

        _locale = _engine.getLocale();
        _specificationSource = _engine.getSpecificationSource();
        _resolver = _engine.getResourceResolver();

        _count = 0;
        _depth = 0;
        _maxDepth = 0;

        try
        {
            page = instantiatePage(name, namespace, specification);

            page.attach(_engine);

            constructComponent(cycle, page, page, specification, namespace);

            setBindings(page);
        }
        finally
        {
            _locale = null;
            _engine = null;
            _specificationSource = null;
            _resolver = null;
        }

        if (LOG.isInfoEnabled())
            LOG.info("Loaded page " + page + " with " + _count + " components (maximum depth " + _maxDepth + ")");

        return page;
    }

    /** 
     *  Sets all bindings, top-down.  Checking (as it goes) that all required parameters
     *  have been set.
     *
     *  @since 1.0.6 
     * 
     **/

    private void setBindings(IComponent container) throws PageLoaderException
    {
        Map components = container.getComponents();

        if (components.isEmpty())
            return;

        ComponentSpecification containerSpec = container.getSpecification();

        Map propertyBindings = new HashMap();

        Iterator i = components.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();
            String id = (String) e.getKey();
            IComponent component = (IComponent) e.getValue();
            ComponentSpecification spec = component.getSpecification();
            ContainedComponent contained = containerSpec.getComponent(id);

            bind(container, component, contained, propertyBindings);

            setBindings(component);
        }
    }

    private void addAssets(IComponent component, ComponentSpecification specification)
    {
        Iterator i = specification.getAssetNames().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();
            AssetSpecification assetSpec = specification.getAsset(name);
            IAsset asset = convert(assetSpec);

            component.addAsset(name, asset);
        }
    }

    /**
     *  Builds an instance of {@link IAsset} from the specification.
     *
     **/

    private IAsset convert(AssetSpecification spec)
    {
        AssetType type = spec.getType();
        String path = spec.getPath();

        if (type == AssetType.EXTERNAL)
            return _pageSource.getExternalAsset(path);

        if (type == AssetType.PRIVATE)
            return _pageSource.getPrivateAsset(path);

        // Could use a sanity check for  type == null,
        // but instead we assume its a context asset.

        return _pageSource.getContextAsset(path);
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