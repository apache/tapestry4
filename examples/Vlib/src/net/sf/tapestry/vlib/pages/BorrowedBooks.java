package net.sf.tapestry.vlib.pages;

import java.rmi.RemoteException;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.vlib.Protected;
import net.sf.tapestry.vlib.VirtualLibraryEngine;
import net.sf.tapestry.vlib.Visit;
import net.sf.tapestry.vlib.components.Browser;
import net.sf.tapestry.vlib.ejb.Book;
import net.sf.tapestry.vlib.ejb.IBookQuery;
import net.sf.tapestry.vlib.ejb.IOperations;

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

public class BorrowedBooks extends Protected
{
    private String message;
    private IBookQuery borrowedQuery;

    private Book currentBook;

    private Browser browser;

    public void detach()
    {
        message = null;
        borrowedQuery = null;
        currentBook = null;

        super.detach();
    }

    public void finishLoad()
    {
        browser = (Browser) getComponent("browser");
    }

    /**
     *  A dirty little secret of Tapestry and page recorders:  persistent
     *  properties must be set before the render (when this method is invoked)
     *  and can't change during the render.  We force
     *  the creation of the borrowed books query and re-execute it whenever
     *  the BorrowedBooks page is rendered.
     *
     **/

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        super.beginResponse(writer, cycle);

        Visit visit = (Visit) getVisit();
        Integer userPK = visit.getUserPK();

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        for (int i = 0; i < 2; i++)
        {
            try
            {
                IBookQuery query = getBorrowedQuery();
                int count = query.borrowerQuery(userPK);

                if (count != browser.getResultCount())
                    browser.initializeForResultCount(count);

                break;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception finding borrowed books.", ex, i > 0);

                setBorrowedQuery(null);
            }
        }
    }

    public void setBorrowedQuery(IBookQuery value)
    {
        borrowedQuery = value;

        fireObservedChange("borrowedQuery", value);
    }

    public IBookQuery getBorrowedQuery()
    {
        if (borrowedQuery == null)
        {
            VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
            setBorrowedQuery(vengine.createNewQuery());
        }

        return borrowedQuery;
    }

    /**
     *  Updates the currentBook dynamic page property.
     *
     **/

    public void setCurrentBook(Book value)
    {
        currentBook = value;
    }

    public Book getCurrentBook()
    {
        return currentBook;
    }

    public void setMessage(String value)
    {
        message = value;
    }

    public String getMessage()
    {
        return message;
    }

    /**
     *  Listener used to return a book.
     *
     **/

    public void returnBook(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();
        Integer bookPK = (Integer)parameters[0];

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        IOperations operations = vengine.getOperations();

        try
        {
            Book book = operations.returnBook(bookPK);

            setMessage("Returned book: " + book.getTitle());
        }
        catch (FinderException ex)
        {
            setError("Could not return book: " + ex.getMessage());
            return;
        }
        catch (RemoteException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }

    /**
     *  Removes the book query beans.
     **/

    public void cleanupPage()
    {
        try
        {
            if (borrowedQuery != null)
                borrowedQuery.remove();

        }
        catch (RemoveException e)
        {
            throw new ApplicationRuntimeException(e);
        }
        catch (RemoteException e)
        {
            throw new ApplicationRuntimeException(e);
        }

        super.cleanupPage();
    }
}