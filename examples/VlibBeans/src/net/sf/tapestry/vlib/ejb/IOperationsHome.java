package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 *  Home interface to the {@link IOperations} stateless
 *  session bean.
 *  
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IOperationsHome extends EJBHome
{
    public IOperations create() throws CreateException, RemoteException;
}