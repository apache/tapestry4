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

/**
 *  Defines the different types of bindings possible for a component.
 *  These are used in the {@link IBindingSpecification} and ultimately
 *  used to create an instance of {@link org.apache.tapestry.IBinding}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public final class BindingType extends Enum
{
    /**
     *  Indicates a {@link org.apache.tapestry.binding.StaticBinding}.
     *
     **/

    public static final BindingType STATIC = new BindingType("STATIC");

    /**
     *  Indicates a standard {@link org.apache.tapestry.binding.ExpressionBinding}.
     *
     **/

    public static final BindingType DYNAMIC = new BindingType("DYNAMIC");

    /**
     *  Indicates that an existing binding (from the container) will be
     *  re-used.
     *
     **/

    public static final BindingType INHERITED = new BindingType("INHERITED");

    /**
     *  Indicates a {@link org.apache.tapestry.binding.FieldBinding}.
     *
     *  <p>
     *  Field bindings are only available in the 1.3 DTD.  The 1.4 DTD
     *  does not support them (since OGNL expressions can do the same thing).
     * 
     **/

    public static final BindingType FIELD = new BindingType("FIELD");

    /**
     *  Indicates a {@link org.apache.tapestry.binding.ListenerBinding}, a
     *  specialized kind of binding that encapsulates a component listener
     *  as a script.  Uses a subclass of {@link BindingSpecification},
     *  {@link ListenerBindingSpecification}.
     *  {@link IListenerBindingSpecification}.
     * 
     *  @since 3.0
     * 
     **/
    
    public static final BindingType LISTENER = new BindingType("LISTENER");

	/**
	 *  A binding to one of a component's localized strings.
	 * 
	 *  @see org.apache.tapestry.IComponent#getString(String)
	 * 
	 *  @since 2.0.4
	 * 
	 **/
	
	public static final BindingType STRING = new BindingType("STRING");
	
    private BindingType(String name)
    {
        super(name);
    }

}