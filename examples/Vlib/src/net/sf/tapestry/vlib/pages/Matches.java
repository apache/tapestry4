package net.sf.tapestry.vlib.pages;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.vlib.IErrorProperty;
import net.sf.tapestry.vlib.VirtualLibraryEngine;
import net.sf.tapestry.vlib.components.Browser;
import net.sf.tapestry.vlib.ejb.Book;
import net.sf.tapestry.vlib.ejb.IBookQuery;
import net.sf.tapestry.vlib.ejb.IBookQueryHome;

/**
 *  Runs queries and displays matches.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class Matches extends BasePage
{
    private IBookQuery bookQuery;
    private Book currentMatch;
    private Browser browser;

    public void detach()
    {
        bookQuery = null;
        currentMatch = null;

        super.detach();
    }

    public void finishLoad()
    {
        browser = (Browser) getComponent("browser");
    }

    /**
     *  Gets the {@link IBookQuery} session bean for the query, creating
     *  it fresh if necessary.
     *
     **/

    public IBookQuery getBookQuery()
    {
        if (bookQuery == null)
        {
            // No existing handle, so time to create a new bean.

            VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

            for (int i = 0; i < 2; i++)
            {
                try
                {
                    IBookQueryHome home = vengine.getBookQueryHome();

                    setBookQuery(home.create());

                    break;
                }
                catch (CreateException ex)
                {
                    throw new ApplicationRuntimeException(ex);
                }
                catch (RemoteException ex)
                {
                    vengine.rmiFailure("Remote exception creating BookQuery.", ex, i > 0);
                }
            }
        }

        return bookQuery;
    }

    /**
     *  Sets the persistent bookQuery property.
     *
     **/

    public void setBookQuery(IBookQuery value)
    {
        bookQuery = value;

        fireObservedChange("bookQuery", value);
    }

    /**
     *  Invoked by the {@link Home} page to perform a query.
     *
     **/

    public void performQuery(String title, String author, Object publisherPK, IRequestCycle cycle)
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        for (int i = 0; i < 2; i++)
        {

            IBookQuery query = getBookQuery();

            try
            {
                int count = query.masterQuery(title, author, publisherPK);

                if (count == 0)
                {
                    Home home = (Home) cycle.getPage("Home");
                    home.setMessage("No matches for your query.");
                    cycle.setPage(home);
                    return;
                }

                browser.initializeForResultCount(count);

                break;
            }
            catch (RemoteException ex)
            {
                String message = "Remote exception processing query.";

                vengine.rmiFailure(message, ex, false);

                if (i > 0)
                {
                    // This method is invoked from the Home page.  We return
                    // without changing the response page.

                    IErrorProperty page = (IErrorProperty) cycle.getPage();
                    page.setError(message);
                    return;
                }
            }
        }

        cycle.setPage(this);

    }

    public Book getCurrentMatch()
    {
        return currentMatch;
    }

    /**
     *  Updates the dynamic currentMatch property.
     *
     **/

    public void setCurrentMatch(Book value)
    {
        currentMatch = value;
    }

    public boolean getOmitHolderLink()
    {
        return !currentMatch.isBorrowed();
    }

    /**
     *  Removes the book query bean, if not null.
     *
     **/

    public void cleanupPage()
    {
        try
        {
            if (bookQuery != null)
                bookQuery.remove();

            bookQuery = null;
        }
        catch (RemoveException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
        catch (RemoteException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }

        super.cleanupPage();
    }
}