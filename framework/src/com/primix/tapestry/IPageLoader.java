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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry;

import com.primix.tapestry.spec.ComponentSpecification;

/**
 * Interface exposed to components as they are loaded by the page loader.
 *
 * @see IComponent#finishLoad(IPageLoader, ComponentSpecification)
 *
 * @author Howard Ship
 * @version $Id$
 */

public interface IPageLoader
{
	/**
	 *  Returns the engine for which this page loader is curently
	 *  constructing a page.
	 *
	 * @since 0.2.12
	 */
	
	public IEngine getEngine();
	
	/**
	 *  A convienience; returns the template source provided by
	 *  the {@link IEngine engine}.
	 *
	 * @since 0.2.12
	 */
	
	public ITemplateSource getTemplateSource();
}
