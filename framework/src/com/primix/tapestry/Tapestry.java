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

package com.primix.tapestry;

import com.primix.tapestry.spec.*;
import java.util.*;

/**
 *  A placeholder for a number of (static) methods that don't belong elsewhere.
 *
 *  @since 1.0.1
 *  @version $Id$
 *  @author Howard Ship
 */

public final class Tapestry
{
	/**
	 *  The version of the framework; this is updated for major releases.
	 *  A trailing '+' indicates that the exact version is not known.
	 *
	 */
	
	public static final String VERSION = "1.0.0+";
	
	/**
	 *  Returns true if the value is null or empty (is the empty string,
	 *  or contains only whitespace).
	 *
	 */
	
	public static boolean isNull(String value)
	{
		if (value == null)
			return true;
		
		if (value.length() == 0)
			return true;
		
		return value.trim().length() == 0;
	}
	
	/**
	 *  Copys all informal {@link IBinding bindings} from a source component
	 *  to the destination component.  Informal bindings are bindings for
	 *  informal parameters.  This will overwrite parameters (formal or
	 *  informal) in the
	 *  destination component if there is a naming conflict.
	 *
	 *  <p>This is often used in cases where a "captive" component is used
	 *  to implement some more complicated component, such as
	 * {@link com.primix.tapestry.valid.AbstractValidatingTextField}.
	 *
	 */
	
	public static void copyInformalBindings(IComponent source, IComponent destination)
	{
		Collection names = source.getBindingNames();
		
		if (names == null)
			return;
			
		ComponentSpecification specification = source.getSpecification();
		Iterator i = names.iterator();
		
		while (i.hasNext())
		{
			String name = (String)i.next();
		
			// If not a formal parameter, then copy it over.
			
			if (specification.getParameter(name) == null)
			{
				IBinding binding = source.getBinding(name);
				
				destination.setBinding(name, binding);
			}
		}
	}

}

