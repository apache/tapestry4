package com.primix.tapestry.parse;

import org.xml.sax.AttributeList;
import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.foundation.xml.*;
import java.util.*;

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
 *  Processes the &lt;binding&gt;, &lt;static-binding&gt; and
 *  &lt;inherited-binding&gt; elements.
 *
 * @see ComponentSpecificationHandler
 * @see BindingSpecification
 *
 * @author Howard Ship
 * @version $Id$
 */


public class BindingHandler extends AbstractElementHandler
{
	private BindingType type;
	private String innerElementName;

	private ContainedComponent component;
	private BindingSpecification binding;

	private String name;

	public BindingHandler(BindingType type, String innerElementName)
	{
		this.type = type;
		this.innerElementName = innerElementName;
	}

	/**
	*  Adds the {@link BindingSpecification} to the containing {@link
	*  ContainedComponent}.
	*
	*/

	public void endElement()
	throws AssemblerException
	{
		component.setBinding(name, binding);
	}

	public BindingSpecification getBinding()
	{
		return binding;
	}

	/**
	*  Returns <code>this</code>.  The nested element handlers can
	*  change properties * of this <code>BindingHandler</code>, or
	*  through the binding property, the * {@link
	*  BindingSpecification}.
	*
	*/

	public Object getFocus()
	{
		return this;
	}

	public void reset()
	{
		characterData = null;
		component = null;
		name = null;
		binding = null;
	}

	public void setName(String value)
	{
		name = value;
	}

	protected void setupHandlers()
	{
		addHandler("name", new PropertyElementHandler("name"));
		addHandler(innerElementName, new PropertyElementHandler("binding.value"));
	}

	/**
	*  Starts the element.  Expects the parent handler's focus to be
	*  a {@link ContainedComponentHandler}.
	*
	*/

	public void startElement(String name, Object parentFocus, AttributeList attributes)
	throws AssemblerException
	{
		name = null;

		component = ((ContainedComponentHandler)parentFocus).getComponent();
		binding = new BindingSpecification(type);
	}
}

