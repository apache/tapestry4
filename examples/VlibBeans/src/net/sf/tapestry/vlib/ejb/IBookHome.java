package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 *  Home interface for the {@link IBook} entity bean.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public interface IBookHome extends EJBHome
{
    public IBook create(Map attributes) throws CreateException, RemoteException;

    public IBook findByPrimaryKey(Integer key) throws FinderException, RemoteException;
}