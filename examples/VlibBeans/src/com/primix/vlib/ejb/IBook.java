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
 *  Remote interface for the {@link BookBean} entity bean.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
package com.primix.vlib.ejb;

import javax.ejb.*;
import java.rmi.*;

public interface IBook extends IEntityBean
{
	public void setAuthor(String value)
	throws RemoteException;

	public String getAuthor()
	throws RemoteException;	

	public void setTitle(String value)
	throws RemoteException;

	public String getTitle()
	throws RemoteException;

	public void setDescription(String value)
	throws RemoteException;

	public String getDescription()
	throws RemoteException;

	public void setISBN(String value)
	throws RemoteException;

	public String getISBN()
	throws RemoteException;

	public void setOwnerPK(Integer value)
	throws RemoteException;

	public Integer getOwnerPK()
	throws RemoteException;
	
	public void setHolderPK(Integer value)
	throws RemoteException;
	
	public Integer getHolderPK()
	throws RemoteException;
	
	public void setPublisherPK(Integer value)
	throws RemoteException;
	
}
