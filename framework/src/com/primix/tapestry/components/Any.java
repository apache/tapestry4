package com.primix.tapestry.components;

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
 *  A component that can substitute for any HTML element.  
 *
 * <table border=1>
 * <tr> 
 *    <th>Parameter</th>
 *    <th>Type</th>
 *	  <th>Read / Write </th>
 *    <th>Required</th> 
 *    <th>Default</th>
 *    <th>Description</th>
 * </tr>
 *
 * <tr>
 *  <td>element</td>
 *  <td>java.lang.String</td>
 *  <td>R</td>
 *  <td>yes</td>
 *  <td>&nbsp;</td>
 *  <td>The element to be produced.</td>
 * </tr>
 *
 * <p>Informal parameters are allowed.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Any extends AbstractComponent
{
	private IBinding elementBinding;

	public IBinding getElementBinding()
	{
		return elementBinding;
	}

	public void render(IResponseWriter writer, IRequestCycle cycle) 
	throws RequestCycleException
	{
		String elementName = null;

		if (!cycle.isRewinding())
		{

			elementName = elementBinding.getString();
			if (elementName == null)
				throw new RequiredParameterException(this, "element", elementBinding, cycle);

			writer.begin(elementName);

			generateAttributes(cycle, writer, null);
		}

		if (wrappedCount > 0)
			renderWrapped(writer, cycle);

		if (!cycle.isRewinding())
		{
			writer.end(elementName);
		}

	}

	public void setElementBinding(IBinding value)
	{
		elementBinding = value;

		// This is not expected to be static because then, what's the point?
	}
}

