//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry;

import java.util.Collection;

/**
 *  An object that provides a component with access to helper beans.
 *  Helper beans are JavaBeans associated with a page or component
 *  that are used to extend the functionality of the component via
 *  aggregation.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.4
 **/


public interface IBeanProvider
{
	/**
	 *  Returns the JavaBean with the specified name.  The bean is created as needed.
	 *
	 *  @throws ApplicationRuntimeException if no such bean is available.
	 *
	 **/
	
	public Object getBean(String name);
	
	/**
	 *  Returns the {@link IComponent} (which may be a 
	 *  {@link net.sf.tapestry.IPage}) for which
	 *  this bean provider is providing beans.
	 *
	 *  @since 1.0.5
	 **/
	
	public IComponent getComponent();
	
	/**
	 *  Returns a collection of the names of any beans which may
	 *  be provided.
	 *
	 *  @since 1.0.6
	 *  @see net.sf.tapestry.spec.ComponentSpecification#getBeanNames()
	 *
	 **/
	
	public Collection getBeanNames();
	
	/**
	 *  Returns a resource resolver.
	 * 
	 *  @since 1.0.8
	 * 
	 **/
	
	public IResourceResolver getResourceResolver();
	
}

