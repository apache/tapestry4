// Copyright 2004, 2005 The Apache Software Foundation
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
 * Remote interface for the Person entity bean.
 * 
 * @author Howard Lewis Ship
 */

public interface IPerson extends IEntityBean
{

    void setLastName(String value)
        throws RemoteException;

    String getLastName()
        throws RemoteException;

    void setFirstName(String value)
        throws RemoteException;

    String getFirstName()
        throws RemoteException;

    void setPassword(String value)
        throws RemoteException;

    String getPassword()
        throws RemoteException;

    void setEmail(String value)
        throws RemoteException;

    String getEmail()
        throws RemoteException;

    boolean getAdmin()
        throws RemoteException;

    void setAdmin(boolean value)
        throws RemoteException;

    boolean getLockedOut()
        throws RemoteException;

    void setLockedOut(boolean value)
        throws RemoteException;

    void setLastAccess(Timestamp value)
        throws RemoteException;

    Timestamp getLastAccess()
        throws RemoteException;
}
