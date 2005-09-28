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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectComponent;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.vlib.ActivatePage;
import org.apache.tapestry.vlib.IMessageProperty;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.components.Browser;
import org.apache.tapestry.vlib.ejb.IBookQuery;
import org.apache.tapestry.vlib.ejb.SortColumn;
import org.apache.tapestry.vlib.ejb.SortOrdering;

/**
 * Shows a list of the user's books, allowing books to be editted or even deleted.
 * <p>
 * Note that, unlike elsewhere, book titles do not link to the {@link ViewBook} page. It seems to me
 * there would be a conflict between that behavior and the edit behavior; making the book titles not
 * be links removes the ambiguity over what happens when the book title is clicked (view vs. edit).
 * 
 * @author Howard Lewis Ship
 */

public abstract class MyLibrary extends ActivatePage implements IMessageProperty
{
    @Persist
    public abstract IBookQuery getOwnedQuery();

    public abstract void setOwnedQuery(IBookQuery value);

    @Persist
    public abstract SortColumn getSortColumn();

    public abstract void setSortColumn(SortColumn column);

    @Persist
    public abstract boolean isDescending();

    @InjectComponent("browser")
    public abstract Browser getBrowser();

    @InjectPage("EditBook")
    public abstract EditBook getEditBook();

    @InjectPage("ConfirmBookDelete")
    public abstract ConfirmBookDelete getConfirmBookDelete();

    public void finishLoad()
    {
        setSortColumn(SortColumn.TITLE);
    }

    public void activate()
    {
        runQuery();

        getRequestCycle().activate(this);
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

                if (count != getBrowser().getResultCount())
                    getBrowser().initializeForResultCount(count);

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
     * Listener invoked to allow a user to edit a book.
     */

    public void editBook(Integer bookId)
    {
        getEditBook().beginEdit(bookId);
    }

    /**
     * Listener invoked to allow a user to delete a book.
     */

    public void deleteBook(Integer bookId)
    {
        getConfirmBookDelete().selectBook(bookId);
    }

}