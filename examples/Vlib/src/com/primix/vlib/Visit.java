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

/**
 *  The visit object for the {@link VirtualLibraryEngine}.
 *
 *  Primarily, this is used to access the home interfaces and EJB instances, and
 *  to identify who the logged in user is.
 *
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class Visit
	implements Serializable
{
	/**
	 *  Used to identify the logged in user.
	 *
	 */
	
	private transient Person user;
	private Integer userPK;

	private VirtualLibraryEngine engine;
	
	public Visit(VirtualLibraryEngine engine)
	{
		this.engine = engine;
	}
	
	public VirtualLibraryEngine getEngine()
	{
		return engine;
	}
	
	/**
	 *  Gets the logged-in user, or null if the user is not logged in.
	 *
	 */
	
	public Person getUser()
	{
		if (user != null)
			return user;
		
		if (userPK == null)
			return null;
		
		for (int i = 0; i < 2; i++)
		{
			try
			{
				user = engine.getOperations().getPerson(userPK);
				
				break;
			}
			catch (FinderException e)
			{
				throw new ApplicationRuntimeException("Could not locate user.", e);
			}
			catch (RemoteException ex)
			{
				engine.rmiFailure("Unable to access logged-in user.", ex, i > 0);
			}
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
	
	
	
	/**
	 *  Changes the logged in user ... this is only invoked from the {@link Login}
	 *  page.
	 *
	 */
	
	public void setUser(Person value)
	{
		user = value;
		userPK = null;		
		
		if (user == null)
			return;
		
		userPK = user.getPrimaryKey();
	}
		
	/**
	 *  Returns true if the user is logged in.
	 *
	 */
	
	public boolean isUserLoggedIn()
	{
		return userPK != null;
	}
	
	/**
	 *  Returns true if the user has not been identified (has not
	 *  logged in).
	 *
	 */
	
	public boolean isUserLoggedOut()
	{
		return userPK == null;
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
		
		engine.clearCache();		
	}
	
}
