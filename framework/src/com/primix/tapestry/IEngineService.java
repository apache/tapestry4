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

package com.primix.tapestry;

import javax.servlet.*;
import java.io.*;

// Appease Javadoc
import com.primix.tapestry.link.*;
import com.primix.tapestry.form.*;
import com.primix.tapestry.engine.*;

/**
 *  A service, provided by the {@link IEngine}, for its pages and/or components.  
 *  Services are
 *  responsible for constructing URLs to represent dynamic application behavior, and for
 *  parsing those URLs when a subsequent request involves them.
 *
 *  <p>The general format for such a URL consists of several segments divided by
 *  slashes:
 *  <ul>
 *  <li>servlet prefix
 *  <li>service name
 *  <li>service URI
 *  </ul>
 *
 * <p>The service URI is additional path info values that are meaningful to the service.
 * For example, the page service provided by 
 * {@link AbstractEngine} and the
 * {@link Page} component stores the name of the target page there, for example:
 *
 *  <blockquote>
 *  /some-app/page/target-page
 * </blockquote>
 *
 * <p>Where "/some-app" is the servlet prefix.
 *
 *
 *  @see IEngine#getService(String)
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public interface IEngineService
{
    /**
	 *  Name of a service that allows behavior to be associated with
	 *  an {@link Action} or {@link Form} component.
	 *  
	 *  <p>This service is used with actions that are tied to the
	 *  dynamic state of the page, which require a rewind of the page.
	 *
	 *  <p>The action service throws a {@link StaleSessionException} if invoked
	 *  in a new session.
	 *
	 */
	
    public final static String ACTION_SERVICE = "action";
	
    /**
	 *  Name of a service that allows stateless behavior for an {@link
	 *  Direct} component.
	 *
	 *  <p>This service rolls back the state of the page but doesn't
	 *  rewind the the dynamic state of the page the was the action
	 *  service does, which is more efficient but less powerful.
	 *
	 *  <p>An array of String parameters may be included with the
	 *  service URL; these will be made available to the {@link Direct}
	 *  component's listener.
	 *
	 *  <p>The direct service throws a {@link StaleSessionException} if invoked
	 *  in a new session.
	 */
	
    public final static String DIRECT_SERVICE = "direct";
	
	
    /**
	 *  Name of a service that allows a new page to be selected.
	 *  Associated with a {@link Page} component.
	 *
	 *  <p>The service requires a single parameter:  the name of the target page.
	 */
	
    public final static String PAGE_SERVICE = "page";
	
    /**
	 *  Name of service that jumps to the home page.  A stand-in for
	 *  when no service is provided, which is typically the entrypoint
	 *  to the application.
	 *
	 */
	
    public final static String HOME_SERVICE = "home";
	
    /**
	 *  Name  ("restart") of a service that invalidates the session and restarts
	 *  the application.  Typically used just
	 *  to recover from an exception.
	 *
	 */
	
    public static final String RESTART_SERVICE = "restart";
	
    /**
	 *  Name ("asset") of a service used to access internal assets.
	 *
	 */
	
    public static final String ASSET_SERVICE = "asset";
	
    /**
	 *  Name ("reset") of a service used to clear cached template
	 *  and specification data and remove all pooled pages.
	 *  This is only used when debugging as
	 *  a quick way to clear the out cached data, to allow updated
	 *  versions of specifications and templates to be loaded (without
	 *  stopping and restarting the servlet container).
	 *
	 * <p>This service is only available if the Java system property
	 *  <code>com.primix.tapestry.enable-reset-service</code>
	 *  is set to <code>true</code>.
	 */
	
    public static final String RESET_SERVICE = "reset";
	
    /**
	 *  Builds a URL for a service.  This is performed during the
	 *  rendering phase of one request cycle and bulds URLs that will
	 *  invoke activity in a subsequent request cycle.
	 *
	 *  @param cycle Defines the request cycle being processed.
	 *  @param component The component requesting the URL.
	 *  @param context Additional elements added to the URI path that express the
	 *  context.  Each service defines it own meaning for this; for example, the page
	 *  service requires a single context element to identify the page to
	 *  link to.
	 *  @returns The URL for the service.  The URL will have need to be encoded.
	 *
	 *  @see IRequestCycle#encodeURL(String)
	 */
	
    public String buildURL(IRequestCycle cycle, IComponent component, String[] context);
	
    /**
	 *  Perform the service, interpreting the URL (from the
	 *  <code>HttpServletRequest</code>) responding appropriately, and
	 *  rendering a result page.
	 *
	 *  <p>The return value indicates whether processing of the request could, in any way,
	 * change the state of the {@link IEngine engine}.  Generally, this is true.
	 *
	 *  @see IEngine#service(RequestContext)
	 *
	 */
	
    public boolean service(IRequestCycle cycle, ResponseOutputStream output)
		throws RequestCycleException, ServletException, IOException;
}
