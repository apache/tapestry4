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
 
package com.primix.tapestry.components;

import com.primix.tapestry.util.*;

/**
 *  Different types of JavaScript events that an {@link IServiceLink}
 *  can provide handlers for.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 0.2.9
 *
 */

public class ServiceLinkEventType
extends Enum
{
	private String attributeName;
	
	public static final ServiceLinkEventType MOUSE_OVER
		= new ServiceLinkEventType("MOUSE_OVER", "onMouseOver");
		
	public static final ServiceLinkEventType MOUSE_OUT
		= new ServiceLinkEventType("MOUSE_OUT", "onMouseOut");
		
	/**
	 *  Constructos a new type of event.  The name should match the
	 *  static final variable (i.e., MOUSE_OVER) and the attributeName
	 *  is the name of the HTML attribute to be managed (i.e., "onMouseOver").
	 *
	 *  <p>This method is protected so that subclasses can be created
	 *  to provide additional managed event types.
	 */
	 
	protected ServiceLinkEventType(String name, String attributeName)
	{
		super(name);
		
		this.attributeName = attributeName;
	}
	
	public String getAttributeName()
	{
		return attributeName;
	}

	private Object readResolve()
	{
		return getSingleton();
	}
	
}
