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

package org.apache.tapestry.vlib.components;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.vlib.OperationsUser;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.BorrowException;
import org.apache.tapestry.vlib.pages.Home;
import org.apache.tapestry.vlib.services.RemoteCallback;

/**
 * Implements the Borrow link that appears on many pages. <table border=1>
 * <tr>
 * <th>Parameter</th>
 * <th>Type</th>
 * <th>Read / Write </th>
 * <th>Required</th>
 * <th>Default</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>book</td>
 * <td>{@link Book}</td>
 * <td>R</td>
 * <td>yes</td>
 * <td>&nbsp;</td>
 * <td>The book to create a link to. </td>
 * </tr>
 * </table>
 * <p>
 * Informal parameters are not allowed. A body is not allowed.
 * 
 * @author Howard Lewis Ship
 */

public abstract class Borrow extends BaseComponent implements OperationsUser
{
    @Parameter(required = true)
    public abstract Book getBook();

    @InjectState("visit")
    public abstract Visit getVisit();

    @InjectPage("Home")
    public abstract Home getHome();

    @Message
    public abstract String borrowedBook(String title);

    public boolean isLinkDisabled()
    {
        Visit visit = getVisit();

        if (!visit.isUserLoggedIn())
            return true;

        // If the user is logged in, they can borrow it if they are
        // not already holding it and aren't the owner.

        Book book = getBook();

        // If the book is not lendable, then disable the link.

        if (!book.isLendable())
            return true;

        // Otherwise, disabled the link if already holding it.

        return visit.isLoggedInUser(book.getHolderId());
    }

    public IPage borrow(final IRequestCycle cycle, final Integer bookId)
    {
        final Visit visit = getVisit();

        RemoteCallback<Book> callback = new RemoteCallback()
        {
            public Book doRemote() throws RemoteException
            {
                try
                {
                    return getOperations().borrowBook(bookId, visit.getUserId());
                }
                catch (BorrowException ex)
                {
                    getErrorPresenter().presentError(ex.getMessage(), cycle);

                    return null;
                }
                catch (FinderException ex)
                {
                    throw new ApplicationRuntimeException(ex);
                }
            }
        };

        Book book = getRemoteTemplate().execute(callback, "Error borrowing book.");

        if (book == null)
            return null;

        Home home = getHome();

        home.setMessage(borrowedBook(book.getTitle()));

        return home;
    }
}