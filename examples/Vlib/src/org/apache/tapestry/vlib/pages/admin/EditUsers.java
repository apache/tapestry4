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

package org.apache.tapestry.vlib.pages.admin;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RequestCycleException;
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

public class EditUsers extends AdminPage
{
    private Person[] users;

    /**
     *  Map of users, keyed on Person primaryKey.
     *
     **/

    private Map userMap;

    /**
     *  The PK of the current user being editted.
     *
     **/

    private Integer userKey;

    /**
     *  The Person corresponding to userKey.
     *
     **/

    private Person user;

    /**
     *  List of Person PKs of users to have passwords reset.
     *
     **/

    private List resetPassword;

    /**
     *  List of Person PKs, of users to be removed.
     *
     **/

    private List deleteUser;

    public void detach()
    {
        users = null;
        userMap = null;
        userKey = null;
        user = null;
        resetPassword = null;
        deleteUser = null;

        super.detach();
    }

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        super.beginResponse(writer, cycle);

        readUsers();
    }

    private void readUsers()
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        for (int i = 0; i < 2; i++)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                users = operations.getPersons();

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Unable to retrieve list of users.", ex, i > 0);
            }
        }

        userMap = new HashMap();

        for (int i = 0; i < users.length; i++)
            userMap.put(users[i].getPrimaryKey(), users[i]);
    }

    /**
     *  Returns the primary keys of all the Persons, in a sort order (by last name, then first name).
     *
     **/

    public Integer[] getUserKeys()
    {
        Integer[] result = new Integer[users.length];

        for (int i = 0; i < users.length; i++)
            result[i] = users[i].getPrimaryKey();

        return result;
    }

    /**
     *  Sets the user property from the primary key (value parameter).
     *
     **/

    public void setUserKey(Integer value)
    {
        userKey = value;

        if (users == null)
            readUsers();

        user = (Person) userMap.get(userKey);

        // Latent bug:  what if the user was deleted between the time the form was rendered and 
        // now?  user will be null, which will trip up some of the components.
    }

    public Person getUser()
    {
        return user;
    }

    public boolean getResetPassword()
    {
        return false;
    }

    public void setResetPassword(boolean value)
    {
        if (value)
        {
            if (resetPassword == null)
                resetPassword = new ArrayList();

            resetPassword.add(userKey);
        }
    }

    public boolean getDeleteUser()
    {
        return false;
    }

    public void setDeleteUser(boolean value)
    {
        if (value)
        {
            if (deleteUser == null)
                deleteUser = new ArrayList();

            deleteUser.add(userKey);

            // Remove the user from the userMap ... this will prevent it from
            // being included in the update list.

            userMap.remove(userKey);
        }
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
        VirtualLibraryEngine vengine = visit.getEngine();

        // Collection of non-deleted persons.

        Collection updatedPersons = userMap.values();

        Person[] updated = (Person[]) updatedPersons.toArray(new Person[updatedPersons.size()]);

        Integer[] resetPasswordArray = toArray(resetPassword);
        Integer[] deleted = toArray(deleteUser);

        Integer adminPK = visit.getUserPK();

        for (int i = 0; i < 2; i++)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                operations.updatePersons(updated, resetPasswordArray, deleted, adminPK);

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Unable to update users.", ex, i > 0);
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

        setMessage("Users updated.");

        users = null;
        userMap = null;

    }

    private Integer[] toArray(List list)
    {
        if (list == null)
            return null;

        if (list.size() == 0)
            return null;

        return (Integer[]) list.toArray(new Integer[list.size()]);
    }
}