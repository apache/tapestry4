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
 *  Remote interface for the Book entity bean.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

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

    public void setOwnerId(Integer value) throws RemoteException;

    public Integer getOwnerId() throws RemoteException;

    public void setHolderId(Integer value) throws RemoteException;

    public Integer getHolderId() throws RemoteException;

    public void setPublisherId(Integer value) throws RemoteException;
    
    public Integer getPublisherId() throws RemoteException;

    public boolean getHidden() throws RemoteException;

    public void setHidden(boolean value) throws RemoteException;

    public boolean getLendable() throws RemoteException;

    public void setLendable(boolean value) throws RemoteException;

    public Timestamp getDateAdded() throws RemoteException;

    public void setDateAdded(Timestamp value) throws RemoteException;
}