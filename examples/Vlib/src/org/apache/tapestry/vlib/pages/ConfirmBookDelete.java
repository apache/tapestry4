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
import javax.ejb.RemoveException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.IOperations;

/**
 *  Presents a confirmation page before deleting a book.  If the user
 *  selects "yes", the book is deleted; otherwise the user is returned
 *  to the {@link MyLibrary} page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class ConfirmBookDelete extends BasePage
{
    public abstract void setBookId(Integer bookId);

    public abstract void setBookTitle(String title);

    /** 
     * Invoked (by {@link MyLibrary}) to select a book to be
     * deleted.  This method sets the temporary page properties
     * (bookPrimaryKey and bookTitle) and invoked {@link IRequestCycle#setPage(IPage)}.
     *
     **/

    public void selectBook(Integer bookId, IRequestCycle cycle)
    {
        setBookId(bookId);

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        int i = 0;
        while (true)
        {
            try
            {
                IOperations operations = vengine.getOperations();
                Book book = operations.getBook(bookId);

                setBookTitle(book.getTitle());

                break;
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception reading read book #" + bookId + ".", ex, i++);
            }
        }

        cycle.activate(this);
    }

    /**
     *  Hooked up to the yes component, this actually deletes the book.
     *
     **/

    public void deleteBook(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();
        Integer bookPK = (Integer) parameters[0];

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Book book = null;

        int i = 0;
        while (true)
        {
            try
            {
                IOperations operations = vengine.getOperations();

                book = operations.deleteBook(bookPK);

                break;
            }
            catch (RemoveException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception deleting book #" + bookPK + ".", ex, i++);
            }
        }

        MyLibrary myLibrary = (MyLibrary) cycle.getPage("MyLibrary");

        myLibrary.setMessage(format("book-deleted", book.getTitle()));

       	myLibrary.activate(cycle);
    }
}