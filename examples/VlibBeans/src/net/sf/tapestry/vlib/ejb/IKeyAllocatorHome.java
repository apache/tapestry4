package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 *  Home interface to the {@link IKeyAllocator} stateless
 *  session bean.
 *  
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IKeyAllocatorHome extends EJBHome
{
	public IKeyAllocator create() throws CreateException, RemoteException;
}