package com.primix.sesstrack;

import javax.ejb.*;
import java.rmi.*;

/**
 *  The home interface for the {@link ISessionTracker} EJB.
 *
 *  @version $Id$
 *  @author Howard Ship
 */

public interface ISessionTrackerHome extends EJBHome
{
	/**
     *  The primary creation method.
     *
     *  @param sessionId  The session id as provided by the servlet container.
     *  This will be the primary key for the bean.
     *  
     *  @param remoteAddress The remote IP address of the client.
     *
     *  @param remoteHost The remote hostname of the client (if none), or null.
     *
     */
     
    public ISessionTracker create(String sessionId, String remoteAddress, String remoteHost)
        throws RemoteException, CreateException;
    
    /**
     *  Used to locate the session tracker bean for a particular session.
     *
     */
     
    public ISessionTracker findByPrimaryKey(String sessionId)
    	throws RemoteException, FinderException;  
}


