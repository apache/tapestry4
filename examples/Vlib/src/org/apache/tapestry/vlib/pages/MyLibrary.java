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

import javax.ejb.FinderException;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.vlib.ActivatePage;
import org.apache.tapestry.vlib.IMessageProperty;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.components.Browser;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.IBookQuery;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.SortColumn;
import org.apache.tapestry.vlib.ejb.SortOrdering;

/**
 *  Shows a list of the user's books, allowing books to be editted or
 *  even deleted.
 *
 *  <p>Note that, unlike elsewhere, book titles do not link to the
 *  {@link ViewBook} page.  It seems to me there would be a conflict between
 *  that behavior and the edit behavior; making the book titles not be links
 *  removes the ambiguity over what happens when the book title is clicked
 *  (view vs. edit).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class MyLibrary
    extends ActivatePage
    implements IMessageProperty
{
    public abstract void setOwnedQuery(IBookQuery value);

    public abstract IBookQuery getOwnedQuery();

    public abstract SortColumn getSortColumn();

    public abstract boolean isDescending();

    private Browser _browser;

    public void finishLoad()
    {
        _browser = (Browser) getComponent("browser");
    }

    public void activate(IRequestCycle cycle)
    {
        runQuery();

        cycle.activate(this);
    }

    public void requery(IRequestCycle cycle)
    {
        runQuery();
    }

    private void runQuery()
    {
        Visit visit = (Visit) getVisit();
        Integer userId = visit.getUserId();

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        SortOrdering ordering = new SortOrdering(getSortColumn(), isDescending());

        int i = 0;
        while (true)
        {
            try
            {
                IBookQuery query = getOwnedQuery();

                if (query == null)
                {
                    query = vengine.createNewQuery();
                    setOwnedQuery(query);
                }

                int count = query.ownerQuery(userId, ordering);

                if (count != _browser.getResultCount())
                    _browser.initializeForResultCount(count);

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception accessing owned books.", ex, i++);

                setOwnedQuery(null);
            }
        }
    }

    /**
     *  Listener invoked to allow a user to edit a book.
     *
     **/

    public void editBook(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();
        Integer bookId = (Integer) parameters[0];
        EditBook page = (EditBook) cycle.getPage("EditBook");

        page.beginEdit(cycle, bookId);
    }

    /**
     *  Listener invoked to allow a user to delete a book.
     *
     **/

    public void deleteBook(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();
        Integer bookId = (Integer) parameters[0];

        ConfirmBookDelete page = (ConfirmBookDelete) cycle.getPage("ConfirmBookDelete");
        page.selectBook(bookId, cycle);
    }

    private void returnBook(Integer bookId)
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        int i = 0;
        while (true)
        {
            try
            {
                IOperations operations = vengine.getOperations();
                Book book = operations.returnBook(bookId);

                setMessage(format("returned-book", book.getTitle()));

                break;
            }
            catch (FinderException ex)
            {
                setError(format("unable-to-return-book", ex.getMessage()));
                return;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception returning book.", ex, i++);
            }
        }

        runQuery();
    }

}