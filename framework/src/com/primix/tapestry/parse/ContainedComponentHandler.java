package com.primix.tapestry.parse;

import org.xml.sax.AttributeList;
import com.primix.foundation.xml.*;
import com.primix.tapestry.spec.*;
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
 *  Processes the &lt;component&gt; element.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class ContainedComponentHandler extends AbstractElementHandler
{
	private ComponentSpecification spec;
	private ContainedComponent component;
	private String id;

	public void endElement()
	{
		// One handler set the name property, the other(s) set properties of the component.
		// We now have the necessary name and have configured the component, so we can
		// add the ContainedComponent to the ComponentSpecification.

		spec.addComponent(id, component);

			spec = null;
		component = null;
		id = null;	
	}

	/**
	*  Returns the <code>ContainedComponent</code> being assembled.
	*  This is <em>not</em> the focus.
	*
	*/

	public ContainedComponent getComponent()
	{
		return component;
	}

	/**
	*  Returns this <code>ContainedComponentHandler</code>.
	*
	*/

	public Object getFocus()
	{
		return this;
	}

	public void reset()
	{
		characterData = null;
		spec = null;
		component = null;
		id = null;
	}

	public void setId(String value)
	{
		id = value;
	}

	protected void setupHandlers()
	{
		addHandler("id", new PropertyElementHandler("id"));
		addHandler("type", new PropertyElementHandler("component.type"));

		addHandler("binding", new BindingHandler(BindingType.DYNAMIC, "property-path"));
		addHandler("static-binding", new BindingHandler(BindingType.STATIC, "value"));
		addHandler("inherited-binding", new BindingHandler(BindingType.INHERITED, "parameter-name"));
	}

	/**
	*  Creates a new {@link ContainedComponent} which acts as the focus for the
	*  handler.  Expects the parent focus to be a {@link ComponentSpecification}.
	*
	*/

	public void startElement(String tagName, Object parentFocus, AttributeList attributes)
	throws AssemblerException
	{
		spec = (ComponentSpecification)parentFocus;

		component = new ContainedComponent();

		id = null;
	}
}

