package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 *  Home interface for the {@link IPublisher} entity bean.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public interface IPublisherHome extends EJBHome
{
    public IPublisher create(String name) throws CreateException, RemoteException;

    public IPublisher findByPrimaryKey(Integer key) throws FinderException, RemoteException;

    /**
     *  Finds Publisher with exact match on name.
     *
     **/

    public IPublisher findByName(String name) throws FinderException, RemoteException;

    /**
     *  Finds all Publishers.
     *
     **/

    public Collection findAll() throws FinderException, RemoteException;
}