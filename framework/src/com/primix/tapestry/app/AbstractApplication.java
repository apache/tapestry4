package com.primix.tapestry.app;

import com.primix.tapestry.components.*;
import com.primix.foundation.prop.PropertyHelper;
import com.primix.foundation.exception.*;
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
 *  Basis for building real Tapestry applications.  Immediate subclasses
 *  provide different strategies for managing page state and other resources
 *  between request cycles.  Final applications will subclass further to add
 *  application specific properties and logic.
 *
 *  Uses a shared instance of
 *  {@link ITemplateSource} and {@link ISpecificationSource}
 *  stored as attributes of the  {@link ServletContext} 
 *  (they will be shared by all sessions
 *  and all Tapestry applications).
 *
 *  <p>Conecrete subclasses must implement {@link #getSpecificationAttributeName()}
 * and {@link #getSpecificationResourceName()} to identify where
 * the specification is stored (within the {@link ServletContext}) and
 * loaded from.
 *
 *  <p>An application is designed to be very lightweight.
 *  Particularily, it should <b>never</b> hold references to any
 *  {@link IPage} or {@link IComponent} objects.  The entire system is
 *  based upon being able to quickly rebuild the state of any page(s).
 *  It actually breaks down if a page is <em>not</em> recreated on
 *  each request cycle.
 *
 * <p>Where possible, instance variables should be <code>transient</code>.  They
 * can be restored inside {@link #setupForRequest(RequestContext)}.
 *
 * <p><b>Note: the changes related to {@link IResourceResolver} mean that we can't
 * really share template and specification sources between Tapestry applications
 * (in seperate servlet contexts, loaded by seperate class loaders).  This must be
 * addressed shortly, to mantain the ability to run multiple Tapestry applications
 * within the same servlet engine. </b>
 *
 * @author Howard Ship
 * @version $Id$
 */


public abstract class AbstractApplication
    implements IApplication, Serializable
{
	protected transient String contextPath;
	protected transient String servletPrefix;

	protected Locale locale;

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

	protected transient ITemplateSource templateSource;

	protected transient ISpecificationSource specificationSource;

	private transient Map services;

	private static final int MAP_SIZE = 7;

	/**
	*  Servlet context attribute name for the default {@link ITemplateSource}
	*  instance.
	*
	*/

	protected static final String TEMPLATE_SOURCE_NAME = 
		"com.primix.tapestry.DefaultTemplateSource";

	/**
	*  Servlet context attribute name for the default {@link ISpecificationSource}
	*  instance.
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
	*  creating pages as needed.
	*
	*/

	protected transient PageSource pageSource;

	/**
	*  The servlet session id, extracted from the initial {@link HttpServletRequest}.
	*
	*/

	protected String sessionId;

	/**
	*  The address of the client, extracted from the initial
	*  {@link HttpServletRequest}.
	*
	*/

	protected String clientAddress;

	private transient IResourceResolver resolver;
    
	private class ActionService implements IApplicationService
	{
		public void service(IRequestCycle cycle, ResponseOutputStream output)
		throws RequestCycleException, ServletException, IOException
		{
			serviceAction(cycle, output);
		}

		public String buildURL(IRequestCycle cycle, IComponent component, String[] parameters)
		{
			String pageName;
			IPageRecorder recorder;

			if (parameters == null ||
				parameters.length != 1)
				throw new IllegalArgumentException(
					"Service action requires one parameter.");

			pageName = cycle.getPage().getName();
			recorder = getPageRecorder(pageName);

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

	private class PageService implements IApplicationService
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

	private class HomeService implements IApplicationService
	{
		public void service(IRequestCycle cycle, ResponseOutputStream output)
		throws RequestCycleException, ServletException, IOException
		{
			IMonitor monitor;
			
			monitor = cycle.getMonitor();
			
			if (monitor != null)
				monitor.serviceBegin(HOME_SERVICE, null);

			cycle.setPage(HOME_PAGE);

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

	private class RestartService implements IApplicationService
	{
		public void service(IRequestCycle cycle, ResponseOutputStream output)
		throws RequestCycleException, IOException, ServletException
		{
			IMonitor monitor;
			
			monitor = cycle.getMonitor();
			
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

	private class DirectService implements IApplicationService
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
			String pageName;
			IPageRecorder recorder;
			int i;

			if (buffer == null)
				buffer = new StringBuffer();
			else
				buffer.setLength(0);

			pageName = cycle.getPage().getName();
			recorder = getPageRecorder(pageName);


			buffer.append(servletPrefix);
			buffer.append('/');
			buffer.append(DIRECT_SERVICE);
			buffer.append('/');
			buffer.append(pageName);
			buffer.append('/');
			buffer.append(component.getIdPath());

			if (parameters != null)
			{
				for (i = 0; i < parameters.length; i++)
				{
					buffer.append('/');
					buffer.append(URLEncoder.encode(parameters[i]));
				}
			}

			return buffer.toString();
		}
	}

	/**
	*  Standard constructor.
	*
	* <p>The locale of the application is set from the locale
	* parameter, unless it is null, in which case the locale is set
	* from <code>Locale.getDefault()</code>.
	*
	*  @param context Allows access to the servlet request, session, etc.
	*  @param locale  The locale for the application, or null to use the
	*  default locale.
	*/

	public AbstractApplication(RequestContext context, Locale locale)
	{
		if (locale == null)
			this.locale = Locale.getDefault();
		else
			this.locale = locale; 

		sessionId = context.getSession().getId();

		clientAddress = context.getRequest().getRemoteHost();

		if (clientAddress == null)
			clientAddress = context.getRequest().getRemoteAddr();
	}

	/**
	*  Sets the exception page's exception property, then renders the exception page.
	*
	*  <p>If the render throws an exception, then copious output is sent to
	*  <code>System.err</code>.
	*
	*/

	protected void activateExceptionPage(IRequestCycle cycle, ResponseOutputStream output, 
		Throwable cause)
	throws ServletException
	{
		IPage exceptionPage;
		PropertyHelper helper;


		try
		{
			exceptionPage = cycle.getPage(EXCEPTION_PAGE);

			helper = PropertyHelper.forClass(exceptionPage.getClass());

			helper.set(exceptionPage, "exception", cause);

			cycle.setPage(exceptionPage);

			render(cycle, output);	

		}
		catch (Exception e)
		{
			reportException("Tapestry unable to present exception page.",  e);

			// And throw the exception.

			throw new ServletException(e.getMessage(), e);
		}
	}

	/**
	*  Writes a detailed report of the exception to <code>System.err</code>.
	*
	*/

	protected void reportException(String reportTitle, Exception e)
	{
		ExceptionAnalyzer analyzer;
		ExceptionDescription[] descriptions;
		ExceptionProperty[] properties;
		String[] stackTrace;
		int i;
		int j;
		String message;

		System.err.println(
			"\n\n**********************************************************\n\n");

		System.err.println(reportTitle);

		System.err.println("\n\n      Session id: " + sessionId +
			"\n  Client address: " + clientAddress +
			"\n\nExceptions:\n");

		// Something has gone seriously wrong.  Report it as best you can.

		analyzer = new ExceptionAnalyzer();
		descriptions = analyzer.analyze(e);

		for (i = 0; i < descriptions.length; i++)
		{
			message = descriptions[i].getMessage();

			if (message == null)
				System.err.println(descriptions[i].getExceptionClassName());
			else
				System.err.println(descriptions[i].getExceptionClassName() + ": " +
					descriptions[i].getMessage());

			properties = descriptions[i].getProperties();

			for (j = 0; j < properties.length; j++)
				System.err.println("   " + properties[j].getName() + ": " +
					properties[j].getValue());

			// Just show the stack trace on the deepest exception.

			if (i + 1 == descriptions.length)
			{
				stackTrace = descriptions[i].getStackTrace();

				for (j = 0; j < stackTrace.length; j++)
					System.err.println(stackTrace[j]);
			}
			else
				System.err.println();
		}

		System.err.println(
			"\n**********************************************************\n");

	}

	/**
	*  Invoked at the end of the request cycle to release any resources specific
	*  to the request cycle.
	*
	*/

	protected abstract void cleanupAfterRequest();

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
	*  a specific instance of the application.  Best to just leave this alone.
	*
	*  <p>Subclasses should override this method, but must also invoke it to provide
	*  the services provided by {@link AbstractApplication}:
	*  <ul>
	*  <li>action
	*  <li>asset
	*  <li>home
	*  <li>direct
	*  <li>page
	*  <li>restart
	*  </ul>
	*
	*/

	public IApplicationService constructService(String name)
	{
		if (name.equals(IApplicationService.ACTION_SERVICE))
			return new ActionService();

		if (name.equals(IApplicationService.ASSET_SERVICE))
			return new AssetService(this);

		if (name.equals(IApplicationService.HOME_SERVICE))
			return new HomeService();

		if (name.equals(IApplicationService.DIRECT_SERVICE))
			return new DirectService();

		if (name.equals(IApplicationService.PAGE_SERVICE))
			return new PageService();

		if (name.equals(IApplicationService.RESTART_SERVICE))
			return new RestartService();

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
		if (specification == null)
			buffer.append("<specification not loaded>");
		else
			buffer.append(specification.getName());

		if (sessionId != null)
		{
			buffer.append(" sessionId=");
			buffer.append(sessionId);
		}

		if (clientAddress != null)
		{
			buffer.append(" clientAddress=");
			buffer.append(clientAddress);
		}
	}

	public Locale getLocale()
	{
		return locale;
	}

	/**
	*  Implemented in subclasses that support monitoring.  Should create and return
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

	public IApplicationService getService(String name)
	{
		IApplicationService result = null;

		if (services != null)
			result = (IApplicationService)services.get(name);

		if (result != null)
			return result;

			result = constructService(name);

		if (result == null)
			throw new ApplicationRuntimeException(
				"Application does not implement a service named " + name + ".");

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
	 *  A temporary method, until we transition Tapestry to Servlet API 2.2,
	 *  where this information is available from the {@link ServletContext}.
	 *
	 *  <p>This is value is obtained from the servlet init parameter
	 *  <code>com.primix.tapestry.context-path</code>.
	 *
	 *  @see InternalAsset
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

	/**
	*  Returns the name of the {@link ServletContext} attribute used to store
	*  the {@link ApplicationSpecification}.
	*
	*  <p>The specification, once created, is placed into the {@link ServletContext}
	*  to be shared by all clients and sessions.  Because a single servlet container
	*  may be running multiple Tapestry applications, each Tapestry application should
	*  use a unique name for storing the specification.
	*
	*  <p>An example value would be <code>com.skunkworx.spec.Skunkworld</code> for an
	*  application named 'Skunkworld' produced by skunkworx.com, but as long as the name
	*  can be reasonably assumed to be unique, alls fair.
	*
	*/

	protected abstract String getSpecificationAttributeName();

	/**
	*  Returns the name of a resource that is used to read the specification.
	*  This is a value that will be fed directly to <code>Class.getResourceAsStream()</code>.
	*  
	*  <p>A typical value would be "/com/skunkworkx/app/Skunkworld.application".
	*
	*/

	protected abstract String getSpecificationResourceName();

	public ISpecificationSource getSpecificationSource()
	{
		return specificationSource;
	}

	public ITemplateSource getTemplateSource()
	{
		return templateSource;
	}

	private void locateApplicationSpecification(RequestContext context)
	throws ResourceUnavailableException
	{
		ServletContext servletContext;
		String attributeName;
		String resource;
		InputStream stream;
		SpecificationParser parser;

		// Specification is transient, but the application may not have been
		// serialized/de-serialized.

		if (specification != null)
			return;

		servletContext = context.getServlet().getServletContext();

		attributeName = getSpecificationAttributeName();

		specification = (ApplicationSpecification)servletContext.getAttribute(attributeName);

		if (specification != null)
			return;

		// OK, time to get the resource and parse it up.

		resource = getSpecificationResourceName();

		stream = getClass().getResourceAsStream(resource);

		if (stream == null)
			throw new ResourceUnavailableException(
				"Could not locate resource " + resource + ".");

		parser = new SpecificationParser();

		try
		{
			specification = parser.parseApplicationSpecification(stream, resource);
		}
		catch (SpecificationParseException e)
		{
			throw new ResourceUnavailableException("Unable to read application specification.",
					e);
		}		

		// Record it into the servlet context for later.  This will help other instances of
		// the application that need to access the specification and help this instance
		// if it is serialized and deserialized.

		servletContext.setAttribute(attributeName, specification);
	}

	private void readObject(ObjectInputStream in)
	throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		restoreAfterDeserialization();
	}

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
		PageSpecification spec;
		int bufferSize;
		boolean discard = true;
		IPage page;

		// Commit all changes and ignore further changes.
		// I've been thinking that we may want to *forbid* changes
		// at this point.

		try
		{
			cycle.commitPageChanges();
		}
		catch (PageRecorderCommitException e)
		{
			throw new RequestCycleException(e.getMessage(), null, cycle, e);
		}

		page = cycle.getPage();
		
		spec = specification.getPageSpecification(page.getName());
		bufferSize = spec.getBufferSize();

		if (bufferSize >= 0)
			output.setBufferSize(bufferSize);

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
	*  Does nothing.  Subclasses may override to provide additional, custom logic to
	*  restore the application after it is de-serialized.
	*
	*/

	protected void restoreAfterDeserialization()
	{
	}

	/**
	*  Delegate method for the servlet.  Services the request.
	*
	*/

	public void service(RequestContext context) throws ServletException, IOException
	{
		String serviceName;
		IApplicationService service;
		IRequestCycle cycle;
		ResponseOutputStream output = null;
		IMonitor monitor;

		try
		{
			locateApplicationSpecification(context);

		}
		catch (ResourceUnavailableException e)
		{
			reportException("Tapestry unable to locate application specification.", e);

			throw new ServletException(e.getMessage(), e);
		}

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
					serviceName = IApplicationService.HOME_SERVICE;

				service = getService(serviceName);

				service.service(cycle, output);		
			}
			catch (PageRedirectException e)
			{
				redirect(e.getTargetPageName(), cycle, output, e);
			}
			catch (StaleLinkException e)
			{
				redirect(STALE_LINK_PAGE, cycle, output, e);
			}
			catch (StaleSessionException e)
			{
				redirect(STALE_SESSION_PAGE, cycle, output, e);
			}
		}
		catch (Exception e)
		{
			if (monitor != null)
				monitor.serviceException(e);

			// Discard any output (if possible).  If output has already been sent to
			// the client, then things get dicey.

			// Attempt to switch to the exception page.  However, this may itself fail
			// for a number of reasons, in which case a ServletException is thrown.

			output.reset();

			activateExceptionPage(cycle, output, e);
		}
		finally
		{
			cycle.cleanup();

			// Closing the buffered output closes the underlying stream as well.

			if (output != null)
				output.forceClose();
				
			cleanupAfterRequest();

		}

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

	protected void serviceAction(IRequestCycle cycle, ResponseOutputStream output)
	throws RequestCycleException, ServletException, IOException
	{
		String pageName;
		String targetActionId;
		String targetIdPath;
		IResponseWriter writer;
		RequestContext context;
		IMonitor monitor;
		IPage page;

		// If the context is new on an action URL, then the session
		// truly expired and we want to redirect to the
		// timeout page to advise the user.

		context = cycle.getRequestContext();

		pageName = context.getPathInfo(1);
		targetActionId = context.getPathInfo(2);
		targetIdPath = context.getPathInfo(3);

		monitor = cycle.getMonitor();
		if (monitor != null)
			monitor.serviceBegin(IApplicationService.ACTION_SERVICE, pageName + "/" + targetActionId);

		if (context.getSession().isNew())
			throw new StaleSessionException();

		page = cycle.getPage(pageName);

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
			monitor.serviceEnd(IApplicationService.ACTION_SERVICE);

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
		String pageName;
		String componentPath;
		IResponseWriter writer;
		RequestContext context;
		IComponent component;
		Direct direct;
		String[] parameters = null;
		IPage page;
		IMonitor monitor;
		int pathInfoCount;
		int i;

		monitor = cycle.getMonitor();

		// If the context is new on an action URL, then the session
		// truly expired and we want to redirect to the
		// timeout page to advise the user.

		context = cycle.getRequestContext();

		pageName = context.getPathInfo(1);
		componentPath = context.getPathInfo(2);

		if (monitor != null)
			monitor.serviceBegin(IApplicationService.DIRECT_SERVICE, 
				pageName + "/" + componentPath);

		if (context.getSession().isNew())
			throw new StaleSessionException();

		page = cycle.getPage(pageName);

		// Allow the page to validate that the user is allowed to visit.  This is simple
		// protection from malicious users who hack the URLs directly, or make inappropriate
		// use of the back button. 

		page.validate(cycle);
		cycle.setPage(page);

		component = page.getNestedComponent(componentPath);

		try
		{
			direct = (Direct)component;
		}
		catch (ClassCastException e)
		{
			throw new RequestCycleException(
				"Component " + pageName + "/" +
				componentPath + " is not type Direct.",
				component, cycle, e);
		}

		pathInfoCount = context.getPathInfoCount();

		// Get any parameters encoded in the URL.

		if (pathInfoCount > 3)
		{
			parameters = new String[pathInfoCount - 3];

			for (i = 3; i < pathInfoCount; i++)
				parameters[i - 3] = context.getPathInfo(i);
		}

		direct.trigger(cycle, parameters);

		// Render the response.

		render(cycle, output);

		if (monitor != null)
			monitor.serviceEnd(IApplicationService.DIRECT_SERVICE);
	}

	/**
	* Processes a 'page' URL.
	*
	*/

	public void servicePage(IRequestCycle cycle, ResponseOutputStream output)
	throws RequestCycleException, ServletException, IOException
	{
		String pageName;
		RequestContext context;
		IMonitor monitor;
		IPage page;

		context = cycle.getRequestContext();

		pageName = context.getPathInfo(1);

		monitor = cycle.getMonitor();
		if (monitor != null)
			monitor.serviceBegin(IApplicationService.PAGE_SERVICE, pageName);

		// At one time, the page service required a session, but that is no longer necessary.
		// User's can now bookmark pages within a Tapestry application.  Pages
		// can implement validate() and throw a PageRedirectException if they don't
		// want to be accessed this way.  For example, most applications have a concept
		// of a "login" and have a fe pages that don't require the user to be logged in,
		// and other pages that do.  The protected pages should redirect to a login page.
		
		page = cycle.getPage(pageName);

		// Allow the page to validate that the user is allowed to visit.  This is simple
		// protection from malicious users who hack the URLs directly, or make inappropriate
		// use of the back button. 

		page.validate(cycle);

		cycle.setPage(page);

		render(cycle, output);

		if (monitor != null)
			monitor.serviceEnd(IApplicationService.PAGE_SERVICE);
	}

	/**
	*  Changes the locale for the application.
	*
	*  @throws IllegalArgumentException if value is null.
	*
	*/

	public void setLocale(Locale value)
	{
		if (value == null)
			throw new IllegalArgumentException("May not set application locale to null.");

		locale = value;
	}

	/**
	*  Invoked from {@link #service(RequestContext)} to ensure that the application's
	*  instance variables are setup.  This allows the application a chance to
	*  restore transient variables that will not have survived deserialization.
	*
	*  Determines the servlet prefix:  this is the base URL used by
	*  {@link IApplicationService services} to build URLs.  It consists
	*  of two parts:  the context path and the servlet path.
	*
	*  <p>The context path is only defined under Servlet API 2.2.  It can
	*  be accessed via the method {@link HttpServletRequest#getContextPath()}.
	*  Since Tapestry is designed to work with Servlet API 2.1, we don't use this.
	*  Instead, we require that the servlet initial parameter
	* <code>com.primix.tapestry.context-path</code> be defined with the
	* correct value, typically "/<i>webapp</i>".
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
		HttpServlet servlet;
		ServletContext servletContext = null;
		String name;
		String servletPath;

		servlet = context.getServlet();
		
		servletContext = servlet.getServletContext();
		
		if (servletPrefix == null)
		{
			servletPath = context.getRequest().getServletPath();

			contextPath = servlet.getInitParameter("com.primix.tapestry.context-path");
			
			if (contextPath == null)
				servletPrefix = servletPath;
			else
				servletPrefix = contextPath + servletPath;
		}	

		// Need to redo a bunch of this because of class loader issues related
		// to IResourceResolver.  Each Tapestry app will need its own template source
		// and page source.  Possibly, there should be chaining, such that templates
		// and specifications provided by the framework can come from dedicated
		// sources .. this allows greater sharing.  Somewhat academic though, since
		// typically only a single Tapestry application will be running within
		// a single servlet container or ServletContext.

		if (templateSource == null)
		{
			templateSource = 
				(ITemplateSource)servletContext.getAttribute(TEMPLATE_SOURCE_NAME);

			if (templateSource == null)
				templateSource = new DefaultTemplateSource(getResourceResolver());

			servletContext.setAttribute(TEMPLATE_SOURCE_NAME, templateSource);
		}

		if (specificationSource == null)
		{
			specificationSource = 
				(ISpecificationSource)servletContext.getAttribute(SPECIFICATION_SOURCE_NAME);

			if (specificationSource == null)
				specificationSource = 
				new DefaultSpecificationSource(getResourceResolver(), 
                	specification);

			servletContext.setAttribute(SPECIFICATION_SOURCE_NAME, specificationSource);
		}

		if (pageSource == null)
		{
			name = PAGE_SOURCE_NAME + "." + specification.getName();

			pageSource = (PageSource)servletContext.getAttribute(name);

			if (pageSource == null)
				pageSource = new PageSource();

			servletContext.setAttribute(name, pageSource);
		}
	}

	/**
	 *  Returns an object which can find resources and classes.
	 *
	 */
	 
    public IResourceResolver getResourceResolver()
    {
    	if (resolver == null)
        	resolver = new ResourceResolver(this);
        
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
    
}
