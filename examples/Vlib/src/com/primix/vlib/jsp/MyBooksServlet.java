package com.primix.vlib.jsp;

import com.primix.servlet.*;
import javax.ejb.*;
import com.primix.vlib.ejb.*;
import java.util.*;
import javax.servlet.*;
import com.primix.tapestry.ApplicationRuntimeException;

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
 *  Servlet for the {@link MyBooksDelegate} page.  Should be mapped
 *  to the URI <code>/mybooks/*</code>.
 *
 *  @version $Id$
 *  @author Howard Ship
 */
 
public class MyBooksServlet extends GatewayServlet
{
	/**
	 *  Returns an instance of {@link MyBooksDelegate}, either one previously
	 *  stored in the {@link HttpSession}, or a fresh instance.
	 *
	 */
	 
	protected IService getDelegate(RequestContext context)
	{
		return MyBooksDelegate.get(context);
	}
}
