package com.primix.vlib;

import com.primix.tapestry.*;
import com.primix.tapestry.app.*;
import com.primix.tapestry.components.*;
import javax.naming.*;
import javax.ejb.*;
import com.primix.vlib.ejb.*;
import java.rmi.*;
import javax.rmi.*;

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
	
	private transient Context environment;	
	
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
	
	public IPerson getUser()
	{
		if (user != null)
			return user;
		
		if (userHandle == null)
			return null;
			
		try
		{
			user = (IPerson)userHandle.getEJBObject();
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException("Could not get user from handle.", e);
		}
		
		return user;
	}
	
	public IPersonHome getPersonHome()
	{
		return (IPersonHome)findNamedObject("ejb/Person", IPersonHome.class);
	}
	
	public IPublisherHome getPublisherHome()
	{
		return (IPublisherHome)findNamedObject("ejb/Publisher", IPublisherHome.class);
	}
	
	public IBookHome getBookHome()
	{
		return (IBookHome)findNamedObject("ejb/Book", IBookHome.class);
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
		
		userHandle = null;
		if (user == null)
			return;
		
		try
		{
			userHandle = user.getHandle();
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException("Could not get handle for user.", e);
		}
	}
	
	/**
	 *  Returns true if the user is known.
	 *
	 */
	 
	public boolean isUserLoggedIn()
	{
		return user != null || userHandle != null;
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
}