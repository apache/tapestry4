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

package org.apache.tapestry.vlib.pages.admin;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.vlib.AdminPage;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.Person;

/**
 *  Allows editting of the users.  Simple flags about the
 *  user can be changed; additionally, the user can have their
 *  password reset (to a random value, which is mailed to them),
 *  or the user can be out-right deleted.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class EditUsers extends AdminPage implements PageRenderListener
{
    public abstract String getPassword();
    
    public abstract void setPassword(String password);

    public abstract UserListEditMap getListEditMap();

    public abstract void setListEditMap(UserListEditMap listEditMap);

    public abstract void setUser(Person person);

    public void synchronizeUser(IRequestCycle cycle)
    {
        UserListEditMap map = getListEditMap();

        Person user = (Person) map.getValue();

        if (user == null)
        {
            setError(getMessage("out-of-date"));
            throw new PageRedirectException(this);
        }

        setUser(user);
    }

    public void pageBeginRender(PageEvent event)
    {
        setupListEditMap();
    }

    private void setupListEditMap()
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Visit visit = (Visit) vengine.getVisit();

        Integer userId = visit.getUserId();
        Person[] users = null;

        int i = 0;
        while (true)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                users = operations.getPersons();

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(getMessage("read-failure"), ex, i++);
            }
        }

        UserListEditMap map = new UserListEditMap();

        for (i = 0; i < users.length; i++)
        {
            Integer id = users[i].getId();

            if (id.equals(userId))
                continue;

            map.add(id, users[i]);
        }

        setListEditMap(map);
    }

    /**
     *  Invoked when the form is submitted.
     *
     **/

    public void updateUsers(IRequestCycle cycle)
    {
        if (isInError())
            return;

        Visit visit = (Visit) getVisit();
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) cycle.getEngine();

        UserListEditMap map = getListEditMap();

        List updatedUsers = map.getValues();

        Person[] updates = (Person[]) updatedUsers.toArray(new Person[updatedUsers.size()]);

        Integer[] resetPasswordUserIds = toArray(map.getResetPasswordKeys());
        Integer[] deletedUserIds = toArray(map.getDeletedKeys());

        String password = getPassword();
        setPassword(null);

        if (Tapestry.isBlank(password) && Tapestry.size(resetPasswordUserIds) != 0)
        {
            setErrorField("inputPassword", getMessage("need-password"));
            return;
        }

        Integer adminId = visit.getUserId();

        int i = 0;
        while (true)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                operations.updatePersons(
                    updates,
                    resetPasswordUserIds,
                    password,
                    deletedUserIds,
                    adminId);
                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(getMessage("update-failure"), ex, i++);
            }
            catch (RemoveException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
        }

        setMessage(getMessage("users-updated"));
    }

    private Integer[] toArray(Collection c)
    {
        int count = Tapestry.size(c);

        if (count == 0)
            return null;

        return (Integer[]) c.toArray(new Integer[count]);
    }
}