/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
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

        myLibrary.setMessage(formatString("book-deleted", book.getTitle()));

       	myLibrary.activate(cycle);
    }
}