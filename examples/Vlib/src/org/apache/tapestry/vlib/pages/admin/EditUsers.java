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
    public abstract UserListEditMap getListEditMap();

    public abstract void setListEditMap(UserListEditMap listEditMap);

    public abstract void setUser(Person person);

    public void synchronizeUser(IRequestCycle cycle)
    {
        UserListEditMap map = getListEditMap();

        Person user = (Person) map.getValue();

        if (user == null)
        {
            setError("The data submitted in the form is out of date.  Please try again.");
            throw new PageRedirectException(this);
        }

        setUser(user);
    }

    public void pageBeginRender(PageEvent event)
    {
        readUsers();
    }

    public void pageEndRender(PageEvent event)
    {
    }

    private void readUsers()
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Person[] users = null;

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

        UserListEditMap map = new UserListEditMap();

        for (int i = 0; i < users.length; i++)
        {
            map.add(users[i].getPrimaryKey(), users[i]);
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
        VirtualLibraryEngine vengine = visit.getEngine();

        UserListEditMap map = getListEditMap();

        List updatedUsers = map.getValues();

        Person[] updated = (Person[]) updatedUsers.toArray(new Person[updatedUsers.size()]);

        Integer[] resetPasswordArray = toArray(map.getResetPasswordKeys());
        Integer[] deleted = toArray(map.getDeletedKeys());

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

    }

    private Integer[] toArray(Collection c)
    {
        int count = Tapestry.size(c);

        if (count == 0)
            return null;

        return (Integer[]) c.toArray(new Integer[count]);
    }
}