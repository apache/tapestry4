package com.primix.tapestry;

import javax.servlet.http.*;
import java.io.*;
import javax.servlet.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.parse.*;
import com.primix.foundation.exception.*;

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


/**
 * Links a servlet container with a Tapestry application.
 *
 * <p>Subclasses provide the servlet with its {@link IEngine engine}
 * by implementing
 * the abstract methods {@link #createEngine(RequestContext)}
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
	* <li>Invoke {@link #getEngine(RequestContext)} to get the {@link IEngine}
	* <li>Invoke {@link IEngine#service(RequestContext)} on the application
	* </ul>
	*/

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException
	{
		RequestContext context;
		IEngine engine;

		// Create a context from the various bits and pieces.

		context = new RequestContext(this, request, response);

		try
		{

			// The subclass provides the delegate.

			engine = getEngine(context);

			if (engine == null)
				throw new ServletException(
					"Could not locate an engine to service this request.");

			engine.service(context);
		}
		catch (ServletException ex)
		{
			log("ServletException", ex);

            show(ex);

			// Rethrow it.

			throw ex;
		}
		catch (IOException ex)
		{
			log("IOException", ex);

            show(ex);

			// Rethrow it.

			throw ex;
		}
	}

	protected void show(Exception ex)
	{
	    System.err.println(
	        "\n\n**********************************************************\n\n");

	    new ExceptionAnalyzer().reportException(ex, System.err);

	    System.err.println(
	        "\n**********************************************************\n");

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
	 *  Retrieves the {@link IEngine} instance for this session
	 *  from the {@link HttpSession}, or invokes
	 *  {@link #createEngine(RequestContext)} to create the
	 *  application instance.
	 *
	 * <p>If the engine does not need to be stored in the {@link HttpSession}
	 * (not possible with the framework provided implementations)
	 * then this method should be overrided as appropriate.
	 *  
	 */

	protected IEngine getEngine(RequestContext context)
	throws ServletException
	{
		IEngine engine;
		
		engine = (IEngine)context.getSessionAttribute(attributeName);
		
		if (engine == null)
		{
			engine = createEngine(context);
			
			context.setSessionAttribute(attributeName, engine);
		}
		
		return engine;
	}
	
	/**
	 *  Reads the application specification when the servlet is
	 *  first initialized.  All {@link IEngine engine instances}
	 *  will have access to the specification via the servlet.
	 *
	 */
	 
	public void init()
	throws ServletException
	{
		String path;
		ServletContext servletContext;
		String resource;
		InputStream stream;
		SpecificationParser parser;

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
		catch (SpecificationParseException ex)
		{
            show(ex);

			throw new ServletException(
				"Unable to read application specification " +
				path + ".",  ex);
		}		

		attributeName = "com.primix.tapestry.engine." + specification.getName();
	}

	/**
	 *  Implemented in subclasses to identify the resource path
	 *  of the application specification.
	 *
	 */
	 
	abstract protected String getApplicationSpecificationPath();
	
	/**
	 *  Invoked by {@link #getEngine(RequestContext)} to create
	 *  the {@link IEngine} instance specific to the
	 *  application, if not already in the
	 *  {@link HttpSession}.
	 *
	 *  <p>The {@link IEngine} instance returned is stored into the 
	 *  {@link HttpSession}.
     *
     *  <p>This implementation instantiates a new engine as specified
     *  by {@link ApplicationSpecification#getEngineClassName()}.
     *
	 */
	 
	protected IEngine createEngine(RequestContext context)
	throws ServletException
	{
        try
        {
            String className = specification.getEngineClassName();

            if (className == null)
                throw new ServletException("Application specification does not specify an engine class name.");

            Class engineClass = Class.forName(className);

            return (IEngine)engineClass.newInstance();
        }
        catch (Exception ex)
        {
            throw new ServletException(ex);
        }
	}
}

