package com.primix.vlib;

import com.primix.tapestry.*;
import com.primix.tapestry.engine.*;
import com.primix.tapestry.components.*;
import javax.naming.*;
import javax.ejb.*;
import com.primix.vlib.ejb.*;
import java.rmi.*;
import javax.rmi.*;
import java.util.*;
import com.primix.vlib.pages.*;
import javax.servlet.*;
import java.io.*;
import java.net.*;

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
 *
 *  The engine for the Primix Virtual Library.  
 *  This exists to implement the external 
 *  service, which allows the {@link ViewBook} and {@link PersonPage}
 *  pages to be bookmarked, and to provide
 *  a way for shutting down the application when the user logs out.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class VirtualLibraryEngine
extends SimpleEngine
{
	public VirtualLibraryEngine()
	{
		super();
		
		debugEnabled = Boolean.getBoolean("com.primix.vlib.debug-enabled");
	}
	
	private transient boolean debugEnabled;
	
    private transient boolean killSession;

	/**
	 *   The name ("external") of a service that exposes books or 
	 *   persons in such as way that they are bookmarkable.
	 *
	 */
	 
	public static final String EXTERNAL_SERVICE = "external";
	
	/**
	 *  The external service is used to make the {@link ViewBook} and 
	 *  {@link PersonPage} pages bookmarkable.  The URL will include the
	 *  page (which must implement the {@link IExternalPage} interface),
	 *  and the primary key of the {@link IBook}
	 *  or {@link IPerson} EJB.
	 *
	 */
	 
	public class ExternalService implements IEngineService
	{
		public String buildURL(IRequestCycle cycle,
                       IComponent component,
                       String[] context)
		{
			if (context == null || context.length != 2)
				throw new ApplicationRuntimeException("external service required two parameters.");
			
			// Not sure what's in that primary key parameter (may have spaces, slashes,
			// or other illegal characters.
			
			return getServletPrefix() +
				"/" + EXTERNAL_SERVICE +
				"/" + context[0] +
				"/" + URLEncoder.encode(context[1]);
		}
		
		public void service(IRequestCycle cycle,
                    ResponseOutputStream output)
             throws RequestCycleException,
                    ServletException,
                    IOException
        {
			serviceExternal(cycle, output);
        }
		
	}
	
	/**
	 *  Removes the operations bean instance, if accessed this request cycle.
	 *
     *  <p>May invalidate the {@link HttpSession} (see {@link #logout()}).
	 */
	 
	protected void cleanupAfterRequest(IRequestCycle cycle)
	{
        Visit visit = (Visit)getVisit();

        if (visit != null)
			visit.cleanupAfterRequest(cycle);

        if (killSession)
		{
			try
			{
	            cycle.getRequestContext().getSession().invalidate();
			}
			catch (IllegalStateException ex)
			{
				// Ignore.  This can be caused by the browser fetching
				// private assets after the engine has shutdown (and
				// invalidated the HttpSession).  It would be nice
				// if HttpContext implemented an isValid() method.
			}
		}
	}
	
	
	
	/**
	 *  Used from a couple of pages to actually borrow a book.  The {@link Direct}
	 * component should set its context to the primary key of the book to borrow.
	 *
	 */
	 	
	/**
	 *  Supports construction of the external service.
	 *
	 */
	 
	protected IEngineService constructService(String name)
	{
		if (name.equals("external"))
			return new ExternalService();
		
		return super.constructService(name);
	}	
	
	/**
	 *  Performs the actual servicing of the external service.
	 *
	 */
	 
	protected void serviceExternal(IRequestCycle cycle, ResponseOutputStream output)
	throws RequestCycleException, ServletException, IOException
	{
		IMonitor monitor;
		String pageName;
		String key;
		Integer primaryKey;
		RequestContext context;
		IExternalPage page;
		
		monitor = cycle.getMonitor();

		context = cycle.getRequestContext();
		pageName = context.getPathInfo(1);
		key = context.getPathInfo(2);
		
		if (monitor != null)
			monitor.serviceBegin(EXTERNAL_SERVICE, pageName + " " + key);

		primaryKey = new Integer(key);
		
		try
		{
			page = (IExternalPage)cycle.getPage(pageName);
		}
		catch (ClassCastException e)
		{
			throw new ApplicationRuntimeException("Page " + pageName + 
				" may not be used with the " +
				EXTERNAL_SERVICE + " service.");
		}
					
		page.setup(primaryKey, cycle);
				
		// We don't invoke page.validate() because the whole point of this
		// service is to allow unknown (fresh) users to jump right
		// to the page.
		
		// Render the response.

		render(cycle, output);

		if (monitor != null)
			monitor.serviceEnd(EXTERNAL_SERVICE);
	}

    /**
     *  Sets the visit property to null, and sets a flag that
     *  invalidates the {@link HttpSession} at the end of the request cycle.
     *
     */

    public void logout()
    {
		Visit visit = (Visit)getVisit();
		
		if (visit != null)
			visit.setUser(null);
			
        killSession = true;
    }

	public boolean isDebugEnabled()
	{
		return debugEnabled;
	}
}