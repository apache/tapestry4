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

import java.sql.Timestamp;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.vlib.Visit;
import org.apache.tapestry.vlib.ejb.Book;

/**
 *  Creates a link to the {@link org.apache.tapestry.vlib.pages.ViewBook} 
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

public abstract class BookLink extends BaseComponent
{
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

    public abstract Book getBook();
}