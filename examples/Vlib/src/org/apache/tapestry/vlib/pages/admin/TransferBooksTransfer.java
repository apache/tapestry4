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
import java.util.List;

import javax.ejb.FinderException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.vlib.AdminPage;
import org.apache.tapestry.vlib.EntitySelectionModel;
import org.apache.tapestry.vlib.IErrorProperty;
import org.apache.tapestry.vlib.IMessageProperty;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.IBookQuery;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.Person;

/**
 *  Second page in Transfer Books wizard; allows the books owned by the
 *  from user to be selected and transfered to the target user.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public abstract class TransferBooksTransfer extends AdminPage implements PageRenderListener
{
    public abstract Person getFromUser();

    public abstract void setFromUser(Person fromUser);

    public abstract Integer getFromUserId();

    public abstract void setFromUserId(Integer fromUserId);

    public abstract Integer getToUserId();

    public abstract void setToUserId(Integer toUserId);

    public abstract Person getToUser();

    public abstract void setToUser(Person toUser);

    public abstract List getSelectedBooks();

    public abstract IPropertySelectionModel getUserBookModel();

    public abstract void setUserBookModel(IPropertySelectionModel userBookModel);

    public void activate(IRequestCycle cycle, Integer fromUserId, Integer toUserId)
    {
        Person fromUser = readPerson(fromUserId);

        IPropertySelectionModel model = buildUserBookModel(fromUser);

        if (model.getOptionCount() == 0)
        {
            IErrorProperty page = (IErrorProperty) cycle.getPage();
            page.setError(format("user-has-no-books", fromUser.getNaturalName()));
            return;
        }

        setFromUserId(fromUserId);
        setFromUser(fromUser);

        setToUserId(toUserId);

        setUserBookModel(model);

        cycle.activate(this);
    }

    /**
     *  Only properties toUserPK and fromUserPK are persistent.  We need to refresh
     *  the fromUser, toUser and userBookModel properties at the start
     *  of each request cycle.
     * 
     **/

    public void pageBeginRender(PageEvent event)
    {
        Person fromUser = getFromUser();

        if (fromUser == null)
        {
            fromUser = readPerson(getFromUserId());
            setFromUser(fromUser);
        }

        if (getUserBookModel() == null)
            setUserBookModel(buildUserBookModel(fromUser));

        Person toUser = getToUser();
        if (toUser == null)
        {
            toUser = readPerson(getToUserId());
            setToUser(toUser);
        }
    }

    public void formSubmit(IRequestCycle cycle)
    {
        List selectedBooks = getSelectedBooks();

        int count = Tapestry.size(selectedBooks);

        if (count == 0)
        {
            setError(getMessage("no-books-selected"));
            return;
        }

        Integer[] keys = (Integer[]) selectedBooks.toArray(new Integer[count]);
        Person toUser = getToUser();

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        int i = 0;
        while (true)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                operations.transferBooks(toUser.getId(), keys);

                break;
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Unable to transfer ownership of books.", ex, i++);
            }
        }

        Person fromUser = getFromUser();
        IMessageProperty selectPage = (TransferBooksSelect) cycle.getPage("TransferBooksSelect");
        selectPage.setMessage(
            format(
                "transfered-books",
                Integer.toString(count),
                fromUser.getNaturalName(),
                toUser.getNaturalName()));
        cycle.activate(selectPage);
    }

    private IPropertySelectionModel buildUserBookModel(Person user)
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Book[] books = null;

        int i = 0;
        while (true)
        {
            books = null;

            try
            {
                IBookQuery query = vengine.createNewQuery();

                int count = query.ownerQuery(user.getId(), null);

                if (count > 0)
                    books = query.get(0, count);

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(
                    "Unable to retrieve books owned by " + user.getNaturalName() + ".",
                    ex,
                    i++);
            }
        }

        EntitySelectionModel model = new EntitySelectionModel();

        if (books != null)
            for (i = 0; i < books.length; i++)
                model.add(books[i].getId(), books[i].getTitle());

        return model;
    }

    private Person readPerson(Integer personId)
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        return vengine.readPerson(personId);
    }
}
