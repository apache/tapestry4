package net.sf.tapestry.vlib.components;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.NullValueForBindingException;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.vlib.VirtualLibraryEngine;
import net.sf.tapestry.vlib.Visit;
import net.sf.tapestry.vlib.ejb.Book;
import net.sf.tapestry.vlib.ejb.BorrowException;
import net.sf.tapestry.vlib.ejb.IOperations;
import net.sf.tapestry.vlib.pages.Home;

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

public class Borrow extends BaseComponent
{
    private IBinding bookBinding;
    private Book book;

    public void setBookBinding(IBinding value)
    {
        bookBinding = value;
    }

    public IBinding getBookBinding()
    {
        return bookBinding;
    }

    /**
     *  Gets the book to create a link for.  This is cached for the duration of the componen's
     * {@link #render(IMarkupWriter, IRequestCycle)} method.
     *
     **/

    public Book getBook()
    {
        if (book == null)
            book = (Book) bookBinding.getObject("book", Book.class);

        if (book == null)
            throw new NullValueForBindingException(bookBinding);

        return book;
    }

    /**
     *  Overriden to simply clear the book property after the component finishes rendering.
     *
     **/

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        book = null;

        super.cleanupAfterRender(cycle);
    }

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

        return visit.isLoggedInUser(book.getHolderPrimaryKey());
    }

    public void borrow(IRequestCycle cycle) throws RequestCycleException
    {
        Object[] parameters = cycle.getServiceParameters();
        Integer bookPK = (Integer)parameters[0];

        Visit visit = (Visit) getPage().getVisit();
        Home home = (Home) cycle.getPage("Home");
        VirtualLibraryEngine vengine = visit.getEngine();

        for (int i = 0; i < 2; i++)
        {
            try
            {
                IOperations bean = vengine.getOperations();
                Book book = bean.borrowBook(bookPK, visit.getUserPK());

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
                vengine.rmiFailure("Remote exception borrowing book.", ex, i > 0);
            }
        }

        cycle.setPage(home);
    }

}