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

package com.primix.tapestry.engine;

import com.primix.tapestry.components.*;
import com.primix.tapestry.util.prop.PropertyHelper;
import com.primix.tapestry.util.exception.*;
import com.primix.tapestry.record.PageRecorder;
import java.io.*;
import javax.servlet.*;
import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.parse.*;
import java.util.*;
import com.primix.tapestry.pageload.*;
import com.primix.tapestry.asset.*;
import java.net.*;
import javax.servlet.http.*;
import org.apache.log4j.*;

/**
 *  Basis for building real Tapestry applications.  Immediate subclasses
 *  provide different strategies for managing page state and other resources
 *  between request cycles.  
 *
 *  Uses a shared instance of
 *  {@link ITemplateSource} and {@link ISpecificationSource}
 *  stored as attributes of the  {@link ServletContext} 
 *  (they will be shared by all sessions).
 *
 *  <p>An application is designed to be very lightweight.
 *  Particularily, it should <b>never</b> hold references to any
 *  {@link IPage} or {@link IComponent} objects.  The entire system is
 *  based upon being able to quickly rebuild the state of any page(s).
 *
 * <p>Where possible, instance variables should be transient.  They
 * can be restored inside {@link #setupForRequest(RequestContext)}.
 *
 *  In practice, a subclass (usually {@link SimpleEngine})
 *  is used without subclassing.  Instead, a 
 *  visit object is specified.  To facilitate this, the application specification
 *  may include a property, <code>com.primix.tapestry.visit-class</code>
 *  which is the class name  to instantiate when a visit object is first needed.  See
 *  {@link #createVisit(IRequestCycle)} for more details.
 *
 * @author Howard Ship
 * @version $Id$
 */


