/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.vlib;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.vlib.ejb.Person;

/**
 *  The visit object for the {@link VirtualLibraryEngine}.
 *
 *  Primarily, this is used to access the home interfaces and EJB instances, and
 *  to identify who the logged in user is.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Visit implements Serializable
{
    /**
     *  Used to identify the logged in user.
     *
     **/

    private transient Person _user;
    private Integer _userId;

    /**
     *  Set when the user first logs in.  This is the time of their previous
     *  login (logging in returns the old value then updates the value
     *  for subsequent logins).
     *
     **/

    private Timestamp _lastAccess;

    /**
     *  Returns the time the user last accessed the database, which may
     *  be null if the user hasn't logged in yet.
     *
     **/

    public Timestamp getLastAccess()
    {
        return _lastAccess;
    }

    /**
     *  Gets the logged-in user, or null if the user is not logged in.
     *
     **/

    public Person getUser(IRequestCycle cycle)
    {
        if (_user != null)
            return _user;

        if (_userId == null)
            return null;

		VirtualLibraryEngine vengine = (VirtualLibraryEngine)cycle.getEngine();
		
        _user = vengine.readPerson(_userId);

        return _user;
    }

    /**
     *  Returns the id of the logged in user, or null if the
     *  user is not logged in.
     *
     **/

    public Integer getUserId()
    {
        return _userId;
    }

    /**
     *  Changes the logged in user. This is only invoked from the 
     *  {@link org.apache.tapestry.vlib.pages.Login} page.
     *
     **/

    public void setUser(Person value)
    {
        _lastAccess = null;
        _user = value;
        _userId = null;

        if (_user == null)
            return;

        _userId = _user.getId();

        _lastAccess = _user.getLastAccess();
    }

    /**
     *  Returns true if the user is logged in.
     *
     **/

    public boolean isUserLoggedIn()
    {
        return _userId != null;
    }

    /**
     *  Returns true if the user has not been identified (has not
     *  logged in).
     *
     **/

    public boolean isUserLoggedOut()
    {
        return _userId == null;
    }

    public boolean isLoggedInUser(Integer id)
    {
        if (_userId == null)
            return false;

        return _userId.equals(id);
    }

    /**
     *  Invoked by pages after they perform an operation that changes the backend
     *  database in such a way that cached data is no longer valid. 
     *
     **/

    public void clearCache()
    {
        _user = null;
    }

}