//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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