package com.primix.tapestry.parse;

import org.xml.sax.AttributeList;
import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.foundation.xml.*;
import java.util.*;
import com.primix.foundation.*;

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
 *  Handles the &lt;property&gt; element, which contains &lt;name&gt;
 *  and &lt;value&gt;.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
  
public class PropertyHandler extends AbstractElementHandler
{
	private IPropertyHolder holder;
	
	private String name;
	private String value;
	
	public void setName(String value)
	{
		name = value;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}
	
	/**
	 *  Returns this <code>PropertyHandler</code>.
	 *
	 */
	 
	public Object getFocus()
	{
		return this;
	}
	
	public void reset()
	{
		characterData = null;
	
		holder = null;
		name = null;
		value = null;
	}
	
	public void startElement(String name, Object parentFocus, AttributeList
	attributes) throws AssemblerException
	{
		holder = (IPropertyHolder)parentFocus;
		name = null;
		value = null;
	}
	
	public void endElement() throws AssemblerException
	{
		holder.setProperty(name, value);
		
		holder = null;
		name = null;
		value = null;
	}
	
	protected void setupHandlers()
	{
		addHandler("name", new PropertyElementHandler("name"));
		addHandler("value", new PropertyElementHandler("value"));
	}
}
