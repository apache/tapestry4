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
	 
	private Handle userHandle;
	private transient IPerson user;
	private transient Integer userPK;
	private transient String fullUserName;
	
	private transient IPublisherHome publisherHome;
	private transient IBookHome bookHome;
	private transient IPersonHome personHome;
	private transient IBookQueryHome bookQueryHome;
	private transient IVlibOperationsHome operationsHome;
	private transient IVlibOperations operations;
	
	private transient Context environment;	
	
	// Includes a null option for searching without care to Publisher
	
	private IPropertySelectionModel publisherSearchModel;
	
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
	}
	
	public IPerson getUser()
	{
		if (user != null)
			return user;
		
		if (userHandle == null)
			return null;
			
		try
		{
			user = (IPerson)userHandle.getEJBObject();
			userPK = (Integer)user.getPrimaryKey();
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException("Could not get user from handle.", e);
		}
		
		return user;
	}
	
	public Integer getUserPK()
	{
		// If the user is not known, then get it.  Side effect:  sets
		// the userPK.
		
		if (userPK == null)
			getUser();
		
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

	public IVlibOperationsHome getOperationsHome()
	{
		if (operationsHome == null)
			operationsHome = (IVlibOperationsHome)findNamedObject("ejb/VlibOperations",
				IVlibOperationsHome.class);
		
		return operationsHome;
	}
	
	/**
	 *  Returns an instance of the Vlib Operations beans, which is a stateless
	 *  session bean for performing certain operations.
	 *
	 *  <p>The bean is automatically removed at the end of the request cycle.
	 *
	 */
	 				
	public IVlibOperations getOperations()
	{
		IVlibOperationsHome home;
		
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
			raw = getEnvironment().lookup(name);
			
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
		
		fullUserName= null;
		userHandle = null;
		
		if (user == null)
			return;
		
		try
		{
			userHandle = user.getHandle();
			userPK = (Integer)user.getPrimaryKey();
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException("Could not get handle for user.", e);
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
		return user != null || userHandle != null;
	}
	
	public boolean isLoggedInUser(Integer primaryKey)
	{
		IPerson user;
		Integer userPK;
		
		user = getUser();
		if (user == null)
			return false;
		
		try
		{
			userPK = (Integer)user.getPrimaryKey();
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e.getMessage(), e);
		}
	
		return userPK.equals(primaryKey);
	}
	
	public IDirectListener getMyBooksListener()
	{
		return null;
	}
	
	public IDirectListener getLogoutListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context,
					IRequestCycle cycle)
			{
				setUser(null);
				cycle.setPage("logout");
			}
		};
	}
	
	public IPropertySelectionModel getPublisherSearchModel()
	{
		if (publisherSearchModel == null)
			publisherSearchModel = buildPublisherModel(true);
		
		return publisherSearchModel;	
	}
	
	private IPropertySelectionModel buildPublisherModel(boolean includeNull)
	{
		VirtualLibraryApplication app;
		IPublisherHome home;
		EntitySelectionModel model;
		IPublisher publisher;
		Iterator i;
		
		model = new EntitySelectionModel();
		
		home = getPublisherHome();
		
		// Add in a default null value, such that the user can
		// not select a specific Publisher.
		
		if (includeNull)
			model.add(null, "");
		
		try
		{
			i = home.findAll().iterator();
			
			while (i.hasNext())
			{
				publisher = (IPublisher)PortableRemoteObject.narrow(i.next(), 
					IPublisher.class);
				
				model.add((Integer)(publisher.getPrimaryKey()),
						  publisher.getName());
			}
		}
		catch (Throwable t)
		{
			throw new ApplicationRuntimeException("Unable to build publisher model: " +
				t.getMessage() + ".", t);
		}
		
		model.sort();
		
		return model;		
	}
	
}