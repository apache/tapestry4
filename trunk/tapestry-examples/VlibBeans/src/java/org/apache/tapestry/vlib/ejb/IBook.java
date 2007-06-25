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
 * Remote interface for the Book entity bean.
 * 
 * @author Howard Lewis Ship
 */

public interface IBook extends IEntityBean
{

    void setAuthor(String value)
        throws RemoteException;

    String getAuthor()
        throws RemoteException;

    void setTitle(String value)
        throws RemoteException;

    String getTitle()
        throws RemoteException;

    void setDescription(String value)
        throws RemoteException;

    String getDescription()
        throws RemoteException;

    void setISBN(String value)
        throws RemoteException;

    String getISBN()
        throws RemoteException;

    void setOwnerId(Integer value)
        throws RemoteException;

    Integer getOwnerId()
        throws RemoteException;

    void setHolderId(Integer value)
        throws RemoteException;

    Integer getHolderId()
        throws RemoteException;

    void setPublisherId(Integer value)
        throws RemoteException;

    Integer getPublisherId()
        throws RemoteException;

    boolean getHidden()
        throws RemoteException;

    void setHidden(boolean value)
        throws RemoteException;

    boolean getLendable()
        throws RemoteException;

    void setLendable(boolean value)
        throws RemoteException;

    Timestamp getDateAdded()
        throws RemoteException;

    void setDateAdded(Timestamp value)
        throws RemoteException;
}
