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
 *  Defines a callback, an object which is used to invoke or reinvoke a method
 *  on an object or component in a later request cycle.  This is used to
 *  allow certain operations (say, submitting an order) to defer to other processes
 *  (say, logging in and/or registerring).
 *
 *  <p>Callbacks must be {@link Serializable}, to ensure that they can be stored
 *  between request cycles.
 *
 *  @version $Id$
 *  @author Howard Ship
 *  @since 0.2.9
 *
 */

package com.primix.tapestry.callback;

import com.primix.tapestry.*;
import java.io.*;

public interface ICallback
extends Serializable
{
	/**
	 *  Performs the call back.  Typical implementation will locate a particular
	 *  page or component and invoke a method upon it, or 
	 *  invoke a method on the {@link IRequestCycle cycle}.
	 *
	 */
	 
	public void peformCallback(IRequestCycle cycle)
	throws RequestCycleException;
}
