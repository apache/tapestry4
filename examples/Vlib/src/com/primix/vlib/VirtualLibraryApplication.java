package com.primix.vlib;

import com.primix.tapestry.*;
import com.primix.tapestry.app.*;
import com.primix.tapestry.components.*;
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
 *
 *  The application class for the Primix Virtual Library.  Primarily,
 *  this is used to access the home interfaces and EJB instances, and
 *  to identify who the logged in user is.  It also provides the external
 *  service, which allows the {@link ViewBook} and {@link PersonPage}
 *  pages to be bookmarked.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class VirtualLibraryApplication extends SimpleApplication
{
	/**
	 *  Used to identify the logged in user.
	 *
	 */
	 
	private transient IPerson user;
	private Integer userPK;
	private transient String fullUserName;
	
	private transient IPublisherHome publisherHome;
	private transient IBookHome bookHome;
	private transient IPersonHome personHome;
	private transient IBookQueryHome bookQueryHome;
	private transient IOperationsHome operationsHome;
	private transient IOperations operations;
	
	private transient Context rootNamingContext;	
	
    private boolean killSession;

	private IPropertySelectionModel publisherModel;
	private IPropertySelectionModel personModel;
	
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
	 
	public class ExternalService implements IApplicationService
	{
		public String buildURL(IRequestCycle cycle,
                       IComponent component,
                       String[] context)
		{
			if (context == null || context.length != 2)
				throw new ApplicationRuntimeException("external service required two parameters.");
			
			// Not sure what's in that primary key parameter (may have spaces, slashes,
			// or other illegal characters.
			
			return getServletPrefix() +
				"/" + EXTERNAL_SERVICE +
				"/" + context[0] +
				"/" + URLEncoder.encode(context[1]);
		}
		
		public void service(IRequestCycle cycle,
                    ResponseOutputStream output)
             throws RequestCycleException,
                    ServletException,
                    IOException
        {
			serviceExternal(cycle, output);
        }
		
	}
	
	public VirtualLibraryApplication(RequestContext context)
	{
		super(context, null);
	}
	
	
	/**
	 *  Removes the operations bean instance, if accessed this request cycle.
	 *
     *  <p>May invalidate the {@link HttpSession} (see {@link #logout()}).
	 */
	 
	protected void cleanupAfterRequest(IRequestCycle cycle)
	{
		if (operations != null)
		{
			try
			{
				operations.remove();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		operations = null;
        
        // Force a rebuild of the publisher and person models on the next
        // request cycle that needs them.  This is needed because new
        // persons and publisher can be added by anyone at anytime.
        
		publisherModel = null;
        personModel = null;

        if (killSession)
            cycle.getRequestContext().getSession().invalidate();
	}
	
	/**
	 *  Gets the logged-in user, or null if the user is not logged in.
	 *
	 */
	 
	public IPerson getUser()
	{
		if (user != null)
			return user;
		
		if (userPK == null)
			return null;
			
		try
		{
			user = getPersonHome().findByPrimaryKey(userPK);
		}
		catch (FinderException e)
		{
			throw new ApplicationRuntimeException("Could not locate user.", e);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException("Could not get user.", e);
		}
		
		return user;
	}
	
	/**
	 *  Returns the primary key of the logged in user, or null if the
	 *  user is not logged in.
	 *
	 */
	 
	public Integer getUserPK()
	{
		return userPK;
	}	
	
	public IPersonHome getPersonHome()
	{
		if (personHome == null)
			personHome = (IPersonHome)findNamedObject("com.primix.vlib.Person", IPersonHome.class);
		
		return personHome;	
	}
	
	public IPublisherHome getPublisherHome()
	{
		if (publisherHome == null)
		  publisherHome = (IPublisherHome)findNamedObject("com.primix.vlib.Publisher",
		  		IPublisherHome.class);
		
		return publisherHome;		
	}
	
	public IBookHome getBookHome()
	{
		if (bookHome == null)
			bookHome = (IBookHome)findNamedObject("com.primix.vlib.Book", IBookHome.class);
		
		return bookHome;	
	}
	
	public IBookQueryHome getBookQueryHome()
	{
		if (bookQueryHome == null)
			bookQueryHome = (IBookQueryHome)findNamedObject("com.primix.vlib.BookQuery",
				IBookQueryHome.class);
		
		return bookQueryHome;
	}

	public IOperationsHome getOperationsHome()
	{
		if (operationsHome == null)
			operationsHome = (IOperationsHome)findNamedObject("com.primix.vlib.Operations",
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
		
		if (operations == null)
		{
			try
			{
				home = getOperationsHome();
				operations = home.create();
			}
			catch (CreateException e)
			{
				throw new ApplicationRuntimeException("Error creating operations bean: " + e, e);
			}
			catch (RemoteException e)
			{
				throw new ApplicationRuntimeException("Remote exception creating operations bean: " + e, e);
			}
		}
		
		return operations;
	}
					
	public Object findNamedObject(String name, Class expectedClass)
	{
		Object raw;
		Object result;
		
		try
		{
			raw = getRootNamingContext().lookup(name);
			
			result = PortableRemoteObject.narrow(raw, expectedClass);
		}
		catch (ClassCastException cce)
		{
			throw new ApplicationRuntimeException(
				"Object " + name + " is not type " +
				expectedClass.getName() + ".", cce);
		}
		catch (NamingException e)
		{
			throw new ApplicationRuntimeException("Unable to resolve object " + name + ": " +
			e.toString(), e);
		}
		
		return result;
	}
	
    public Context getRootNamingContext()
    {
        if (rootNamingContext == null)
        {
            try
            {
                rootNamingContext = new InitialContext();
            }
            catch (NamingException e)
            {
                throw new ApplicationRuntimeException("Unable to locate root naming context.", e);
            }
        }

        return rootNamingContext;
    }

	/**
	 *  Changes the logged in user ... this is only invoked from the {@link Login}
	 *  page.
	 *
	 */
	 	
	public void setUser(IPerson value)
	{
		user = value;
		userPK = null;		
		fullUserName = null;
		
		if (user == null)
			return;
		
		try
		{
			userPK = (Integer)user.getPrimaryKey();
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException("Could not get primary key for user.", e);
		}
	}
	
	/**
	 *  Returns the full name of the logged-in user, 
	 *  i.e., from {@link IPerson#getNaturalName()}.
	 *
	 */
	 
	public String getFullUserName()
	{
		if (fullUserName == null)
		{
			try
			{
				fullUserName = getUser().getNaturalName();
			}
			catch (RemoteException e)
			{
				throw new ApplicationRuntimeException("Could not get user's name: " + e.toString(),
						e);
			}		
		}
		
		return fullUserName;
	}
	
	/**
	 *  Returns true if the user is logged in.
	 *
	 */
	 
	public boolean isUserLoggedIn()
	{
		return userPK != null;
	}
	
	public boolean isLoggedInUser(Integer primaryKey)
	{
		if (userPK == null)
			return false;
				
		return userPK.equals(primaryKey);
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
		IOperations bean;
		Publisher[] publishers;
		EntitySelectionModel model;
		int i;
		
		model = new EntitySelectionModel();
		
		// Add in a default null value, such that the user can
		// not select a specific Publisher.
		
		model.add(null, "");

		bean = getOperations();
		
		try
		{
			publishers = bean.getPublishers();
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e.getMessage(), e);
		}
		
		// Add in the actual publishers.  They are sorted by name.
		
		for (i = 0; i < publishers.length; i++)
			model.add(publishers[i].getPrimaryKey(), publishers[i].getName());
		
		return model;		
	}
	
	/**
	 *  Used from pages that want to add a book.
	 *  Gets the {@link NewBook} page and sets it's returnPage property to point back to
	 *  the current page.
	 *
	 */
	 
	public IDirectListener getAddNewBookListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context,
					IRequestCycle cycle)
			{
				NewBook newBookPage;
				IPage currentPage;
				
				currentPage = cycle.getPage();
				newBookPage = (NewBook)cycle.getPage("NewBook");
				
				newBookPage.setReturnPage(currentPage.getName());
				
				cycle.setPage(newBookPage);
			}
		};
	}

	/**
	 *  Invoked by pages after they perform an operation that changes the backend
	 *  database in such a way that cached data is no longer valid.  Currently,
	 *  this should be invoked after changing the user's profile, or adding
	 *  a new {@link IPublisher} entity.
	 *
	 */
	 
	public void clearCache()
	{
		user = null;
		fullUserName = null;
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
		EntitySelectionModel model;
		IOperations bean;
		Person[] persons;
		int i;
		
		bean = getOperations();
		
		try
		{
			persons = bean.getPersons();
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		
		model = new EntitySelectionModel();
		
		// On this one, we don't include a null option.
		
		for (i = 0; i < persons.length; i++)
			model.add(persons[i].getPrimaryKey(),
					  persons[i].getNaturalName());
				
		return model;	  
		
	}
	
	/**
	 *  Used from a couple of pages to actually borrow a book.  The {@link Direct}
	 * component should set its context to the primary key of the book to borrow.
	 *
	 */
	 
	public IDirectListener getBorrowListener()
	{
	    return new IDirectListener()
	    {
	        public void directTriggered(IComponent component, String[] context,
	                IRequestCycle cycle)
					throws RequestCycleException
	        {
				Integer bookPK;
				
				// The primary key of the book to borrow is encoded in the context.
				bookPK = new Integer(context[0]);
				
				borrowBook(bookPK, cycle);
	        }
	    };
	}
	
	private void borrowBook(Integer bookPK, IRequestCycle cycle)
	throws RequestCycleException
	{
		IOperations bean;
		Home home;
		Integer borrowerPK;
		IBook book;
		home = (Home)cycle.getPage("Home");

		bean = getOperations();				

		try
		{
			book = bean.borrowBook(bookPK, userPK);

			home.setMessage("Borrowed: " + book.getTitle());
		}
		catch (FinderException e)
		{
			throw new ApplicationRuntimeException(
				"Unable to find book or user. ", e);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}

		cycle.setPage(home);				
	}
	

	/**
	 *  Supports construction of the external service.
	 *
	 */
	 
	protected IApplicationService constructService(String name)
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
     *  Sets the user property to null, and sets a flag that
     *  invalidates the {@link HttpSession} at the end of the request cycle.
     *
     */

    public void logout()
    {
        user = null;
        killSession = true;
    }
}