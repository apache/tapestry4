// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

import org.apache.tapestry.vlib.ejb.Person;

/**
 * The visit object for the {@link VirtualLibraryEngine}. Primarily, this is used to access the
 * home interfaces and EJB instances, and to identify who the logged in user is.
 * 
 * @author Howard Lewis Ship
 */

public class Visit implements Serializable
{
    private static final long serialVersionUID = 8589862098677603655L;

    /**
     * Used to identify the logged in user.
     */

    private Person _user;

    /**
     * Returns the time the user last accessed the database, which may be null if the user hasn't
     * logged in yet.
     */

    public Timestamp getLastAccess()
    {
        return _user == null ? null : _user.getLastAccess();
    }

    /**
     * Gets the logged-in user, or null if the user is not logged in.
     */

    public Person getUser()
    {
        return _user;
    }

    /**
     * Returns the id of the logged in user, or null if the user is not logged in.
     */

    public Integer getUserId()
    {
        return _user == null ? null : _user.getId();
    }

    /**
     * Changes the logged in user. This is only invoked from the
     * {@link org.apache.tapestry.vlib.pages.Login} page.
     */

    public void setUser(Person user)
    {
        _user = user;
    }

    /**
     * Returns true if the user is logged in.
     */

    public boolean isUserLoggedIn()
    {
        return _user != null;
    }

    /**
     * Returns true if the user has not been identified (has not logged in).
     */

    public boolean isUserLoggedOut()
    {
        return _user == null;
    }

    public boolean isLoggedInUser(Integer id)
    {
        if (_user == null)
            return false;

        return _user.getId().equals(id);
    }

    /**
     * Invoked by pages after they perform an operation that changes the backend database in such a
     * way that cached data is no longer valid.
     */

    public void clearCache()
    {
        _user = null;
    }

}