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

package org.apache.tapestry.vlib.pages;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.FinderException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.vlib.EntitySelectionModel;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.VlibPage;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.IBookQuery;
import org.apache.tapestry.vlib.ejb.Person;
import org.apache.tapestry.vlib.services.RemoteCallback;

/**
 * Used to manage giving away of books to other users.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

public abstract class GiveAwayBooks extends VlibPage implements PageBeginRenderListener
{
    public abstract IPropertySelectionModel getBooksModel();

    public abstract void setBooksModel(IPropertySelectionModel booksModel);

    public abstract IPropertySelectionModel getPersonModel();

    public abstract void setPersonModel(IPropertySelectionModel personModel);

    public abstract List getSelectedBooks();

    public abstract Integer getTargetUserId();

    @Message
    public abstract String updateFailure();

    @Message
    public abstract String selectAtLeastOneBook(String targetName);

    @Message
    public abstract String transferedBooks(int count, String targetName);

    @Message
    public abstract String readBooksFailure();

    @InjectPage("MyLibrary")
    public abstract MyLibrary getMyLibrary();

    public void formSubmit()
    {
        final Integer targetUserId = getTargetUserId();

        Person target = getRemoteTemplate().getPerson(targetUserId);

        List selectedBooks = getSelectedBooks();

        int count = Tapestry.size(selectedBooks);

        if (count == 0)
        {
            setError(selectAtLeastOneBook(target.getNaturalName()));
            return;
        }

        final Integer[] bookIds = (Integer[]) selectedBooks.toArray(new Integer[count]);

        RemoteCallback callback = new RemoteCallback()
        {
            public Object doRemote() throws RemoteException
            {
                try
                {
                    getOperations().transferBooks(targetUserId, bookIds);
                    return null;
                }
                catch (FinderException ex)
                {
                    throw new ApplicationRuntimeException(ex);
                }
            }
        };

        getRemoteTemplate().execute(callback, updateFailure());

        MyLibrary myLibrary = getMyLibrary();

        myLibrary.setMessage(transferedBooks(count, target.getNaturalName()));

        myLibrary.activate();
    }

    private IPropertySelectionModel buildPersonModel()
    {
        Visit visit = getVisitState();
        Integer userPK = visit.getUserId();

        Person[] persons = getRemoteTemplate().getPersons();

        EntitySelectionModel result = new EntitySelectionModel();

        for (int i = 0; i < persons.length; i++)
        {

            Person p = persons[i];
            Integer pk = p.getId();

            // Skip the current user

            if (pk.equals(userPK))
                continue;

            result.add(pk, p.getNaturalName());
        }

        return result;
    }

    public void pageBeginRender(PageEvent event)
    {
        IPropertySelectionModel model = getBooksModel();

        if (model == null)
        {
            model = buildBooksModel();
            setBooksModel(model);
        }

        model = getPersonModel();
        if (model == null)
        {
            model = buildPersonModel();
            setPersonModel(model);
        }
    }

    private IPropertySelectionModel buildBooksModel()
    {
        Visit visit = getVisitState();
        final Integer userPK = visit.getUserId();

        RemoteCallback<Book[]> callback = new RemoteCallback()
        {
            public Book[] doRemote() throws RemoteException
            {
                IBookQuery query = getBookQuerySource().newQuery();

                int count = query.ownerQuery(userPK, null);

                return count == 0 ? null : query.get(0, count);
            }
        };

        Book[] books = getRemoteTemplate().execute(callback, readBooksFailure());

        EntitySelectionModel result = new EntitySelectionModel();

        if (books != null)
            for (int i = 0; i < books.length; i++)
                result.add(books[i].getId(), books[i].getTitle());

        return result;

    }

    public void pageValidate(PageEvent event)
    {
        super.pageValidate(event);

        IPropertySelectionModel model = buildBooksModel();

        if (model.getOptionCount() == 0)
        {
            MyLibrary page = getMyLibrary();

            page.activate();

            throw new PageRedirectException(page);
        }

        setBooksModel(model);
    }

}
