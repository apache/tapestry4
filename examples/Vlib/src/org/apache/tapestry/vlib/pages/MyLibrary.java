/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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