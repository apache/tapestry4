package com.primix.servlet;

/*
 * ServletUtils - Support library for improved Servlets and JavaServer Pages.
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

/**
 * A base class for creating HTTP Servlets that work with the Model-View-Controller pattern.
 * <p>A GatewayServlet subclass has very little implementation, in fact it has only one
 * real responsibility: Locate or create a delegate object and delegate all behavior to it
 * by invoking {@link IService#service(RequestContext)}.
 *
 * <p>Subclasses provide the servlet with its delegate by implementing
 * the abstract method {@link #getDelegate(RequestContext)}.
 *
 * @version $Id$
 * @author Howard Ship
 */

abstract public class GatewayServlet extends HttpServlet
{

	/**
	* Handles the GET and POST requests. Performs the following:
	* <ul>
	* <li>Construct a {@link RequestContext}
	* <li>Invoke {@link #getDelegate(RequestContext)} to get the servlet
	* <li>Invoke {@link IService#service(RequestContext)} on the delegate
	* </ul>
	*/

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		RequestContext context;
		boolean debugEnabled;
		IService delegate = null;

		// Create a context from the various bits and pieces.

		context = new RequestContext(this, request, response);

		// Determine if debug is enabled (from the servlet's initParameters).

		debugEnabled = context.isDebugEnabled();

		if (debugEnabled)
			log("BEGIN request cycle");
		try
		{

			// The subclass provides the delegate.

			delegate = getDelegate(context);

			//

			if (delegate == null)
				throw new ServletException("Could not locate a delegate to service this request.");

			//

			if (debugEnabled)
				log("Delegating to: " + delegate);

			//

			delegate.service(context);
		}
		catch (ServletException ex)
		{
			handleServletException(context, delegate, ex);
		}
		catch (IOException ex)
		{
			handleIOException(context, delegate, ex);
		}
        catch (Exception ex)
        {
            handleOtherException(context, delegate, ex);
        }
		finally
		{
			if (debugEnabled)
				log("END request cycle");
		}
	}


	/**
	* Respond the same to a POST as to a GET.
	*/

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		doGet(request, response);
	}
	
	/**
	* Invoked by 
	* {@link #doGet(HttpServletRequest, HttpServletResponse)}
	* to locate the delegate for the servlet.
	* This method should create the delegate, or locate it within the
	* <code>HttpSession</code>.
	*
	* <p>If the servlet really has no state, then the subclass can
	* implement the {@link IService} interface and return
	* <code>this</code>.  This enables the benefits of using the
	* {@link RequestContext} without the overhead of having
	* a second class (whose lifetime is a single request cycle).
	* Typically, the servlet is placed inside the <code>HttpServletRequest</code>
	* for later access by a JSP.
	*/

	abstract protected IService getDelegate(RequestContext context);
	
	/**
	 *  Invoked if an{@link  ServletException} is thrown by the delegate.
	 *  This implementation logs the exception and re-throws it.
	 *
	 */
	 
	protected void handleServletException(RequestContext context,IService delegate,
		ServletException ex)
	throws IOException, ServletException
	{
		log("Exception", ex);
		
		throw ex;
	}
	
	/**
	 *  Invoked if an {@link IOException} is thrown by the delegate.
	 *  This implementation logs the exception and re-throws it.
	 *
	 */
	 
	protected void handleIOException(RequestContext context, IService delegate,
		IOException ex)
	throws IOException, ServletException
	{
		log("Exception", ex);
		
		throw ex;
	}

	/**
	 *  Invoked if an {@link  Exception} (presumably, a {@link RuntimeException}
	 *  is thrown by the delegate.
	 *  This implementation logs the exception and re-throws it, wrapped
     *  in a {@link ServletException}.
	 *
	 */
	 
	protected void handleOtherException(RequestContext context,IService delegate,
		Exception ex)
	throws IOException, ServletException
	{
		log("Exception", ex);
		
		throw new ServletException(ex);
	}

}

