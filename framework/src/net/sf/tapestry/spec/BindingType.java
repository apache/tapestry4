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

package net.sf.tapestry.spec;

import net.sf.tapestry.util.Enum;

/**
 *  Defines the different types of bindings possible for a component.
 *  These are used in the {@link BindingSpecification} and ultimately
 *  used to create an instance of {@link net.sf.tapestry.IBinding}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public final class BindingType extends Enum
{
    /**
     *  Indicates a {@link net.sf.tapestry.binding.StaticBinding}.
     *
     **/

    public static final BindingType STATIC = new BindingType("STATIC");

    /**
     *  Indicates a standard {@link net.sf.tapestry.binding.ExpressionBinding}.
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
     *  Indicates a {@link net.sf.tapestry.binding.FieldBinding}.
     *
     **/

    public static final BindingType FIELD = new BindingType("FIELD");


	/**
	 *  A binding to one of a component's localized strings.
	 * 
	 *  @see net.sf.tapestry.IComponent#getString(String)
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