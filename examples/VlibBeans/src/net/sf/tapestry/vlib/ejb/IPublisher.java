package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;

/**
 *  Remote interface for the Publisher entity bean.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public interface IPublisher extends IEntityBean
{
    public void setName(String value) throws RemoteException;

    public String getName() throws RemoteException;
}