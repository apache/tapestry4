package net.sf.tapestry.vlib.ejb;

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

    public void setOwnerPK(Integer value) throws RemoteException;

    public Integer getOwnerPK() throws RemoteException;

    public void setHolderPK(Integer value) throws RemoteException;

    public Integer getHolderPK() throws RemoteException;

    public void setPublisherPK(Integer value) throws RemoteException;

    public boolean getHidden() throws RemoteException;

    public void setHidden(boolean value) throws RemoteException;

    public boolean getLendable() throws RemoteException;

    public void setLendable(boolean value) throws RemoteException;

    public Timestamp getDateAdded() throws RemoteException;

    public void setDateAdded(Timestamp value) throws RemoteException;
}