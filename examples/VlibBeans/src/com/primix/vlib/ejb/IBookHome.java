package com.primix.vlib.ejb;

import javax.ejb.*;
import java.rmi.*;
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
 *  Home interface for the {@link BookBean} entity bean.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */
 
public interface IBookHome extends EJBHome
{
	public IBook create(String title, String ISBN, IPublisher publisher, IPerson owner)
		throws CreateException, RemoteException;
	
	public IBook findByPrimaryKey(Integer key)
		throws FinderException, RemoteException;	
	
	public Collection findByOwner(IPerson owner)
		throws FinderException, RemoteException;
		
	public Collection findByHolder(IPerson holder)
		throws FinderException, RemoteException;
		
	public Collection findByPublisher(IPublisher publisher)
		throws FinderException, RemoteException;
		
	/**
	 *  Finds Books with an exact match on Title.  There may be more than one
	 *  (different people may own the same book).
	 *
	 */
	 
	public Collection findByTitle(String title)
		throws FinderException, RemoteException;	
}	