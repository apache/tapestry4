/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 *  A service, provided by the {@link IEngine}, for its pages and/or components.  
 *  Services are
 *  responsible for constructing {@link Gesture}s (an encoding of URLs)
 *  to represent dynamic application behavior, and for
 *  parsing those URLs when a subsequent request involves them.
 *
 *  @see IEngine#getService(String)
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface IEngineService
{
	/**
	 *  The name ("action") of a service that allows behavior to be associated with
	 *  an {@link IAction} component, such as {@link ActionLink} or {@link Form}.
	 *  
	 *  <p>This service is used with actions that are tied to the
	 *  dynamic state of the page, and which require a rewind of the page.
	 *
	 **/

	public final static String ACTION_SERVICE = "action";

	/**
	 *  The name ("direct") of a service that allows stateless behavior for an {@link
	 *  DirectLink} component.
	 *
	 *  <p>This service rolls back the state of the page but doesn't
	 *  rewind the the dynamic state of the page the was the action
	 *  service does, which is more efficient but less powerful.
	 *
	 *  <p>An array of String parameters may be included with the
	 *  service URL; these will be made available to the {@link DirectLink}
	 *  component's listener.
	 *
	 **/

	public final static String DIRECT_SERVICE = "direct";

    /**
     *  The name ("external") of a service that a allows {@link IExternalPage} to be selected.
     *  Associated with a {@link ExternalLink} component.
     *
     *  <p>This service enables {@link IExternalPage}s to be accessed via a URL.
     *  External pages may be booked marked using their URL for future reference.
     *
     *  <p>An array of Object parameters may be included with the
     *  service URL; these will be passed to the 
     *  {@link IExternalPage#activateExternalPage(Object[], IRequestCycle)} method.
     *
     **/

    public final static String EXTERNAL_SERVICE = "external";

	/**
	 *  The name ("page") of a service that allows a new page to be selected.
	 *  Associated with a {@link PageLink} component.
	 *
	 *  <p>The service requires a single parameter:  the name of the target page.
	 **/

	public final static String PAGE_SERVICE = "page";

	/**
	 *  The name ("home") of a service that jumps to the home page.  A stand-in for
	 *  when no service is provided, which is typically the entrypoint
	 *  to the application.
	 *
	 **/

	public final static String HOME_SERVICE = "home";

	/**
	 *  The name ("restart") of a service that invalidates the session and restarts
	 *  the application.  Typically used just
	 *  to recover from an exception.
	 *
	 **/

	public static final String RESTART_SERVICE = "restart";

	/**
	 *  The name ("asset") of a service used to access internal assets.
	 *
	 **/

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
	 *  <code>net.sf.tapestry.enable-reset-service</code>
	 *  is set to <code>true</code>.
	 **/

	public static final String RESET_SERVICE = "reset";

	/**
	 *  Query parameter that identfies the service for the
     *  request.
	 *
	 *  @since 1.0.3
     * 
	 **/

	public static final String SERVICE_QUERY_PARAMETER_NAME = "service";

	/**
	 *  The query parameter for additional context needed by the
	 *  service.  This is used to store things like the page name or component id.
	 *  When there are multiple pieces of data, they are seperated by
	 *  slashes.  Not all services use a context.
	 *
	 *  @since 1.0.3
 	 *
	 **/

	public static final String CONTEXT_QUERY_PARMETER_NAME = "context";

	/**
	 *  The query parameter for application specific parameters to the
	 *  service (this is used with the direct service).  Each of these
	 *  values is encoded with {@link java.net.URLEncoder#encode(String)} before
	 *  being added to the URL.  Multiple values are handle by repeatedly
     *  establishing key/value pairs (this is a change from behavior in 
     *  2.1 and earlier).
	 *
	 *  @since 1.0.3
     * 
	 **/

	public static final String PARAMETERS_QUERY_PARAMETER_NAME = "sp";

	/**
	 *  Builds a URL for a service.  This is performed during the
	 *  rendering phase of one request cycle and bulds URLs that will
	 *  invoke activity in a subsequent request cycle.
     * 
     *  <p><b>Through release 2.1, parameters was String[],
     *  not Object[].  This is an incompatible change.</b>
	 *
	 *  @param cycle Defines the request cycle being processed.
	 *  @param component The component requesting the URL.  Generally, the
	 *  service context is established from the component.
	 *  @param parameters Additional parameters specific to the
	 *  component requesting the Gesture.
	 *  @return The URL for the service.  The URL will have to be encoded
	 *  via {@link HttpServletResponse#encodeURL(java.lang.String)}.
	 *
	 **/

	public Gesture buildGesture(
		IRequestCycle cycle,
		IComponent component,
		Object[] parameters);

	/**
	 *  Perform the service, interpreting the URL (from the
	 *  {@link javax.servlet.http.HttpServletRequest}) 
	 *  responding appropriately, and
	 *  rendering a result page.
	 *
	 *  <p>The return value indicates whether processing of the request could, in any way,
	 *  change the state of the {@link IEngine engine}.  Generally, this is true.
	 *
	 *  @see IEngine#service(RequestContext)
	 *  @param engine a view of the {@link IEngine} with additional methods needed by services
	 *  @param cycle the incoming request
	 *  @param output stream to which output should ultimately be directed
	 * 
	 **/

 	public boolean service(
		IEngineServiceView engine,
		IRequestCycle cycle,
		ResponseOutputStream output)
		throws RequestCycleException, ServletException, IOException;

	/**
	 *  Returns the name of the service.
	 *
	 *  @since 1.0.1
	 **/

	public String getName();
}