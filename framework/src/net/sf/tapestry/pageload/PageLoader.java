/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package net.sf.tapestry.pageload;

import java.lang.reflect.*;
import com.primix.tapestry.event.*;
import com.primix.tapestry.spec.*;
import java.util.*;
import com.primix.tapestry.*;
import com.primix.tapestry.binding.*;

import net.sf.tapestry.*;
import net.sf.tapestry.binding.*;
import net.sf.tapestry.spec.*;

import org.apache.log4j.*;

/**
 *  Runs the process of building the component hierarchy for an entire page.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class PageLoader implements IPageLoader
{
	private static final Category CAT = Category.getInstance(PageLoader.class);

	private static final int MAP_SIZE = 11;

	private IEngine engine;
	private IResourceResolver resolver;
	private ISpecificationSource specificationSource;
	private IPageSource pageSource;

	/**
	 * The locale of the application, which is also the locale
	 * of the page being loaded.
	 *
	 */

	private Locale locale;

	/**
	 *  Number of components instantiated, excluding the page itself.
	 *
	 */

	private int count;

	/**
	 *  The recursion depth.  A page with no components is zero.  A component on
	 *  a page is one.
	 *
	 */

	private int depth;

	/**
	 *  The maximum depth reached while building the page.
	 *
	 */

	private int maxDepth;

	/**
	 *  Constructor.
	 *
	 */

	public PageLoader(IPageSource pageSource)
	
	{
		this.pageSource = pageSource;
	}

	/**
	 *  Binds properties of the component as defined by the container's specification.
	 *
	 * <p>This implementation is very simple, we will need a lot more
	 *  sanity checking and eror checking in the final version.
	 *
	 *  @param container The containing component.  For a dynamic
	 *  binding ({@link PropertyBinding}) the property name
	 *  is evaluated with the container as the root.
	 *  @param component The contained component being bound.
	 *  @param spec The specification of the contained component.
	 * @param contained The contained component specification (from the container's
	 * {@link ComponentSpecification}).
	 * @param propertyBindings a cache of {@link PropertyBinding}s for the container
	 *
	 */

	private void bind(
		IComponent container,
		IComponent component,
		ContainedComponent contained,
		Map propertyBindings)
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
					Tapestry.getString(
						"PageLoader.formal-parameters-only",
						component.getExtendedId(),
						name),
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
					Tapestry.getString(
						"PageLoader.required-parameter-not-bound",
						name,
						component.getExtendedId()),
					component,
					null);
		}
	}

	private IBinding convert(
		BindingType type,
		String bindingValue,
		IComponent container,
		Map propertyBindings)
	{
		// The most common type.  propertyBindings is a cache of
		// property bindings for the container, we re-use
		// the bindings for the same property path.

		if (type == BindingType.DYNAMIC)
		{
			IBinding result = (IBinding) propertyBindings.get(bindingValue);

			if (result == null)
			{
				result = new PropertyBinding(container, bindingValue);
				propertyBindings.put(bindingValue, result);
			}

			return result;
		}

		// static and field bindings are pooled.  This allows the
		// same instance to be used with many components.

		if (type == BindingType.STATIC)
			return pageSource.getStaticBinding(bindingValue);

		if (type == BindingType.FIELD)
			return pageSource.getFieldBinding(bindingValue);

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
	 * @param page The page on which the container exists.
	 * @param container The component to be set up.
	 * @param containerSpec The specification for the container.
	 *
	 */

	private void constructComponent(
		IPage page,
		IComponent container,
		ComponentSpecification containerSpec)
		throws PageLoaderException
	{
		ComponentSpecification spec;

		depth++;
		if (depth > maxDepth)
			maxDepth = depth;

		List ids = new ArrayList(containerSpec.getComponentIds());
		int count = ids.size();

		for (int i = 0; i < count; i++)
		{
			String id = (String) ids.get(i);

			// Get the sub-component specification from the
			// container's specification.

			ContainedComponent contained = containerSpec.getComponent(id);

			// Get the component specification for the contained
			// component.

			try
			{
				spec = specificationSource.getSpecification(contained.getType());
			}
			catch (ResourceUnavailableException ex)
			{
				throw new PageLoaderException(
					Tapestry.getString("PageLoader.unable-to-load-specification"),
					ex);
			}

			// Instantiate the contained component.

			IComponent component = instantiateComponent(page, container, id, spec);

			// Add it, by name, to the container.

			container.addComponent(component);

			// Recursively construct the component

			constructComponent(page, component, spec);
		}

		addAssets(container, containerSpec);

		container.finishLoad(this, containerSpec);
	}

	/**
	 *  Instantiates a component from its specification. We instantiate
	 *  the component object, then set its specification, page, container and id.
	 *
	 *  @see AbstractComponent
	 */

	private IComponent instantiateComponent(
		IPage page,
		IComponent container,
		String id,
		ComponentSpecification spec)
		throws PageLoaderException
	{
		String className;
		Class componentClass;
		IComponent result = null;

		className = spec.getComponentClassName();
		componentClass = resolver.findClass(className);

		try
		{
			result = (IComponent) componentClass.newInstance();

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

		count++;

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
	 */

	private IPage instantiatePage(String name, ComponentSpecification spec)
		throws PageLoaderException
	{
		String className;
		Class pageClass;
		IPage result = null;

		className = spec.getComponentClassName();

		pageClass = resolver.findClass(className);

		try
		{
			result = (IPage) pageClass.newInstance();

			result.setSpecification(spec);
			result.setName(name);
			result.setLocale(locale);
		}
		catch (ClassCastException ex)
		{
			throw new PageLoaderException(
				Tapestry.getString("PageLoader.class-not-page", className),
				name,
				ex);
		}
		catch (Exception ex)
		{
			throw new PageLoaderException(
				Tapestry.getString("PageLoader.unable-to-instantiate", className),
				name,
				ex);
		}

		return result;
	}

	/**
	 *  Invoked by the {@link PageSource} to load a specific page.  This
	 *  method is not reentrant ... the PageSource ensures that
	 *  any given instance of PageLoader is loading only a single page at a time.
	 *
	 *  @param name the name of the page to load
	 *  @param engine the engine the page is loaded for (this is used
	 *  to define the locale of the new page, and provide access
	 *  to the corect specification source, etc.).
	 *  @param type the page type (the path to its component specification)
	 *
	 */

	public IPage loadPage(String name, IEngine engine, String type)
		throws PageLoaderException
	{
		IPage page = null;
		ComponentSpecification specification;

		this.engine = engine;

		locale = engine.getLocale();
		specificationSource = engine.getSpecificationSource();
		resolver = engine.getResourceResolver();

		count = 0;
		depth = 0;
		maxDepth = 0;

		try
		{
			specification = specificationSource.getSpecification(type);

			page = instantiatePage(name, specification);

			constructComponent(page, page, specification);

			setBindings(page);
		}
		catch (ResourceUnavailableException ex)
		{
			throw new PageLoaderException(ex.getMessage(), name, ex);
		}
		finally
		{
			locale = null;
			engine = null;
			specificationSource = null;
			resolver = null;
		}

		if (CAT.isInfoEnabled())
			CAT.info(
				"Loaded page "
					+ page
					+ " with "
					+ count
					+ " components (maximum depth "
					+ maxDepth
					+ ")");

		return page;
	}

	/** 
	 *  Sets all bindings, top-down.  Checking (as it goes) that all required parameters
	 *  have been set.
	 *
	 * @since 1.0.6 **/

	private void setBindings(IComponent container) throws PageLoaderException
	{
		Map components = container.getComponents();

		if (components.isEmpty())
			return;

		ComponentSpecification containerSpec = container.getSpecification();

		Map propertyBindings = new HashMap(MAP_SIZE);

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

	private void addAssets(
		IComponent component,
		ComponentSpecification specification)
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
	 */

	private IAsset convert(AssetSpecification spec)
	{
		AssetType type = spec.getType();
		String path = spec.getPath();

		if (type == AssetType.EXTERNAL)
			return pageSource.getExternalAsset(path);

		if (type == AssetType.PRIVATE)
			return pageSource.getPrivateAsset(path);

		// Could use a sanity check for  type == null,
		// but instead we assume its a context asset.

		return pageSource.getContextAsset(path);
	}

	public IEngine getEngine()
	{
		return engine;
	}

	public ITemplateSource getTemplateSource()
	{
		return engine.getTemplateSource();
	}
}