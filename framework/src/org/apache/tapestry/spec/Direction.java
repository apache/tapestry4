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

import org.apache.commons.lang.enum.Enum;
import org.apache.tapestry.Tapestry;

/**
 *  Represents different types of parameters.  Currently only 
 *  in and custom are supported, but this will likely change
 *  when Tapestry supports out parameters is some form (that reflects
 *  form style processing).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.3
 *
 */
 
public class Direction extends Enum
{
    /**
     *  The parameter value is input only; the component property value
     *  is unchanged or not relevant after the component renders.
     *  The property is set from the binding before the component renders,
     *  then reset to initial value after the component renders.
     * 
     */
    
	public static final Direction IN = new Direction("IN");
	
    
    /**
     *  Encapsulates the semantics of a form component's value parameter.
     * 
     *  <p>The parameter is associated with a {@link org.apache.tapestry.form.IFormComponent}.
     *  The property value is set from the binding before the component renders (when renderring,
     *  but not when rewinding).
     *  The binding is updated from the property value
     *  after after the component renders when the
     *  <b>containing form</b> is <b>rewinding</b>, <i>and</i>
     *  the component is not {@link org.apache.tapestry.form.IFormComponent#isDisabled() disabled}.
     * 
     *  @since 2.2
     * 
     */

    public static final Direction FORM = new Direction("FORM", false);    
    
	/**
	 *  Processing of the parameter is entirely the responsibility
	 *  of the component, which must obtain an manipulate
	 *  the {@link org.apache.tapestry.IBinding} (if any) for the parameter.
	 * 
	 **/
	
	public static final Direction CUSTOM = new Direction("CUSTOM");


	/**
	 *  Causes a synthetic property to be created that automatically
	 *  references and de-references the underlying binding.
	 * 
	 *  @since 3.0
	 * 
	 */
	
	public static final Direction AUTO = new Direction("AUTO");
	
	/**
	 * If true, then this direction is allowed with invariant bindings (the usual case).
	 * If false, then {@link org.apache.tapestry.param.ParameterManager} will not allow
	 * an invariant binding.
	 * 
	 * @since 3.0
	 */
	
	private boolean _allowInvariant;
	
    protected Direction(String name)
    {
        this(name, true);
    }

	protected Direction(String name, boolean allowInvariant)
	{
		super(name);
		
		_allowInvariant = allowInvariant;
	}
	
	/**
	 * @since 3.0
	 */
	public boolean getAllowInvariant()
	{
		return _allowInvariant;
	}

    /**
     *  Returns a user-presentable name for the direction.
     * 
     */
    
    public String getDisplayName()
    {
        return Tapestry.getMessage("Direction." + getName());
    }
}
