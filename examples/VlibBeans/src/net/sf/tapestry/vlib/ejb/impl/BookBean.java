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

package net.sf.tapestry.vlib.ejb.impl;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Map;

import javax.ejb.CreateException;

/**
 *  Implementation of the Book entity.
 *
 *  <p>We're using container managed persistance.
 *
 *  @see net.sf.tapestry.vlib.ejb.IBook
 *  @see net.sf.tapestry.vlib.ejb.IBookHome
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
            "holderPK",
            "ownerPK",
            "publisherPK",
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

    public abstract Integer getHolderPK();

    public abstract void setHolderPK(Integer value);
    
    public abstract Integer getOwnerPK() throws RemoteException;

    public abstract void setOwnerPK(Integer value);

    public abstract void setPublisherPK(Integer value);

    public abstract Integer getPublisherPK();

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