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

import com.primix.tapestry.*;

/** 
 *  Prevents its contents from being rendered until triggered by
 *  an {@link InsertBlock} component.
 *
 *  <p>Has no parameters and does not allow informal parameters.  Allows
 *  a body (in fact, its pretty useless without one).
 *
 *  <p>Block and {@link InsertBlock} are used to build a certain class
 *  of complicated component that can't be assembled using the normal
 *  wrapping containment.  Such a super component would have two or more
 *  sections that need to be supplied by the containing page (or component).
 *
 *  <p>Using Blocks, the blocks can be provided as parameters to the super
 *  component.
 *
 *  @version $Id$
 *  @author Howard Ship
 *  @since 0.2.9
 */
 
public class Block
extends AbstractComponent
{
	/**
	 *  Does nothing; the idea of a Block is to defer the rendering of
	 *  the body of the block until a {@link InsertBlock} forces it
	 *  out.
	 *
	 */
	 
	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		// Nothing!
	}
}
