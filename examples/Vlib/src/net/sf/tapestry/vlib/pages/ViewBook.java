package net.sf.tapestry.vlib.pages;

import java.rmi.RemoteException;
import java.text.DateFormat;

import javax.ejb.FinderException;

import net.sf.tapestry.IExternalPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.vlib.VirtualLibraryEngine;
import net.sf.tapestry.vlib.ejb.Book;
import net.sf.tapestry.vlib.ejb.IOperations;

/**
 *  Shows the details of a book, and allows the user to borrow it.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ViewBook extends BasePage implements IExternalPage
{
    private Book book;

    public void detach()
    {
        super.detach();

        book = null;
    }

    public Book getBook()
    {
        return book;
    }

    public void setBook(Book value)
    {
        book = value;

        fireObservedChange("book", value);
    }

    public void activateExternalPage(Object[] parameters, IRequestCycle cycle)
    {
        Integer bookPK = (Integer) parameters[0];
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        for (int i = 0; i < 2; i++)
        {
            IOperations bean = vengine.getOperations();

            try
            {
                setBook(bean.getBook(bookPK));

                cycle.setPage(this);

                return;
            }
            catch (FinderException ex)
            {
                vengine.presentError("Book not found in database.", cycle);
                return;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception obtaining information for book " + bookPK + ".", ex, i > 0);
            }
        }

    }

    /**
     *  Only want to show the holder if it doesn't match the owner.
     *
     **/

    public boolean getShowHolder()
    {
        Integer ownerPK;
        Integer holderPK;

        ownerPK = book.getOwnerPrimaryKey();
        holderPK = book.getHolderPrimaryKey();

        return !ownerPK.equals(holderPK);
    }

    private DateFormat dateFormat;

    public DateFormat getDateFormat()
    {
        if (dateFormat == null)
            dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, getLocale());

        return dateFormat;
    }
}