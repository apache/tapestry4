package com.primix.vlib.jsp;

import com.primix.servlet.*;
import com.primix.vlib.ejb.*;

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
 *  Servlet for the {@link HomeDelegate} page.  Should be mapped
 *  to the URI <code>/home/*</code>.
 *
 *  @version $Id$
 *  @author Howard Ship
 */
 
public class HomeServlet extends VlibServlet
{
	/**
	 *  Returns an instance of {@link HomeDelegate}, either one previously
	 *  stored in the {@link HttpSession}, or a fresh instance.
	 *
	 */
	 
	protected IService getDelegate(RequestContext context)
	{
		return HomeDelegate.get(context);
	}
	
	public static void writeLink(RequestContext context, HTMLWriter writer)
	{
		writeLink(context, writer, "/home", "[Home]");
	}
	
	public static void writeBorrowLink(RequestContext context, HTMLWriter writer,
		Book book)
	{
		VirtualLibraryApplication application;
		String label = "[Borrow]";
		
		application = VirtualLibraryApplication.get(context);
		
		if (application.isLoggedInUser(book.getHolderPrimaryKey()))
		{
			writer.print(label);
			return;
		}
		
		writeLink(context, writer, "/home/" + book.getPrimaryKey(), label);
	}
}
