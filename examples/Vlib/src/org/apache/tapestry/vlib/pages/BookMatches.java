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

	import org.apache.tapestry.IRequestCycle;
	import org.apache.tapestry.html.BasePage;
	import org.apache.tapestry.vlib.IMessageProperty;
	import org.apache.tapestry.vlib.VirtualLibraryEngine;
	import org.apache.tapestry.vlib.components.Browser;
	import org.apache.tapestry.vlib.ejb.IBookQuery;
	import org.apache.tapestry.vlib.ejb.MasterQueryParameters;
	import org.apache.tapestry.vlib.ejb.SortColumn;
	import org.apache.tapestry.vlib.ejb.SortOrdering;

/**
 *  Runs queries and displays matches.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public abstract class BookMatches extends BasePage
{
    private Browser _browser;

    public void finishLoad()
    {
        _browser = (Browser) getComponent("browser");
    }

    public abstract IBookQuery getBookQuery();

    public abstract void setBookQuery(IBookQuery bookQuery);

    public abstract SortColumn getSortColumn();

    public abstract boolean isDescending();

    public abstract MasterQueryParameters getQueryParameters();

    public abstract void setQueryParameters(MasterQueryParameters queryParameters);

    /**
     *  Invoked by the {@link Home} page to perform a query.
     *
     **/

    public void performQuery(MasterQueryParameters parameters, IRequestCycle cycle)
    {
        setQueryParameters(parameters);

        int count = executeQuery();

        if (count == 0)
        {
            IMessageProperty page = (IMessageProperty) cycle.getPage();
			page.setMessage(getMessage("no-matches"));
            return;
        }

        _browser.initializeForResultCount(count);
        cycle.activate(this);
    }

    public void requery(IRequestCycle cycle)
    {
        int count = executeQuery();

        if (count != _browser.getResultCount())
            _browser.initializeForResultCount(count);
    }

    private int executeQuery()
    {
        VirtualLibraryEngine vengine = (VirtualLibraryEngine) getEngine();

        MasterQueryParameters parameters = getQueryParameters();

        SortOrdering ordering = new SortOrdering(getSortColumn(), isDescending());

        int i = 0;
        while (true)
        {
            try
            {
                IBookQuery query = getBookQuery();

                if (query == null)
                {
                    query = vengine.createNewQuery();
                    setBookQuery(query);
                }
                
                return query.masterQuery(parameters, ordering);
            }
            catch (RemoteException ex)
            {
                vengine.rmiFailure("Remote exception processing query.", ex, i++);
                
                setBookQuery(null);
            }

        }
    }

}