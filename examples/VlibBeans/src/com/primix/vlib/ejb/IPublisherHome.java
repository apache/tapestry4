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

package com.primix.vlib.ejb;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;

/**
 *  Home interface for the {@link PublisherBean} entity bean.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */
 
public interface IPublisherHome extends EJBHome
{
	public IPublisher create(String name)
	throws CreateException, RemoteException;

	public IPublisher findByPrimaryKey(Integer key)
	throws FinderException, RemoteException;	

	/**
	 *  Finds Publisher with exact match on name.
	 *
	 */
	 
	public IPublisher findByName(String name)
	throws FinderException, RemoteException;
	
	/**
	 *  Finds all Publishers.
	 *
	 */
	 
	 public Collection findAll()
	 throws FinderException, RemoteException;
}