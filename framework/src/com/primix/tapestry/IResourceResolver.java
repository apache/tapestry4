/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry;

import java.net.URL;

/**
 * An object which is used to resolve classes and class-path resources.
 * This is needed because, in an application server, different class loaders
 * will be loading the Tapestry framework and the specific Tapestry application.
 *
 * <p>The class loader for the framework needs to be able to see resources in
 * the application, but the application's class loader is a descendent of the
 * framework's class loader.  To resolve this, we need a 'hook', an instance
 * that provides access to the application's class loader.
 * 
 *
 * @author Howard Ship
 * @version $Id$
 */

public interface IResourceResolver
{
	/**
	 *  Forwarded, unchanged, to the class loader.  Returns null if the
	 *  resource is not found.
	 *
	 */

	public URL getResource(String name);

	/**
	 *  Forwarded, to the the method
	 *  <code>Class.forName(String, boolean, ClassLoader)</code>, using
	 *  the application's class loader.
	 *
	 *  Throws an {@link ApplicationRuntimeException} on any error.
	 */

	public Class findClass(String name);
}