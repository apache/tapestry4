package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 *  A stateless session bean for sending mail to users registerred in the Vlib
 *  database.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface IMailSender extends EJBObject
{
    /**
     *   Sends a single mail message.
     *
     *   <p>TBD:  Error reporting exceptions.
     *
     **/

    public void sendMail(String emailAddress, String subject, String content) throws RemoteException;
}