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

/**
 *  Defines a formal parameter to a component.  A <code>ParameterSpecification</code>
 *  is contained by a {@link ComponentSpecification}.
 *
 *  <p>TBD:  Support for specifying read, write, read/write.  Identify arrays in some
 *  way.  Distinguish between the type allowed and the type cooreced to (to boolean,
 *  to int, to String).  In reality, very little is done with the parameter type, and
 * the required property just ensures that the parameter has a binding, not that the binding
 *  produces a non-null value.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

package com.primix.tapestry.spec;

public class ParameterSpecification
{
	private boolean required = false;
	private String type = "java.lang.Object";	

	/**
	*  Returns the class name of the expected type of the parameter.  The default value
	*  is <code>java.lang.Object</code> which matches anything.
	*
	*/

	public String getType()
	{
		return type;
	}

	/**
	*  Returns true if the parameter is required by the component.
	*  The default is false, meaning the parameter is optional.
	*
	*/

	public boolean isRequired()
	{
		return required;
	}

	public void setRequired(boolean value)
	{
		required = value;
	}

	public void setType(String value)
	{
		type = value;
	}
}

