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
package net.sf.tapestry.vlib.pages;

import java.rmi.RemoteException;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IExternalPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.vlib.VirtualLibraryEngine;
import net.sf.tapestry.vlib.components.Browser;
import net.sf.tapestry.vlib.ejb.Book;
import net.sf.tapestry.vlib.ejb.IBookQuery;
import net.sf.tapestry.vlib.ejb.IOperations;
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