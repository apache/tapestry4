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