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

package com.primix.tapestry.engine;

import java.net.URL;
import com.primix.tapestry.*;

/**
 *  Used to resolve classes and resources, this class is used to
 *  bridge some troubling class loader problems.  Typically,
 *  the web application is loaded by one class loader, and (unless
 *  Tapestry is bundled with the web application), the Tapestry framework
 *  is loaded by a different class loader.
 *
 *  <p>This trick is:  classes and resources may be 'hosted' by
 *  either class loader.  Generally, the application class loader
 *  has the 'broadest' view, meaning it's a child class loader to the
 *  framework's class loader.  That means we want to search using it
 *  (because child class loaders delegate to their parent class loader).
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 

class ResourceResolver implements IResourceResolver
{
	private Class resourceClass;
	private ClassLoader classLoader;

	ResourceResolver(Object hook)
	{
		resourceClass = hook.getClass();
		classLoader = resourceClass.getClassLoader();
	}

	public URL getResource(String name)
	{
		URL result;

		// This class loader is related to the web application being loaded.

		result = resourceClass.getResource(name);

		// This class loader is related to the Tapestry framework.
		// To be honest, I'm in over my head with these security frameworks
		// and class loader issues.

		if (result == null)
			result = getClass().getResource(name);

		return result;	
	}

	public Class findClass(String name)
	{
		try
		{
			return Class.forName(name, true, classLoader);
		}
		catch (Throwable t)
		{
			throw new ApplicationRuntimeException(
				"Could not load class " + name + " from " + classLoader
				+ ": " + t.getMessage(), t);
		}
	}
}

