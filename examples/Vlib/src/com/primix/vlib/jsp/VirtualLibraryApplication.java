package com.primix.vlib.jsp;

import com.primix.servlet.*;
import javax.naming.*;
import javax.ejb.*;
import com.primix.vlib.ejb.*;
import java.rmi.*;
import javax.rmi.*;
import java.util.*;
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
 *  A cut-and-paste of {@link com.primix.vlib.VirtualLibraryApplication}, for
 *  use in the JSP implementation.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class VirtualLibraryApplication implements Serializable
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
	
	private static final String ATTRIBUTE_NAME = "application";
	
	/**
	 *  Creates the VirtualLibraryApplication instance, and stores it
	 *  into the {@link HttpSession}.
	 *
	 */
	 
	public VirtualLibraryApplication(RequestContext context)
	{
		context.setSessionAttribute(ATTRIBUTE_NAME, this);
	}
	
	/**
	 *  Gets the VirtualLibraryApplication from the HttpSession, creating
	 *  it if necessary.
	 *
	 */
	 
	public static VirtualLibraryApplication get(RequestContext context)
	{
		VirtualLibraryApplication result;
		
		result = (VirtualLibraryApplication)context.getSessionAttribute(ATTRIBUTE_NAME);
		
		if (result == null)
			result = new VirtualLibraryApplication(context);
		
		return result;	
	}
	
	/**
	 *  Removes the operations bean instance, if accessed this request cycle.
	 *
	 */
	 
	public void cleanup()
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
   	}
	
	/**
	 *  Gets the logged-in user, or null if the user is not logged in.
	 *
	 */
	 
	public IPerson getUser()
	throws ServletException
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
			throw new ServletException("Could not locate user.", e);
		}
		catch (RemoteException e)
		{
			throw new ServletException("Could not get user.", e);
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
	throws ServletException
	{
		if (personHome == null)
			personHome = (IPersonHome)findNamedObject("ejb/Person", IPersonHome.class);
		
		return personHome;	
	}
	
	public IPublisherHome getPublisherHome()
	throws ServletException
	{
		if (publisherHome == null)
		  publisherHome = (IPublisherHome)findNamedObject("ejb/Publisher",
		  		IPublisherHome.class);
		
		return publisherHome;		
	}
	
	public IBookHome getBookHome()
	throws ServletException
	{
		if (bookHome == null)
			bookHome = (IBookHome)findNamedObject("ejb/Book", IBookHome.class);
		
		return bookHome;	
	}
	
	public IBookQueryHome getBookQueryHome()
	throws ServletException
	{
		if (bookQueryHome == null)
			bookQueryHome = (IBookQueryHome)findNamedObject("ejb/BookQuery",
				IBookQueryHome.class);
		
		return bookQueryHome;
	}

	public IOperationsHome getOperationsHome()
	throws ServletException
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
	throws ServletException
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
				throw new ServletException("Error creating operations bean: " + e, e);
			}
			catch (RemoteException e)
			{
				throw new ServletException("Remote exception creating operations bean: " + e, e);
			}
		}
		
		return operations;
	}
					
	public Object findNamedObject(String name, Class expectedClass)
	throws ServletException
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
			throw new ServletException(
				"Object " + name + " is not type " +
				expectedClass.getName() + ".", cce);
		}
		catch (NamingException e)
		{
			throw new ServletException("Unable to resolve object " + name + ": " +
			e.toString(), e);
		}
		
		return result;
	}
	
	public Context getEnvironment()
	throws ServletException
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
				throw new ServletException("Unable to acquire initial naming context.", e);
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
				throw new ServletException(
					"Unable to resolve environment naming context from initial context.", 
					e);
			}		
		}
		
		return environment;
	}

	/**
	 *  Changes the logged in user ... this is only invoked from the {@link Login}
	 *  page.
	 *
	 */
	 	
	public void setUser(IPerson value)
	throws ServletException
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
			throw new ServletException("Could not get primary key for user.", e);
		}
	}
	
	/**
	 *  Returns the full name of the logged-in user, 
	 *  i.e., from {@link IPerson#getNaturalName()}.
	 *
	 */
	 
	public String getFullUserName()
	throws ServletException
	{
		if (fullUserName == null)
		{
			try
			{
				fullUserName = getUser().getNaturalName();
			}
			catch (RemoteException e)
			{
				throw new ServletException("Could not get user's name: " + e.toString(),
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
	}

	/**
	 *  Returns true only if the book's owner is not the same as its holder.
	 *
	 */
	 
	public boolean getShowHolder(Book book)
	{
		Integer ownerPK;
		Integer holderPK;
		
		ownerPK = book.getOwnerPrimaryKey();
		holderPK = book.getHolderPrimaryKey();
		
		return ! ownerPK.equals(holderPK);
	}

}