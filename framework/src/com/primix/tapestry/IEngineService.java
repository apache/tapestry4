/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2002 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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

import javax.servlet.*;
import java.io.*;

// Appease Javadoc
import com.primix.tapestry.link.*;
import com.primix.tapestry.util.pool.Pool;

import com.primix.tapestry.form.*;
import com.primix.tapestry.engine.*;
import javax.servlet.http.*;

/**
 *  A service, provided by the {@link IEngine}, for its pages and/or components.  
 *  Services are
 *  responsible for constructing {@link Gesture}s (an encoding of URLs)
 *  to represent dynamic application behavior, and for
 *  parsing those URLs when a subsequent request involves them.
 *
 *  @see IEngine#getService(String)
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public interface IEngineService
{
	/**
	 *  The name ("action") of a service that allows behavior to be associated with
	 *  an {@link IAction} component, such as {@link Action} or {@link Form}.
	 *  
	 *  <p>This service is used with actions that are tied to the
	 *  dynamic state of the page, and which require a rewind of the page.
	 *
	 */

	public final static String ACTION_SERVICE = "action";

	/**
	 *  The name ("direct") of a service that allows stateless behavior for an {@link
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
	 */

	public final static String DIRECT_SERVICE = "direct";

	/**
	 *  The name ("page") of a service that allows a new page to be selected.
	 *  Associated with a {@link Page} component.
	 *
	 *  <p>The service requires a single parameter:  the name of the target page.
	 */

	public final static String PAGE_SERVICE = "page";

	/**
	 *  The name ("home") of a service that jumps to the home page.  A stand-in for
	 *  when no service is provided, which is typically the entrypoint
	 *  to the application.
	 *
	 */

	public final static String HOME_SERVICE = "home";

	/**
	 *  The name ("restart") of a service that invalidates the session and restarts
	 *  the application.  Typically used just
	 *  to recover from an exception.
	 *
	 */

	public static final String RESTART_SERVICE = "restart";

	/**
	 *  The name ("asset") of a service used to access internal assets.
	 *
	 */

	public static final String ASSET_SERVICE = "asset";

	/**
	 *  The name ("reset") of a service used to clear cached template
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
	*  The query parameter into which each engine service records 
	*  its information when building a URL.
	*
	*  @since 1.0.3
	*/

	public static final String SERVICE_QUERY_PARAMETER_NAME = "service";

	/**
	 *  The query parameter for additional context needed by the
	 *  service.  This is used to store things like the page name or component id.
	 *  When there are multiple pieces of data, they are seperated by
	 *  slashes.
	 *
	 *  @since 1.0.3
	 *
	 */

	public static final String CONTEXT_QUERY_PARMETER_NAME = "context";

	/**
	 *  The query parameter for application specific parameters to the
	 *  service (this is used with the direct service).  Each of these
	 *  values is encoded with {@link java.net.URLEncoder#encode(String)} before
	 *  being added to the URL.  Multiple values are seperated with slashes.
	 *
	 *  @since 1.0.3
	 *
	 */

	public static final String PARAMETERS_QUERY_PARAMETER_NAME = "parameters";

	/**
	 *  Builds a URL for a service.  This is performed during the
	 *  rendering phase of one request cycle and bulds URLs that will
	 *  invoke activity in a subsequent request cycle.
	 *
	 *  @param cycle Defines the request cycle being processed.
	 *  @param component The component requesting the URL.  Generally, the
	 *  service context is established from the component.
	 *  @param parameters Additional parameters specific to the
	 *  component requesting the Gesture.
	 *  @returns The URL for the service.  The URL will have to be encoded
	 *  via {@link HttpServletResponse#encodeURL(java.lang.String)}.
	 *
	 */

	public Gesture buildGesture(
		IRequestCycle cycle,
		IComponent component,
		String[] parameters);

	/**
	 *  Perform the service, interpreting the URL (from the
	 *  {@link HttpServletRequest}) responding appropriately, and
	 *  rendering a result page.
	 *
	 *  <p>The return value indicates whether processing of the request could, in any way,
	 * change the state of the {@link IEngine engine}.  Generally, this is true.
	 *
	 *  @see IEngine#service(RequestContext)
	 *  @param engine a view of the {@link IEngine} with additional methods needed by services
	 *  @param cycle the incoming request
	 *  @param output stream to which output should ultimately be directed
	 */

	public boolean service(
		IEngineServiceView engine,
		IRequestCycle cycle,
		ResponseOutputStream output)
		throws RequestCycleException, ServletException, IOException;

	/**
	 *  Returns the name of the service.
	 *
	 *  @since 1.0.1
	 */

	public String getName();
}