//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
	private static final long serialVersionUID = 8589862098677603655L;
	
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

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) cycle.getEngine();

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