package com.primix.tapestry.parse;

import org.xml.sax.*;
import java.util.*;
import com.primix.tapestry.spec.*;
import com.primix.foundation.xml.*;
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
 * Used to assemble an <code>ApplicationSpecification</code> from
 * an XML document.
 *
 *
 * @author Howard Ship
 * @version $Id$
 */


public class ApplicationSpecificationHandler
    extends AbstractElementHandler implements IRootElementHandler
{
	private ApplicationSpecification spec;

	/**
	*  Returns the {@link ApplicationSpecification}.
	*
	*/

	public Object getFocus()
	{
		return spec;
	}

	/**
	*  Returns "application".
	*
	*/

	public String getRootElementTag()
	{
		return "application";
	}

	/**
	*  Returns the {@link ApplicationSpecification}.
	*
	*/

	public Object getRootObject()
	{
		return spec;
	}

	public void reset()
	{
		characterData = null;
		spec = null;
	}

	protected void setupHandlers()
	{
		addHandler("name", new PropertyElementHandler("name"));
		addHandler("page", new PageSpecificationHandler());
		addHandler("component-alias", new ComponentDefinitionHandler());
		// handler for properties ...
	}

	/**
	*  Creates a new {@link ApplicationSpecification}.
	*
	*/

	public void startElement(String name, Object parentFocus, AttributeList attributes)
	throws AssemblerException
	{
		spec = new ApplicationSpecification();
	}
}

