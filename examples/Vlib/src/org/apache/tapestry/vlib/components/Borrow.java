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

package org.apache.tapestry.vlib.components;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.BorrowException;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.pages.Home;

/**
 *  Implements the Borrow link that appears on many pages.
 *
 *  <table border=1>
 *  <tr> <th>Parameter</th> <th>Type</th> <th>Read / Write </th> <th>Required</th> <th>Default</th> <th>Description</th>
 *  </tr>
 *
 *  <tr>
 *      <td>book</td>
 *      <td>{@link Book}</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>The book to create a link to.
 *      </td>
 *  </tr>
 *
 *  </table>
 *
 *  <p>Informal parameters are not allowed.  A body is not allowed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public abstract class Borrow extends BaseComponent
{
    public abstract Book getBook();

    public boolean isLinkDisabled()
    {
        Visit visit = (Visit) getPage().getVisit();

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

    public void borrow(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();
        Integer bookPK = (Integer) parameters[0];

        Visit visit = (Visit) getPage().getVisit();
        Home home = (Home) cycle.getPage("Home");
        VirtualLibraryEngine vengine = (VirtualLibraryEngine)cycle.getEngine();

        int i = 0;
        while (true)
        {
            try
            {
                IOperations bean = vengine.getOperations();
                Book book = bean.borrowBook(bookPK, visit.getUserId());

                home.setMessage("Borrowed: " + book.getTitle());

                break;
            }
            catch (BorrowException ex)
            {
                vengine.presentError(ex.getMessage(), cycle);
                return;
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException("Unable to find book or user. ", ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception borrowing book.", ex, i++);
            }
        }

        cycle.activate(home);
    }

}