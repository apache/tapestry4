package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 *  Home interface for the {@link IBookQuery} session bean.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IBookQueryHome extends EJBHome
{
    public IBookQuery create() throws CreateException, RemoteException;
}