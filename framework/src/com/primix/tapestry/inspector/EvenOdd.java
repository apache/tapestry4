/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
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


package com.primix.tapestry.inspector;

import com.primix.tapestry.*;
import java.util.*;

/**
 *  Used to create &lt;tr;&gt; tags with alternating values
 *  of "even" and "odd" for the HTML class attribute.  The sequence always
 *  starts with "even".
 *
 *  <p>Allows informal parameters.  Allows a body
 *  (in fact, doesn't make sense without one).
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class EvenOdd extends AbstractComponent
implements ILifecycle
{
	private boolean even;
	private IBinding initialBinding;
	
	private String[] reservedNames = 
	{ "class"
	};
	
	public IBinding getInitialBinding()
	{
		return initialBinding;
	}

	public void setInitialBinding(IBinding value)
	{
		initialBinding = value;
	}

	private String getNextClass()
	{
		String result;
		
		result = even ? "even" : "odd";
		
		even = !even;
		
		return result;
	}
	
	/**
	 *  Sets the initial value for the component.  The default behavior
	 *  is for the first line to be "even" and the second to be "odd".
	 *  If the initial parameter is bound, then it is used to
	 *  determine the initial value ... true is the normal behavior (even
	 *  first), false is the alternate behavior (odd first).
	 *
	 */
	 
	public void prepareForRender(IRequestCycle cycle)
	{
		even = true;
		
		if (initialBinding != null)
			even = initialBinding.getBoolean();
	}
	
	public void render(IResponseWriter writer,
                   IRequestCycle cycle)
    throws RequestCycleException
    {
		writer.begin("tr");
		writer.attribute("class", getNextClass());
		
		generateAttributes(cycle, writer, reservedNames);
		
		renderWrapped(writer, cycle);
		
		writer.end();
    }
}
