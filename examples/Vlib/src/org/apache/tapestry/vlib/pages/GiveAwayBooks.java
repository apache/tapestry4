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

package org.apache.tapestry.vlib.pages;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.FinderException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.vlib.EntitySelectionModel;
import org.apache.tapestry.vlib.IActivate;
import org.apache.tapestry.vlib.Protected;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.IBookQuery;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.Person;

/**
 *  Used to manage giving away of books to other users.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public abstract class GiveAwayBooks extends Protected implements PageRenderListener
{
    public abstract IPropertySelectionModel getBooksModel();

    public abstract void setBooksModel(IPropertySelectionModel booksModel);

    public abstract IPropertySelectionModel getPersonModel();

    public abstract void setPersonModel(IPropertySelectionModel personModel);

    public abstract List getSelectedBooks();

    public abstract Integer getTargetUserId();

    public void formSubmit(IRequestCycle cycle)
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        Integer targetUserId = getTargetUserId();

        Person target = vengine.readPerson(targetUserId);

        List selectedBooks = getSelectedBooks();

        int count = Tapestry.size(selectedBooks);

        if (count == 0)
        {
            setError(format("select-at-least-one-book", target.getNaturalName()));
            return;
        }

        Integer[] bookIds = (Integer[]) selectedBooks.toArray(new Integer[count]);

        int i = 0;
        while (true)
        {
            IOperations operations = vengine.getOperations();

            try
            {
                operations.transferBooks(targetUserId, bookIds);

                break;
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(getMessage("update-failure"), ex, i++);
            }
        }

        MyLibrary myLibrary = (MyLibrary) cycle.getPage("MyLibrary");

        myLibrary.setMessage(
            format("transfered-books", Integer.toString(count), target.getNaturalName()));

        myLibrary.activate(cycle);
    }

    private IPropertySelectionModel buildPersonModel()
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Visit visit = (Visit) vengine.getVisit();
        Integer userPK = visit.getUserId();

        Person[] persons = null;

        int i = 0;
        while (true)
        {
            IOperations operations = vengine.getOperations();

            try
            {
                persons = operations.getPersons();

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(getMessage("read-users-failure"), ex, i++);
            }
        }

        EntitySelectionModel result = new EntitySelectionModel();

        for (i = 0; i < persons.length; i++)
        {

            Person p = persons[i];
            Integer pk = p.getId();

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
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Visit visit = (Visit) vengine.getVisit();
        Integer userPK = visit.getUserId();
        Book[] books = null;

        int i = 0;
        while (true)
        {
            books = null;

            try
            {
                IBookQuery query = vengine.createNewQuery();

                int count = query.ownerQuery(userPK, null);

                if (count > 0)
                    books = query.get(0, count);

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure(getMessage("read-books-failure"), ex, i++);
            }
        }

        EntitySelectionModel result = new EntitySelectionModel();

        if (books != null)
            for (i = 0; i < books.length; i++)
                result.add(books[i].getId(), books[i].getTitle());

        return result;

    }

    public void pageValidate(PageEvent event)
    {
        super.pageValidate(event);

        IPropertySelectionModel model = buildBooksModel();

        if (model.getOptionCount() == 0)
        {
            IRequestCycle cycle = getRequestCycle();
            IActivate page = (IActivate) cycle.getPage("MyLibrary");

            page.activate(cycle);

            throw new PageRedirectException(page);
        }

        setBooksModel(model);
    }

}
