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
 *  Remote interface for the {@link PersonBean} entity bean.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public interface IPerson extends IEntityBean
{
	public void setLastName(String value)
	throws RemoteException;

	public String getLastName()
	throws RemoteException;
	
	public void setFirstName(String value)
	throws RemoteException;
	
	public String getFirstName()
	throws RemoteException;
	
	public void setPassword(String value)
	throws RemoteException;
	
	public String getPassword()
	throws RemoteException;
	
	public void setEmail(String value)
	throws RemoteException;
	
	public String getEmail()
	throws RemoteException;	

	public Collection getHeldBooks()
	throws FinderException, RemoteException;	

	public Collection getOwnedBooks()
	throws FinderException, RemoteException;	

	/**
	 *  Returns the natural concatination of the first and last name, or just
	 *  the last name if there is not first name.
	 *
	 */
	 
	public String getNaturalName()
	throws RemoteException;	
}