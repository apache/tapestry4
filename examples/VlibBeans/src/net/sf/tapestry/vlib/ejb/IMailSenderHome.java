package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 *  Home interface for {@link IMailSender}, a stateless session bean.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface IMailSenderHome extends EJBHome
{
	public IMailSender create() throws CreateException, RemoteException;
}