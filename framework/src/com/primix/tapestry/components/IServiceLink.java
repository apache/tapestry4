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
 *  A component that renders an HTML &lt;a&gt; element.  It exposes some
 *  properties to the components it wraps.  This is basically to facilitate
 *  the {@link Rollover} component.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public interface IServiceLink extends IComponent
{
    /**
     *  The name of an {@link IRequestCycle} attribute in which the
     *  current service link is stored.  Service links do not nest.
     *
     */
	 
    public static final String ATTRIBUTE_NAME = 
	"com.primix.tapestry.components.IServiceLink";

    /**
     *  Returns whether this service link component is enabled or disabled.
     *
     */
 
    public boolean isEnabled();
	
	/**
	 *  Allows a component to set additional attributes.  This is used by
	 *  {@link Rollover} to set mouse over and mouse out handlers on
	 *  the {@link IServiceLink} that wraps it.
	 *
	 */
	 
	public void setAttribute(String attributeName, String attributeValue);
}
