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
	
	private transient Context environment;	
	
	private static final Map externalReferences = new HashMap();
	
    // This duplicates data normally in the weblogic.xml file ... but in
    // standalone mode (i.e., under ServletExec Debugger), we don't have
    // access to the environment naming context.
    
	static
	{
		externalReferences.put("ejb/Person", "com.primix.vlib.Person");
		externalReferences.put("ejb/Book", "com.primix.vlib.Book");
		externalReferences.put("ejb/BookQuery", "com.primix.vlib.BookQuery");
		externalReferences.put("ejb/Publisher", "com.primix.vlib.Publisher");
		externalReferences.put("ejb/Operations", "com.primix.vlib.Operations");
	}
	
	/**
	 *  When using a standalone servlet container (such as Servlet Exec Debugger),
	 *  set all the necessary system properties for locating the JNDI naming
	 *  context and set an additional property, standalone, so that
	 *  we know.
	 *
	 */
	 
	private static final boolean standaloneServletContainer 
		= Boolean.getBoolean("standalone");
	
	// Includes a null option for searching without care to Publisher
	
	private IPropertySelectionModel publisherModel;
	private IPropertySelectionModel personModel;
	
	public VirtualLibraryApplication(RequestContext context)
	{
		super(context, null);
	}
	
	protected String getSpecificationAttributeName()
	{
		return "vlib.spec";	
	}
	
	
	protected String getSpecificationResourceName()
	{
		return "/com/primix/vlib/Vlib.application";	
	}
	
	/**
	 *  Removes the operations bean instance, if accessed this request cycle.
	 *
	 */
	 
	protected void cleanupAfterRequest()
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
        
	}
	
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
	
	public Integer getUserPK()
	{
		return userPK;
	}	
	
	public IPersonHome getPersonHome()
	{
		if (personHome == null)
			personHome = (IPersonHome)findNamedObject("ejb/Person", IPersonHome.class);
		
		return personHome;	
	}
	
	public IPublisherHome getPublisherHome()
	{
		if (publisherHome == null)
		  publisherHome = (IPublisherHome)findNamedObject("ejb/Publisher",
		  		IPublisherHome.class);
		
		return publisherHome;		
	}
	
	public IBookHome getBookHome()
	{
		if (bookHome == null)
			bookHome = (IBookHome)findNamedObject("ejb/Book", IBookHome.class);
		
		return bookHome;	
	}
	
	public IBookQueryHome getBookQueryHome()
	{
		if (bookQueryHome == null)
			bookQueryHome = (IBookQueryHome)findNamedObject("ejb/BookQuery",
				IBookQueryHome.class);
		
		return bookQueryHome;
	}

	public IOperationsHome getOperationsHome()
	{
		if (operationsHome == null)
			operationsHome = (IOperationsHome)findNamedObject("ejb/Operations",
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
		String resolvedName = name;
		
		try
		{
			if (standaloneServletContainer)
				resolvedName = (String)externalReferences.get(name);
		
			raw = getEnvironment().lookup(resolvedName);
			
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
	
	public Context getEnvironment()
	{
		InitialContext initial;
		
		if (environment == null)
		{
			try
			{
				initial = new InitialContext();
			}
			catch (NamingException e)
			{
				throw new ApplicationRuntimeException("Unable to acquire initial naming context.", e);
			}
			
			try
			{
				if (standaloneServletContainer)
					environment = initial;
				else	
					environment = (Context)initial.lookup("java:comp/env");
			}
			catch (NamingException e)
			{
				throw new ApplicationRuntimeException(
					"Unable to resolve environment naming context from initial context.", 
					e);
			}		
		}
		
		return environment;
	}
	
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
	 *  Returns true if the user is known.
	 *
	 */
	 
	public boolean isUserLoggedIn()
	{
		return getUser() != null;
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
	 *  Gets the newBook page and sets it's returnPage property to point back to
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
	 *  a new Publisher entity.
	 *
	 */
	 
	public void clearCache()
	{
		user = null;
		fullUserName = null;
		publisherModel = null;
		personModel = null;
	}
	
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
	 *  Used from a couple of pages; allows a link to the user's own inventory.
	 *  Context should have one element: the primary key of the person.
	 *
	 */
	 
	public IDirectListener getPersonLinkListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context, 
						IRequestCycle cycle)
						throws RequestCycleException
			{
				PersonPage page;
				Integer personPK;
				
				page = (PersonPage)cycle.getPage("Person");
				
				personPK = new Integer(context[0]);
				
				page.setup(personPK);
				
				cycle.setPage(page);
			}
		};
	}
	
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
	
	public IDirectListener getViewBookListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component,
					String[] context,
					IRequestCycle cycle) throws RequestCycleException
			{
				Integer bookPK;
				ViewBook page;
				
				bookPK = new Integer(context[0]);
				page = (ViewBook)cycle.getPage("ViewBook");
				
				page.setup(bookPK, cycle);
			}
		};
	}
	
}