public abstract class AbstractEngine
    implements IEngine, Externalizable, HttpSessionBindingListener
{
	private static final Category CAT = 
		Category.getInstance(AbstractEngine.class);

	private transient String contextPath;
	private transient String servletPrefix;
	private transient String clientAddress;
	private transient String sessionId;

	/**
	*  An object used to contain application-specific server side state.
	*
	*/

	private Object visit;

	/**
	 *  The current locale for the engine, which may be changed at any time.
	 *
	 */

	private Locale locale;

	/**
	*  The specification for the application, which
	*  lives in the {@link ServletContext}.  If the
	*  session (and application) moves to a different context (i.e.,
	*  a different JVM), then
	*  we want to reconnect to the specification in the new context.
	*  A check is made on every request
	*  cycle as needed.
	*
	*/

	protected transient ApplicationSpecification specification;

	/**
	*  The source for template data. The template source is stored
	*  in the {@link ServletContext} as a named attribute.
	*  After de-serialization, the application can re-connect to
	*  the template source (or create a new one).
	*
	*/

	protected transient ITemplateSource templateSource;

	/**
	*  The source for component specifications, stored in the
	*  {@link ServletContext} (like {@link #templateSource}).
	*
	*/


	protected transient ISpecificationSource specificationSource;

	private transient Map services;

	private static final int MAP_SIZE = 7;

	/**
	*  The name of the application specification property used to specify the
	*  class of the visit object.
	*
	*/

	public static final String VISIT_CLASS_PROPERTY_NAME = "com.primix.tapestry.visit-class";

	/**
	*  Servlet context attribute name for the default {@link ITemplateSource}
	*  instance.  The application's name is appended.
	*
	*/

	protected static final String TEMPLATE_SOURCE_NAME = 
		"com.primix.tapestry.DefaultTemplateSource";

	/**
	*  Servlet context attribute name for the default {@link ISpecificationSource}
	*  instance.  The application's name is appended.
	*
	*/

	protected static final String SPECIFICATION_SOURCE_NAME =
		"com.primix.tapestry.DefaultSpecificationSource";

	/**
	*  Servlet context attribute name for the {@link IPageSource}
	*  instance.  The application's name is appended.
	*
	*/

	protected static final String PAGE_SOURCE_NAME = "com.primix.tapestry.PageSource";


	/**
	*  The source for pages, which acts as a pool, but is capable of
	*  creating pages as needed.  Stored in the
	*  {@link ServletContext}, like {@link #templateSource}.
	*
	*/

	protected transient PageSource pageSource;

	private transient boolean resetServiceEnabled;

	private transient IResourceResolver resolver;

	private class ActionService implements IEngineService
	{
		public void service(IRequestCycle cycle, ResponseOutputStream output)
		throws RequestCycleException, ServletException, IOException
		{
			serviceAction(cycle, output);
		}

		public String buildURL(IRequestCycle cycle, IComponent component, String[] parameters)
		{
			if (parameters == null ||
				parameters.length != 1)
				throw new IllegalArgumentException(
					"Service action requires one parameter.");

			String pageName = cycle.getPage().getName();

			// Because we know that all of the terms are 'URL safe' (they contain
			// only alphanumeric characters and the '.') we don't have to
			// use HTMLUtils.buildURL().

			return servletPrefix +
				"/" + ACTION_SERVICE +
				"/" + pageName +
				"/" + parameters[0] +
				"/" + component.getIdPath();

		}
	}

	private class PageService implements IEngineService
	{
		public void service(IRequestCycle cycle, ResponseOutputStream output)
		throws RequestCycleException, ServletException, IOException
		{
			servicePage(cycle, output);
		}

		public String buildURL(IRequestCycle cycle, IComponent component, String[] parameters)
		{
			if (parameters == null ||
				parameters.length != 1)
				throw new IllegalArgumentException(
					"Service page requires exactly one parameter.");

			// We assume that the page name is URL safe here.  We
			// could check here that the page is defined by the
			// specification.

			return servletPrefix +
				"/" + PAGE_SERVICE +
				"/" + parameters[0];
		}
	}

	private class HomeService implements IEngineService
	{
		public void service(IRequestCycle cycle, ResponseOutputStream output)
		throws RequestCycleException, ServletException, IOException
		{
			IMonitor monitor = cycle.getMonitor();

			if (monitor != null)
				monitor.serviceBegin(HOME_SERVICE, null);

			IPage home = cycle.getPage(HOME_PAGE);

			home.validate(cycle);

			// If it validates, then render it.

			cycle.setPage(home);
			render(cycle, output);

			if (monitor != null)
				monitor.serviceEnd(HOME_SERVICE);
		}

		public String buildURL(IRequestCycle cycle, IComponent component,  String[] parameters)
		{
			if (parameters != null &&
				parameters.length > 0)
				throw new IllegalArgumentException("Service home requires no parameters.");

			return servletPrefix +
				"/" + HOME_SERVICE;
		}
	}

	private class RestartService implements IEngineService
	{
		public void service(IRequestCycle cycle, ResponseOutputStream output)
		throws RequestCycleException, IOException, ServletException
		{
			IMonitor monitor = cycle.getMonitor();

			if (monitor != null)
				monitor.serviceBegin(RESTART_SERVICE, null);

			restart(cycle);

			if (monitor != null)
				monitor.serviceEnd(RESTART_SERVICE);
		}

		public String buildURL(IRequestCycle cycle, IComponent component, String[] parameters)
		{
			if (parameters != null &&
				parameters.length > 0)
				throw new IllegalArgumentException("Service restart requires no parameters.");

			return servletPrefix +
				"/" + RESTART_SERVICE;
		}
	}

	/**
	*  Used during testing to force reloads of templates and
	*  specifications.
	*
	*/

	private class ResetService implements IEngineService
	{
		public void service(IRequestCycle cycle, ResponseOutputStream output)
		throws RequestCycleException, IOException, ServletException
		{
			serviceReset(cycle, output);
		}

		public String buildURL(IRequestCycle cycle, IComponent component, 
			String[] parameters)
		{
			if (parameters != null &&
				parameters.length > 0)
				throw new IllegalArgumentException("Service reset requires no parameters.");

			String pageName = component.getPage().getName();

			return getServletPrefix() + "/" + RESET_SERVICE + "/" + pageName;
		}
	}

	private class DirectService implements IEngineService
	{
		private StringBuffer buffer;

		public void service(IRequestCycle cycle, ResponseOutputStream output)
		throws RequestCycleException, IOException, ServletException
		{
			serviceDirect(cycle, output);
		}

		public String buildURL(IRequestCycle cycle, IComponent component, 
			String[] parameters)
		{
			// The Java 2 performance and idiom guide isn't too keen
			// on reusing StringBuffers.  The problem is that if you create
			// a very long string with a buffer, then create short strings,
			// the short strings use as much memory as the long one.  In
			// my experience, though, URL length tends to be pretty standard
			// (+/- 10 or 20 characters).

			if (buffer == null)
				buffer = new StringBuffer();
			else
				buffer.setLength(0);

			String pageName = cycle.getPage().getName();


			buffer.append(servletPrefix);
			buffer.append('/');
			buffer.append(DIRECT_SERVICE);
			buffer.append('/');
			buffer.append(pageName);
			buffer.append('/');
			buffer.append(component.getIdPath());

			if (parameters != null)
			{
				for (int i = 0; i < parameters.length; i++)
				{
					buffer.append('/');
					buffer.append(URLEncoder.encode(parameters[i]));
				}
			}

			return buffer.toString();
		}
	}

	/**
	*  Sets the Exception page's exception property, then renders the Exception page.
	*
	*  <p>If the render throws an exception, then copious output is sent to
	*  <code>System.err</code> and a {@link ServletException} is thrown.
	*
	*/

	protected void activateExceptionPage(IRequestCycle cycle, ResponseOutputStream output, 
		Throwable cause)
	throws ServletException
	{
		try
		{
			IPage exceptionPage = cycle.getPage(EXCEPTION_PAGE);

			PropertyHelper helper = PropertyHelper.forClass(exceptionPage.getClass());

			helper.set(exceptionPage, "exception", cause);

			cycle.setPage(exceptionPage);

			render(cycle, output);	

		}
		catch (Throwable ex)
		{
			// Worst case scenario.  The exception page itself is broken, leaving
			// us with no option but to write the cause to the output.

			reportException("Unable to process client request.", cause);

			// Also, write the exception thrown when redendering the exception
			// page, so that can get fixed as well.

			reportException("Tapestry unable to present exception page.",  ex);

			// And throw the exception.

			throw new ServletException(ex.getMessage(), ex);
		}
	}

	/**
	*  Writes a detailed report of the exception to <code>System.err</code>.
	*
	*/

	protected void reportException(String reportTitle, Throwable ex)
	{
		CAT.warn(reportTitle, ex);

		System.err.println(
			"\n\n**********************************************************\n\n");

		System.err.println(reportTitle);

		System.err.println("\n\n      Session id: " + sessionId +
			"\n  Client address: " + clientAddress +
			"\n\nExceptions:\n");

		new ExceptionAnalyzer().reportException(ex, System.err);

		System.err.println(
			"\n**********************************************************\n");

	}

	/**
	*  Invoked at the end of the request cycle to release any resources specific
	*  to the request cycle.
	*
	*/

	protected abstract void cleanupAfterRequest(IRequestCycle cycle);

	/**
	*  Invoked by {@link #getService(String)} to construct a named service.  This method
	*  should return a new instance of the named service, or null if the
	*  application doesn't implement the named service.
	*
	*  This seems a little awkward, but the idea is to avoid creating the service
	*  object until it is needed, since once created they have the lifecycle of the
	*  application object, which can be quite long.  I don't know that its worth
	*  the cycles to create a more involved solution -- for example, extend the
	*  interface so that a pooled service object could be bound temporarily to
	*  a specific instance of the engine.  Best to just leave this alone.
	*
	*  <p>Subclasses should override this method, but must also invoke it to provide
	*  the services provided by AbstractEngine:
	*  <ul>
	*  <li>action
	*  <li>asset
	*  <li>home
	*  <li>direct
	*  <li>page
	*  <li>restart
	*  <li>reset  (if system property 
	*  <code>com.primix.tapestry.enable-reset-service</code> is true)
	*  </ul>
	*
	*/

	protected IEngineService constructService(String name)
	{
		if (name.equals(IEngineService.ACTION_SERVICE))
			return new ActionService();

		if (name.equals(IEngineService.ASSET_SERVICE))
			return new AssetService(this);

		if (name.equals(IEngineService.HOME_SERVICE))
			return new HomeService();

		if (name.equals(IEngineService.DIRECT_SERVICE))
			return new DirectService();

		if (name.equals(IEngineService.PAGE_SERVICE))
			return new PageService();

		if (name.equals(IEngineService.RESTART_SERVICE))
			return new RestartService();

		if (name.equals(IEngineService.RESET_SERVICE) && 
			resetServiceEnabled)
			return new ResetService();

		return null;
	}

	/**
	*  Extends the description of the class generated by {@link #toString()}.
	*  If a subclass adds additional instance variables that should be described
	*  in the instance description, it may override this method.  Subclasses
	*  should invoke this implementation first.  They should append a space
	*  before each value.
	*
	*  @see #toString()
	*/

	public void extendDescription(StringBuffer buffer)
	{
		buffer.append(specification.getName());
	}

	/**
	*  Returns the locale for the engine, which may be null, which means
	*  to use the JVM default locale.
	*
	*/

	public Locale getLocale()
	{
		if (locale == null)
			return Locale.getDefault();

		return locale;
	}

	/**
	*  Overriden in subclasses that support monitoring.  Should create and return
	*  an instance of {@link IMonitor} that is appropriate for the request cycle described
	*  by the {@link RequestContext}.  May return null.
	*
	*  <p>The monitor is used to create a {@link RequestCycle}.
	*
	*  <p>This implementation returns null always.  Subclasses may override without
	*  invoking it.
	*
	*  <p>TBD:  Lifecycle of the monitor ... should there be a commit?
	*
	*/

	public IMonitor getMonitor(RequestContext context)
	{
		return null;
	}

	public IPageSource getPageSource()
	{
		return pageSource;
	}

	/**
	*  Returns a service with the given name, constructing it 
	*  (via {@link #constructService(String)} if necessary.
	*/

	public IEngineService getService(String name)
	{
		IEngineService result = null;

		if (services != null)
			result = (IEngineService)services.get(name);

		if (result != null)
			return result;

		result = constructService(name);

		if (result == null)
			throw new ApplicationRuntimeException(
				"Engine does not implement a service named " + name + ".");

		if (services == null)
			services = new HashMap(MAP_SIZE);

		services.put(name, result);

		return result;
	}

	public String getServletPrefix()
	{
		return servletPrefix;
	}

	/**
	* Returns the context path, the prefix to apply to any URLs so that they
	* are recognizing as belonging to the Servlet 2.2 context.
	*
	*  @see ContextAsset
	*/

	public String getContextPath()
	{
		return contextPath;
	}

	/**
	*  Returns the specification, if available, or null otherwise.
	*
	*  <p>To facilitate deployment across multiple servlet containers, the
	*  application is serializable.  However, the reference to the specification
	*  is transient.   When an application instance is deserialized, it reconnects
	*  with the application specification by locating it in the {@link ServletContext}
	*  or parsing it fresh.
	*
	*/

	public ApplicationSpecification getSpecification()
	{
		return specification;
	}

	public ISpecificationSource getSpecificationSource()
	{
		return specificationSource;
	}

	public ITemplateSource getTemplateSource()
	{
		return templateSource;
	}

	/**
	*  Reads the state serialized by {@link #writeExternal(ObjectOutput)}.
	*
	*/

	public void readExternal(ObjectInput in)
	throws IOException, ClassNotFoundException
	{
		locale = (Locale)in.readObject();
		visit = in.readObject();
	}

	/**
	*  Writes the following properties:
	*
	*  <ul>
	*  <li>locale ({@link Locale})
	*  <li>visit
	*  </ul>
	*
	*/

	public void writeExternal(ObjectOutput out)
	throws IOException
	{
		out.writeObject(locale);
		out.writeObject(visit);
	}

	/**
	 *  Invoked, typically, when an exception occurs while servicing the request.
	 *  This method resets the output, sets the new page and renders it.
	 *
	 */

	protected void redirect(String pageName, IRequestCycle cycle, ResponseOutputStream out,
		RequestCycleException exception)
	throws IOException, RequestCycleException, ServletException
	{
		// Discard any output from the previous page.

		out.reset();

		cycle.setPage(pageName);

		render(cycle, out);
	}

	/**
	*  Invoked to render an actual result page (as opposed to a rewind).
	*
	* <p>Invokes {@link IRequestCycle#commitPageChanges()} before rendering the
	* page.
	*
	*/

	protected void render(IRequestCycle cycle, ResponseOutputStream output)
	throws RequestCycleException, ServletException, IOException
	{
		IResponseWriter writer;
		boolean discard = true;
		IPage page;

		if (CAT.isDebugEnabled())
			CAT.debug("Begin render response.");

		// Commit all changes and ignore further changes.

		page = cycle.getPage();

		writer = page.getResponseWriter(output);

		output.setContentType(writer.getContentType());

		try
		{
			cycle.renderPage(writer);

			discard = false;
		}
		finally
		{
			// Closing the writer closes its PrintWriter and a whole stack of java.io objects,
			// which tend to stream a lot of output that eventually hits the
			// ResponseOutputStream.  If we are discarding output anyway (due to an exception
			// getting thrown during the render), we can save ourselves some trouble
			// by ignoring it.

			if (discard)
				output.setDiscard(true);

			writer.close();

			if (discard)
				output.setDiscard(false);
		}

	}

	/**
	* Invalidates the session, then redirects the client web browser to
	* the servlet's prefix, starting a new visit.
	*
	* <p>Subclasses should perform their own restart (if necessary, which is
	* rarely) before invoking this implementation.
	*
	*/

	protected void restart(IRequestCycle cycle)
	throws IOException
	{
		RequestContext context;
		String url;

		context = cycle.getRequestContext();

		url = context.getAbsoluteURL(servletPrefix);

		context.getSession().invalidate();

		context.redirect(url);
	}


	/**
	*  Delegate method for the servlet.  Services the request.
	*
	*/

	public void service(RequestContext context) throws ServletException, IOException
	{
		String serviceName;
		IEngineService service;
		IRequestCycle cycle;
		ResponseOutputStream output = null;
        IMonitor monitor;

		try
		{
		
			NDC.push(context.getSession().getId());

			if (CAT.isInfoEnabled())
				CAT.info("Begin service " + context.getRequest().getRequestURI());

			if (specification == null)
				specification = context.getServlet().getApplicationSpecification();

			// Build the resolver around the servlet, since that's guarenteed
			// to be in the application's class loader (which has the broadest
			// possible view).

			if (resolver == null)
				resolver = new ResourceResolver(context.getServlet());

			try
			{
				setupForRequest(context);

				monitor = getMonitor(context);

				cycle = new RequestCycle(this, context, monitor);

				output = new ResponseOutputStream(context.getResponse());
			}
			catch (Exception e)
			{
				reportException("Tapestry unable to begin processing request.", e);

				throw new ServletException(e.getMessage(), e);
			}

			try
			{
				try
				{
					serviceName = context.getPathInfo(0);

					if (serviceName == null ||
						serviceName.equals(""))
						serviceName = IEngineService.HOME_SERVICE;

					service = getService(serviceName);

					service.service(cycle, output);		
				}
				catch (PageRedirectException ex)
				{
					redirect(ex.getTargetPageName(), cycle, output, ex);
				}
				catch (StaleLinkException ex)
				{
					handleStaleLinkException(ex, cycle, output);
				}
				catch (StaleSessionException ex)
				{
					handleStaleSessionException(ex, cycle, output);
				}
			}
			catch (Exception ex)
			{
				if (monitor != null)
					monitor.serviceException(ex);

				// Discard any output (if possible).  If output has already been sent to
				// the client, then things get dicey.  Note that this block
				// gets activated if the StaleLink or StaleSession pages throws
				// any kind of exception.

				// Attempt to switch to the exception page.  However, this may itself fail
				// for a number of reasons, in which case a ServletException is thrown.

				output.reset();

				if (CAT.isInfoEnabled())
					CAT.info("Uncaught exception", ex);

				activateExceptionPage(cycle, output, ex);
			}
			finally
			{
				cycle.cleanup();

				// Closing the buffered output closes the underlying stream as well.

				if (output != null)
					output.forceClose();

				cleanupAfterRequest(cycle);

				if (CAT.isInfoEnabled())
					CAT.info("End service");

			}
			}
			finally
			{
				NDC.pop();
			}
	}

	/**
	 *  Invoked by {@link #service(RequestContext)} if a {@link StaleLinkException}
	 *  is thrown by the {@link IEngineService service}.  This implementation
	 *  invokes 
	 *  {@link #redirect(String, IRequestCycle, ResponseOutputStream, RequestCycleException)}
	 *  to render the StaleLink page.
	 *
	 *  <p>Subclasses may override this method (without
	 *  invoking this implementation).  A common practice
	 *  is to present an error message on the application's
	 *  Home page.	
	 *
	 *  @since 0.2.10
	 */

	protected void handleStaleLinkException(StaleLinkException ex, IRequestCycle cycle, 
		ResponseOutputStream output)
	throws IOException, ServletException, RequestCycleException
	{
		redirect(STALE_LINK_PAGE, cycle, output, ex);
	}

	/**
	 *  Invoked by {@link #service(RequestContext)} if a {@link StaleSessionException}
	 *  is thrown by the {@link IEngineService service}.  This implementation
	 *  invokes 
	 *  {@link #redirect(String, IRequestCycle, ResponseOutputStream, RequestCycleException)}
	 *  to render the StaleSession page.
	 *
	 *  <p>Subclasses may override this method (without
	 *  invoking this implementation).  A common practice
	 *  is to present an error message on the application's
	 *  Home page.	
	 *
	 *  @since 0.2.10
	 */

	protected void handleStaleSessionException(StaleSessionException ex, IRequestCycle cycle, 
		ResponseOutputStream output)
	throws IOException, ServletException, RequestCycleException
	{
		redirect(STALE_SESSION_PAGE, cycle, output, ex);
	}

	/**
	*  Processes an 'action' URL.
	*  <ul>
	*  <li>The specified page is loaded and rolled back to its prior state.
	*  <li>{@link IRequestCycle#rewindPage(String, String)} is invoked, 
	*  to rewind the page to
	*  the target action id and target component id path,
	*  by going through the motions of
	*  rendering again.
	*  <li>{@link #render(IRequestCycle, ResponseOutputStream)} is invoked, to render
	*  the response page.
	*  </ul>
	*
	*/

	private void serviceAction(IRequestCycle cycle, ResponseOutputStream output)
	throws RequestCycleException, ServletException, IOException
	{
		// If the context is new on an action URL, then the session
		// truly expired and we want to redirect to the
		// timeout page to advise the user.

		RequestContext context = cycle.getRequestContext();

		if (context.getPathInfoCount() != 4)
			throw new ApplicationRuntimeException(
				"Service action requires exactly three parameters.");

		String pageName = context.getPathInfo(1);
		String targetActionId = context.getPathInfo(2);
		String targetIdPath = context.getPathInfo(3);

		IMonitor monitor = cycle.getMonitor();
		if (monitor != null)
			monitor.serviceBegin(IEngineService.ACTION_SERVICE, pageName + "/" + targetActionId);

		if (context.getSession().isNew())
			throw new StaleSessionException();

		IPage page = cycle.getPage(pageName);

		// Allow the page to validate that the user is allowed to visit.  This is simple
		// protected from malicious users who hack the URLs directly, or make inappropriate
		// use of the back button. 

		page.validate(cycle);

		// Setup the page for the rewind, then do the rewind.

		cycle.setPage(page);
		cycle.rewindPage(targetActionId, targetIdPath);

		// During the rewind, a component may change the page.  This will take
		// effect during the second render, which renders the HTML response.

		// Render the response.

		render(cycle, output);

		if (monitor != null)
			monitor.serviceEnd(IEngineService.ACTION_SERVICE);

	}

	/**
	*  Processes a 'direct' URL.
	*  <ul>
	*  <li>The specified page is loaded and rolled back to its prior state.
	*  <li>The referenced component is located and cast to {@link IAction}.
	*  <li>{@link IAction#trigger(IRequestCycle)} is invoked to trigger the
	*  behaviour associated with the component.
	*  <li>{@link #render(IRequestCycle,ResponseOutputStream)} is invoked to
	*  render the response page.
	*  </ul>
	*
	*/

	private void serviceDirect(IRequestCycle cycle, ResponseOutputStream output)
	throws RequestCycleException, ServletException, IOException
	{
		IDirect direct;
		String[] parameters = null;

		IMonitor monitor = cycle.getMonitor();

		// If the context is new on an action URL, then the session
		// truly expired and we want to redirect to the
		// timeout page to advise the user.

		RequestContext context = cycle.getRequestContext();

		int pathInfoCount = context.getPathInfoCount();

		if (pathInfoCount < 3)
			throw new ApplicationRuntimeException(
				"Service direct requires at least two parameters.");

		String pageName = context.getPathInfo(1);
		String componentPath = context.getPathInfo(2);

		if (monitor != null)
			monitor.serviceBegin(IEngineService.DIRECT_SERVICE, 
				pageName + "/" + componentPath);

		if (context.getSession().isNew())
			throw new StaleSessionException();

		IPage page = cycle.getPage(pageName);

		// Allow the page to validate that the user is allowed to visit.  This is simple
		// protection from malicious users who hack the URLs directly, or make inappropriate
		// use of the back button. 

		page.validate(cycle);
		cycle.setPage(page);

		IComponent component = page.getNestedComponent(componentPath);

		try
		{
			direct = (IDirect)component;
		}
		catch (ClassCastException ex)
		{
			throw new RequestCycleException(
				"Component " + pageName + "/" +
				componentPath + " does not implement the IDirect interface.",
				component, ex);
		}

		// Get any parameters encoded in the URL.

		if (pathInfoCount > 3)
		{
			parameters = new String[pathInfoCount - 3];

			for (int i = 3; i < pathInfoCount; i++)
				parameters[i - 3] = context.getPathInfo(i);
		}

		direct.trigger(cycle, parameters);

		// Render the response.

		render(cycle, output);

		if (monitor != null)
			monitor.serviceEnd(IEngineService.DIRECT_SERVICE);
	}

	/**
	* Processes a 'page' URL.
	*
	*/

	private void servicePage(IRequestCycle cycle, ResponseOutputStream output)
	throws RequestCycleException, ServletException, IOException
	{
		RequestContext context = cycle.getRequestContext();

		if (context.getPathInfoCount() != 2)
			throw new ApplicationRuntimeException(
				"Service page requires exactly one parameter.");

		String pageName = context.getPathInfo(1);

		IMonitor monitor = cycle.getMonitor();
		if (monitor != null)
			monitor.serviceBegin(IEngineService.PAGE_SERVICE, pageName);

		// At one time, the page service required a session, but that is no longer necessary.
		// User's can now bookmark pages within a Tapestry application.  Pages
		// can implement validate() and throw a PageRedirectException if they don't
		// want to be accessed this way.  For example, most applications have a concept
		// of a "login" and have a fe pages that don't require the user to be logged in,
		// and other pages that do.  The protected pages should redirect to a login page.

		IPage page = cycle.getPage(pageName);

		// Allow the page to validate that the user is allowed to visit.  This is simple
		// protection from malicious users who hack the URLs directly, or make inappropriate
		// use of the back button. 

		page.validate(cycle);

		cycle.setPage(page);

		render(cycle, output);

		if (monitor != null)
			monitor.serviceEnd(IEngineService.PAGE_SERVICE);
	}

	/**
	* <p>Clears the cache of pages, specifications and templates.
	*
	*/

	private void serviceReset(IRequestCycle cycle, ResponseOutputStream output)
	throws RequestCycleException, ServletException, IOException
	{
		RequestContext context;
		ServletContext servletContext;
		IMonitor monitor;
		String name;
		String pageName;
		IPage page;

		monitor = cycle.getMonitor();

		context = cycle.getRequestContext();

		if (context.getPathInfoCount() != 2)
			throw new ApplicationRuntimeException(
				"Service reset requires exactly one parameter.");

		pageName = context.getPathInfo(1);

		if (monitor != null)
			monitor.serviceBegin("reset", pageName);

		pageSource.reset();
		specificationSource.reset();
		templateSource.reset();

		page = cycle.getPage(pageName);

		page.validate(cycle);

		cycle.setPage(page);

		// Render the same page (that contained the reset link).

		render(cycle, output);

		if (monitor != null)
			monitor.serviceEnd("reset");
	}

	/**
	*  Changes the locale for the engine.
	*
	*/

	public void setLocale(Locale value)
	{
		locale = value;
	}

	/**
	*  Invoked from {@link #service(RequestContext)} to ensure that the engine's
	*  instance variables are setup.  This allows the application a chance to
	*  restore transient variables that will not have survived deserialization.
	*
	*  Determines the servlet prefix:  this is the base URL used by
	*  {@link IEngineService services} to build URLs.  It consists
	*  of two parts:  the context path and the servlet path.
	*
	* <p>If the parameter is not specified, then no context prefix is used,
	* which is appropriate for Servlet API 2.1 containers.
	*
	* <p>The servlet path is retrieved from {@link HttpServletRequest#getServletPath()}.
	*
	* <p>The final path is available via the {@link #getServletPrefix()} method.
	*
	*  <p>In addition, this method locates and/or creates the:
	*  <ul>
	*  <li>{@link ITemplateSource} 
	*  <li>{@link ISpecificationSource}
	*  <li>{@link IPageSource}
	*  </ul>
	*
	*  <p>Subclasses should invoke this implementation first, then perform their
	*  own setup.
	*
	*/

	protected void setupForRequest(RequestContext context)
	{
		String name;
		HttpServlet servlet = context.getServlet();
		ServletContext servletContext = servlet.getServletContext();
		HttpServletRequest request = context.getRequest();

		sessionId = context.getSession().getId();
		clientAddress = request.getRemoteHost();
		if (clientAddress == null)
			clientAddress = request.getRemoteAddr();

		// servletPrefix is null, so this means either we're doing the
		// first request in this session, or we're handling a subsequent
		// request in another JVM (i.e. another server in the cluster).
		// In any case, we have to do some late (re-)initialization.

		if (servletPrefix == null)
		{
			String servletPath = request.getServletPath();

			// Get the context path, which may be the empty string
			// (but won't be null).

			contextPath = request.getContextPath();

			servletPrefix = contextPath + servletPath;

			resetServiceEnabled = 
				Boolean.getBoolean("com.primix.tapestry.enable-reset-service");
		}	

		String applicationName = specification.getName();

		if (templateSource == null)
		{
			name = TEMPLATE_SOURCE_NAME + "." + applicationName;

			templateSource = 
				(ITemplateSource)servletContext.getAttribute(name);

			if (templateSource == null)
				templateSource = new DefaultTemplateSource(getResourceResolver());

			servletContext.setAttribute(name, templateSource);
		}

		if (specificationSource == null)
		{
			name = SPECIFICATION_SOURCE_NAME + "." + applicationName;

			specificationSource = 
				(ISpecificationSource)servletContext.getAttribute(name);

			if (specificationSource == null)
				specificationSource = 
				new DefaultSpecificationSource(getResourceResolver(), 
					specification);

			servletContext.setAttribute(name, specificationSource);
		}

		if (pageSource == null)
		{
			name = PAGE_SOURCE_NAME + "." + applicationName;

			pageSource = (PageSource)servletContext.getAttribute(name);

			if (pageSource == null)
				pageSource = new PageSource(getResourceResolver());

			servletContext.setAttribute(name, pageSource);
		}
	}

	/**
	*  Returns an object which can find resources and classes.
	*
	*/

	public IResourceResolver getResourceResolver()
	{
		return resolver;
	}    

	/**
	*  Generates a description of the instance.  
	*  Invokes {@link #extendDescription(StringBuffer)}
	*  to fill in details about the instance.
	*
	*  @see #extendDescription(StringBuffer)
	*
	*/

	public String toString()
	{
		StringBuffer buffer;

		buffer = new StringBuffer(super.toString());

		buffer.append('[');

		extendDescription(buffer);

		buffer.append(']');

		return buffer.toString();
	}

	/**
	*  Invoked when the application object is stored into
	*  the {@link HttpSession}.  This implementation does nothing.
	*
	*/

	public void valueBound(HttpSessionBindingEvent event)
	{
	}

	/**
	*  Invoked when the application object is removed from the {@link HttpSession}.
	*  This occurs when the session times out or is explicitly invalidated
	*  (for example, by the reset or restart services).  Invokes
	*  {@link #cleanupEngine()}.
	*
	*/

	public void valueUnbound(HttpSessionBindingEvent event)
	{
		// Note: there's a possible latent bug here.  If cleaning up the
		// application requires loading any resources (specifically
		// component specifications) and we need a ResourceResolver and
		// the application instance is newly deserialized (i.e., was deserialized
		// so that it could unbound from the HttpSession) ... then 
		// we may trip over a NullPointerException because the resolver
		// will be null.  Don't have a great solution for this!

		cleanupEngine();
	}

	/**
	*  Invoked when the engine is removed from the {@link HttpSession} i.e.,
	*  because the sesssion timed out or was invalidated.
	*
	*  <p>Locates all active pages (pages which have been activated) and
	*  invokes {@link IPage#cleanupPage()} on them.  This gives 
	*  pages a chance to release any long held resources.  This primarily
	*  exists so that pages that hold references to stateful session EJBs
	*  can remove those EJBs in a timely manner.
	*
	*  <p>Subclasses may override this method to clean up any engine-held
	*  resources, but should invoke this implementation first.
	*/

	protected void cleanupEngine()
	{
		Iterator i;
		String name;
		IPageSource source;
		IPageRecorder recorder;
		IPage page;

		source = getPageSource();

		i = getActivePageNames().iterator();

		while (i.hasNext())
		{
			name = (String)i.next();

			try
			{
				page = source.getPage(this, name, null);
				recorder = getPageRecorder(name);

				recorder.rollback(page);

				page.cleanupPage();
			}
			catch (Throwable t)
			{
				reportException("Unable to cleanup page " + name + ".", t);
			}
		}
	}

	/**
	*  Returns true if the reset service is currently enabled.
	*
	*/

	public boolean isResetServiceEnabled()
	{
		return resetServiceEnabled;
	}

	/**
	*  Implemented by subclasses to return the names of the active pages
	*  (pages for which recorders exist).
	*
	*/

	abstract public Collection getActivePageNames();

	/**
	 *  Gets the visit object, if it has been created already.
	 *
	 */

	public Object getVisit()
	{
		return visit;
	}

	/**
	*  Gets the visit object, invoking {@link #createVisit(IRequestCycle)} to create
	*  it lazily if needed.
	*
	*
	*/

	public Object getVisit(IRequestCycle cycle)
	{
		if (visit == null && cycle != null)
			visit = createVisit(cycle);

		return visit;
	}

	public void setVisit(Object value)
	{
		visit = value;
	}

	public boolean getHasVisit()
	{
		return visit != null;
	}

	/**
	*  Invoked to lazily create a new visit object when it is first
	*  referenced (by {@link #getVisit(IRequestCycle)}).  This implementation works
	*  by looking up the name of the class
	*  in the application specification.
	*
	*  <p>Subclasses may want to override this method if some other means
	*  of instantiating a visit object is required.
	*/

	protected Object createVisit(IRequestCycle cycle)
	{
		String visitClassName;
		Class visitClass;

		visitClassName = specification.getProperty(VISIT_CLASS_PROPERTY_NAME);
		if (visitClassName == null)
			throw new ApplicationRuntimeException(
				"Could not create visit object because property " +
				VISIT_CLASS_PROPERTY_NAME + 
				" was not specified in the application specification.");

		if (CAT.isDebugEnabled())
			CAT.debug("Creating visit object as instance of " + visitClassName);

		visitClass = resolver.findClass(visitClassName);

		try
		{
			return visitClass.newInstance();
		}
		catch (Throwable t)
		{
			throw new ApplicationRuntimeException(
				"Unable to instantiate visit object from class " +
				visitClassName + ".", t);
		}

	}
}
