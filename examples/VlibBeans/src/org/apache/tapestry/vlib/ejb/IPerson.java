//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.vlib.ejb;

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

	public boolean getAdmin() throws RemoteException;

	public void setAdmin(boolean value) throws RemoteException;

	public boolean getLockedOut() throws RemoteException;

	public void setLockedOut(boolean value) throws RemoteException;

	public void setLastAccess(Timestamp value) throws RemoteException;

	public Timestamp getLastAccess() throws RemoteException;
}