package com.primix.tapestry.inspector;

import com.primix.foundation.*;
import com.primix.tapestry.*;

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

/**
 *  Identifies different views for the inspector.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class View extends Enum
{
	/**
	 *  View that displays the basic specification information, plus
	 *  formal and informal parameters (and related bindings), and 
	 *  assets.
	 *
	 */

	public static final View SPECIFICATION = new View("SPECIFICATION");

	/**
	 *  View that displays the HTML template for the component, if one
	 *  exists.
	 *
	 */

	public static final View TEMPLATE = new View("TEMPLATE");


	/**
	 *  View that shows the persistent properties of the page containing
	 *  the inspected component.
	 *
	 */


	public static final View PROPERTIES = new View("PROPERTIES");

	/**
	 *  View that shows information about the {@link IEngine}.
	 *
	 */

	public static final View ENGINE = new View("ENGINE");

	/**
	 *  View for controlling logging of the application as it runs.
	 *
	 */
	 
	public static final View LOGGING = new View("LOGGING");
	
	private View(String enumerationId)
	{
		super(enumerationId);
	}


	private Object readResolve()
	{
		return getSingleton();
	}
}

