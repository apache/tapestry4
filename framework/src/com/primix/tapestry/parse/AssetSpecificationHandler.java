package com.primix.tapestry.parse;

import org.xml.sax.AttributeList;
import com.primix.foundation.*;
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
 *  Parses an name and path and constructs a {@link AssetSpecification}.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class AssetSpecificationHandler extends AbstractElementHandler
{
	private AssetType type;
	private String innerElementName;

	private ComponentSpecification componentSpecification;
	private String name;
	private String path;

	public AssetSpecificationHandler(AssetType type, String innerElementName)
	{
		this.type = type;
		this.innerElementName = innerElementName;
	}

	/**
	*  Constructs a {@link AssetSpecification} and adds it to the
	*  the {@link ComponentSpecification}.
	*
	*/

		public void endElement() throws AssemblerException
	{
		AssetSpecification assetSpec;

		assetSpec = new AssetSpecification(type, path);

		componentSpecification.addAsset(name, assetSpec);

			name = null;
		path = null;
		componentSpecification = null;
	}

	/**
	*  Returns this <code>AssetSpecificationHandler</code>.
	*
	*/

	public Object getFocus()
	{
		return this;
	}

	public void reset()
	{
		characterData = null;

		name = null;
		path = null;
		componentSpecification = null;
	}

	public void setName(String value)
	{
		name = value;
	}

	public void setPath(String value)
	{
		path = value;
	}

	protected void setupHandlers()
	{
		addHandler("name", new PropertyElementHandler("name"));
		addHandler(innerElementName, new PropertyElementHandler("path"));
	}

	/**
	*  Expects <code>parentFocus</code> to be a {@link
	*  ComponentSpecification}.
	*
	*/

		public void startElement(String name, Object parentFocus, AttributeList attributes) 
	throws AssemblerException
	{
		name = null;
		path = null;

		componentSpecification = (ComponentSpecification)parentFocus;
	}
}

