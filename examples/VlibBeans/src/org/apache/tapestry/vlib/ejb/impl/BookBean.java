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

package org.apache.tapestry.vlib.ejb.impl;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Map;

import javax.ejb.CreateException;

/**
 *  Implementation of the Book entity.
 *
 *  <p>We're using container managed persistance.
 *
 *  @see org.apache.tapestry.vlib.ejb.IBook
 *  @see org.apache.tapestry.vlib.ejb.IBookHome
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public abstract class BookBean extends AbstractEntityBean
{
    protected String[] getAttributePropertyNames()
    {
        return new String[] {
            "title",
            "description",
            "ISBN",
            "holderId",
            "ownerId",
            "publisherId",
            "author",
            "hidden",
            "lendable",
            "dateAdded" };
    }

    public abstract void setBookId(Integer value);
    
    public abstract Integer getBookId();

    public abstract String getAuthor();

    public abstract void setAuthor(String value);

    public abstract String getDescription();

    public abstract void setDescription(String value);

    public abstract String getISBN();

    public abstract void setISBN(String value);
    
    public abstract String getTitle();

    public abstract void setTitle(String value);

    public abstract Integer getHolderId();

    public abstract void setHolderId(Integer value);
    
    public abstract Integer getOwnerId() throws RemoteException;

    public abstract void setOwnerId(Integer value);

    public abstract void setPublisherId(Integer value);

    public abstract Integer getPublisherId();

    public abstract boolean getHidden();

    public abstract void setHidden(boolean value);

    public abstract boolean getLendable();

    public abstract void setLendable(boolean value);

    public abstract Timestamp getDateAdded();
    
    public abstract void setDateAdded(Timestamp value);

    // Create methods

    public Integer ejbCreate(Map attributes) throws CreateException, RemoteException
    {
        setLendable(true);
 
        updateEntityAttributes(attributes);

        setBookId(allocateKey());
        
        return null;
    }

    public void ejbPostCreate(Map attributes)
    {
        // No post create work needed but the method must be implemented
    }

}