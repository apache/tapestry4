package com.primix.vlib.jsp;

import com.primix.servlet.*;
import javax.ejb.*;
import com.primix.vlib.ejb.*;
import java.util.*;
import javax.servlet.*;
import java.io.*;

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

public abstract class VlibDelegate
implements IService, Serializable
{
	protected final VirtualLibraryApplication application;
	
	/**
	 *  Creates the VlibDelegate and locates the {@link VirtualLibraryApplication}.
	 *
	 */
	 
	public VlibDelegate(RequestContext context)
	{
		application = VirtualLibraryApplication.get(context);
	}
	
	public VirtualLibraryApplication getApplication()
	{
		return application;
	}
	
	
	/**
	 *  Sets up the invocation of the JSP (to render output), first
	 *  setting the <code>page.title</code> and <code>page.subtitle</code>
	 *  request attributes.  Also, sets the <code>delegate</code>
	 *  request attribute to this.
	 *
	 */
	 
	public void forward(String jspName, String pageTitle, String pageSubtitle,
		RequestContext context)
	throws ServletException, IOException
	{
		context.setAttribute("page.title", pageTitle);
		
		if (pageSubtitle != null)
			context.setAttribute("page.subtitle", pageSubtitle);
		
		context.setAttribute("delegate", this);
		
		context.forward(jspName);
	}
}
