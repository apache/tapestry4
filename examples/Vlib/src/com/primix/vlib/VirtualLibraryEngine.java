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

package com.primix.vlib;

import com.primix.tapestry.*;
import com.primix.tapestry.engine.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.form.*;
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
import org.apache.log4j.*;

// Appease Javadoc
import javax.servlet.http.HttpSession;

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
	public static final Category CAT =
		Category.getInstance(VirtualLibraryEngine.class);
	
	private static boolean debugEnabled
		= Boolean.getBoolean("com.primix.vlib.debug-enabled");
	
    private transient boolean killSession;
	
	// Home interfaces are static, such that they are only
	// looked up once (JNDI lookup is very expensive).
	
	private static IBookQueryHome bookQueryHome;
	private static IOperationsHome operationsHome;
	private transient IOperations operations;
	
	private static Context rootNamingContext;	
	
	private transient IPropertySelectionModel publisherModel;
	private transient IPropertySelectionModel personModel;
	
	
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
				throw new ApplicationRuntimeException("external service requires two parameters.");
			
			// Not sure what's in that primary key parameter (may have spaces, slashes,
			// or other illegal characters.
			
			return getServletPrefix() +
				"/" + EXTERNAL_SERVICE +
				"/" + context[0] +
				"/" + URLEncoder.encode(context[1]);
		}
		
		public boolean service(IRequestCycle cycle,
				ResponseOutputStream output)
			throws RequestCycleException,
			ServletException,
			IOException
		{
			serviceExternal(cycle, output);
			
			return true;
		}
		
		public String getName()
		{
			return EXTERNAL_SERVICE;
		}
		
	}
	
	/**
	 *  Creates an instance of {@link Visit}.
	 *
	 */
	
	public Object createVisit(IRequestCycle cycle)
	{
		return new Visit(this);
	}
	
	/**
	 *  Removes the operations bean instance, if accessed this request cycle.
	 *
	 *  <p>May invalidate the {@link HttpSession} (see {@link #logout()}).
	 */
	
	protected void cleanupAfterRequest(IRequestCycle cycle)
	{
		clearCache();
		
		if (killSession)
		{
			try
			{
				HttpSession session = cycle.getRequestContext().getSession();
				
				if (session != null)
					session.invalidate();
			}
			catch (IllegalStateException ex)
			{
				// Ignore.
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
	
	
	public IBookQueryHome getBookQueryHome()
	{
		if (bookQueryHome == null)
			bookQueryHome = (IBookQueryHome)findNamedObject("vlib/BookQuery",
					IBookQueryHome.class);
		
		return bookQueryHome;
	}
	
	public IOperationsHome getOperationsHome()
	{
		if (operationsHome == null)
			operationsHome = (IOperationsHome)findNamedObject("vlib/Operations",
					IOperationsHome.class);
		
		return operationsHome;
	}
	
	/**
	 *  Returns an instance of the Vlib Operations beans, which is a stateless
	 *  session bean for performing certain operations.
	 *
	 *  <p>The bean is automatically removed at the end of the request cycle.
	 *
	 */
	
	public IOperations getOperations()
	{
		IOperationsHome home;
		
		for (int i = 0; i < 2; i++)
		{
			
			if (operations == null)
			{
				try
				{
					home = getOperationsHome();
					operations = home.create();
					
					break;
				}
				catch (CreateException ex)
				{
					throw new ApplicationRuntimeException(
						"Error creating operations bean.", ex);
				}
				catch (RemoteException ex)
				{
					rmiFailure(
						"Remote exception creating operations bean.", ex, i > 0);
				}
			}
		}
		
		return operations;
	}
	
	public Object findNamedObject(String name, Class expectedClass)
	{
		Object result = null;
		
		for (int i = 0; i < 2; i++)
		{
			try
			{
				Object raw = getRootNamingContext().lookup(name);
				
				result = PortableRemoteObject.narrow(raw, expectedClass);
				
				break;
			}
			catch (ClassCastException ex)
			{
				throw new ApplicationRuntimeException(
					"Object " + name + " is not type " +
						expectedClass.getName() + ".", ex);
			}
			catch (NamingException ex)
			{
				namingFailure("Unable to resolve object " + name + ".", ex, i > 0);
			}
		}
		
		return result;
	}
	
	public Context getRootNamingContext()
	{
		for (int i = 0; i < 2; i++)
		{
			if (rootNamingContext == null)
			{
				try
				{
					rootNamingContext = new InitialContext();
					
					break;
				}
				catch (NamingException ex)
				{
					namingFailure(
						"Unable to locate root naming context.", ex, i > 0);
				}
			}
		}
		
	    return rootNamingContext;
	}
	
	/**
	 *  Builds a model for entering in a publisher name, including an intial
	 *  blank option.  Problem:  thie model is held for a long while, so it won't
	 *  reflect publishers added by this user or others.  Solution:  coming; perhaps
	 *  we'll age-out the model after a few minutes.
	 *
	 */
	
	public IPropertySelectionModel getPublisherModel()
	{
		if (publisherModel == null)
			publisherModel = buildPublisherModel();
		
		return publisherModel;	
	}
	
	private IPropertySelectionModel buildPublisherModel()
	{
		Publisher[] publishers = null;
		
		EntitySelectionModel model = new EntitySelectionModel();
		
		// Add in a default null value, such that the user can
		// not select a specific Publisher.
		
		model.add(null, "");
		
		for (int i = 0; i < 2; i++)
		{
			IOperations bean = getOperations();
			
			try
			{
				publishers = bean.getPublishers();
				
				// Exit the retry loop
				
				break;
			}
			catch (RemoteException ex)
			{
				rmiFailure(
					"Unable to obtain list of publishers.", ex, i > 0);
			}
		}
		
		// Add in the actual publishers.  They are sorted by name.
		
		for (int i = 0; i < publishers.length; i++)
			model.add(publishers[i].getPrimaryKey(), publishers[i].getName());
		
		return model;		
	}
	
	/**
	 *  Invoked from {@link Visit#clearCache()} (and at the end of the request
	 *  cycle) to clear the publisher and person
	 *  {@link IPropertySelectionModel} models.
	 *
	 */
	
	
	public void clearCache()
	{
		publisherModel = null;
		personModel = null;
	}
	
	/**
	 *  Returns a model that contains all the known Person's, sorted by last name,
	 *  then first.  The label for the model matches the user's natural name.
	 *
	 */
	
	public IPropertySelectionModel getPersonModel()
	{
		if (personModel == null)
			personModel = buildPersonModel();
		
		return personModel;	
	}
	
	private IPropertySelectionModel buildPersonModel()
	{
		Person[] persons = null;
		
		for (int i = 0; i < 2; i++)
		{
			IOperations bean = getOperations();
			
			try
			{
				persons = bean.getPersons();
				
				break;
			}
			catch (RemoteException ex)
			{
				rmiFailure(
					"Unable to obtain list of persons.", ex, i > 0);
			}
		}
		
		EntitySelectionModel model = new EntitySelectionModel();
		
		// On this one, we don't include a null option.
		
		for (int i = 0; i < persons.length; i++)
			model.add(persons[i].getPrimaryKey(),
					persons[i].getNaturalName());
		
		return model;	  
		
	}
	
	/**
	 *  Creates a new {@link IBookQuery} EJB instance.
	 *
	 */
	
	public IBookQuery createNewQuery()
    {
		IBookQuery result = null;
		
		for (int i = 0; i < 2; i++)
		{
			IBookQueryHome home = getBookQueryHome();
			
			try
			{
				result = home.create();
				
				break;
			}
			catch (CreateException ex)
			{
				throw new ApplicationRuntimeException(
					"Could not create BookQuery bean.", ex);
			}
			catch (RemoteException ex)
			{
				rmiFailure("Remote exception creating BookQuery bean.", ex, i > 0);
			}
		}
		
		return result;
	}

	/**
	 *  Invoked in various places to present an error message to the user.
	 *  This sets the error property of either the {@link Home} or
	 *  {@link MyLibrary} page (the latter only if the user is logged in),
	 *  and sets the selected page for rendering the response.
	 *
	 */
	
	public void presentError(String error, IRequestCycle cycle)
	{
		String pageName = "Home";
		// Get, but don't create, the visit.
		Visit visit = (Visit)getVisit();
		
		if (visit != null && visit.isUserLoggedIn())
			pageName = "MyLibrary";
		
		IErrorProperty page = (IErrorProperty)cycle.getPage(pageName);
		
		page.setError(error);
		
		cycle.setPage(pageName);
	}
	
	/**
	 *  Invoked after an operation on a home or remote interface
	 *  throws a RemoteException; this clears any cache of
	 *  home and remote interfaces.  
	 *
	 * @param message the message for the exception, or for the log message
	 * @param ex the exception thrown
	 * @param throwException if true, an {@link ApplicationRuntimeException}
	 * is thrown after the message is logged.
	 *
	 */
	
	public void rmiFailure(String message, RemoteException ex, boolean throwException)
	{
		CAT.error(message, ex);
		
		if (throwException)
			throw new ApplicationRuntimeException(message, ex);

		clearEJBs();
	}
	
	/**
	 *  As with {@link #rmiFailure(RemoteException)}, but for
	 * {@link NamingException}.
	 *
	 */
	
	public void namingFailure(String message, NamingException ex, boolean throwException)
	{
		CAT.error(message, ex);
		
		if (throwException)
			throw new ApplicationRuntimeException(message, ex);
		
		clearEJBs();
	}
	
	private void clearEJBs()
	{
		bookQueryHome = null;
		operations = null;
		operationsHome = null;
		rootNamingContext = null;
	}
}
