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
 *  Processes the &lt;component-alias&gt; element within an &lt;application&gt;
 *  element.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class ComponentDefinitionHandler extends AbstractElementHandler
{
	private ApplicationSpecification spec;
	private String alias;
	private String specificationPath;

	/**
	*  Invokes <code>ApplicationSpecification.setComponentAlias()</code>.
	*
	*  @see ApplicationSpecification#setComponentAlias(String,String)
	*/

	public void endElement()
	throws AssemblerException
	{
		spec.setComponentAlias(alias, specificationPath);
	}

	/**
	*  Returns this <code>ComponentDefinitionHandler</code>.
	*
	*/

	public Object getFocus()
	{
		return this;
	}

	public void reset()
	{
		characterData = null;
		alias = null;
		specificationPath = null;
	}

	public void setAlias(String value)
	{
		alias = value;
	}

	public void setSpecificationPath(String value)
	{
		specificationPath = value;
	}

	protected void setupHandlers()
	{
		addHandler("alias", new PropertyElementHandler("alias"));
		addHandler("specification-path", new PropertyElementHandler("specificationPath"));
	}

	/**
	* Expects the parentFocus to be a {@link ApplicationSpecification}.
	*
	*/

	public void startElement(String name, Object parentFocus, AttributeList attributes)
	throws AssemblerException
	{
		alias = null;
		specificationPath = null;

		spec = (ApplicationSpecification)parentFocus;
	}
}

