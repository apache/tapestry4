package com.primix.sesstrack;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */
 
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
