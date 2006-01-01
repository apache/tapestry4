// Copyright 2004, 2005, 2006 The Apache Software Foundation
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
import org.apache.tapestry.annotations.InjectComponent;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.annotations.Meta;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.vlib.OperationsUser;
import org.apache.tapestry.vlib.components.Browser;
import org.apache.tapestry.vlib.ejb.IBookQuery;
import org.apache.tapestry.vlib.ejb.MasterQueryParameters;
import org.apache.tapestry.vlib.ejb.SortColumn;
import org.apache.tapestry.vlib.ejb.SortOrdering;
import org.apache.tapestry.vlib.services.RemoteCallback;

/**
 * Runs queries and displays matches.
 * 
 * @author Howard Lewis Ship
 */

@Meta("page-type=Search")
public abstract class BookMatches extends BasePage implements OperationsUser
{
    @InjectComponent("browser")
    public abstract Browser getBrowser();

    @Persist
    public abstract IBookQuery getBookQuery();

    public abstract void setBookQuery(IBookQuery bookQuery);

    @Persist
    public abstract SortColumn getSortColumn();

    public abstract void setSortColumn(SortColumn column);

    @Persist
    public abstract boolean isDescending();

    @Persist
    public abstract MasterQueryParameters getQueryParameters();

    public abstract void setQueryParameters(MasterQueryParameters queryParameters);

    @Message
    public abstract String noMatches();

    public void finishLoad()
    {
        setSortColumn(SortColumn.TITLE);
    }

    @InjectPage("Home")
    public abstract Home getHome();

    /**
     * Invoked by the {@link Home} page to perform a query.
     */

    public void performQuery(MasterQueryParameters parameters)
    {
        setQueryParameters(parameters);

        int count = executeQuery();

        if (count == 0)
        {
            Home page = getHome();
            page.setMessage(noMatches());
            return;
        }

        getBrowser().initializeForResultCount(count);
        getRequestCycle().activate(this);
    }

    public void requery(IRequestCycle cycle)
    {
        int count = executeQuery();

        Browser browser = getBrowser();

        if (count != browser.getResultCount())
            browser.initializeForResultCount(count);
    }

    private int executeQuery()
    {
        final MasterQueryParameters parameters = getQueryParameters();

        final SortOrdering ordering = new SortOrdering(getSortColumn(), isDescending());

        RemoteCallback<Integer> callback = new RemoteCallback()
        {
            public Integer doRemote() throws RemoteException
            {
                IBookQuery query = getBookQuery();

                if (query == null)
                {
                    query = getBookQuerySource().newQuery();
                    setBookQuery(query);
                }

                try
                {
                    return query.masterQuery(parameters, ordering);
                }
                catch (RemoteException ex)
                {
                    setBookQuery(null);
                    throw ex;
                }
            }
        };

        return getRemoteTemplate().execute(callback, "Error processing query.");
    }

}