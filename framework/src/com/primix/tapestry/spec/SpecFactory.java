/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2002 by Howard Lewis Ship
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

package com.primix.tapestry.spec;

import com.primix.tapestry.bean.FieldBeanInitializer;
import com.primix.tapestry.bean.IBeanInitializer;
import com.primix.tapestry.bean.PropertyBeanInitializer;
import com.primix.tapestry.bean.StaticBeanInitializer;

/**
 * A Factory used by the Specification Parser to create Tapestry
 * domain objects.
 * <p>
 * The default implementation here creates the expected runtime
 * instances of classes in packages:
 * <ul>
 *  <li>com.primix.tapestry.spec</li>
 *  <li>com.primix.tapestry.bean</li>
 * </ul>
 * <p>
 * This class is extended by Spindle - the Eclipse Plugin for Tapestry
 * @author GWL
 * @see com.primix.tapestry.parse.SpecificationParser
 * @since 1.0.9
 * 
 **/

public class SpecFactory
{
	/**
	 * Creates a concrete instance of {@link ApplicationSpecification}.
	 **/
	
	public ApplicationSpecification createApplicationSpecification()
	{
		return new ApplicationSpecification();
	}

	/**
	 * Creates a concrete instance of {@link AssetSpecification}.
	 **/
	
	public AssetSpecification createAssetSpecification(AssetType type, String path)
	{
		return new AssetSpecification(type, path);
	}

	/**
	 * Creates a concrete instance of {@link BeanSpecification}.
	 **/
	
	public BeanSpecification createBeanSpecification(
		String className,
		BeanLifecycle lifecycle)
	{
		return new BeanSpecification(className, lifecycle);
	}

	/**
	 * Creates a concrete instance of {@link BindingSpecification}.
	 **/
	
	public BindingSpecification createBindingSpecification(
		BindingType type,
		String value)
	{
		return new BindingSpecification(type, value);
	}

	/**
	 * Creates a concrete instance of {@link ComponentSpecification}.
	 **/
	
	public ComponentSpecification createComponentSpecification()
	{
		return new ComponentSpecification();
	}

	/**
	 * Creates a concrete instance of {@link ContainedComponent}.
	 **/
	
	public ContainedComponent createContainedComponent()
	{
		return new ContainedComponent();
	}

	/**
	 * Creates a concrete instance of {@link PageSpecification}.
	 **/
	
	public PageSpecification createPageSpecification()
	{
		return new PageSpecification();
	}

	/**
	 * Creates a concrete instance of {@link PageSpecification}.
	 **/
	
	public PageSpecification createPageSpecification(String specificationPath)
	{
		return new PageSpecification(specificationPath);
	}

	/**
	 * Creates a concrete instance of {@link ParameterSpecification}.
	 **/
	
	public ParameterSpecification createParameterSpecification()
	{
		return new ParameterSpecification();
	}

	/**
	 * Creates a concrete instance of {@link IBeanInitializer}.
	 * <p>
	 * Default implementation returns an instance of {@link PropertyBeanInitializer}.
	 **/

	public IBeanInitializer createPropertyBeanInitializer(
		String propertyName,
		String propertyPath)
	{
		return new PropertyBeanInitializer(propertyName, propertyPath);
	}

	/**
	 * Creates a concrete instance of {@link IBeanInitializer}.
	 * <p>
	 * Default implementation returns an instance of {@link StaticBeanInitializer}.
	 **/

	public IBeanInitializer createStaticBeanInitializer(
		String propertyName,
		Object staticValue)
	{
		return new StaticBeanInitializer(propertyName, staticValue);
	}

	/**
	 * Creates a concrete instance of {@link IBeanInitializer}.
	 * <p>
	 * Default implementation returns an instance of {@link FieldBeanInitializer}.
	 **/

	public IBeanInitializer createFieldBeanInitializer(
		String propertyName,
		String fieldName)
	{
		return new FieldBeanInitializer(propertyName, fieldName);
	}

}