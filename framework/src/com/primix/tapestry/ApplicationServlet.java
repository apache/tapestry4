package com.primix.tapestry;

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
 * included    with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

import javax.servlet.http.*;
import java.io.*;
import javax.servlet.*;
import com.primix.tapestry.app.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.parse.*;

/**
 * Links a servlet container with a Tapestry application.
 *
 * <p>Subclasses provide the servlet with its application by implementing
 * the abstract methods {@link #createApplication(RequestContext)}
 * and {@link #getApplicationSpecificationPath()}.
 *
 * <p>This class is derived from the original class 
 * <code>com.primix.servlet.GatewayServlet</code>
 * part of the <b>ServletUtils</b> framework available from
 * <a href="http://www.gjt.org/servlets/JCVSlet/list/gjt/com/primix/servlet">The Giant 
 * Java Tree</a>.
 *
 * @version $Id$
 * @author Howard Ship
 */

abstract public class ApplicationServlet extends HttpServlet
{
	private ApplicationSpecification specification;
	private String attributeName;

	/**
	* Handles the GET and POST requests. Performs the following:
	* <ul>
	* <li>Construct a {@link RequestContext}
	* <li>Invoke {@link #getApplication(RequestContext)} to get the {@link IApplication}
	* <li>Invoke {@link IApplication#service(RequestContext)} on the application
	* </ul>
	*/

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException
	{
		RequestContext context;
		IApplication application;

		// Create a context from the various bits and pieces.

		context = new RequestContext(this, request, response);

		try
		{

			// The subclass provides the delegate.

			application = getApplication(context);

			if (application == null)
				throw new ServletException(
					"Could not locate an application to service this request.");

			application.service(context);
		}
		catch (ServletException e)
		{
			log("ServletException", e);

			// Rethrow it.

			throw e;
		}
		catch (IOException e)
		{
			log("IOException", e);

			// Rethrow it.

			throw e;
		}
	}


	/**
	* Respond the same to a POST as to a GET.
	*/

	public void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException
	{
		doGet(request, response);
	}

	/**
	 *  Returns the application specification, which is read
	 *  by the {@link #init(ServletConfig)} method.
	 *
	 */
	 
	public ApplicationSpecification getApplicationSpecification()
	{
		return specification;
	}

	/**
	 *  Retrieves the {@link IApplication} instance for this session
	 *  from the {@link HttpSession}, or invokes
	 *  {@link #createApplication(RequestContext)} to create the
	 *  application instance.
	 *
	 * <p>If the application does not need to be stored in the {@link HttpSession}
	 * (not possible with the framework provided implementations)
	 * then this method should be overrided as appropriate.
	 *  
	 */

	protected IApplication getApplication(RequestContext context)
	throws ServletException
	{
		IApplication application;
		
		application = (IApplication)context.getSessionAttribute(attributeName);
		
		if (application == null)
		{
			application = createApplication(context);
			
			context.setSessionAttribute(attributeName, application);
		}
		
		return application;
	}
	
	/**
	 *  Reads the application specification when the servlet is
	 *  first initialized.  All {@link IApplication application instances}
	 *  will have access to the specification via the servlet.
	 *
	 */
	 
	public void init(ServletConfig config)
	throws ServletException
	{
		String path;
		ServletContext servletContext;
		String resource;
		InputStream stream;
		SpecificationParser parser;

		super.init(config);
		
		path = getApplicationSpecificationPath();

		// Make sure we locate the specification using our
		// own class loader.

		stream = getClass().getResourceAsStream(path);

		if (stream == null)
		throw new ServletException(
			"Could not locate application specification " + path + ".");

		parser = new SpecificationParser();

		try
		{
			specification = parser.parseApplicationSpecification(stream, path);
		}
		catch (SpecificationParseException e)
		{
			throw new ServletException(
				"Unable to read application specification " +
				path + ".",  e);
		}		

		attributeName = "com.primix.tapestry.application." + specification.getName();
	}

	/**
	 *  Implemented in subclasses to identify the resource path
	 *  of the application specification.
	 *
	 */
	 
	abstract protected String getApplicationSpecificationPath();
	
	/**
	 *  Invoked by {@link #getApplication(RequestContext)} to create
	 *  the {@link IApplication} instance specific to the
	 *  application, if not already in the
	 *  {@link HttpSession}.
	 *
	 *  <p>The application instance returned is stored into the session.
	 */
	 
	abstract protected IApplication createApplication(RequestContext context)
	throws ServletException;
}

