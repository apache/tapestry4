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