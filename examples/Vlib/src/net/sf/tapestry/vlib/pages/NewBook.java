package net.sf.tapestry.vlib.pages;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.vlib.Protected;
import net.sf.tapestry.vlib.VirtualLibraryEngine;
import net.sf.tapestry.vlib.Visit;
import net.sf.tapestry.vlib.ejb.IOperations;

/**
 *  Collects information for a new book.  
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class NewBook extends Protected
{
    private Map attributes;
    private String publisherName;

    public void detach()
    {
        attributes = null;
        publisherName = null;

        super.detach();
    }

    public Map getAttributes()
    {
        if (attributes == null)
            attributes = new HashMap();

        return attributes;
    }

    public String getPublisherName()
    {
        return publisherName;
    }

    public void setPublisherName(String value)
    {
        publisherName = value;
    }

    public void addBook(IRequestCycle cycle)
    {
        Map attributes = getAttributes();

        Integer publisherPK = (Integer) attributes.get("publisherPK");

        if (publisherPK == null && Tapestry.isNull(publisherName))
        {
            setErrorField(
                "inputPublisherName",
                "Must enter a publisher name or select an existing publisher from the list.",
                null);
            return;
        }

        if (publisherPK != null && !Tapestry.isNull(publisherName))
        {
            setErrorField(
                "inputPublisherName",
                "Must either select an existing publisher or enter a new publisher name.",
                publisherName);
            return;
        }

        if (isInError())
            return;

        Visit visit = (Visit) getVisit();
        Integer userPK = visit.getUserPK();
        VirtualLibraryEngine vengine = visit.getEngine();

        attributes.put("ownerPK", userPK);
        attributes.put("holderPK", userPK);

        for (int i = 0; i < 2; i++)
        {
            try
            {

                IOperations operations = vengine.getOperations();

                if (publisherPK != null)
                    operations.addBook(attributes);
                else
                {
                    operations.addBook(attributes, publisherName);

                    // Clear the app's cache of info; in this case, known publishers.

                    visit.clearCache();
                }

                break;
            }
            catch (CreateException ex)
            {
                setError("Error adding book: " + ex.getMessage());
                return;
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception adding new book.", ex, i > 0);
            }
        }

        // Success.  First, update the message property of the return page.

        MyLibrary myLibrary = (MyLibrary) cycle.getPage("MyLibrary");

        myLibrary.setMessage("Added: " + attributes.get("title"));

        cycle.setPage(myLibrary);
    }

}