//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.vlib.components;

import java.sql.Timestamp;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.vlib.Visit;
import net.sf.tapestry.vlib.ejb.Book;

/**
 *  Creates a link to the {@link net.sf.tapestry.vlib.pages.ViewBook} 
 *  page using the external service.
 *
 *
 * <table border=1>
 * <tr> <th>Parameter</th> <th>Type</th> <th>Read / Write </th> <th>Required</th> <th>Default</th> <th>Description</th>
 * </tr>
 * <tr>
 *  <td>book</td> <td>{@link Book}</td>
 *  <td>R</td>
 *  <td>yes</td> <td>&nbsp;</td>
 *  <td>The {@link Book} to create a link to.</td>
 * </tr>
 *
 * </table>
 *
 *  <p>Informal parameters are allowed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class BookLink extends BaseComponent
{
    private IBinding bookBinding;
    private Book book;
    private String[] context;

    /**
     *  One week, in milliseconds (1/1024 second).  Books that have been added in the last
     *  week are marked new, until the user logs in, at which point, its books
     *  added since the user last logged in.
     * 
     **/

    private static final long ONE_WEEK_MILLIS = 1024l * 60l * 60l * 24l * 7l;

    public IBinding getBookBinding()
    {
        return bookBinding;
    }

    public void setBookBinding(IBinding value)
    {
        bookBinding = value;
    }

    public Book getBook()
    {
        if (book == null)
            book = (Book) bookBinding.getObject("book", Book.class);

        return book;
    }

    public boolean isNew()
    {
        IEngine engine = page.getEngine();
        Visit visit = (Visit) engine.getVisit();
        Timestamp lastAccess = null;

        if (visit != null)
            lastAccess = visit.getLastAccess();

        Book book = getBook();
        Timestamp dateAdded = book.getDateAdded();

        // Some old records may not contain a value for dateAdded

        if (dateAdded == null)
            return false;

        // If don't know the last access time (because the user
        // hasn't logged in yet), then show anything newer
        // than a week.

        if (lastAccess == null)
        {
            long now = System.currentTimeMillis();

            return (now - dateAdded.getTime()) <= ONE_WEEK_MILLIS;
        }

        // Return true if lastAccess is earlier than date added.

        return lastAccess.compareTo(dateAdded) <= 0;
    }

    /**
     *  The context has two elements.  The first is the page to jump to
     *  ({@link PersonPage}), the second is the primary key of the person.
     *
     **/

    public String[] getContext()
    {
        if (context == null)
        {
            context = new String[2];
            context[0] = "ViewBook";
        }

        context[1] = getBook().getPrimaryKey().toString();

        return context;
    }

    /**
     *  Overrides render() to always set the book property to null after
     *  renderring.
     *
     **/

    public void render(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        try
        {
            super.render(writer, cycle);
        }
        finally
        {
            book = null;
        }
    }
}