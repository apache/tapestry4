package com.primix.vlib.components;

import com.primix.tapestry.components.*;
import com.primix.tapestry.*;
import com.primix.vlib.ejb.*;

// To appease Javadoc
import com.primix.vlib.pages.ViewBook;
import com.primix.vlib.pages.PersonPage;

/*
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; wihtout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Creates a link to the {@link ViewBook} page using the external service.
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
 * <p>Informal parameters are allowed.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class BookLink extends BaseComponent
{
    private IBinding bookBinding;
    private Book book;
    private String[] context;

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
            book = (Book)bookBinding.getObject("book", Book.class);

        return book;
    }

    /**
     *  The context has two elements.  The first is the page to jump to
     *  ({@link PersonPage}), the second is the primary key of the person.
     *
     */

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
     */

    public void render(IResponseWriter writer, IRequestCycle cycle)
    throws RequestCycleException
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
