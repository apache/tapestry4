package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 *  Home interface for the {@link IPerson} entity bean.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IPersonHome extends EJBHome
{
    public IPerson create(Map attributes) throws CreateException, RemoteException;

    public IPerson findByPrimaryKey(Integer key) throws FinderException, RemoteException;

    /**
     *  Finds by exact match on email (which is how users are identified for
     *  login purposes).  Note:  need to figure out how to do a caseless
     *  search instead.
     *
     **/

    public IPerson findByEmail(String email) throws FinderException, RemoteException;
}