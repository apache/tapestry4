package net.sf.tapestry.vlib.pages;

import java.rmi.RemoteException;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.vlib.VirtualLibraryEngine;
import net.sf.tapestry.vlib.ejb.Book;
import net.sf.tapestry.vlib.ejb.IOperations;

/**
 *  Presents a confirmation page before deleting a book.  If the user
 *  selects "yes", the book is deleted; otherwise the user is returned
 *  to the {@link MyLibrary} page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ConfirmBookDelete extends BasePage
{
    private String bookTitle;
    private Integer bookPK;

    public void detach()
    {
        super.detach();

        bookTitle = null;
        bookPK = null;
    }

    public String getBookTitle()
    {
        return bookTitle;
    }

    public Integer getBookPrimaryKey()
    {
        return bookPK;
    }

    /** 
     * Invoked (by {@link MyLibrary}) to select a book to be
     * deleted.  This method sets the temporary page properties
     * (bookPrimaryKey and bookTitle) and invoked {@link IRequestCycle#setPage(IPage)}.
     *
     **/

    public void selectBook(Integer bookPK, IRequestCycle cycle)
    {
        this.bookPK = bookPK;

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        for (int i = 0; i < 2; i++)
        {
            try
            {
                IOperations operations = vengine.getOperations();
                Book book = operations.getBook(bookPK);

                bookTitle = book.getTitle();

                break;
            }
            catch (FinderException ex)
            {
                throw new ApplicationRuntimeException(ex);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception reading read book #" + bookPK + ".", ex, i > 0);
            }
        }

        cycle.setPage(this);
    }

    /**
     *  Hooked up to the yes component, this actually deletes the book.
     *
     **/

    public void deleteBook(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();
        Integer bookPK =(Integer)parameters[0];

        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Book book = null;

        for (int i = 0; i < 2; i++)
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
                vengine.rmiFailure("Remote exception deleting book #" + bookPK + ".", ex, i > 0);
            }
        }

        MyLibrary myLibrary = (MyLibrary) cycle.getPage("MyLibrary");

        myLibrary.setMessage("Deleted book: " + book.getTitle());

        cycle.setPage(myLibrary);
    }
}