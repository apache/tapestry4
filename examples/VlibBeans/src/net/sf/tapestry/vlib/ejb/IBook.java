/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package net.sf.tapestry.vlib.ejb;

import javax.ejb.*;
import java.rmi.*;
import java.sql.Timestamp;

/**
 *  Remote interface for the Book entity bean.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public interface IBook extends IEntityBean
{
	public void setAuthor(String value) throws RemoteException;

	public String getAuthor() throws RemoteException;

	public void setTitle(String value) throws RemoteException;

	public String getTitle() throws RemoteException;

	public void setDescription(String value) throws RemoteException;

	public String getDescription() throws RemoteException;

	public void setISBN(String value) throws RemoteException;

	public String getISBN() throws RemoteException;

	public void setOwnerPK(Integer value) throws RemoteException;

	public Integer getOwnerPK() throws RemoteException;

	public void setHolderPK(Integer value) throws RemoteException;

	public Integer getHolderPK() throws RemoteException;

	public void setPublisherPK(Integer value) throws RemoteException;

	public boolean isHidden() throws RemoteException;

	public void setHidden(boolean value) throws RemoteException;

	public boolean isLendable() throws RemoteException;

	public void setLendable(boolean value) throws RemoteException;

	public Timestamp getDateAdded() throws RemoteException;

	public void setDateAdded(Timestamp value) throws RemoteException;
}