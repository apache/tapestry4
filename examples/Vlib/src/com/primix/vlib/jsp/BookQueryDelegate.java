package com.primix.vlib.jsp;

import com.primix.servlet.*;
import javax.ejb.*;
import com.primix.vlib.ejb.*;
import java.util.*;
import javax.servlet.*;
import com.primix.tapestry.ApplicationRuntimeException;
import java.io.IOException;
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
 *  Abstract {@link VlibDelegate} class that maintains a reference
 *  to a {@link IBookQuery}.
 *
 *  @version $Id$
 *  @author Howard Ship
 */
 
public abstract class BookQueryDelegate extends VlibDelegate
{
	/**
	 *  Handle of the active book query.
	 *
	 */
	 
	private Handle queryHandle;
	
	/**
	 *  Reference to the active book query.
	 *
	 */
	 
	private transient IBookQuery query;

	public BookQueryDelegate(RequestContext context)
	{
		super(context);
	}
	
	public IBookQuery getQuery()
	{
		if (query == null)
			setQueryFromHandle();
		
		return query;	
	}

	private void setQueryFromHandle()
	{
		query = null;
		
		if (queryHandle == null)
			return;
		
		try
		{
			query = (IBookQuery)PortableRemoteObject.narrow(queryHandle.getEJBObject(),
					IBookQuery.class);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException (e);
		}
	}

	/**
	 *  Gets the current query or creates a new one.
	 *
	 */

	public IBookQuery getOrCreateQuery()
	{
		if (query == null)
			setQueryFromHandle();
		
		if (query == null)
			createNewQuery();
			
		return query;
	}
			

	private void createNewQuery()
	{
		IBookQueryHome home;
		
		home = application.getBookQueryHome();
		
		try
		{
			query = home.create();
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		catch (CreateException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		
		try
		{
			queryHandle = query.getHandle();
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
	}

}
