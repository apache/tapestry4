/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
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

package com.primix.tapestry.spec;

import com.primix.tapestry.bean.*;
import java.util.*;

/**
 *  A specification of a helper bean for a component.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.4
 */


public class BeanSpecification
{
	private String className;
	private BeanLifecycle lifecycle;
		
	/**
	 *  A List of {@link IBeanInitializer}.
	 *
	 */
	
	private List initializers;
	
	public BeanSpecification(String className, BeanLifecycle lifecycle)
	{
		this.className = className;
		this.lifecycle = lifecycle;
	}
	
	public String getClassName()
	{
		return className;
	}
	
	public BeanLifecycle getLifecycle()
	{
		return lifecycle;
	}

	/**
	 *  @since 1.0.5
	 *
	 */
	
	public void addInitializer(IBeanInitializer initializer)
	{
		if (initializers == null)
			initializers = new ArrayList();
		
		initializers.add(initializer);
	}
	
	/**
	 *  Returns the {@link List} of {@link IBeanInitializers}.  The caller
	 *  should not modify this value!.  May return null if there
	 *  are no initializers.
	 *
	 *  @since 1.0.5
	 *
	 */
	
	public List getInitializers()
	{
		return initializers;
	}
	
	public String toString()
	{
		StringBuffer buffer = new StringBuffer("BeanSpecification[");
		
		buffer.append(className);
		buffer.append(", lifecycle ");
		buffer.append(lifecycle.getEnumerationId());
		
		if (initializers != null &&
				initializers.size() > 0)
		{
			buffer.append(", ");
			buffer.append(initializers.size());
			buffer.append(" initializers");
		}
		
		buffer.append(']');
		
		return buffer.toString();
	}
}

