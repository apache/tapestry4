package net.sf.tapestry.vlib.pages;

import java.rmi.RemoteException;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IExternalPage;
import net.sf.tapestry.IPageLoader;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.PageLoaderException;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.vlib.VirtualLibraryEngine;
import net.sf.tapestry.vlib.components.Browser;
import net.sf.tapestry.vlib.ejb.Book;
import net.sf.tapestry.vlib.ejb.IBookQuery;
import net.sf.tapestry.vlib.ejb.IOperations;
import net.sf.tapestry.vlib.ejb.IPerson;
import net.sf.tapestry.vlib.ejb.Person;

/**
 *  Displays the book inventory list for a single {@link IPerson}, showing
 *  what books are owned by the person, who has them borrowed, etc.  If the
 *  user is logged in, then books can be borrowed from this page as well.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class PersonPage extends BasePage implements IExternalPage
{
    private IBookQuery query;
    private Book currentMatch;
    private Person person;
    private Browser browser;

    public void detach()
    {
        query = null;
        currentMatch = null;
        person = null;

        super.detach();
    }

    public void finishLoad()
    {
        browser = (Browser) getComponent("browser");
    }

    public void setPerson(Person value)
    {
        person = value;

        fireObservedChange("person", value);
    }

    public Person getPerson()
    {
        return person;
    }

    public String getEmailURL()
    {
        return "mailto:" + person.getEmail();
    }

    /**
     *  Gets the {@link IBookQuery} session bean that contains
     *  the books owned by the user, creating it fresh as needed.
     *
     **/

    public IBookQuery getQuery()
    {
        if (query == null)
        {
            VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
            setQuery(vengine.createNewQuery());
        }

        return query;
    }

    /**
     *  Sets the query persistent page property.
     *
     **/

    public void setQuery(IBookQuery value)
    {
        query = value;

        fireObservedChange("query", value);
    }

    /**
     *  Invoked by the external service to being viewing the
     *  identified person.
     *
     **/

    public void activateExternalPage(Object[] parameters, IRequestCycle cycle)
    {
        Integer personPK = (Integer)parameters[0];
        
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        for (int i = 0; i < 2; i++)
        {
            try
            {
                IBookQuery query = getQuery();

                int count = query.ownerQuery(personPK);

                browser.initializeForResultCount(count);

                IOperations operations = vengine.getOperations();

                setPerson(operations.getPerson(personPK));

                break;
            }
            catch (FinderException e)
            {
                vengine.presentError("Person " + personPK + " not found in database.", getRequestCycle());
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception for owner query.", ex, i > 0);

                setQuery(null);
            }
        }

        cycle.setPage(this);
    }

    public Book getCurrentMatch()
    {
        return currentMatch;
    }

    public void setCurrentMatch(Book value)
    {
        currentMatch = value;
    }

    public boolean getOmitHolderLink()
    {
        return !currentMatch.isBorrowed();
    }

    /**
     *  Removes the book query bean, if the handle to the bean
     *  is non-null.
     *
     **/

    public void cleanupPage()
    {
        try
        {
            if (query != null)
                query.remove();
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