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

import org.apache.tapestry.ApplicationRuntimeException;
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
 *  Shows a list of books the user has borrowed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class BorrowedBooks extends ActivatePage implements IMessageProperty
{
    private Browser _browser;

    public abstract void setBorrowedQuery(IBookQuery value);

    public abstract IBookQuery getBorrowedQuery();

    public abstract SortColumn getSortColumn();

    public abstract boolean isDescending();

    public void finishLoad()
    {
        _browser = (Browser) getComponent("browser");
    }

    public void activate(IRequestCycle cycle)
    {
        runQuery();

        cycle.activate(this);
    }

    /**
     *  Invoked as listener method after the sortColumn or
     *  descending properties are changed.
     * 
     *  @param cycle
     *  @since 3.0
     * 
     *
     **/

    public void requery(IRequestCycle cycle)
    {
        runQuery();
    }

    private void runQuery()
    {
        Visit visit = (Visit) getVisit();
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

                if (count != _browser.getResultCount())
                    _browser.initializeForResultCount(count);

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
     *  Listener used to return a book.
     *
     **/

    public void returnBook(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();
        Integer bookPK = (Integer) parameters[0];

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        IOperations operations = vengine.getOperations();

        try
        {
            Book book = operations.returnBook(bookPK);

            setMessage(format("returned-book", book.getTitle()));

            runQuery();
        }
        catch (FinderException ex)
        {
            setError(format("unable-to-return-book", ex.getMessage()));
            return;
        }
        catch (RemoteException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }

}