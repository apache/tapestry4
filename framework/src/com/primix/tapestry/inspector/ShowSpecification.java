package com.primix.tapestry.inspector;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.components.*;
import java.util.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */


/**
 *  Component of the {@link Inspector} page used to display
 *  the specification, parameters and bindings and assets of the inspected component.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class ShowSpecification extends BaseComponent
implements ILifecycle
{
	private IComponent inspectedComponent;
	private ComponentSpecification inspectedSpecification;
	private String parameterName;
	private String assetName;
	
	public void cleanupAfterRender(IRequestCycle cycle)
	{
		inspectedComponent = null;
		inspectedSpecification = null;
		parameterName = null;
		assetName = null;
	}
	
	/**
	 *  Gets the inspected component and specification from the {@link Inspector} page.
	 *
	 */
	 
	public void prepareForRender(IRequestCycle cycle)
	{
		Inspector inspector;
		
		inspector = (Inspector)page;
		
		inspectedComponent = inspector.getInspectedComponent();
		inspectedSpecification = inspectedComponent.getSpecification();
	}
				
	public IComponent getInspectedComponent()
	{
		return inspectedComponent;
	}
	
	public ComponentSpecification getInspectedSpecification()
	{
		return inspectedSpecification;
	}
	
	/**
	 *  Returns a sorted list of formal parameter names.
	 *
	 */
	 
	public List getFormalParameterNames()
	{
		List result;
		Collection names;
		
		names = inspectedSpecification.getParameterNames();
		if (names == null)
			return null;
		
		result = new ArrayList(names);
		
		Collections.sort(result);
		
		return result;
	}
	
	/** 
	 *  Returns a sorted list of informal parameter names.  This is
	 *  the list of all bindings, with the list of parameter names removed,
	 *  sorted.
	 *
	 */
	 
	public List getInformalParameterNames()
	{
		List result;
		Collection names;
		
		names = inspectedComponent.getBindingNames();
		if (names == null)
			return null;
		
		result = new ArrayList(names);
		
		names = inspectedSpecification.getParameterNames();
		if (names != null)
			result.removeAll(names);
		
		Collections.sort(result);
		
		return result;		
	}
	
	public String getParameterName()
	{
		return parameterName;
	}
	
	public void setParameterName(String value)
	{
		parameterName = value;
	}
	
	/**
	 *  Returns the {@link ParameterSpecification} corresponding to
	 *  the value of the parameterName property.
	 *
	 */
	 
	public ParameterSpecification getParameterSpecification()
	{
		return inspectedSpecification.getParameter(parameterName);
	}
	
	/**
	 *  Returns the {@link IBinding} corresponding to the value of
	 *  the parameterName property.
	 *
	 */
	 
	public IBinding getBinding()
	{
		return inspectedComponent.getBinding(parameterName);
	}

	public void setAssetName(String value)
	{
		assetName = value;
	}
	
	public String getAssetName()
	{
		return assetName;
	}
	
	/**
	 *  Returns the {@link IAsset} corresponding to the value
	 *  of the assetName property.
	 *
	 */
	 
	public IAsset getAsset()
	{
		return (IAsset)inspectedComponent.getAssets().get(assetName);
	}
	
	/**
	 *  Returns a sorted list of asset names, or null if the
	 *  component contains no assets.
	 *
	 */
	 
	public List getAssetNames()
	{
		Map assets;
		List result;
		
		assets = inspectedComponent.getAssets();
		
		if (assets == null)
			return null;
		
		result = new ArrayList(assets.keySet());
		Collections.sort(result);
		
		return result;
	}
}
