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