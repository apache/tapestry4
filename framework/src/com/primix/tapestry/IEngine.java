/*`
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

package com.primix.tapestry;

import java.io.*;
import javax.servlet.*;
import java.util.*;
import com.primix.tapestry.spec.*;
import java.net.*;

// Appease Javadoc
import com.primix.tapestry.components.*;
import javax.servlet.http.*;

/**
 * Defines the core, session-persistant object used to run a Tapestry
 * application for a single client (each client will have its own instance of the engine).
 *
 * <p>The engine exists to provide core services to the pages and components
 * that make up the application.  The engine is a delegate to the
 * {@link ApplicationServlet} via the {@link #service(RequestContext)} method.
 *
 * <p>Engine instances are persisted in the {@link HttpSession} and are serializable.
 *
 * @author Howard Ship
 * @version $Id$
 */

public interface IEngine
{
    /**
	 *  The name ("Home") of the default page presented when a user first accesses the
	 *  application.
	 *
	 */
	
    public static final String HOME_PAGE = "Home";
	
    /**
	 *  The name ("Exception") of the page used for reporting exceptions.
	 *  
	 *  <p>Such a page must have
	 *  a writable JavaBeans property named 'exception' of type 
	 * <code>java.lang.Throwable</code>.
	 *
	 */
	
    public static final String EXCEPTION_PAGE = "Exception";
	
    /**
	 *  The name ("StaleLink") of the page used for reporting stale links.
	 *
	 */
	
    public static final String STALE_LINK_PAGE = "StaleLink";
	
    /**
	 *  The name ("StaleSession") of the page used for reporting state sessions.
	 *
	 */
	
    public static final String STALE_SESSION_PAGE = "StaleSession";
	
    /**
	 *  Forgets changes to the named page by discarding the page recorder for the page.
	 *  This is used when transitioning from one part
	 *  of an application to another.  All property changes for the page are lost.
	 *
	 *  <p>This should be done if the page is no longer needed or relevant, otherwise
	 *  the properties for the page will continue to be recorded by the engine, which
	 *  is wasteful (especially if clustering or failover is employed on the application).
	 *
	 *  <p>Throws an {@link ApplicationRuntimeException} if there are uncommitted changes
	 *  for the recorder (in the current request cycle).
	 *
	 */
	
    public void forgetPage(String name);
	
    /**
	 *  Returns the locale for the engine.  This locale is used when selecting
	 *  templates and assets.
	 */
	
    public Locale getLocale();
	
    /**
	 *  Changes the engine's locale.  Any subsequently loaded pages will be
	 *  in the new locale (though pages already loaded stay in the old locale).
	 *  Generally, you should render a new page after changing the locale, to
	 *  show that the locale has changed.
	 *
	 */
	
    public void setLocale(Locale value);
	
    /**
	 *  Returns a recorder for a page.  Returns null if the page record has
	 *  not been created yet.
	 *
	 *  @see #createPageRecorder(String, IRequestCycle)
	 */
	
    public IPageRecorder getPageRecorder(String pageName);
	
	/**
	 *  Creates a new page recorder for the named page.
	 *
	 */
	
	public IPageRecorder createPageRecorder(String pageName, IRequestCycle cycle);
	
    /**
	 *  Returns the object used to load a page from its specification.
	 *
	 */
	
    public IPageSource getPageSource();
	
    /**
	 *  Gets the named service, or throws an {@link
	 *  ApplicationRuntimeException} if the application can't provide
	 *  the named server.
	 *
	 *  <p>The object returned has a short lifecycle (it isn't
	 *  serialized with the engine).  Repeated calls with the
	 *  same name are not guarenteed to return the same object,
	 *  especially in different request cycles.
	 *
	 */
	
    public IEngineService getService(String name);
	
    /**
	 *  Returns the URL prefix that corresponds to the servlet for the application.  
	 *  This is required by instances of {@link IEngineService} that need 
	 *  to construct URLs for the application.  This value will include
	 *  the context path.
	 */
	
    public String getServletPrefix();
	
    /**
	 *  Returns the context path, a string which is prepended to the names of
	 *  any assets or servlets.  This may be the empty string, but won't be null.
	 *
	 *  <p>This value is obtained from {@link HttpServletRequest#getContextPath()}.
	 */
	
    public String getContextPath();
	
    /**
	 *  Returns the application specification that defines the application
	 *  and its pages.
	 *
	 */
	
    public ApplicationSpecification getSpecification();
	
    /**
	 *  Returns the source of all component specifications for the application.  
	 *  The source is shared between sessions.
	 *
	 */
	
    public ISpecificationSource getSpecificationSource();
	
    /**
	 *  Returns the source for HTML templates.
	 *
	 */
	
    public ITemplateSource getTemplateSource();
	
    /**
	 *  Method invoked from the {@link ApplicationServlet} to perform processing of the
	 *  request.  The return value for this method indicates whether the state of the engine
	 *  could possibly change during processing; in most cases this is true.  In a few
	 *  possible cases, this is false.  The ApplicationServlet sometimes performs extra
	 *  work to ensure that the engine is properly replicated in a clustering environment; if
	 *  this method returns false, it can skip that extra work.
	 *
	 *  @returns true if the state of the engine was, or could have been, changed during
	 *  processing.
	 *
	 */
	
    public boolean service(RequestContext context)
		throws ServletException, IOException;
	
    /**
	 *  Returns an object that can resolve resources and classes.
	 *
	 */
	
    public IResourceResolver getResourceResolver();
	
    /**
	 *  Returns the visit object, an object that represents the client's visit
	 *  to the application.  This is where most server-side state is stored (with
	 *  the exception of persistent page properties).
	 *
	 *  <p>Returns the visit, if it exists, or null if it has not been created.
	 *
	 */
	
    public Object getVisit();
	
	/**
	 *  Returns the visit object, creating it if necessary.
	 *
	 */
	
	public Object getVisit(IRequestCycle cycle);
	
    /**
	 *  Allows the visit object to be removed; typically done when "shutting down"
	 *  a user's session (by setting the visit to null).
	 *
	 */
	
    public void setVisit(Object value);
	
	/**
	 *  Returns true if the application allows the reset service.
	 *
	 *  @since 0.2.9
	 */
	
	public boolean isResetServiceEnabled();
}
