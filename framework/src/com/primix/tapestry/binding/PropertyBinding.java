package com.primix.tapestry.binding;

import com.primix.foundation.prop.*;
import java.util.*;
import com.primix.tapestry.*;

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
 *  Implements a dynamic binding, based on getting and fetching
 *  values using JavaBeans property access.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class PropertyBinding extends AbstractBinding
{
	/**
	*  The root object against which the nested property name is evaluated.
	*
	*/

	private IComponent root;

	/**
	*  Allows dynamic property access to the root object.
	*
	*/

	private PropertyHelper helper;

	/**
	*  The name of the property to bind to.  This may be either a
	*  simple name, or a nested property name.  A nested property
	*  name consists of a series of simple names seperated by
	*  periods.  In this way the path can 'navigate' to various other
	*  objects. For example, the property 'page.application.foo' is
	*  equivalent to the expression
	*  <code>getPage().getApplication().getFoo()</code>.
	*
	*/

	private String propertyPath;

	/**
	*  Creates a {@link PropertyBinding} from the root object
	*  and a nested property name.
	*
	*  <p>There's a lot of room for optimization here because we can
	*  count on some portions of the nested property name to be
	*  effectively static.  Note that we type the root object as
	*  {@link IComponent}.  We have some expectations that
	*  certain properties of the root and reachable from the root
	*  will be constant for the lifetime of the binding.  This means
	*  that certain property prefixes can be optimized, such as:
	*
	* <ul>
	* <li>page
	* <li>page.application
	* <li>container
	* <li>container.name
	* </ul>
	*
	* <p>Another option (much more involved) is to replace the
	* dynamic property access, which depends upon reflection (i.e.,
	* the <code>Method</code> class), with dynamically generated
	* bytecodes.  This has been done before, to create <a
	* href="http://java.sun.com/products/jfc/tsc/articles/generic-listener/index.html">
	* dynamic adapter classes</a>.
	*
	* <p>These operate orders-of-magnitude faster, though there is
	* the question of building the bytecodes (non trivial!) and all
	* the other classloader and security issues.
	*
	* <p>In the meantime, no optimization is done.
	*/

	public PropertyBinding(IComponent root, String propertyPath)
	{
		this.root = root;
		this.propertyPath = propertyPath;
	}

	public String getPropertyPath()
	{
		return propertyPath;
	}

	public IComponent getRoot()
	{
		return root;
	}

	/**
	 *  Gets the value of the property path, with the assistance of a 
	 *  {@link PropertyHelper}.
	 *
	 *  @throws BindingException if an exception is thrown accessing the property.
	 *
	 */
	 
	public Object getValue()
	{
		if (helper == null)
			setupHelper();

		try
		{
			return helper.get(root, propertyPath);
		}
		catch (Throwable e)
		{
			String message = e.getMessage();
			StringBuffer buffer;
			
			buffer = new StringBuffer("Unable to resolve property ");
			buffer.append(propertyPath);
			buffer.append(" of ");
			buffer.append(root);
			
			if (message == null)
				buffer.append('.');
			else
			{
				buffer.append(": ");
				buffer.append(message);
			}		

			throw new BindingException(buffer.toString(), this, e);
		}
	}

	/**
	 *  Returns false.
	 *
	 */
	 
	public boolean isStatic()
	{
		return false;
	}

	public void setBoolean(boolean value) throws ReadOnlyBindingException
	{
		setValue(value ? Boolean.TRUE : Boolean.FALSE);
	}

	public void setInt(int value) throws ReadOnlyBindingException
	{
		setValue(new Integer(value));
	}

	public void setString(String value) throws ReadOnlyBindingException
	{
		setValue(value);
	}

	private void setupHelper()
	{
		helper = PropertyHelper.forClass(root.getClass());
	}

	/**
	 *  Updates the property for the binding to the given value.  
	 *
	 *  @throws BindingException if the property can't be updated (typically
	 *  due to an security problem, or a missing mutator method).
	 */
	 
	public void setValue(Object value)
	{
		if (helper == null)
			setupHelper();

		try
		{
			helper.set(root, propertyPath, value);
		}
		catch (Throwable e)
		{
			String message = e.getMessage();
			StringBuffer buffer;
			
			buffer = new StringBuffer("Unable to update property ");
			buffer.append(propertyPath);
			buffer.append(" of ");
			buffer.append(root);
			buffer.append(" to <");
			buffer.append(value);
			
			if (message == null)
				buffer.append(">.");
			else
			{
				buffer.append(">: ");
				buffer.append(message);
			}		

			throw new BindingException(buffer.toString(), this, e);
		}
		
	}

	public String toString()
	{
		return "PropertyBinding[" + root.getExtendedId() + " " + propertyPath + "]";
	}
}

