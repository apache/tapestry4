//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//


package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;
import java.sql.Timestamp;

/** 
 *  Remote interface for the Person entity bean.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IPerson extends IEntityBean
{
	public void setLastName(String value) throws RemoteException;

	public String getLastName() throws RemoteException;

	public void setFirstName(String value) throws RemoteException;

	public String getFirstName() throws RemoteException;

	public void setPassword(String value) throws RemoteException;

	public String getPassword() throws RemoteException;

	public void setEmail(String value) throws RemoteException;

	public String getEmail() throws RemoteException;

	public boolean isAdmin() throws RemoteException;

	public void setAdmin(boolean value) throws RemoteException;

	public boolean isLockedOut() throws RemoteException;

	public void setLockedOut(boolean value) throws RemoteException;

	public boolean isVerified() throws RemoteException;

	public void setVerified(boolean value) throws RemoteException;

	/**
	 *  Returns the natural concatination of the first and last name, or just
	 *  the last name if there is no first name.
	 *
	 **/

	public String getNaturalName() throws RemoteException;

	public void setLastAccess(Timestamp value) throws RemoteException;

	public Timestamp getLastAccess() throws RemoteException;
}