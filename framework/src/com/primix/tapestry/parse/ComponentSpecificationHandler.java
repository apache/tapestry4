package com.primix.tapestry.parse;

import org.xml.sax.AttributeList;
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
 *  Assembles a {@link ComponentSpecification} from an XML document.
 *
 * @author Howard Ship
 * @version $Id$
 */



public class ComponentSpecificationHandler
    extends AbstractElementHandler
    implements IRootElementHandler
{
	private ComponentSpecification specification;

	/**
	*  Returns the {@link ComponentSpecification}.
	*
	*/

	public Object getFocus()
	{
		return specification;
	}

	/**
	*  Returns "specification", the root element of a XML component specification document.
	*
	*/

	public String getRootElementTag()
	{
		return "specification";
	}

	/**
	*  Returns the <code>ComponentSpecification</code>.
	*
	*  @see ComponentSpecification
	*
	*/

	public Object getRootObject()
	{
		return specification;
	}

	public void reset()
	{
		characterData = null;

		specification = null;
	}

	protected void setupHandlers()
	{
		addHandler("class", new PropertyElementHandler("componentClassName"));
		addHandler("component", new ContainedComponentHandler());

		addHandler("allow-body", new BooleanPropertyHandler("allowBody"));
		addHandler("allow-informal-parameters",
			new BooleanPropertyHandler("allowInformalParameters"));
		addHandler("parameter", new ParameterSpecificationHandler());

		addHandler("external-asset", new AssetSpecificationHandler(AssetType.EXTERNAL, "URL"));
		addHandler("internal-asset", new AssetSpecificationHandler(AssetType.INTERNAL, "path"));
		addHandler("private-asset", new AssetSpecificationHandler(AssetType.PRIVATE, "resource-path"));

		addHandler("property", new PropertyHandler());
	}

	/**
	*  Creates a new {@link ComponentSpecification} as the focus.
	*
	*
	*/

	public void startElement(String name, Object parentFocus, AttributeList attributes)
	throws AssemblerException
	{
		characterData = null;

		specification = new ComponentSpecification();
	}
}

