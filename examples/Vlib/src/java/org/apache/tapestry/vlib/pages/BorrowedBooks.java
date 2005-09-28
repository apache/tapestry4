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

import javax.ejb.FinderException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectComponent;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.annotations.Persist;
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
 * Shows a list of books the user has borrowed.
 * 
 * @author Howard Lewis Ship
 */

public abstract class BorrowedBooks extends ActivatePage implements IMessageProperty
{
    @InjectComponent("browser")
    public abstract Browser getBrowser();

    @Persist
    public abstract IBookQuery getBorrowedQuery();

    public abstract void setBorrowedQuery(IBookQuery value);

    @Persist
    public abstract SortColumn getSortColumn();

    public abstract void setSortColumn(SortColumn column);

    @Persist
    public abstract boolean isDescending();

    @Message
    public abstract String returnedBook(String title);

    @Message
    public abstract String unableToReturnBook(String message);

    @InjectState("visit")
    public abstract Visit getVisitState();

    public void finishLoad()
    {
        setSortColumn(SortColumn.TITLE);
    }

    public void activate()
    {
        runQuery();

        getRequestCycle().activate(this);
    }

    /**
     * Invoked as listener method after the sortColumn or descending properties are changed.
     * 
     * @param cycle
     * @since 3.0
     */

    public void requery(IRequestCycle cycle)
    {
        runQuery();
    }

    private void runQuery()
    {
        Visit visit = getVisitState();
        Integer userPK = visit.getUserId();

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        SortOrdering ordering = new SortOrdering(getSortColumn(), isDescending());

        int i = 0;
        while (true)
        {
            try
            {
                IBookQuery query = getBorrowedQuery();

                if (query == null)
                {
                    query = vengine.createNewQuery();
                    setBorrowedQuery(query);
                }

                int count = query.borrowerQuery(userPK, ordering);
                Browser browser = getBrowser();

                if (count != browser.getResultCount())
                    browser.initializeForResultCount(count);

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception finding borrowed books.", ex, i++);

                setBorrowedQuery(null);
            }
        }
    }

    /**
     * Listener used to return a book.
     */

    public void returnBook(Integer bookPK)
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        IOperations operations = vengine.getOperations();

        try
        {
            Book book = operations.returnBook(bookPK);

            setMessage(returnedBook(book.getTitle()));

            runQuery();
        }
        catch (FinderException ex)
        {
            setError(unableToReturnBook(ex.getMessage()));
            return;
        }
        catch (RemoteException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }

}