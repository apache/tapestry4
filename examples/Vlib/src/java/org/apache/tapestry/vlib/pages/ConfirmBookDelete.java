// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

import javax.ejb.RemoveException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.annotations.Meta;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.vlib.OperationsUser;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.services.RemoteCallback;

/**
 * Presents a confirmation page before deleting a book. If the user selects
 * "yes", the book is deleted; otherwise the user is returned to the
 * {@link MyLibrary} page.
 * 
 * @author Howard Lewis Ship
 */

@Meta("page-type=MyLibrary")
public abstract class ConfirmBookDelete extends BasePage implements OperationsUser
{

    public abstract void setBookId(Integer bookId);

    public abstract void setBookTitle(String title);

    @InjectPage("MyLibrary")
    public abstract MyLibrary getMyLibrary();

    @Message
    public abstract String bookDeleted(String title);

    /**
     * Invoked (by {@link MyLibrary}) to select a book to be deleted. This
     * method sets the temporary page properties (bookPrimaryKey and bookTitle)
     * and invoked {@link IRequestCycle#setPage(IPage)}.
     */

    public void selectBook(Integer bookId)
    {
        setBookId(bookId);

        Book book = getRemoteTemplate().getBook(bookId);

        setBookTitle(book.getTitle());

        getRequestCycle().activate(this);
    }

    /**
     * Hooked up to the yes component, this actually deletes the book.
     */

    public void deleteBook(final Integer bookId)
    {
        RemoteCallback<Book> callback = new RemoteCallback()
        {

            public Book doRemote()
                throws RemoteException
            {
                try
                {
                    return getOperations().deleteBook(bookId);
                }
                catch (RemoveException ex)
                {
                    throw new ApplicationRuntimeException(ex);
                }
            }
        };

        Book book = getRemoteTemplate().execute(callback, "Error deleting book #" + bookId + ".");

        MyLibrary myLibrary = getMyLibrary();

        myLibrary.setMessage(bookDeleted(book.getTitle()));

        myLibrary.activate();
    }
}
