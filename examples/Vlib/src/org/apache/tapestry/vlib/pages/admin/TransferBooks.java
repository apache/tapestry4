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
import java.util.List;

import javax.ejb.FinderException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.vlib.AdminPage;
import org.apache.tapestry.vlib.EntitySelectionModel;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.IBookQuery;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.Person;

/**
 *  PageLink which allows books to be transferred, en mass, from one user to another.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class TransferBooks extends AdminPage implements PageRenderListener
{
    public abstract Person getFromUser();

    public abstract void setFromUser(Person fromUser);

    public abstract Person getToUser();

    public abstract void setToUser(Person toUser);

    public abstract List getSelectedBooks();

    public abstract IPropertySelectionModel getUserBookModel();

    public abstract void setUserBookModel(IPropertySelectionModel userBookModel);

    public void pageBeginRender(PageEvent event)
    {
        if (isSelectionsOk() && getUserBookModel() == null)
            setUserBookModel(buildUserBookModel());
    }

    public void pageEndRender(PageEvent event)
    {
    }

    public abstract boolean isSelectionsOk();

    public abstract void setSelectionsOk(boolean value);

    public Integer getFromUserPK()
    {
        return getPK(getFromUser());
    }

    public void setFromUserPK(Integer pk)
    {
        setFromUser(readPerson(pk));
    }

    public Integer getToUserPK()
    {
        return getPK(getToUser());
    }

    public void setToUserPK(Integer pk)
    {
        setToUser(readPerson(pk));
    }

    private Integer getPK(Person person)
    {
        if (person == null)
            return null;

        return person.getPrimaryKey();
    }

    public void processSelectForm(IRequestCycle cycle)
    {
        setSelectionsOk(false);

        Person fromUser = getFromUser();
        Person toUser = getToUser();

        if (fromUser.getPrimaryKey().equals(toUser.getPrimaryKey()))
        {
            setError("Please select two different people.");
            return;
        }

        IPropertySelectionModel model = buildUserBookModel();

        if (model.getOptionCount() == 0)
        {
            setError("Selected user owns no books.");
            return;
        }

        // Selections ok, enable bottom half of page.

        setSelectionsOk(true);
        setUserBookModel(model);
    }

    public void processXferForm(IRequestCycle cycle)
    {
        setSelectionsOk(false);

        List selectedBooks = getSelectedBooks();

        int count = selectedBooks.size();

        if (count == 0)
        {
            setError("No books selected.");
            return;
        }

        Integer[] keys = (Integer[]) selectedBooks.toArray(new Integer[count]);
        Person toUser = getToUser();

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        for (int i = 0; i < 2; i++)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                operations.transferBooks(toUser.getPrimaryKey(), keys);

                break;
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Unable to transfer ownership of books.", ex, i > 0);
            }
        }

        setMessage("Transfered " + count + " books to " + toUser.getNaturalName() + ".");
    }

    private IPropertySelectionModel buildUserBookModel()
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Book[] books = null;
        Person fromUser = getFromUser();

        for (int i = 0; i < 2; i++)
        {
            books = null;

            try
            {
                IBookQuery query = vengine.createNewQuery();

                int count = query.ownerQuery(fromUser.getPrimaryKey());

                if (count > 0)
                    books = query.get(0, count);

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(
                    "Unable to retrieve books owned by " + fromUser.getNaturalName() + ".",
                    ex,
                    i > 0);
            }
        }

        EntitySelectionModel model = new EntitySelectionModel();

        if (books != null)
            for (int i = 0; i < books.length; i++)
                model.add(books[i].getPrimaryKey(), books[i].getTitle());

        return model;
    }

    private Person readPerson(Integer primaryKey)
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Person result = null;

        for (int i = 0; i < 2; i++)
        {
            try
            {
                IOperations ops = vengine.getOperations();

                result = ops.getPerson(primaryKey);

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Unable to retrieve person #" + primaryKey + ".", ex, i > 0);
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }

        }

        return result;
    }
}