/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.vlib.pages;

import java.rmi.RemoteException;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.components.Browser;
import org.apache.tapestry.vlib.ejb.IBookQuery;
import org.apache.tapestry.vlib.ejb.Person;
import org.apache.tapestry.vlib.ejb.SortColumn;
import org.apache.tapestry.vlib.ejb.SortOrdering;

/**
 *  Displays the book inventory list for a single {@link org.apache.tapestry.vlib.ejb.IPerson}, showing
 *  what books are owned by the person, who has them borrowed, etc.  If the
 *  user is logged in, then books can be borrowed from this page as well.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class ViewPerson extends BasePage implements IExternalPage, PageRenderListener
{
    public abstract Integer getPersonId();

    public abstract void setPersonId(Integer personId);

    public abstract void setPerson(Person value);

    public abstract Person getPerson();

    public abstract IBookQuery getQuery();

    public abstract void setQuery(IBookQuery value);

    public abstract SortColumn getSortColumn();

    public abstract boolean isDescending();

    private Browser _browser;

    public void finishLoad()
    {
        _browser = (Browser) getComponent("browser");
    }

    /**
     *  Invoked by the external service to being viewing the
     *  identified person.
     *
     **/

    public void activateExternalPage(Object[] parameters, IRequestCycle cycle)
    {
        Integer personId = (Integer) parameters[0];

        setPersonId(personId);

        // Force the query to be re-run when the person changes.

        int count = runQuery();
        
        _browser.initializeForResultCount(count);
    }

    public void requery(IRequestCycle cycle)
    {
        int count = runQuery();
        
        if (_browser.getResultCount() != count)
        	_browser.setResultCount(count);
    }

    private int runQuery()
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();
        Integer personId = getPersonId();

        SortOrdering ordering = new SortOrdering(getSortColumn(), isDescending());

        int i = 0;
        while (true)
        {
            IBookQuery query = getQuery();

            if (query == null)
            {
                query = vengine.createNewQuery();

                setQuery(query);
            }

            try
            {
                return query.ownerQuery(personId, ordering);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception for owner query.", ex, i++);

                setQuery(null);
            }
        }

    }

    public void pageBeginRender(PageEvent event)
    {
        Person person = getPerson();

        if (person == null)
        {
            VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

            person = vengine.readPerson(getPersonId());

            setPerson(person);
        }
    }

}