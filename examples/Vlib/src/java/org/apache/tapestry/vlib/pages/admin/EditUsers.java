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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectComponent;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.util.DefaultPrimaryKeyConverter;
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
public abstract class EditUsers extends AdminPage implements PageBeginRenderListener,
        PageDetachListener
{
    public abstract String getPassword();

    public abstract void setPassword(String password);

    public abstract void setUser(Person person);

    public abstract Person getUser();

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

    public class UserConverter extends DefaultPrimaryKeyConverter
    {
        private Set _resetPasswordValues;

        public void clear()
        {
            _resetPasswordValues = null;
            super.clear();
        }

        public void setResetPassword(boolean resetPassword)
        {
            _resetPasswordValues = updateValueSetForLastValue(_resetPasswordValues, resetPassword);
        }

        public boolean isResetPassword()
        {
            return checkValueSetForLastValue(_resetPasswordValues);
        }

        public Set getResetPasswordValues()
        {
            return createUnmodifiableSet(_resetPasswordValues);
        }
    }

    private UserConverter _userConverter;

    public UserConverter getUserConverter()
    {
        if (_userConverter == null)
            _userConverter = new UserConverter();

        return _userConverter;
    }

    public void pageBeginRender(PageEvent event)
    {
        readUsers();
    }

    public void pageDetached(PageEvent event)
    {
        _userConverter = null;
    }

    private void readUsers()
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Visit visit = getVisitState();

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
                vengine.rmiFailure(readFailure(), ex, i++);
            }
        }

        UserConverter converter = getUserConverter();

        converter.clear();

        for (i = 0; i < users.length; i++)
        {
            Integer id = users[i].getId();

            // Skip the current user; you aren't allowed to edit yourself

            if (id.equals(userId))
                continue;

            converter.add(id, users[i]);
        }
    }

    /**
     * Invoked when the form is submitted.
     */

    public void updateUsers(IRequestCycle cycle)
    {
        Visit visit = getVisitState();

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) cycle.getEngine();

        UserConverter converter = getUserConverter();

        Person[] updates = (Person[]) converter.getValues().toArray(new Person[0]);
        Integer[] deletedIds = extractIds(converter.getDeletedValues());
        Integer[] resetPasswordIds = extractIds(converter.getResetPasswordValues());

        String password = getPassword();
        setPassword(null);

        if (HiveMind.isBlank(password) && resetPasswordIds.length > 0)
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

                operations.updatePersons(updates, resetPasswordIds, password, deletedIds, adminId);
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

    private Integer[] extractIds(Set valueSet)
    {
        int count = valueSet.size();

        Integer[] result = new Integer[count];

        Iterator i = valueSet.iterator();
        int index = 0;

        while (i.hasNext())
        {
            Person person = (Person) i.next();

            result[index++] = person.getId();
        }

        return result;
    }
}