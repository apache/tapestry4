// Copyright 2004, 2005 The Apache Software Foundation
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectComponent;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.components.IPrimaryKeyConverter;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.vlib.AdminPage;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.Person;

/**
 * Allows editting of the users. Simple flags about the user can be changed; additionally, the user
 * can have their password reset (to a random value, which is mailed to them), or the user can be
 * out-right deleted.
 * 
 * @author Howard Lewis Ship
 */
public abstract class EditUsers extends AdminPage implements PageBeginRenderListener
{
    public abstract String getPassword();

    public abstract void setPassword(String password);

    public abstract void setUser(Person person);

    public abstract Person getUser();

    public abstract void setUserList(List<Person> users);

    public abstract void setUserMap(Map<Integer, Person> map);

    public abstract Map<Integer, Person> getUserMap();

    public abstract void setResetPasswordKeys(Set<Integer> set);

    public abstract Set<Integer> getResetPasswordKeys();

    public abstract void setDeleteKeys(Set<Integer> set);

    public abstract Set<Integer> getDeleteKeys();

    @Message
    public abstract String outOfDate();

    @Message
    public abstract String readFailure();

    @Message
    public abstract String needPassword();

    @Message
    public abstract String updateFailure();

    @Message
    public abstract String usersUpdated();

    @InjectState("visit")
    public abstract Visit getVisitState();

    @InjectComponent("password")
    public abstract IFormComponent getPasswordField();

    public class UserConverter implements IPrimaryKeyConverter
    {

        public Object getPrimaryKey(Object value)
        {
            Person user = (Person) value;

            return user.getId();
        }

        public Object getValue(Object primaryKey)
        {
            Integer id = (Integer) primaryKey;

            return getUserMap().get(id);

            // TODO: Handle null
        }

    }

    public IPrimaryKeyConverter getUserConverter()
    {
        return new UserConverter();
    }

    // public void synchronizeUser(IRequestCycle cycle)
    // {
    // UserListEditMap map = getListEditMap();
    //
    // Person user = (Person) map.getValue();
    //
    // if (user == null)
    // {
    // getValidationDelegate().record(null, outOfDate());
    // throw new PageRedirectException(this);
    // }
    //
    // setUser(user);
    // }

    public boolean isDeleted()
    {
        return false;
    }

    public void setDeleted(boolean deleted)
    {
        if (deleted)
            getDeleteKeys().add(getUser().getId());
    }

    public boolean isResetPassword()
    {
        return false;
    }

    public void setResetPassword(boolean resetPassword)
    {
        if (resetPassword)
            getResetPasswordKeys().add(getUser().getId());
    }

    public void pageBeginRender(PageEvent event)
    {
        readUsers();
        setDeleteKeys(new HashSet<Integer>());
        setResetPasswordKeys(new HashSet<Integer>());
    }

    private void readUsers()
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Visit visit = getVisitState();

        Integer userId = visit.getUserId();
        Person[] users = null;

        List<Person> userList = new ArrayList<Person>();

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
                vengine.rmiFailure(readFailure(), ex, i++);
            }
        }

        Map<Integer, Person> map = new HashMap<Integer, Person>();

        for (i = 0; i < users.length; i++)
        {
            Integer id = users[i].getId();

            // Skip the current user; they can't edit themself

            if (id.equals(userId))
                continue;

            map.put(id, users[i]);
            userList.add(users[i]);
        }

        setUserMap(map);
        setUserList(userList);
    }

    /**
     * Invoked when the form is submitted.
     */

    public void updateUsers(IRequestCycle cycle)
    {
        Visit visit = getVisitState();

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) cycle.getEngine();

        Map<Integer, Person> userMap = getUserMap();

        Set<Integer> deletedIds = getDeleteKeys();
        Set<Integer> resetPasswordIds = getResetPasswordKeys();

        // Remove any users who have been deleted

        userMap.keySet().removeAll(deletedIds);

        Person[] updates = userMap.values().toArray(new Person[0]);

        String password = getPassword();
        setPassword(null);

        if (HiveMind.isBlank(password) && !resetPasswordIds.isEmpty())
        {
            getValidationDelegate().record(getPasswordField(), needPassword());
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
                        resetPasswordIds.toArray(new Integer[0]),
                        password,
                        deletedIds.toArray(new Integer[0]),
                        adminId);
                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(updateFailure(), ex, i++);
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

        setMessage(usersUpdated());
    }
}