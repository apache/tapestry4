/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.vlib.components;

import java.sql.Timestamp;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.vlib.Visit;
import net.sf.tapestry.vlib.ejb.Book;

/**
 *  Creates a link to the {@link net.sf.tapestry.vlib.pages.ViewBook} 
 *  page using the external service.
 *
 *
 *  <table border=1>
 *  <tr> <th>Parameter</th> 
 *  <th>Type</th> 
 *  <th>Direction</th> 
 *  <th>Required</th> 
 *  <th>Default</th> 
 *  <th>Description</th>
 *  </tr>
 * 
 * <tr>
 *  <td>book</td> 
 *  <td>{@link Book}</td>
 *  <td>in</td>
 *  <td>yes</td> 
 *  <td>&nbsp;</td>
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
    private Book _book;

    /**
     *  One week, in milliseconds (1/1000 second).  Books that have been added in the last
     *  week are marked new, until the user logs in, at which point, its books
     *  added since the user last logged in.
     * 
     **/

    private static final long ONE_WEEK_MILLIS = 1000l * 60l * 60l * 24l * 7l;

    public boolean isNewlyAdded()
    {
        IEngine engine = getPage().getEngine();
        Visit visit = (Visit) engine.getVisit();
        Timestamp lastAccess = null;

        if (visit != null)
            lastAccess = visit.getLastAccess();

        Timestamp dateAdded = _book.getDateAdded();

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

    public Book getBook()
    {
        return _book;
    }

    public void setBook(Book book)
    {
        _book = book;
    }

}