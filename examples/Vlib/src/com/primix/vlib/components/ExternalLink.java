package com.primix.vlib.components;

import com.primix.tapestry.components.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.*;
import com.primix.vlib.*;

/*
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
 * but WITHOUT ANY WARRANTY; wihtout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Creates a link to a page using the external service.
 *
 * @author Howard Ship
 * @version $Id$
 */


public abstract class ExternalLink extends AbstractServiceLink
{
	private IBinding primaryKeyBinding;
	private String[] context;
	
	public ExternalLink(IPage page, IComponent container, String id, ComponentSpecification spec) 
	{
		super(page, container, id, spec);
	}
	
	public String getServiceName(IRequestCycle cycle)
	{
		return VirtualLibraryApplication.EXTERNAL_SERVICE;
	}
	
	/**
	 *  Overriden in subclasses to specify the name of the page.
	 *
	 */
	 
	protected abstract String getPageName();
	
	public void setPrimaryKeyBinding(IBinding value)
	{
		primaryKeyBinding = value;
	}
	
	public IBinding getPrimaryKeyBinding()
	{
		return primaryKeyBinding;
	}
	
	public String[] getContext(IRequestCycle cycle)
	throws RequestCycleException
	{
		Integer primaryKey;
		
		primaryKey = (Integer)primaryKeyBinding.getValue();
		
		if (primaryKey == null)
			throw new RequiredParameterException(this, "primaryKey", primaryKeyBinding, cycle);
		
		if (context == null)
		{
			context = new String[2];context = new String[2];
			context[0] = getPageName();
		}
		
		context[1] = primaryKey.toString();
		
		return context;
	}
}
