package com.primix.sesstrack;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;

/**
 *  The remote interface for a stateful session EJB that can track the
 *  URIs 'hit' by a user navigating the web application.  This is
 *  primarily to support trace back in the case of failure.
 *
 *  @version $Id$
 *  @author Howard Ship
 */

public interface ISessionTracker
    extends Remote, EJBObject
{

    /**
     *  Track a new hit on the web application.
     *
     */

    public void addHit(ServerHit hit)
          throws RemoteException;

    /**
     *  Get a List of the {@link ServerHit hits}, in order that they were tracked.
     *
     */

    public List getHits() throws RemoteException;

    /**
     *  Get the session id being tracked.
     *
     */

    public String getSessionId() throws RemoteException;
    
    /**
     *  Get the time (in milliseconds since the epoch) that this session tracker
     *  was created.
     *
     */
     
    public long getTimeCreated() throws RemoteException;
    
    /**
     *  Get the time (in milliseconds since the epoch) that this session tracker
     *  was last updated.
     *
     */
     
    public long getTimeUpdated() throws RemoteException;
    
    /**
     *  Returns the remote address as a String representing the client IP address.
     *
     */
     
    public String getRemoteAddress() throws RemoteException;
    
    /**
     *  Returns the remote host (if known) as a String.  If the remote host was not known
     *  at the time this session tracker was created, the remote address is returned
     *  instead.
     *
     */
     
    public String getRemoteHost() throws RemoteException;
}
