package com.primix.tapestry.parse;

import org.xml.sax.AttributeList;
import com.primix.foundation.xml.*;
import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;

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
 *  Processes the &lt;parameter&gt; element for a {@link ComponentSpecificationHandler}.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class ParameterSpecificationHandler extends AbstractElementHandler
{
	private String name;
	private ComponentSpecification componentSpecification;
	private ParameterSpecification parameterSpecification;

	public void endElement()
	{
		componentSpecification.addParameter(name, parameterSpecification);

		componentSpecification = null;
		parameterSpecification = null;
		name = null;
	}

	/**
	*  Returns this <code>ParameterSpecificationHandler</code>.
	*
	*/

	public Object getFocus()
	{
		return this;
	}

	/**
	*  Returns the {@link ParameterSpecification} created by
	*  {@link #startElement(String, Object, AttributeList)}.
	*
	*/

	public ParameterSpecification getParameter()
	{
		return parameterSpecification;
	}

	public void reset()
	{
		characterData = null;

		name = null;
		componentSpecification = null;
		parameterSpecification = null;
	}

	public void setName(String value)
	{
		name = value;
	}

	protected void setupHandlers()
	{
		addHandler("name", new PropertyElementHandler("name"));
		addHandler("java-type", new PropertyElementHandler("parameter.type"));
		addHandler("required", new BooleanPropertyHandler("parameter.required"));
	}

	/**
	*  Creates a new {@link ParameterSpecification} as the focus.  Expects
	*  the parentFocus to be a {@link ComponentSpecification}.
	*
	*/

	public void startElement(String name, Object parentFocus, AttributeList attributes)
	throws AssemblerException
	{
		characterData = null;
		name = null;

		componentSpecification = (ComponentSpecification)parentFocus;
		parameterSpecification = new ParameterSpecification();
	}
}

