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

package org.apache.tapestry;

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
	 *  {@link org.apache.tapestry.IPage}) for which
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
	 *  @see org.apache.tapestry.spec.IComponentSpecification#getBeanNames()
	 *
	 **/
	
	public Collection getBeanNames();
	
    /**
     *  Returns true if the provider can provide the named bean.
     * 
     *  @since 2.2
     * 
     **/
    
    public boolean canProvideBean(String name);
    
	/**
	 *  Returns a resource resolver.
	 * 
	 *  @since 1.0.8
	 * 
	 **/
	
	public IResourceResolver getResourceResolver();
	
}

