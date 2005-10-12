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
import java.text.DateFormat;

import javax.ejb.FinderException;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Meta;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.vlib.OperationsUser;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.IOperations;

/**
 * Shows the details of a book, and allows the user to borrow it.
 * 
 * @author Howard Lewis Ship
 */
@Meta("page-type=Search")
public abstract class ViewBook extends BasePage implements IExternalPage, PageBeginRenderListener,
        OperationsUser
{
    private DateFormat _dateFormat;

    @Persist
    public abstract Integer getBookId();

    public abstract void setBookId(Integer bookId);

    public abstract Book getBook();

    public abstract void setBook(Book value);

    public void activateExternalPage(Object[] parameters, IRequestCycle cycle)
    {
        Integer bookId = (Integer) parameters[0];

        setBookId(bookId);
    }

    private void readBook()
    {
        // This doesn't handle invalid book id as nicely as the 3.0 code did, but I'm
        // getting a bit lazy!

        Book book = getRemoteTemplate().getBook(getBookId());

        setBook(book);
    }

    public DateFormat getDateFormat()
    {
        if (_dateFormat == null)
            _dateFormat = DateFormat.getDateTimeInstance(
                    DateFormat.MEDIUM,
                    DateFormat.SHORT,
                    getLocale());

        return _dateFormat;
    }

    public void pageBeginRender(PageEvent event)
    {
        if (getBook() == null)
            readBook();
    }

}