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

package org.apache.tapestry.spec;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines a contained component.  This includes the information needed to
 * get the contained component's specification, as well as any bindings
 * for the component.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public class ContainedComponent extends LocatablePropertyHolder implements IContainedComponent
{
	private String type;

	private String copyOf;
    
    private boolean inheritInformalParameters;

	protected Map bindings;

	private static final int MAP_SIZE = 3;

	/**
	 *  Returns the named binding, or null if the binding does not
	 *  exist.
	 *
	 **/

	public IBindingSpecification getBinding(String name)
	{
		if (bindings == null)
			return null;

		return (IBindingSpecification) bindings.get(name);
	}

	/**
	 *  Returns an umodifiable <code>Collection</code>
	 *  of Strings, each the name of one binding
	 *  for the component.
	 *
	 **/

	public Collection getBindingNames()
	{
		if (bindings == null)
			return Collections.EMPTY_LIST;

		return Collections.unmodifiableCollection(bindings.keySet());
	}

	public String getType()
	{
		return type;
	}

	public void setBinding(String name, IBindingSpecification spec)
	{
		if (bindings == null)
			bindings = new HashMap(MAP_SIZE);

		bindings.put(name, spec);
	}

	public void setType(String value)
	{
		type = value;
	}

	/**
	 * 	Sets the String Id of the component being copied from.
	 *  For use by IDE tools like Spindle.
	 * 
	 *  @since 1.0.9
	 **/

	public void setCopyOf(String id)
	{
		copyOf = id;
	}

	/**
	 * 	Returns the id of the component being copied from.
	 *  For use by IDE tools like Spindle.
	 * 
	 *  @since 1.0.9
	 **/

	public String getCopyOf()
	{
		return copyOf;
	}

	/**
     * Returns whether the contained component will inherit 
     * the informal parameters of its parent. 
     * 
	 * @since 3.0
	 **/
	public boolean getInheritInformalParameters()
	{
		return inheritInformalParameters;
	}

	/**
     * Sets whether the contained component will inherit 
     * the informal parameters of its parent. 
     * 
     * @since 3.0
	 */
	public void setInheritInformalParameters(boolean value)
	{
		inheritInformalParameters = value;
	}

}